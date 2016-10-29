package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.annotation.Component;

/**
 * Created by twogoods on 16/10/29.
 */
@Component
public class DaoBean {
    public void execute(){
        System.out.println("sql execute...");
    }
}
