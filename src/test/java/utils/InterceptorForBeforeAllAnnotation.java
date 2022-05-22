package utils;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;

public class InterceptorForBeforeAllAnnotation
{
    @RuntimeType
    public static void intercept( @This Object self,
                                  @Origin Method method,
                                  @AllArguments Object[] args,
                                  @SuperMethod Method superMethod ) throws Throwable
    {


        System.out.println( "A beforeAll proxy " + method.getName() + " to method " + superMethod.getName() );
        superMethod.invoke( self, args );
    }
}
