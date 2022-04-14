package br.com.softdesigner.sicreddesafiotecnico.enums;

public enum VotoEnum {
    SIM("SIM"), NAO("NAO");

    private String value;

    VotoEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(String value) {
        return getValue().equals(value);
    }

    public boolean equals(VotoEnum value) {
        return getValue().equals(value.getValue());
    }
}
