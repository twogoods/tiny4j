package com.tg.tiny4j.core.ioc.context;

import com.tg.tiny4j.core.ioc.beans.reader.ServiceBean;

/**
 * Created by twogoods on 16/10/27.
 */
public class ClassPathXmlApplicationContextTest {
    public static void main(String[] args) throws Exception {
        System.out.println("******************");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test.xml");
        ServiceBean serviceBean=(ServiceBean)applicationContext.getBean("testService");
        System.out.println(serviceBean);
        serviceBean.service();

        System.out.println("*******************");
        ServiceBean serviceBean2=(ServiceBean)applicationContext.getBean("serviceBean");
        System.out.println(serviceBean2);
        serviceBean2.service();

        System.out.println("*******************");
        //IOC上下文
        ApplicationContextHolder holder=applicationContext.getBean("applicationContextHolder", ApplicationContextHolder.class);
        System.out.println("holder get bean : "+holder.getBean("serviceBean"));
    }

}