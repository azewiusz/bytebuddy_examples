package visitors;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.pool.TypePool;

public class AnnotationClassVisitor extends ClassVisitor
{
    public Class[] classList;
    public TypePool typePool;
    public TypeDescription typeDescription;
    public VisitorWrapperForJUnit5ExtendWithAnnotation parent;

    public AnnotationClassVisitor( int api, ClassVisitor classVisitor, Class[] classList, TypePool typePool,
                                   TypeDescription typeDescription, VisitorWrapperForJUnit5ExtendWithAnnotation parent )
    {
        super( api, classVisitor );
        this.classList = classList;
        this.typePool = typePool;
        this.typeDescription = typeDescription;
        this.parent = parent;
    }

    public static String getTypeSignature( Class c )
    {
        return "L" + c.getCanonicalName().replace( ".", "/" ) + ";";
    }

    public boolean containsClass( String description )
    {
        for ( Class c : classList )
        {
            if ( getTypeSignature( c ).equals( description ) )
            {
                return true;
            }
        }
        return false;
    }

    public AnnotationVisitor visitAnnotation( String description, boolean condition )
    {
        if ( containsClass( description ) )
        {
            return null;
        }
        return super.visitAnnotation( description, condition );
    }

    public void visitNestMember( String description )
    {
        if ( cv != null )
        {
            final TypeDescription resolvedType = typePool.describe( description.replace( "/", "." ) ).resolve();
            // TO DO, but how ?
            //  cv.visitNestMember( description );
        }
    }
}
