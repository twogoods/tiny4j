package com.tg.tiny4j.core.ioc.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by twogoods on 16/10/25.
 */
public interface Resource {

    InputStream getInputStream() throws IOException;
}
