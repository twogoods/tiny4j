package com.tg.tiny4j.core.aop;

/**
 * Created by twogoods on 16/10/30.
 */
public class AddOperate implements Operate{
    private String prop;
    @Override
    public int cal(int a, int b) {
        System.out.println("method: cal(a,b)");
        //System.out.println("prop:"+prop);
        return a+b+cal(a);
    }

    @Override
    public int cal(int a) {
        System.out.println("method: cal(a)");
        return a+a;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }
}
