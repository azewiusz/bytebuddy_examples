package utils;

import interceptors.InterceptorForTestAnnotation;
import interceptors.afterall.InterceptorForAfterAllAnnotation;
import interceptors.afterall.InterceptorForAfterAllCallback;
import interceptors.beforeall.InterceptorForBeforeAllAnnotation;
import interceptors.beforeall.InterceptorForBeforeAllCallback;
import interceptors.each.InterceptorForAfterEachAnnotation;
import interceptors.each.InterceptorForAfterEachCallback;
import interceptors.each.InterceptorForBeforeEachAnnotation;
import interceptors.each.InterceptorForBeforeEachCallback;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import visitors.VisitorWrapperForJUnit5ExtendWithAnnotation;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstrumentationUtils
{
    public static List<Class> getExtensionClasses( Class testClass )
    {
        List<Class> extensionPoints = new ArrayList<>();
        for ( Annotation a : testClass.getAnnotations() )
        {
            if ( a.annotationType().equals( ExtendWith.class ) )
            {
                ExtendWith ew = ( ExtendWith ) a;
                extensionPoints.addAll( Arrays.asList( ew.value() ) );
            }
        }
        return extensionPoints;
    }

    public static AnnotationDescription instrumentTestExtensionClasses( Class testClass, ClassLoader classLoader )
    {

        final List<Class> extensionClasses = getExtensionClasses( testClass );

        AnnotationDescription.Builder builder = AnnotationDescription.Builder.ofType( ExtendWith.class );


        List<Class> resultingClasses = new ArrayList<>();

        int n = 0;
        for ( Class classc : extensionClasses )
        {
            Class<?> previousClass = classc;
            if ( BeforeAllCallback.class.isAssignableFrom( previousClass ) )
            {
                previousClass = stagedTypeTransform( previousClass, new ByteBuddy().subclass( previousClass )
                        .name( testClass.getName() + "BeforeAllCallbackProxy" + n )
                        .method( ElementMatchers.named( "beforeAll" ) )
                        .intercept( MethodDelegation.to( InterceptorForBeforeAllCallback.class ) ).make() );
                resultingClasses.add( previousClass );
            }
            if ( AfterAllCallback.class.isAssignableFrom( previousClass ) )
            {
                final Class<?> localClass = stagedTypeTransform( previousClass,
                        new ByteBuddy().subclass( previousClass )
                                .name( testClass.getName() + "AfterAllCallbackProxy" + n )
                                .method( ElementMatchers.named( "afterAll" ) )
                                .intercept( MethodDelegation.to( InterceptorForAfterAllCallback.class ) ).make() );

                resultingClasses.remove( previousClass );
                resultingClasses.add( localClass );
                previousClass = localClass;
            }
            if ( AfterEachCallback.class.isAssignableFrom( previousClass ) )
            {
                final Class<?> localClass = stagedTypeTransform( previousClass,
                        new ByteBuddy().subclass( previousClass )
                                .name( testClass.getName() + "AfterEachCallbackProxy" + n )
                                .method( ElementMatchers.named( "afterEach" ) )
                                .intercept( MethodDelegation.to( InterceptorForAfterEachCallback.class ) ).make() );
                resultingClasses.remove( previousClass );
                resultingClasses.add( localClass );
                previousClass = localClass;
            }
            if ( BeforeEachCallback.class.isAssignableFrom( previousClass ) )
            {
                final Class<?> localClass = stagedTypeTransform( previousClass,
                        new ByteBuddy().subclass( previousClass )
                                .name( testClass.getName() + "BeforeEachCallbackProxy" + n )
                                .method( ElementMatchers.named( "beforeEach" ) )
                                .intercept( MethodDelegation.to( InterceptorForBeforeEachCallback.class ) ).make() );
                resultingClasses.remove( previousClass );
                resultingClasses.add( localClass );
                previousClass = localClass;
            }
            n++;
        }
        final Class[] results = new Class[ resultingClasses.size() ];

        for ( Class c : resultingClasses )
        {
            System.out.println( "Resulting " + c.getName() );
        }

        final AnnotationDescription annotationDescription =
                builder.defineTypeArray( "value", resultingClasses.toArray( results ) ).build();

        return annotationDescription;

    }

    public static Class filterOutJUnit5ExtendWithAnnotation( Class testClass, ClassLoader classLoader )
    {
        final Class<?> transormedClass = stagedTypeTransform( testClass,
                new ByteBuddy().rebase( testClass, ClassFileLocator.ForClassLoader.of( classLoader ) )
                        .name( testClass.getName() + "ProxyStrippedOut" )
                        .visit( new VisitorWrapperForJUnit5ExtendWithAnnotation() )
//                        .visit( new AsmVisitorWrapper.ForDeclaredMethods()
//                                .method( ElementMatchers.isAnnotatedWith( BeforeAll.class )
//                                                .or( ElementMatchers.isAnnotatedWith( AfterAll.class ) ),
//                                        new VisitorWrapperForMethods() ) )
                        .make() );

        return transormedClass;
    }

    public static Class stagedTypeTransform( Class originalClass, DynamicType.Unloaded transformedClass )
    {
        String rootPathOfClassLoader = originalClass.getClassLoader().getResource( "." ).getPath();
        try
        {
            transformedClass.saveIn( new File( rootPathOfClassLoader ) );
            Class c = originalClass.getClassLoader().loadClass( transformedClass.getTypeDescription().getName() );
            return c;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Class annotateInstrumentedTestClass( Class instrumentedClass,
                                                       AnnotationDescription annotationDescription,
                                                       ClassLoader classLoader )
    {
        Class<?> transformedClass = stagedTypeTransform( instrumentedClass,
                new ByteBuddy().rebase( instrumentedClass, ClassFileLocator.ForClassLoader.of( classLoader ) )
                        .name( instrumentedClass.getName() + "AnnotateProxy2" )
                        .annotateType( annotationDescription ).make() );
        return transformedClass;
    }

    public static void printClass( Class c )
    {
        System.out.println( "Class name " + c.getName() );
        System.out.println( "-> Annotations " );
        for ( Annotation an : c.getAnnotations() )
        {
            System.out.println( "    -> Annotation  " + an.annotationType().getName() );
        }
        for ( Method m : c.getDeclaredMethods() )
        {
            System.out.println( "-> Method " + m.getName() + " modifer " + Modifier.toString( m.getModifiers() ) );
            for ( Annotation an : m.getDeclaredAnnotations() )
            {
                System.out.println( "    -> Annotation  " + an.annotationType().getName() );
            }
        }
    }

    public static Class instrumentTestClass( Class testClass )
    {
        ClassLoader classLoader = testClass.getClassLoader();
        TypePool typePool = TypePool.Default.of( classLoader );

//        final Class<?> testClass1 = stagedTypeTransform( testClass,
//                new ByteBuddy().rebase( testClass )
//                        .name( testClass.getName() + "MakeTestMethodPublic" )
//                        .method( ElementMatchers.isAnnotatedWith( Test.class ) )
//                        .intercept( MethodDelegation.to( InterceptorForTestAnnotationNoAction.class ) )
//                        .transform( Transformer.ForMethod.withModifiers( Visibility.PUBLIC ) )
//                        .make() );

        final AnnotationDescription annotationExtendWith = instrumentTestExtensionClasses( testClass, classLoader );


        final Class strippedOffExtendWithAnnotation = filterOutJUnit5ExtendWithAnnotation( testClass, classLoader );


        Class beforeAll = stagedTypeTransform( strippedOffExtendWithAnnotation,
                new ByteBuddy().rebase( strippedOffExtendWithAnnotation,
                                ClassFileLocator.ForClassLoader.of( classLoader ) )
                        .name( testClass.getName() + "BeforeAll" )
                        .method( ElementMatchers.isAnnotatedWith( BeforeAll.class ) )
                        .intercept( MethodDelegation.withDefaultConfiguration()
                                .to( InterceptorForBeforeAllAnnotation.class ) ).make() );


        Class afterAll = stagedTypeTransform( beforeAll,
                new ByteBuddy().rebase( beforeAll, ClassFileLocator.ForClassLoader.of( classLoader ) )
                        .name( testClass.getName() + "AfterAll" )
                        .method( ElementMatchers.isAnnotatedWith( AfterAll.class ) )
                        .intercept( MethodDelegation.withDefaultConfiguration()
                                .to( InterceptorForAfterAllAnnotation.class ) ).make() );

        Class beforeEach = stagedTypeTransform( afterAll,
                new ByteBuddy().rebase( afterAll, ClassFileLocator.ForClassLoader.of( classLoader ) )
                        .name( testClass.getName() + "BeforeEach" )
                        .method( ElementMatchers.isAnnotatedWith( BeforeEach.class ) )
                        .intercept( MethodDelegation.withDefaultConfiguration()
                                .to( InterceptorForBeforeEachAnnotation.class ) ).make() );

        Class afterEach = stagedTypeTransform( beforeEach,
                new ByteBuddy().rebase( beforeEach, ClassFileLocator.ForClassLoader.of( classLoader ) )
                        .name( testClass.getName() + "AfterEach" )
                        .method( ElementMatchers.isAnnotatedWith( AfterEach.class ) )
                        .intercept( MethodDelegation.withDefaultConfiguration()
                                .to( InterceptorForAfterEachAnnotation.class ) ).make() );


        final Class annotatedTestClass =
                annotateInstrumentedTestClass( afterEach, annotationExtendWith, classLoader );

        final AnnotationDescription annotationDescriptionTest =
                AnnotationDescription.Builder.ofType( Test.class ).build();

        final Class<?> proxyClassTest = stagedTypeTransform( annotatedTestClass,
                new ByteBuddy().rebase( annotatedTestClass, ClassFileLocator.ForClassLoader.of( classLoader ) )
                        .name( testClass.getName() + "Proxy4" )
                        .method( ElementMatchers.isAnnotatedWith( Test.class ) )
                        .intercept( MethodDelegation.to( InterceptorForTestAnnotation.class ) )
                        //.annotateMethod( annotationDescriptionTest )
                        .make() );

        return proxyClassTest;

    }

}
