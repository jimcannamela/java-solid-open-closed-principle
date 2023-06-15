package com.galvanize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class SQLBuilder {

    private Driver driver;
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<String> tables = new ArrayList<>();
    private LinkedHashMap<String, String> whereClauses = new LinkedHashMap<>();

    public Driver getDriver() {
        return driver;
    }

    public SQLBuilder(Driver driver) {
        this.driver = driver;
    }

    public SQLBuilder select(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public SQLBuilder from(String... tables) {
        this.tables.addAll(Arrays.asList(tables));
        return this;
    }

    public SQLBuilder where(String column, String value) {
        this.whereClauses.put(column, value);
        return this;
    }

    public String build() {
        return new StringBuilder()
                .append("SELECT ")
                .append(
                        columns.stream()
                                .map(this::quoteColumnName)
                                .collect(joining(", "))
                )
                .append("\nFROM ")
                .append(
                        tables.stream()
                                .map(this::quoteTableName)
                                .collect(joining(", "))
                )
                .append("\nWHERE ")
                .append(
                        whereClauses.entrySet().stream()
                                .map(entry -> whereClause(entry.getKey(), entry.getValue()))
                                .collect(joining(" AND "))
                ).toString();
    }

    private String quoteColumnName(String column) {
        return driver.quoteColumn(column);
    }

    private String quoteTableName(String table) {
        return driver.quoteTable(table);
    }

    private String quoteValue(String value) {
        return driver.quoteValue(value);
    }

    private String whereClause(String column, String value) {
        return format("%s = %s", quoteColumnName(column), quoteValue(value));
    }
}