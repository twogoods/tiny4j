package com.tg.tiny4j.web.metadata;

import java.util.List;

/**
 * Created by twogoods on 16/12/3.
 */
public class UrlPraseInfo {
    private String match;
    private List<String> params;

    public UrlPraseInfo() {
    }

    public UrlPraseInfo(String match, List<String> params) {
        this.match = match;
        this.params = params;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
