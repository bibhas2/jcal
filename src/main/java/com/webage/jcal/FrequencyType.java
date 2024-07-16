package com.webage.jcal;

/**
 * An enumeration type used to describe the frequency of an event.
 * 
 */
public enum FrequencyType {
    /**
     * Frequency: Once a year
     */
    YEARLY("YEARLY"),
    /**
     * Frequency: Once a month
     */
    MONTHLY("MONTHLY"),
    /**
     * Frequency: Once a wekk
     */
    WEEKLY("WEEKLY"),
    /**
     * Frequency: Daily
     */
    DAILY("DAILY");

    private String frequency;

    private FrequencyType(String frequency) {
        this.frequency = frequency;
    }

    /**
     * Returns the String value of the frequency type
     * @return the String value of the frequency type
     */
    public String getFrequency() {
        return frequency;
    }
}
