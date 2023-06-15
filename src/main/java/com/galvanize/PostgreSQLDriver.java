package com.galvanize;

import static java.lang.String.format;

public class PostgreSQLDriver implements Driver {


    @Override
    public String quoteColumn(String column) {
        if (column.contains(".")) {
            String tableName = column.substring(0, column.indexOf("."));
            String columnName = column.substring(column.indexOf(".") + 1);
            return format("%s.\"%s\"", quoteTable(tableName), columnName);
        } else {
            return format("\"%s\"", column);
        }
    }

    @Override
    public String quoteTable(String table) {
        return format("\"%s\"", table);
    }

    @Override
    public String quoteValue(String value) {
        return format("'%s'", value);
    }

}
