# Workflow Changes for Cache Fix

## ⚠️ Important Note
I cannot directly modify files in `.github/workflows/` due to GitHub App permissions. These changes need to be applied manually to `.github/workflows/maven-project.yml`.

## Changes Overview

This document shows the specific changes needed to fix cache issues in the test workflows. Each section shows:
- **Location**: Where in the file to make the change
- **Current code**: What currently exists
- **Updated code**: What it should be changed to
- **Why**: Explanation of the fix

---

## Change 1: Update Maven Cache in Build Job

**Location**: `maven-project.yml`, build job, around line 116

**Current**:
```yaml
      - name: Cache Maven packages
        # Restores Maven dependencies from previous workflow runs to speed up the build.
        # After build completes, dependencies are uploaded as artifact for parallel test jobs.
        uses: actions/cache@v4
        with:
          path: .m2-cache
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}
          restore-keys: |
            ${{ runner.os }}-maven-
```

**Updated**:
```yaml
      - name: Cache Maven packages
        # Restores Maven dependencies from previous workflow runs to speed up the build.
        # After build completes, dependencies are uploaded as artifact for parallel test jobs.
        uses: actions/cache@v4
        id: maven-cache-build
        continue-on-error: true
        with:
          path: .m2-cache
          key: ${{ runner.os }}-maven-build-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}
          restore-keys: |
            ${{ runner.os }}-maven-build-
            ${{ runner.os }}-maven-

      - name: Validate Maven cache
        id: validate-build-cache
        run: |
          echo "Validating Maven cache..."
          if [ -d ".m2-cache/repository" ] && [ "$(find .m2-cache/repository -type f 2>/dev/null | wc -l)" -gt 100 ]; then
            echo "cache-valid=true" >> "$GITHUB_OUTPUT"
            echo "✓ Maven cache is valid (contains repository with files)"
          else
            echo "cache-valid=false" >> "$GITHUB_OUTPUT"
            echo "⚠ Maven cache is invalid or empty, will re-download dependencies"
            rm -rf .m2-cache
            mkdir -p .m2-cache
          fi
```

**Why**: Adds validation to ensure the cache is actually usable, and adds `continue-on-error` so build doesn't fail if cache restore fails.

---

## Change 2: Update Maven Cache in Modern Tests Job

**Location**: `maven-project.yml`, modern-tests job, after "Download Maven cache artifact" step (around line 179)

**Current**:
```yaml
      - name: Download Maven cache artifact
        uses: actions/download-artifact@v4
        with:
          name: maven-dependencies-cache
          path: .m2-cache
```

**Updated**:
```yaml
      - name: Download Maven cache artifact
        uses: actions/download-artifact@v4
        id: download-maven-artifact-modern
        continue-on-error: true
        with:
          name: maven-dependencies-cache
          path: .m2-cache

      - name: Validate downloaded Maven cache
        id: validate-maven-modern
        run: |
          echo "Checking Maven dependencies availability..."

          # Check if artifact download succeeded and has content
          if [ "${{ steps.download-maven-artifact-modern.outcome }}" == "success" ] && \
             [ -d ".m2-cache/repository" ] && \
             [ "$(find .m2-cache/repository -type f 2>/dev/null | wc -l)" -gt 100 ]; then
            echo "cache-valid=true" >> "$GITHUB_OUTPUT"
            echo "✓ Maven dependencies available from artifact"
          else
            echo "cache-valid=false" >> "$GITHUB_OUTPUT"
            echo "⚠ Maven dependencies not available, will download during test execution"
            rm -rf .m2-cache
            mkdir -p .m2-cache
          fi

      - name: Restore Maven cache (fallback)
        if: steps.validate-maven-modern.outputs.cache-valid != 'true'
        uses: actions/cache/restore@v4
        continue-on-error: true
        with:
          path: .m2-cache
          key: ${{ runner.os }}-maven-modern-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}
          restore-keys: |
            ${{ runner.os }}-maven-modern-
            ${{ runner.os }}-maven-build-
            ${{ runner.os }}-maven-
```

**Why**:
1. Adds fallback if artifact download fails
2. Validates the downloaded artifact has actual content
3. Provides separate cache key for modern tests
4. Falls back to cache restore if artifact is missing

---

## Change 3: Update Maven Cache in Legacy Tests Job

**Location**: `maven-project.yml`, legacy-tests job, after "Download Maven cache artifact" step (around line 282)

