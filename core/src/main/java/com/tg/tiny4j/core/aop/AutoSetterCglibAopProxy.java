package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.AutoSetterCglibMethodinvocation;
import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.InterfaceMaker;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.text.WordUtils;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by twogoods on 16/10/31.
 */
public class AutoSetterCglibAopProxy extends CglibAopProxy{

    private Map<String,String> autoSetter=new HashMap<>();

    public AutoSetterCglibAopProxy(AopAdvice aopAdvice) {
        super(aopAdvice);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (aopAdvice.getInterceptor() == null) {
            return methodProxy.invokeSuper(o, objects);
        } else {
            return aopAdvice.getInterceptor().invoke(new AutoSetterCglibMethodinvocation(o, method, objects, methodProxy,aopAdvice.getTarget().getClazz(),autoSetter));
        }
    }

    private Class createSetter() {
        Class objCla = aopAdvice.getTarget().getClazz();
        Field[] fields = objCla.getDeclaredFields();
        InterfaceMaker im = new InterfaceMaker();
        for (Field f : fields) {
            String setterName = "set" + WordUtils.capitalize(f.getName());
            try {
                objCla.getDeclaredMethod(setterName,f.getType());
            } catch (NoSuchMethodException e) {
                im.add(new Signature(setterName, Type.VOID_TYPE,new Type[]{Type.getType(f.getType())}), null);
                autoSetter.put(setterName,setterName);
            }
        }
        return im.create();
    }

    @Override
    public Object getProxy() throws AdviceDefinitionException {
        checkAopAdvice();
        enhancer.setSuperclass(aopAdvice.getTarget().getClazz());
        enhancer.setInterfaces(new Class[]{createSetter()});
        enhancer.setCallback(this);
        return enhancer.create();
    }

    public Map<String, String> getAutoSetter() {
        return autoSetter;
    }
}
