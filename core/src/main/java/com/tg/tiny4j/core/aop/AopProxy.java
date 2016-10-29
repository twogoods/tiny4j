package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;

/**
 * Created by twogoods on 16/10/24.
 */
public interface AopProxy {
    Object getProxy() throws AdviceDefinitionException;
}
