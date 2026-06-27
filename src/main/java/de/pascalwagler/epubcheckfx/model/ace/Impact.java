package de.pascalwagler.epubcheckfx.model.ace;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Impact {
    CRITICAL("critical"),
    SERIOUS("serious"),
    MODERATE("moderate"),
    MINOR("minor");

    private final String value;

    Impact(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Impact fromValue(String value) {
        for (Impact level : values()) {
            if (level.value.equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown impact level: " + value);
    }
}
