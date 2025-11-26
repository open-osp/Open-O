# OpenO EMR Test Documentation

## Overview

This directory contains comprehensive documentation for the OpenO EMR modern test framework, which uses JUnit 5 (Jupiter) running alongside legacy JUnit 4 tests.

## Current Status

- **Framework**: ✅ Production Ready
- **Tests**: 23 of 24 passing (96% pass rate)
  - Unit Tests: 12/12 passing ✅
  - Integration Tests: 11/12 passing (1 fails due to lst_gender table dependency)
- **Java 21 Support**: ✅ Fully compatible with ByteBuddy experimental flag

## Quick Start Guide

If you're new to the test framework, start here:

1. **[Modern Test Framework Guide](modern-test-framework-guide.md)** - Complete guide to using the framework
2. **[Test Writing Best Practices](test-writing-best-practices.md)** - How to write effective tests

## Documentation Index

### Essential Guides

| Document | Purpose | When to Read |
|----------|---------|--------------|
| [Modern Test Framework Guide](modern-test-framework-guide.md) | Main guide for using the test framework | **Start here** - When writing any tests |
| [Test Writing Best Practices](test-writing-best-practices.md) | Comprehensive best practices and patterns | Before writing your first test |
| [Implementation Summary](modern-test-implementation-summary.md) | High-level overview of what was implemented | Understanding the framework setup |

### Technical Documentation

| Document | Purpose | When to Read |
|----------|---------|--------------|
| [Framework Complete Documentation](modern-test-framework-complete.md) | Detailed technical implementation | Deep technical understanding needed |
| [Legacy Test Reference](legacy-test-reference.md) | Documentation of existing JUnit 4 tests | Working with legacy tests |

## Test Framework Architecture

```
src/test-modern/
├── java/ca/openosp/openo/
│   ├── test/
│   │   ├── base/              # Base test classes
│   │   ├── unit/              # Unit test infrastructure
│   │   └── mocks/             # Mock implementations
│   └── [domain]/              # Domain-specific tests
│       ├── dao/               # DAO tests (integration + unit)
│       │   └── archive/       # Original single-file tests (reference only)
│       └── manager/           # Service/Manager tests
└── resources/                 # Test configurations
```

## Running Tests

### Using Make Script (Recommended)
```bash
# Run all tests (modern + legacy)
make install --run-tests

# Run only modern tests (JUnit 5)
make install --run-modern-tests

# Run only legacy tests (JUnit 4)
make install --run-legacy-tests

# Run only modern unit tests
make install --run-unit-tests

# Run only modern integration tests
make install --run-integration-tests
```

### Using Maven Directly
```bash
# Run all tests (modern + legacy)
mvn test

# Run specific test types
mvn test -Dgroups="unit"          # Unit tests only
mvn test -Dgroups="integration"   # Integration tests only
mvn test -Dtest=TicklerDao*       # Specific test pattern
```

## Key Features

### 1. Dual Framework Support
- **JUnit 5 (modern)** tests in `src/test-modern/` - 23 tests implemented
- **JUnit 4 (legacy)** tests in `src/test/` - ~374 test files remain active
- Both run seamlessly together in CI/CD pipeline
- See [Legacy Test Reference](legacy-test-reference.md) for existing test details

### 2. SpringUtils Anti-Pattern Handling
- **Integration Tests**: Automatic SpringUtils configuration
- **Unit Tests**: MockedStatic support for isolation

### 3. BDD Naming Convention
```java
// Clear, self-documenting test names
void should_returnActiveTicklers_when_demographicNumberProvided()
```

### 4. Comprehensive Tagging
```java
@Tag("integration")  // Test type
@Tag("dao")          // Layer
@Tag("read")         // Operation
@Tag("filter")       // Extended operation
```

### 5. Multi-File Scalability
Tests are organized by operation type for scalability:
- `TicklerDaoFindIntegrationTest` - Find operations
- `TicklerDaoQueryIntegrationTest` - Query operations
- `TicklerDaoAggregateIntegrationTest` - Aggregation operations
- `TicklerDaoWriteIntegrationTest` - Write operations

## Writing Your First Test

### Integration Test Example

```java
@DisplayName("My Component Integration Tests")
@Tag("integration")
public class MyComponentIntegrationTest extends OpenOTestBase {

    @Test
    @DisplayName("should perform expected action when condition is met")
    void should_performAction_when_conditionMet() {
        // Given - setup
        Entity entity = createTestEntity();

        // When - execute
        Result result = component.process(entity);

        // Then - verify
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("SUCCESS");
    }
}
```

### Unit Test Example

```java
@DisplayName("My Component Unit Tests")
@Tag("unit")
public class MyComponentUnitTest extends OpenOUnitTestBase {

    @Mock
    private DependencyDao mockDao;

    @BeforeEach
    void setUp() {
        registerMock(DependencyDao.class, mockDao);
    }

    @Test
    void should_validateInput_when_processing() {
        // Test with mocked dependencies
    }
}
```

## Common Issues and Solutions

### Issue: SpringUtils.getBean() returns null
**Solution**: Ensure test extends `OpenOTestBase` and Spring context is configured

### Issue: ByteBuddy Java 21 compatibility error
**Solution**: Verify `-Dnet.bytebuddy.experimental=true` is in Maven configuration

### Issue: Static initialization failures
**Solution**: Mock dependencies before creating static mocks (see unit testing guide)

### Issue: Test not discovered by Maven
**Solution**: Ensure test class is declared `public` and follows naming convention

## Contributing

When adding new test documentation:
1. Follow the existing structure and naming conventions
2. Update this README with any new guides
3. Ensure examples are from actual working code
4. Include both positive and negative test cases

## Support

For questions or issues:
1. Check the [Test Writing Best Practices](test-writing-best-practices.md)
2. Review existing test implementations in `src/test-modern/`
3. Consult the main project documentation in `/workspace/CLAUDE.md`

---

*Last Updated: September 2025*
*Version: 1.0*
*Status: Production Ready*