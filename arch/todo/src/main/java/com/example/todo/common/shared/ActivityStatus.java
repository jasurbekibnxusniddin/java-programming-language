package com.example.todo.common.shared;

public enum ActivityStatus {
    PENDING(0),
    IN_PROGRESS(1),
    COMPLETED(2),
    CANCELLED(3);

    private final int code;

    ActivityStatus(int code) { this.code = code; }

    public int getCode() { return code; }

    // helper to get enum by code
    public static ActivityStatus fromCode(int code) {
        for (ActivityStatus s : values()) {
            if (s.code == code) return s;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
