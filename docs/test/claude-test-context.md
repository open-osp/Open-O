# Modern Test Framework - Claude Context

> This file is automatically injected by hooks when working on test files.
> For complete documentation, see test-writing-best-practices.md.

===============================================================================
CRITICAL: Before writing tests, READ the actual DAO/Manager interface!
Never invent methods - verify they exist in the codebase first.
===============================================================================

## BDD NAMING CONVENTION

### Method Naming (ONE underscore max)
Valid patterns:
   1. should<Action>_when<Condition>           (PREFERRED)
   2. <methodName>_<scenario>_<expectedOutcome>
   3. should<ExpectedBehavior>                  (simple cases)

Examples:
   shouldReturnTickler_whenValidIdProvided()
   findById_validId_returnsTickler()
   shouldLoadSpringContext()

Invalid: testFindById(), test_find_by_id(), should_return_when_valid()

### @DisplayName (lowercase "should")
   @DisplayName("should return tickler when valid ID is provided")

===============================================================================

## REQUIRED STRUCTURE

```java
@Tag("integration")  // Test type: integration, unit
@Tag("dao")          // Layer: dao, manager, service, controller
@Tag("read")         // CRUD: create, read, update, delete
@DisplayName("Component Tests")
public class ComponentTest extends OpenOTestBase {  // MUST be public!

    @PersistenceContext(unitName = "testPersistenceUnit")
    private EntityManager entityManager;

    @Test
    @DisplayName("should perform action when condition met")
    void shouldPerformAction_whenConditionMet() {
        // Given - set up test data
        Entity entity = createTestEntity();

        // When - perform the action
        Result result = componentUnderTest.doSomething(entity);

        // Then - verify outcome with AssertJ
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(expected);
    }
}
```

===============================================================================

## TAG HIERARCHY

Level 1 (Test Type):   @Tag("integration"), @Tag("unit"), @Tag("slow"), @Tag("fast")
Level 2 (Layer):       @Tag("dao"), @Tag("manager"), @Tag("service"), @Tag("controller")
Level 3 (CRUD):        @Tag("create"), @Tag("read"), @Tag("update"), @Tag("delete")
Level 4 (Extended):    @Tag("query"), @Tag("search"), @Tag("filter"), @Tag("aggregate")

===============================================================================

## TEST DATA MANAGEMENT

```java
protected Entity createTestEntity() {
    Entity entity = new Entity();
    entity.setField("deterministic value");  // NOT new Date() or random
    return entity;
}

protected Entity createAndPersist() {
    Entity entity = createTestEntity();
    entityManager.persist(entity);
    entityManager.flush();
    return entity;
}
```

===============================================================================

## ASSERTIONS (Use AssertJ)

```java
// Good - fluent AssertJ
assertThat(result).isNotNull();
assertThat(result.getStatus()).isEqualTo("ACTIVE");
assertThat(resultList)
    .hasSize(3)
    .extracting(Entity::getStatus)
    .containsOnly("ACTIVE");

// Avoid - JUnit basic assertions
assertNotNull(result);  // Less readable
assertEquals("ACTIVE", result.getStatus());  // Argument order confusion
```

===============================================================================

## COMMON PITFALLS

- Non-public test class      -> Maven Surefire won't discover it
- Testing invented methods   -> Read actual interface first!
- Random/time-dependent data -> Use deterministic values
- Testing implementation     -> Test behavior, not internal methods
- Too many things in one test -> One behavior per test

===============================================================================

## CHECKLIST BEFORE COMMITTING

[ ] Method name follows BDD pattern (ONE underscore)
[ ] @DisplayName with lowercase "should"
[ ] Appropriate @Tag annotations (min: type + layer)
[ ] Extends OpenOTestBase (or appropriate base)
[ ] Given-When-Then structure with comments
[ ] Uses AssertJ assertions
[ ] Deterministic test data (no random/time values)
[ ] Test passes consistently
[ ] Test fails when expected behavior is broken

===============================================================================

## UNIT TESTING WITH MOCKS (SpringUtils Anti-Pattern)

```java
public class ManagerUnitTest extends OpenOUnitTestBase {  // Note: OpenOUnitTestBase for unit tests
    @Mock private SomeDao mockDao;
    private MockedStatic<LogAction> logActionMock;

    @BeforeEach
    void setUp() {
        registerMock(SomeDao.class, mockDao);           // 1. Register dependency mocks first
        logActionMock = mockStatic(LogAction.class);     // 2. Then create static mocks
    }

    @AfterEach
    void tearDown() {
        if (logActionMock != null) logActionMock.close();
    }
}
```

===============================================================================

Full documentation: docs/test/test-writing-best-practices.md
