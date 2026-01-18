# Test Workflow Cache Fix Plan

## Problem Statement

The test workflows for modern and legacy tests are experiencing cache-related issues that cause failures. The requirement is that caches should work when available but gracefully fall back to normal downloads when there are issues.

## Current State Analysis

### Maven Dependencies Caching
- **Location**: `.m2-cache` directory
- **Key**: `${{ runner.os }}-maven-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}`
- **Issue**: Both modern and legacy tests share the same cache key, which can cause collisions

### Docker Layer Caching
- **Tomcat Cache**: `/tmp/.buildx-cache`
- **MariaDB Cache**: `/tmp/.buildx-cache-db`
- **Issue**: No validation that caches are valid after restore

### Maven Artifact Sharing
- **Mechanism**: Build job uploads `.m2-cache` as artifact, test jobs download it
- **Issue**: No fallback if artifact download fails

## Root Causes

1. **No cache failure handling**: If cache restore fails or is corrupted, jobs continue without validation
2. **No artifact fallback**: Test jobs depend on artifact without alternative
3. **Cache key collisions**: Modern and legacy tests may have different dependency states
4. **No integrity validation**: Restored caches aren't checked for completeness

## Proposed Solution

### 1. Separate Cache Keys for Modern vs Legacy Tests

**Rationale**: Modern tests use H2 in-memory database, legacy tests use MariaDB. They may have different dependency states.

**Implementation**:
```yaml
# In build job - keep general cache
- name: Cache Maven packages (build)
  uses: actions/cache@v4
  with:
    path: .m2-cache
    key: ${{ runner.os }}-maven-build-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}
    restore-keys: |
      ${{ runner.os }}-maven-build-
      ${{ runner.os }}-maven-

# In modern-tests job
- name: Restore Maven cache (modern tests)
  uses: actions/cache/restore@v4
  id: cache-modern
  continue-on-error: true
  with:
    path: .m2-cache
    key: ${{ runner.os }}-maven-modern-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}
    restore-keys: |
      ${{ runner.os }}-maven-modern-
      ${{ runner.os }}-maven-build-
      ${{ runner.os }}-maven-

# In legacy-tests job
- name: Restore Maven cache (legacy tests)
  uses: actions/cache/restore@v4
  id: cache-legacy
  continue-on-error: true
  with:
    path: .m2-cache
    key: ${{ runner.os }}-maven-legacy-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}
    restore-keys: |
      ${{ runner.os }}-maven-legacy-
      ${{ runner.os }}-maven-build-
      ${{ runner.os }}-maven-
```

### 2. Add Cache Validation and Fallback

**Rationale**: Ensure restored caches are valid before using them, fall back to re-download if not.

**Implementation**:
```yaml
# After cache restore in modern-tests and legacy-tests jobs
- name: Validate Maven cache
  id: validate-cache
  run: |
    echo "Validating Maven cache..."
    if [ -d ".m2-cache/repository" ] && [ "$(find .m2-cache/repository -type f | wc -l)" -gt 100 ]; then
      echo "cache-valid=true" >> "$GITHUB_OUTPUT"
      echo "Maven cache is valid (contains repository with files)"
    else
      echo "cache-valid=false" >> "$GITHUB_OUTPUT"
      echo "Maven cache is invalid or empty, will re-download dependencies"
      rm -rf .m2-cache
      mkdir -p .m2-cache
    fi
```

### 3. Artifact Download with Fallback

**Rationale**: If artifact download fails, job should continue and re-download dependencies via Maven.

**Implementation**:
```yaml
# In modern-tests and legacy-tests jobs
- name: Download Maven cache artifact
  uses: actions/download-artifact@v4
  id: download-artifact
  continue-on-error: true
  with:
    name: maven-dependencies-cache
    path: .m2-cache

- name: Check artifact download
  id: check-artifact
  run: |
    if [ "${{ steps.download-artifact.outcome }}" == "success" ] && [ -d ".m2-cache/repository" ]; then
      echo "artifact-available=true" >> "$GITHUB_OUTPUT"
      echo "Maven dependencies artifact downloaded successfully"
    else
      echo "artifact-available=false" >> "$GITHUB_OUTPUT"
      echo "Maven dependencies artifact not available, will download during build"
      mkdir -p .m2-cache
    fi
```

