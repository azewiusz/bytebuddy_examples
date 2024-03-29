package interceptors.each;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import utils.StringUtils;
import utils.TestLogSignatures;

import java.lang.reflect.Method;

import static utils.Indentation.AFTER_EACH_ANNOTATION;

public class InterceptorForAfterEachAnnotation
{
    @RuntimeType
    public static void intercept( @This Object self, @Origin Method method, @AllArguments Object[] args,
                                  @SuperMethod Method superMethod ) throws Throwable
    {

        System.out.println( StringUtils.prefix( " ", AFTER_EACH_ANNOTATION ) + TestLogSignatures.AFTER_EACH_CLASS );
        superMethod.invoke( self, args );
    }
}
