package com.github.scoolfx.model;

public record BsDate(int year, BsMonth month, int day) {
    public String format() {
        return String.format("%04d-%02d-%02d", year, month.getValue(), day);
    }
}