# JUnit5 test lifecycle interception

Provided is an example interception technique for modifying the lifecycle of test classes written for JUnit5 platform.
Please do not mistake this code purpose with current state of the art existing JUnit5 Extension Providers.

The main purpose of presented code is to plug in a custom logic before callbacks defined with some of JUnit5
annotations. This all is done without actually a need to alter any existing structure of tests. Created using
the [ByteBuddy library](https://bytebuddy.net/#/) version 1.12.10.

Which annotations are covered:

At a class method level:

* @Test
* @BeforeAll
* @AfterAll
* @BeforeEach
* @AfterEach

At test class extension level (by intercepting top level class annotation @ExtendWith).

For methods:

* beforeAll
* afterAll
* beforeEach
* afterEach

The examples show how to intercept above handler calls in order to enable/disable JUnit5 annotation at runtime. Such
technique could be used to create a custom JUnit5 test groups based not only on single class/tag grouping but also on
other criteria (like grouping by feature toggle state).

Current implementation works only for test classes without @Nested members (tested with JUnit Jupiter 5.8.2). See test
class here [TestSet.java](src/test/java/tests/TestSet.java)

Example Usage:

```java
 @Test
    void test1()
            {
            Class instrumentedTestClass=InstrumentationUtils.instrumentTestClass(TestSet.class );
        new RunJUnit5TestsFromJava().runTestMethod(instrumentedTestClass.getName());
        }
```

Output

```
[[BEFORE_ALL_EXTENSION intercepted
Before All callback from Extension Class
    [[BEFORE_ALL_CLASS intercepted
    BeforeAll from test class tests.TestSetProxy4
        [[BEFORE_EACH_EXTENSION intercepted
          Before Each callback from Extension Class
          [[BEFORE_EACH_CLASS intercepted
          Before Each from test class tests.TestSetProxy4
            [[TEST_CLASS intercepted
               Executing test1A
          [[AFTER_EACH_CLASS intercepted
          After Each from test class tests.TestSetProxy4
        [[AFTER_EACH_EXTENSION intercepted
          After Each callback from Extension Class
        [[BEFORE_EACH_EXTENSION intercepted
          Before Each callback from Extension Class
          [[BEFORE_EACH_CLASS intercepted
          Before Each from test class tests.TestSetProxy4
            [[TEST_CLASS intercepted
               Test 1B - executing 
          [[AFTER_EACH_CLASS intercepted
          After Each from test class tests.TestSetProxy4
        [[AFTER_EACH_EXTENSION intercepted
          After Each callback from Extension Class
    [[AFTER_ALL_CLASS intercepted
    AfterAll from test class tests.TestSetProxy4
[[AFTER_ALL_EXTENSION intercepted
After All callback from Extension Class
```

