package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.InstrumentationUtils;
import utils.RunJUnit5TestsFromJava;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class CoreInstrumentationTest
{
    @Test
    @DisplayName("Instrument a class that has no inner classes defined")
    void test1()
    {
        assertDoesNotThrow( () -> {
            Class instrumentedTestClass =
                    InstrumentationUtils.instrumentTestClass( TestSetWithExtension.class, Arrays.asList() );
            new RunJUnit5TestsFromJava().runTestMethod( instrumentedTestClass.getName() );
        } );
    }

    @Test
    @DisplayName("Instrument an inner class")
    void test2()
    {
        assertDoesNotThrow( () -> {
            Class instrumentedTestClass =
                    InstrumentationUtils.instrumentTestClass( TestSetWithInnerClasses.HelperTest.class,
                            Arrays.asList( TestSetWithInnerClasses.class ) );
            new RunJUnit5TestsFromJava().runTestMethod( instrumentedTestClass.getName() );
        } );
    }
}
