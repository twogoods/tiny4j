package com.tg.tiny4j.web.metadata;

import java.util.Objects;

/**
 * Created by twogoods on 16/11/8.
 */
public class ExceptionHandleInfo {
    private String exceptionName;
    private String methodName;

    public ExceptionHandleInfo() {
    }

    public ExceptionHandleInfo(String exceptionName, String methodName) {
        this.exceptionName = exceptionName;
        this.methodName = methodName;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionHandleInfo that = (ExceptionHandleInfo) o;
        return Objects.equals(exceptionName, that.exceptionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exceptionName);
    }
}
