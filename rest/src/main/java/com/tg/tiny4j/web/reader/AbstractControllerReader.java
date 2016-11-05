package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.commons.utils.StringUtils;
import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.metadata.RequestHandleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public abstract class AbstractControllerReader implements Reader{
    private static Logger log= LogManager.getLogger(AbstractControllerReader.class);

    private Map<String,ControllerInfo> apis=new HashMap<>();

    private Map<String,RequestHandleInfo> requestHandleMap=new HashMap<>();

    @Override
    public ControllerInfo read(Class clazz) {
        /**url,controller对象,方法,请求方法,拦截器信息
         * url对应一个请求方法<methodname,objname(controller名),requestmethod(请求方法),是否CROS,拦截器信息>
         * 在一个map,存controller对象,或者ioc里拿
         *
         * 拦截器的先后执行顺序
         */
        ControllerInfo controllerInfo=new ControllerInfo();
        controllerInfo.setName(getBeanName(null,clazz));
        controllerInfo.setClassName(clazz.getName());
        controllerInfo.setClazz(clazz);
        apis.put(controllerInfo.getName(),controllerInfo);
        return controllerInfo;
    }

    protected String getBeanName(String annotName, Class clazz) {
        if (StringUtils.isEmpty(annotName)) {
            return StringUtils.firstCharLowercase(clazz.getSimpleName());
        }
        return annotName;
    }

    public Map<String, ControllerInfo> getApis() {
        return apis;
    }

    public Map<String, RequestHandleInfo> getRequestHandleMap() {
        return requestHandleMap;
    }
}
