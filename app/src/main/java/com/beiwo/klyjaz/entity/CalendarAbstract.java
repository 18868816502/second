package com.beiwo.klyjaz.entity;


import java.util.Map;

public class CalendarAbstract {

    private Map<String, Integer> returnHash;
    private Map<String, Integer> unReturnHash;

    public Map<String, Integer> getReturnHash() {
        return returnHash;
    }

    public void setReturnHash(Map<String, Integer> returnHash) {
        this.returnHash = returnHash;
    }

    public Map<String, Integer> getUnReturnHash() {
        return unReturnHash;
    }

    public void setUnReturnHash(Map<String, Integer> unReturnHash) {
        this.unReturnHash = unReturnHash;
    }
}