### 4. Add Basic Integrity Check Before Using Cache

**Rationale**: Detect corrupted caches early and trigger re-download.

**Implementation**:
```yaml
# Before running tests
- name: Integrity check Maven cache
  run: |
    echo "Checking Maven cache integrity..."
    corrupted=false

    # Check for critical Maven files
    if [ ! -f ".m2-cache/repository/org/springframework/spring-core/5.3.39/spring-core-5.3.39.jar" ]; then
      echo "Warning: Key dependency missing from cache"
      corrupted=true
    fi

    # Check for empty or zero-byte files
    if find .m2-cache/repository -type f -size 0 | grep -q .; then
      echo "Warning: Found empty files in Maven cache"
      corrupted=true
    fi

    if [ "$corrupted" == "true" ]; then
      echo "Maven cache appears corrupted, cleaning up..."
      rm -rf .m2-cache
      mkdir -p .m2-cache
    else
      echo "Maven cache integrity check passed"
    fi
```

### 5. Make Cache Save Non-Blocking

**Rationale**: Don't fail the entire job if cache save fails.

**Implementation**:
```yaml
# At end of jobs (if they save cache)
- name: Save Maven cache
  uses: actions/cache/save@v4
  if: always() && steps.cache-modern.outputs.cache-hit != 'true'
  continue-on-error: true
  with:
    path: .m2-cache
    key: ${{ runner.os }}-maven-modern-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}
```

### 6. Add Retry Logic for Network-Related Operations

**Rationale**: Transient network failures shouldn't break the build.

**Implementation**:
```yaml
- name: Download Maven cache artifact (with retry)
  uses: nick-fields/retry-action@v3
  continue-on-error: true
  with:
    timeout_minutes: 5
    max_attempts: 3
    retry_on: error
    command: |
      # Download artifact using gh cli as alternative
      gh run download ${{ github.run_id }} -n maven-dependencies-cache -D .m2-cache || \
      # Or fall back to actions/download-artifact behavior
      exit 0
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

## Implementation Order

1. **Phase 1: Add validation and fallback** (Minimal changes, maximum safety)
   - Add `continue-on-error: true` to cache and artifact operations
   - Add validation steps after cache restore
   - Add cleanup logic for invalid caches

2. **Phase 2: Separate cache keys** (Reduce collisions)
   - Update cache keys to include test type (modern/legacy)
   - Update restore-keys to include fallback chain

3. **Phase 3: Add integrity checks** (Detect corruption early)
   - Add checks for critical files
   - Add checks for zero-byte files
   - Add cleanup logic

4. **Phase 4: Add retry logic** (Handle transient failures)
   - Add retry wrapper for artifact downloads
   - Add retry for cache operations

## Testing Plan

1. **Test with working cache**: Verify existing behavior still works
2. **Test with missing cache**: Verify fallback to download works
3. **Test with corrupted cache**: Verify cleanup and re-download works
4. **Test with failed artifact**: Verify Maven re-downloads dependencies
5. **Test parallel execution**: Verify modern and legacy tests don't interfere

## Rollback Plan

If issues occur:
1. Revert to previous workflow version
2. Cache keys will naturally expire after 7 days
3. No data loss risk - Maven can always re-download

## Benefits

- ✅ Caches work when available (performance)
- ✅ Graceful fallback when cache fails (reliability)
- ✅ No test failures due to cache issues (stability)
- ✅ Separate cache keys reduce collisions (correctness)
- ✅ Integrity checks detect corruption early (safety)
- ✅ Non-blocking cache operations (resilience)

## Notes

- The `actions/cache@v4` action already has built-in fallback behavior for missing caches
- The main issues are with corrupted caches and artifact failures
- This solution adds explicit validation and fallback logic
- The solution is backwards compatible and can be applied incrementally
