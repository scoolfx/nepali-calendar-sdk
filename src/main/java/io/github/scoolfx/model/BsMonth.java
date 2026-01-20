package io.github.scoolfx.model;

public enum BsMonth {
    BAISAKH(1, "Baisakh"), JESHTHA(2, "Jeshtha"), ASAR(3, "Asar"),
    SHRAWAN(4, "Shrawan"), BHADRA(5, "Bhadra"), ASWIN(6, "Aswin"),
    KARTIK(7, "Kartik"), MANGSIR(8, "Mangsir"), POUSH(9, "Poush"),
    MAGH(10, "Magh"), FALGUN(11, "Falgun"), CHAITRA(12, "Chaitra");

    private final int value;
    private final String name;

    BsMonth(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static BsMonth fromValue(int value) {
        if (value < 1 || value > 12) throw new IllegalArgumentException("Month must be 1-12");
        return values()[value - 1];
    }
}