**Current**:
```yaml
      - name: Download Maven cache artifact
        uses: actions/download-artifact@v4
        with:
          name: maven-dependencies-cache
          path: .m2-cache
```

**Updated**:
```yaml
      - name: Download Maven cache artifact
        uses: actions/download-artifact@v4
        id: download-maven-artifact-legacy
        continue-on-error: true
        with:
          name: maven-dependencies-cache
          path: .m2-cache

      - name: Validate downloaded Maven cache
        id: validate-maven-legacy
        run: |
          echo "Checking Maven dependencies availability..."

          # Check if artifact download succeeded and has content
          if [ "${{ steps.download-maven-artifact-legacy.outcome }}" == "success" ] && \
             [ -d ".m2-cache/repository" ] && \
             [ "$(find .m2-cache/repository -type f 2>/dev/null | wc -l)" -gt 100 ]; then
            echo "cache-valid=true" >> "$GITHUB_OUTPUT"
            echo "✓ Maven dependencies available from artifact"
          else
            echo "cache-valid=false" >> "$GITHUB_OUTPUT"
            echo "⚠ Maven dependencies not available, will download during test execution"
            rm -rf .m2-cache
            mkdir -p .m2-cache
          fi

      - name: Restore Maven cache (fallback)
        if: steps.validate-maven-legacy.outputs.cache-valid != 'true'
        uses: actions/cache/restore@v4
        continue-on-error: true
        with:
          path: .m2-cache
          key: ${{ runner.os }}-maven-legacy-${{ hashFiles('**/pom.xml', '**/dependencies-lock*.json') }}
          restore-keys: |
            ${{ runner.os }}-maven-legacy-
            ${{ runner.os }}-maven-build-
            ${{ runner.os }}-maven-
```

**Why**: Same as modern tests, but with separate cache key for legacy tests to avoid conflicts.

---

## Change 4: Make Docker Layer Cache Non-Blocking

**Location**: Multiple places where "Cache Docker layers" appears

**Find all instances of**:
```yaml
      - name: Cache Docker layers
        uses: actions/cache@v4
        with:
          path: /tmp/.buildx-cache
          ...
```

**Add to each**:
```yaml
      - name: Cache Docker layers
        uses: actions/cache@v4
        continue-on-error: true  # <-- Add this line
        with:
          path: /tmp/.buildx-cache
          ...
```

**Why**: Ensures workflow doesn't fail if Docker layer cache has issues.

---

## Change 5: Add Cache Integrity Check (Optional Enhancement)

**Location**: Before "Run modern tests" step (around line 263) and before "Run legacy tests" step (around line 484)

**Add new step**:
```yaml
      - name: Check Maven cache integrity
        run: |
          echo "Checking Maven cache integrity..."

          # Check for zero-byte files (sign of corruption)
          if find .m2-cache/repository -type f -size 0 2>/dev/null | head -n 1 | grep -q .; then
            echo "⚠ Found empty files in Maven cache, cleaning up..."
            rm -rf .m2-cache
            mkdir -p .m2-cache
          else
            echo "✓ Maven cache integrity check passed"
          fi
```

**Why**: Detects corrupted caches early and cleans them up before tests run.

---

## Testing After Changes

After applying these changes, test with:

1. **Normal operation**: Run the workflow - should use caches and be faster
2. **Without artifact**: Delete the artifact manually in a test run - should fall back to cache restore
3. **Without cache**: Clear caches - should fall back to Maven download
4. **Corrupted cache**: Not easily testable, but validation logic handles it

## Expected Behavior

- ✅ If caches work: Fast execution using cached dependencies
- ✅ If artifact fails: Falls back to cache restore
- ✅ If cache fails: Falls back to Maven re-download
- ✅ If cache is corrupted: Detected and cleaned up, Maven re-downloads
- ✅ No workflow failures due to cache issues

## Summary of Key Changes

1. **Separated cache keys**: `maven-build`, `maven-modern`, `maven-legacy`
2. **Added validation**: Checks if cache actually contains files
3. **Added fallback**: If artifact fails, try cache; if cache fails, Maven downloads
4. **Made non-blocking**: All cache operations have `continue-on-error: true`
5. **Added integrity checks**: Detects empty/corrupted files

These changes ensure the workflow is resilient to cache failures while maintaining performance benefits when caches work.
