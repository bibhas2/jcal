package com.webage.jcal;

/**
 * An enumeration type used to indicate an event's status.
 */
public enum StatusType {
    /**
     * Tentative
     */
    TENTATIVE("TENTATIVE"),
    /**
     * Confirmed
     */
    CONFIRMED("CONFIRMED"),
    /**
     * Cancelled
     */
    CANCELLED("CANCELLED");

    private String status;

    private StatusType(String status) {
        this.status = status;
    }

    /**
     * Returns the String value of a status.
     * @return the String value of a status
     */
    public String getStatus() {
        return status;
    }
}
