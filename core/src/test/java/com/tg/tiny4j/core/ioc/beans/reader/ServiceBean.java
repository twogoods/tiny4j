package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.annotation.Component;
import com.tg.tiny4j.core.ioc.annotation.Inject;
import com.tg.tiny4j.core.ioc.annotation.Value;

/**
 * Created by twogoods on 16/10/29.
 */
@Component
public class ServiceBean {
    @Value("${user.nick:test}")
    private String s1;
    @Value("${user.name:test}")
    private String s2;

    @Inject
    private DaoBean daoBean;

    public void service(){
        daoBean.execute();
    }

    @Override
    public String toString() {
        return "ServiceBean{" +
                "s1='" + s1 + '\'' +
                ", s2='" + s2 + '\'' +
                '}';
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }
}
