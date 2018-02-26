package com.galvanize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class SQLBuilder {

    private final Object driver;
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<String> tables = new ArrayList<>();
    private LinkedHashMap<String, String> whereClauses = new LinkedHashMap<>();

    public SQLBuilder(Object driver) {
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
        if (driver instanceof MySQLDriver) {
            return ((MySQLDriver) driver).quoteMySQLColumn(column);
        } else {
            return ((PostgreSQLDriver) driver).quotePostgresColumn(column);
        }
    }

    private String quoteTableName(String table) {
        if (driver instanceof MySQLDriver) {
            return ((MySQLDriver) driver).quoteMySQLTable(table);
        } else {
            return ((PostgreSQLDriver) driver).quotePostgresColumn(table);
        }
    }

    private String quoteValue(String value) {
        if (driver instanceof MySQLDriver) {
            return ((MySQLDriver) driver).quoteMySQLValue(value);
        } else {
            return ((PostgreSQLDriver) driver).quotePostgresValue(value);
        }
    }

    private String whereClause(String column, String value) {
        return format("%s = %s", quoteColumnName(column), quoteValue(value));
    }
}