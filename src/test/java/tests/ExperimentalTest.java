package tests;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.Test;
import utils.InstrumentationUtils;
import utils.RunJUnit5TestsFromJava;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ExperimentalTest
{
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    void test1()
    {
        Class instrumentedTestClass = InstrumentationUtils.instrumentTestClass( TestSet.class );
        new RunJUnit5TestsFromJava().runTestMethod( instrumentedTestClass.getName() );
        assertTrue( true );
    }
}
