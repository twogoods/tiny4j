package com.tg.tiny4j.commons.utils;

import com.esotericsoftware.reflectasm.MethodAccess;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import net.sf.cglib.reflect.MethodDelegate;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-04-04
 */
public class ClassUtilTest {
    public interface BeanDelegate {
        String getValueFromDelegate(String s);
    }

    @Test
    public void testMethodDelegate() throws Exception {
        SampleBean bean = new SampleBean();
        bean.setValue("Hello cglib!");
        //将SampleBean的getValue方法 绑定到接口的方法
        BeanDelegate delegate = (BeanDelegate) MethodDelegate.create(bean, "echo", BeanDelegate.class);
        System.out.println(delegate.getValueFromDelegate("twogoods"));
    }

    @Test
    public void testFastClass() throws Exception {
        FastClass fastClass = FastClass.create(SampleBean.class);
        FastMethod fastMethod = fastClass.getMethod(SampleBean.class.getMethod("echo", String.class, int.class));
        SampleBean myBean = new SampleBean();
        myBean.setValue("Hello cglib!");
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            fastMethod.invoke(myBean, new Object[]{"haha" + i, 12});
        }
        System.out.println("cglib:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        Method m = SampleBean.class.getMethod("echo", String.class, int.class);
        for (int i = 0; i < 1000000; i++) {
            m.invoke(myBean, "haha" + i, 12);
        }
        System.out.println("reflect:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            myBean.echo("haha" + i, 12);
        }
        System.out.println("normal:" + (System.currentTimeMillis() - start));

        MethodAccess access = MethodAccess.get(SampleBean.class);
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            access.invoke(myBean, "echo", "haha" + i, 12);
        }
        System.out.println("ReflectASM:" + (System.currentTimeMillis() - start));
    }
}