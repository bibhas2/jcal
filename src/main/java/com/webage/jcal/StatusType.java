package com.webage.jcal;

public enum StatusType {
    TENTATIVE("TENTATIVE"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED");

    private String status;

    private StatusType(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
