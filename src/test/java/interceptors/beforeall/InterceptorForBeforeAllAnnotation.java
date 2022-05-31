package interceptors.beforeall;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import utils.StringUtils;
import utils.TestLogSignatures;

import java.lang.reflect.Method;

import static utils.Indentation.BEFORE_ALL_ANNOTATION;

public class InterceptorForBeforeAllAnnotation
{
    @RuntimeType
    public static void intercept( @Origin Method method, @AllArguments Object[] args,
                                  @SuperMethod(nullIfImpossible = true) Method superMethod ) throws Throwable
    {
        System.out.println(
                StringUtils.prefix( " ", BEFORE_ALL_ANNOTATION ) + TestLogSignatures.BEFORE_ALL_CLASS );
        superMethod.invoke( args );
    }
}
