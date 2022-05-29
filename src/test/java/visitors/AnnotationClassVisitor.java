package visitors;

import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;

public class AnnotationClassVisitor extends ClassVisitor
{
    public Class[] classList;

    public AnnotationClassVisitor( int api, ClassVisitor classVisitor, Class[] classList )
    {
        super( api, classVisitor );
        this.classList = classList;
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
            // TO DO, but how ?
            cv.visitNestMember( description );
        }
    }
}
