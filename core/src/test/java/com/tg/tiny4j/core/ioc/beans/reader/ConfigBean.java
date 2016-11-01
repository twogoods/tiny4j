package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.annotation.Bean;
import com.tg.tiny4j.core.ioc.annotation.Configuration;
import com.tg.tiny4j.core.ioc.annotation.Inject;
import com.tg.tiny4j.core.ioc.annotation.Value;

/**
 * Created by twogoods on 16/10/31.
 */
@Configuration
public class ConfigBean {

    @Value("${user.name:testname}")
    private String name;
    @Value("${user.pass}")
    private String pass;

    @Inject
    private ServiceBean serviceBean;

    @Bean
    public DaoBean beanAnnDao(){
        System.out.println("in ConfigBean do @Bean method: beanAnnDao");
        return new DaoBean();
    }

    @Bean
    public ServiceBean beanAnnService(){
        //new ServiceBean(beanAnnDao()); 还不支持构造器注入
        System.out.println("in ConfigBean do @Bean method: beanAnnService");
        ServiceBean newBean=new ServiceBean();
        newBean.setS1(name+"---"+serviceBean.getS1());
        newBean.setS2(pass+"---"+serviceBean.getS2());
        newBean.setDaoBean(beanAnnDao());
        return newBean;
    }

}
