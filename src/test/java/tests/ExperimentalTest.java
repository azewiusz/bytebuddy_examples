package tests;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.InterceptorForBeforeAllAnnotation;
import utils.InterceptorForTestAnnotation;
import utils.RunJUnit5TestsFromJava;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ExperimentalTest
{
    @Test
    void test1()
    {

        AnnotationDescription annotationDescriptionTest = AnnotationDescription.Builder.ofType( Test.class ).build();
        AnnotationDescription annotationDescriptionBeforeAll =
                AnnotationDescription.Builder.ofType( BeforeAll.class ).build();
        final Class<?> proxyClass =
                new ByteBuddy().subclass( TestSet.class ).modifiers( Modifier.PUBLIC ).name( "TestSetProxy1" )
                        .method( ElementMatchers.isAnnotatedWith( BeforeAll.class ) )
                        .intercept( MethodDelegation.to( InterceptorForBeforeAllAnnotation.class ) )
                        .annotateMethod( annotationDescriptionBeforeAll )
                        .method( ElementMatchers.isAnnotatedWith( Test.class ) )
                        .intercept( MethodDelegation.to( InterceptorForTestAnnotation.class ) )
                        .annotateMethod( annotationDescriptionTest ).make()
                        .load( ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION ).getLoaded();

        new RunJUnit5TestsFromJava().runTestMethod( proxyClass.getName() + "#test1B()" );
        assertTrue( true );
    }
}
