package com.galvanize;

import static java.lang.String.format;

class MySQLDriver {

    public String quoteMySQLColumn(String column) {
        if (column.contains(".")) {
            String tableName = column.substring(0, column.indexOf("."));
            String columnName = column.substring(column.indexOf(".") + 1);
            return format("%s.`%s`", quoteMySQLTable(tableName), columnName);
        } else {
            return format("`%s`", column);
        }
    }

    public String quoteMySQLTable(String table) {
        return format("`%s`", table);
    }

    public String quoteMySQLValue(String value) {
        return format("\"%s\"", value);
    }


}
