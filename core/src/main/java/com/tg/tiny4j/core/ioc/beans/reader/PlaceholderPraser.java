package com.tg.tiny4j.core.ioc.beans.reader;


/**
 * Created by twogoods on 16/10/29.
 */
public class PlaceholderPraser {

    public static KeyAndDefault prase(String config) {
        if (config.startsWith("${") && config.endsWith("}")) {
            String subConfig = config.substring(2, config.length() - 1);
            String[] configAndDefault = subConfig.split(":");
            if (configAndDefault.length == 1) {
                return new KeyAndDefault(configAndDefault[0], null);
            } else {
                return new KeyAndDefault(configAndDefault[0], configAndDefault[1]);
            }
        }
        return new KeyAndDefault(config, null);
    }

    public static class KeyAndDefault {
        private String name;
        private String defaultValue;

        private KeyAndDefault(String name, String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }
}
