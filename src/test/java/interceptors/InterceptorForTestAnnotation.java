package interceptors;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import utils.StringUtils;

import java.lang.reflect.Method;

import static utils.Indentation.TEST_ANNOTATION;
import static utils.TestLogSignatures.TEST_CLASS;

public class InterceptorForTestAnnotation
{
    @RuntimeType
    public static void intercept( @This Object self, @Origin Method method, @AllArguments Object[] args,
                                  @SuperMethod Method superMethod ) throws Throwable
    {
        System.out.println( StringUtils.prefix( " ", TEST_ANNOTATION ) + TEST_CLASS );
        superMethod.invoke( self, args );
    }
}
