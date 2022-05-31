package tests;

import extensions.BaseLifecycleExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.StringUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Indentation.AFTER_ALL_ANNOTATION;
import static utils.Indentation.AFTER_EACH_ANNOTATION;
import static utils.Indentation.BEFORE_ALL_ANNOTATION;
import static utils.Indentation.BEFORE_EACH_ANNOTATION;
import static utils.Indentation.TEST_BODY;

@ExtendWith(BaseLifecycleExtension.class)
public class TestSetWithExtension
{

    @BeforeAll
    public static void beforeAll()
    {
        System.out.println( StringUtils.prefix( " ", BEFORE_ALL_ANNOTATION ) + "BeforeAll from test class " +
                TestSetWithExtension.class.getName() );
    }

    @AfterAll
    public static void afterAll()
    {
        System.out.println( StringUtils.prefix( " ", AFTER_ALL_ANNOTATION ) + "AfterAll from test class " +
                TestSetWithExtension.class.getName() );
    }

    @BeforeEach
    public void beforeEach()
    {
        System.out.println( StringUtils.prefix( " ", BEFORE_EACH_ANNOTATION ) + "Before Each from test class " +
                TestSetWithExtension.class.getName() );
    }

    @AfterEach
    public void afterEach()
    {
        System.out.println( StringUtils.prefix( " ", AFTER_EACH_ANNOTATION ) + "After Each from test class " +
                TestSetWithExtension.class.getName() );
    }

    @Order(2)
    @Tag("first_line")
    @Test
    void test1A( TestInfo ti )
    {
        System.out.println( StringUtils.prefix( " ", TEST_BODY ) + "Executing " + ti.getTestMethod().get().getName() );
        assertTrue( true );
    }

    @Order(1)
    @Test
    void test1B()
    {
        System.out.println( StringUtils.prefix( " ", TEST_BODY ) + "Test 1B - executing " );
        assertTrue( true );
    }
}
