package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.web.metadata.BaseInfo;
import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.metadata.RequestHandleInfo;
import com.tg.tiny4j.web.metadata.RequestMapper;

import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public interface Reader {
    BaseInfo read(Class clazz) throws Exception;

    RequestMapper getRequestMapper();
}
