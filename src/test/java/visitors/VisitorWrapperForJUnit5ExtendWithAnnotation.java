package visitors;

import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.extension.ExtendWith;

public class VisitorWrapperForJUnit5ExtendWithAnnotation implements AsmVisitorWrapper
{
    @Override
    public int mergeWriter( int i )
    {
        return 0;
    }

    @Override
    public int mergeReader( int i )
    {
        return 0;
    }

    @Override
    public ClassVisitor wrap( TypeDescription typeDescription, ClassVisitor classVisitor,
                              Implementation.Context context, TypePool typePool,
                              FieldList<FieldDescription.InDefinedShape> fieldList,
                              MethodList<?> methodList, int i, int i1 )
    {


        return new AnnotationClassVisitor( Opcodes.ASM7, classVisitor, new Class[] { ExtendWith.class } );

    }

}
