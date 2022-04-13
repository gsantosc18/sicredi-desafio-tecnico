package br.com.softdesigner.sicreddesafiotecnico.enums;

public enum UserStatusEnum {
    ABLE_TO_VOTE("ABLE_TO_VOTE"), UNABLE_TO_VOTE("UNABLE_TO_VOTE");

    private final String value;

    UserStatusEnum(String value) {
        this.value = value;
    }

    public boolean equals(String status) {
        return this.value.equals(status);
    }

    public String getValue() {
        return this.value;
    }
}
