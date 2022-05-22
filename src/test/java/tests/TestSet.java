package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSet
{
    @BeforeAll
    public static void beforeAll()
    {
        System.out.println( "BeforeAll from class " + TestSet.class.getName() );
    }

    @AfterAll
    public static void afterAll()
    {
        System.out.println( "AfterAll from class " + TestSet.class.getName() );
    }

    @BeforeEach
    public void beforeEach()
    {
        System.out.println( "Before Each from " + TestSet.class.getName() );
    }

    @AfterEach
    public void afterEach()
    {
        System.out.println( "After Each from " + TestSet.class.getName() );
    }

    @Order(2)
    @Tag("first_line")
    @Test
    void test1A( TestInfo ti )
    {
        System.out.println( "Executing " + ti.getTestMethod().get().getName() );
        assertTrue( true );
    }

    @Order(1)
    @Test
    void test1B()
    {
        System.out.println( "Test 1B - executing " );
        assertTrue( true );
    }


    @Tag("first_line")
    @Disabled
    @Nested
    class HelperTest
    {

        @Order(1)
        @Test
        void test1C()
        {
            System.out.println( "Test 1C - executing" );
            assertTrue( true );
        }


        @Order(2)
        @Test
        void test2C()
        {
            System.out.println( "Test 2C - executing" );
            assertTrue( true );
        }

    }

}
