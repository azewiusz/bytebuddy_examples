package extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import utils.StringUtils;

import static utils.Indentation.AFTER_ALL_CALLBACK;
import static utils.Indentation.AFTER_EACH_ANNOTATION;
import static utils.Indentation.BEFORE_ALL_CALLBACK;
import static utils.Indentation.BEFORE_EACH_ANNOTATION;

public class BaseLifecycleExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback,
        AfterEachCallback
{
    @Override
    public void beforeAll( ExtensionContext extensionContext ) throws Exception
    {
        System.out.println(
                StringUtils.prefix( " ", BEFORE_ALL_CALLBACK ) + "Before All callback from Extension Class" );
    }

    @Override
    public void afterAll( ExtensionContext extensionContext ) throws Exception
    {
        System.out.println( StringUtils.prefix( " ", AFTER_ALL_CALLBACK ) + "After All callback from Extension Class" );
    }

    @Override
    public void beforeEach( ExtensionContext extensionContext ) throws Exception
    {
        System.out.println(
                StringUtils.prefix( " ", BEFORE_EACH_ANNOTATION ) + "Before Each callback from Extension Class" );
    }

    @Override
    public void afterEach( ExtensionContext extensionContext ) throws Exception
    {
        System.out.println(
                StringUtils.prefix( " ", AFTER_EACH_ANNOTATION ) + "After Each callback from Extension Class" );
    }
}
