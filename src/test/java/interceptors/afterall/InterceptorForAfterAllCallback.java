package interceptors.afterall;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import utils.StringUtils;
import utils.TestLogSignatures;

import java.lang.reflect.Method;

import static utils.Indentation.AFTER_ALL_CALLBACK;

public class InterceptorForAfterAllCallback
{
    @RuntimeType
    public static void intercept( @This Object self, @Origin Method method, @AllArguments Object[] args,
                                  @SuperMethod Method superMethod ) throws Throwable
    {

        System.out.println( StringUtils.prefix( " ", AFTER_ALL_CALLBACK ) + TestLogSignatures.AFTER_ALL_EXTENSION );
        superMethod.invoke( self, args );
    }
}
