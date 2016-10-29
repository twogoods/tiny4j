package com.tg.tiny4j.core.ioc.resource;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by twogoods on 16/10/25.
 */
public class ResourceLoadTest {
    @Test
    public void test() throws IOException {
        Resource resource=ResourceLoad.getInstance().loadResource("Resource.class");
        InputStream inputStream=resource.getInputStream();
        byte[] bytes=new byte[1024];
        inputStream.read(bytes);
        System.out.println(new String(bytes));
        inputStream.close();
    }
}