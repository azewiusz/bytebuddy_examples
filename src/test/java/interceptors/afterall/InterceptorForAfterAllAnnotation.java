package interceptors.afterall;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import utils.StringUtils;
import utils.TestLogSignatures;

import java.lang.reflect.Method;

import static utils.Indentation.AFTER_ALL_ANNOTATION;

public class InterceptorForAfterAllAnnotation
{
    @RuntimeType
    public static void intercept( @Origin Method method, @AllArguments Object[] args,
                                  @SuperMethod Method superMethod ) throws Throwable
    {

        System.out.println( StringUtils.prefix( " ", AFTER_ALL_ANNOTATION ) + TestLogSignatures.AFTER_ALL_CLASS );
        superMethod.invoke( args );
    }
}
