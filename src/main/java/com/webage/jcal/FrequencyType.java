package com.webage.jcal;

public enum FrequencyType {
    YEARLY("YEARLY"),
    MONTHLY("MONTHLY"),
    WEEKLY("WEEKLY"),
    DAILY("DAILY");

    private String frequency;

    private FrequencyType(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequency() {
        return frequency;
    }
}
