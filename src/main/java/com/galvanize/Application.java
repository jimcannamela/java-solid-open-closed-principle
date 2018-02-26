package com.galvanize;

public class Application {

    public static void main(String[] args) {
        System.out.println(
                new SQLBuilder(new PostgreSQLDriver())
                        .select("name", "age")
                        .from("users")
                        .where("id", "7")
                        .build()
        );
        System.out.println(
                new SQLBuilder(new MySQLDriver())
                        .select("name", "age")
                        .from("users")
                        .where("id", "7")
                        .build()
        );
    }

}
