package com.galvanize;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SQLBuilderTest {

    @Test
    public void itWorksWithMySQLDriver() {
        SQLBuilder builder = new SQLBuilder(new MySQLDriver())
                .select("foo", "bar.baz")
                .from("foo", "bar")
                .where("blah", "other");

        String expected = "SELECT `foo`, `bar`.`baz`\n" +
                "FROM `foo`, `bar`\n" +
                "WHERE `blah` = \"other\"";

        assertEquals(expected, builder.build());
    }

    @Test
    public void itWorksWithPostgreSQLDriver() {
        SQLBuilder builder = new SQLBuilder(new PostgreSQLDriver())
                .select("sales", "salespeople.baz")
                .from("sales", "salespeople")
                .where("blah", "other");

        String expected = "SELECT \"sales\", \"salespeople\".\"baz\"\n" +
                "FROM \"sales\", \"salespeople\"\n" +
                "WHERE \"blah\" = 'other'";

        assertEquals(expected, builder.build());
    }
}
