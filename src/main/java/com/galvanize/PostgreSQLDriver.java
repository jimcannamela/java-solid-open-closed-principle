package com.galvanize;

import static java.lang.String.format;

public class PostgreSQLDriver {

    public String quotePostgresColumn(String column) {
        if (column.contains(".")) {
            String tableName = column.substring(0, column.indexOf("."));
            String columnName = column.substring(column.indexOf(".") + 1);
            return format("%s.\"%s\"", quotePostgresTable(tableName), columnName);
        } else {
            return format("\"%s\"", column);
        }
    }

    public String quotePostgresTable(String table) {
        return format("\"%s\"", table);
    }

    public String quotePostgresValue(String value) {
        return format("'%s'", value);
    }

}
