package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.metadata.RequestHandleInfo;

import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public interface Reader {
    ControllerInfo read(Class clazz);

    Map<String, ControllerInfo> getApis();

    Map<String, RequestHandleInfo> getRequestHandleMap();
}
