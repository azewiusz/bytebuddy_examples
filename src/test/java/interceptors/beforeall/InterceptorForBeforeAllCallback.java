package interceptors.beforeall;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import utils.StringUtils;
import utils.TestLogSignatures;

import java.lang.reflect.Method;

import static utils.Indentation.BEFORE_ALL_CALLBACK;

public class InterceptorForBeforeAllCallback
{
    @RuntimeType
    public static void intercept( @This Object self, @AllArguments Object[] args,
                                  @SuperMethod Method superMethod ) throws Throwable
    {

        System.out.println( StringUtils.prefix( " ", BEFORE_ALL_CALLBACK ) + TestLogSignatures.BEFORE_ALL_EXTENSION );
        superMethod.invoke( self, args );
    }
}
