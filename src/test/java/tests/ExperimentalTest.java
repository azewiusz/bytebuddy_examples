package tests;

import org.junit.jupiter.api.Test;
import utils.InstrumentationUtils;
import utils.RunJUnit5TestsFromJava;


class ExperimentalTest
{
    @Test
    void test1()
    {
        Class instrumentedTestClass = InstrumentationUtils.instrumentTestClass( TestSet.class );
        new RunJUnit5TestsFromJava().runTestMethod( instrumentedTestClass.getName() );
    }
}
