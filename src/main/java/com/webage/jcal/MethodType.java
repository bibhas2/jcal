package com.webage.jcal;

public enum MethodType {
    REQUEST("REQUEST"),
    CANCEL("CANCEL");

    private String method;

    private MethodType(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
