import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import ysoserial.payloads.util.Gadgets;

public class javasistdemo {
    public static void main(String[] args) throws Exception{
//        ClassPool pool = ClassPool.getDefault();
//        pool.insertClassPath(new ClassClassPath(Gadgets.StubTransletPayload.class));
////        pool.insertClassPath(new ClassClassPath(abstTranslet));
//        final CtClass clazz = pool.get(Gadgets.StubTransletPayload.class.getName());
//
//
//        clazz.makeClassInitializer().insertAfter();
//        // sortarandom name to allow repeated exploitation (watch out for PermGen exhaustion)
//        clazz.setName("ysoserial.Pwner" + System.nanoTime());
//        CtClass superC = pool.get(abstTranslet.getName());
//        clazz.setSuperclass(superC);
    }
}
