package utils;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

public class RunJUnit5TestsFromJava
{
    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    public void runTestMethod( String testMethod )
    {

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors( selectMethod( testMethod ) )
                .build();
        Launcher launcher = LauncherFactory.create();

        TestPlan testPlan = launcher.discover( request );

        launcher.registerTestExecutionListeners( listener );
        launcher.execute( request );
    }

}
