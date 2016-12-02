package com.tg.tiny4j.web.integration;

import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.metadata.InterceptorInfo;
import com.tg.tiny4j.web.reader.AbstractClassReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebAppControllerReader extends AbstractClassReader {

    public List<String> getControllerName() {
        List<String> objNamesList=new ArrayList<>();
        Map<String, ControllerInfo> controllerInfoMap = getRequestMapper().getApis();
        for (String key : controllerInfoMap.keySet()) {
            objNamesList.add(controllerInfoMap.get(key).getName());
        }
        for (InterceptorInfo interceptor : getRequestMapper().getInterceptorList()) {
            objNamesList.add(interceptor.getName());
        }
        return objNamesList;
    }

    public void setInstances(Map<String,Object> instances){
        Map<String, ControllerInfo> controllerInfoMap = getRequestMapper().getApis();
        for (String key : controllerInfoMap.keySet()) {
            controllerInfoMap.get(key).setObject(instances.get(key));
        }
        for (InterceptorInfo interceptor : getRequestMapper().getInterceptorList()) {
            interceptor.setObj(instances.get(interceptor.getName()));
        }
    }

}
