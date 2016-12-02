package com.tg.tiny4j.commons.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
/**
 * Created by twogoods on 16/12/1.
 */
public class JsonUtil {

    private static final Logger log = LogManager.getLogger(JsonUtil.class);

    public static String jsonFromObject(Object object) {
        String str;
        try {
            str = JSON.toJSONString(object);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unable to serialize to json: " + object, e);
            return null;
        }
        return str;
    }

    public static String jsonFromObject(Object object, String dateFormat) {
        String str;
        try {
            str = JSON.toJSONStringWithDateFormat(object, dateFormat);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unable to serialize to json: " + object, e);
            return null;
        }
        return str;
    }

    public static <T> T objectFromJson(String json, Class<T> klass) {
        T object;
        try {
            object = JSON.parseObject(json, klass);
        } catch (RuntimeException e) {
            log.error("Runtime exception during deserializing " + klass.getSimpleName() + " from "
                    + StringUtils.abbreviate(json, 80));
            throw e;
        } catch (Exception e) {
            log.error("Exception during deserializing " + klass.getSimpleName() + " from "
                    + StringUtils.abbreviate(json, 80));
            return null;
        }
        return object;
    }

    public static <T> T objectFromJson(String json, TypeReference<T> klass) {
        T object;
        try {
            object = JSON.parseObject(json, klass);
        } catch (RuntimeException e) {
            log.error("Runtime exception during deserializing from " + StringUtils.abbreviate(json, 80));
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception during deserializing from " + StringUtils.abbreviate(json, 80));
            return null;
        }
        return object;
    }

    public static Map toMap(String jsonString) {
        return JSON.parseObject(jsonString);
    }

    public static void writeValue(Writer writer, Object value) {
        JSON.writeJSONStringTo(value, writer);
    }

    public static void writeValue(HttpServletResponse response,Object value) throws IOException{
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        JSON.writeJSONStringTo(value, response.getWriter());
    }
}
