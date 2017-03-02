package com.tg.tiny4j.core.ioc.context;

import com.tg.tiny4j.core.ioc.beans.reader.ConfigBean;
import com.tg.tiny4j.core.ioc.beans.reader.ServiceBean;
import org.junit.Test;

/**
 * Created by twogoods on 16/10/27.
 */
public class ClassPathXmlApplicationContextTest {
    @Test
    public void test() throws Exception {
        System.out.println("1******************");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test.xml");


//        ServiceBean serviceBean=(ServiceBean)applicationContext.getBean("testService");
//        System.out.println(serviceBean);
//        serviceBean.service();

        System.out.println("2*******************");

//        ServiceBean serviceBean2=(ServiceBean)applicationContext.getBean("serviceBean");
//        System.out.println(serviceBean2);
//        serviceBean2.service();

        System.out.println("3*******************");

        //IOC上下文
//        ApplicationContextHolder holder=applicationContext.getBean("applicationContextHolder", ApplicationContextHolder.class);
//        System.out.println("holder get bean : "+holder.getBean("serviceBean"));

        System.out.println("4*******************");
        ConfigBean configBean=(ConfigBean)applicationContext.getBean("configBean");
        System.out.println("configbean get dao:"+configBean.beanAnnDao());
        System.out.println("configbean get service:"+configBean.beanAnnService().hashCode());
        ServiceBean beanAnnService=(ServiceBean)applicationContext.getBean("beanAnnService");
        System.out.println("application get service: "+beanAnnService.hashCode());
        System.out.println("application get dao: "+applicationContext.getBean("beanAnnDao"));
        beanAnnService.service();
        System.out.println(beanAnnService.getDaoBean().hashCode()+"---"+applicationContext.getBean("beanAnnDao").hashCode());

    }

}