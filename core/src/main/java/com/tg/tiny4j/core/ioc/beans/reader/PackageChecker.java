package com.tg.tiny4j.core.ioc.beans.reader;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by twogoods on 16/10/29.
 */
public class PackageChecker {
    /**
     * 多个包名去除有包涵关系的包
     *
     * @param arr
     * @return
     */
    protected static List<String> deDuplicatePackage(String[] arr) {
        List<String> list = new LinkedList<>();
        for (String item : arr) {
            list.add(item);
        }
        //从短到长排序
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() > o2.length() ? 0 : 1;
            }
        });
        for (int i = 0; i < list.size(); i++) {
            for (int j = list.size() - 1; i < j; j--) {
                if (list.get(j).contains(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }
}
