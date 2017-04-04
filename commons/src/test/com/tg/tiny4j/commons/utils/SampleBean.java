package com.tg.tiny4j.commons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-04-04
 */
public class SampleBean {
    private String value;

    public SampleBean() {
    }

    public SampleBean(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String echo(String name, int age) {
//        List<String> names = new ArrayList<>(100);
//        for (int i = 0; i < 100; i++) {
//            names.add(name + i);
//        }
//        List<Integer> ages = new ArrayList<>(100);
//        for (int i = 0; i < 100; i++) {
//            ages.add(age + i);
//        }
        return name + "--" + age;
    }
}
