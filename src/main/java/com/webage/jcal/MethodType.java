package com.webage.jcal;

/**
 * An enumeration type used to indicate the METHOD in a calendar invite.
 * 
 */
public enum MethodType {
    /**
     * Method is REQUEST
     */
    REQUEST("REQUEST"),
    /**
     * Method is CANCEL
     */
    CANCEL("CANCEL");

    private String method;

    private MethodType(String method) {
        this.method = method;
    }

    /**
     * Return the String value of the method.
     * @return The method name
     */
    public String getMethod() {
        return method;
    }
}
