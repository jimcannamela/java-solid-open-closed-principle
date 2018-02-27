package com.galvanize;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.galvanize.util.ClassProxy;
import com.galvanize.util.InstanceProxy;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SolutionTest {

    @Test
    public void aTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassProxy _Driver = ClassProxy.interfaceNamed("com.galvanize.Driver")
                .ensureMethod(m -> m
                        .isPublic()
                        .returns(String.class)
                        .withParameters(String.class)
                        .named("quoteColumn")
                )
                .ensureMethod(m -> m
                        .isPublic()
                        .returns(String.class)
                        .withParameters(String.class)
                        .named("quoteTable")
                )
                .ensureMethod(m -> m
                        .isPublic()
                        .returns(String.class)
                        .withParameters(String.class)
                        .named("quoteValue")
                );

        ClassProxy _MySQLDriver = ClassProxy.classNamed("com.galvanize.MySQLDriver")
                .ensureImplements(_Driver);

        ClassProxy.classNamed("com.galvanize.PostgreSQLDriver")
                .ensureImplements(_Driver);

        ClassProxy _SQLBuilder = ClassProxy.classNamed("com.galvanize.SQLBuilder")
                .ensureConstructor(_Driver.getDelegate())
                .ensureMethod(m -> m
                        .isPublic()
                        .withParameters(String[].class)
                        .named("select")
                )
                .ensureMethod(m -> m
                        .isPublic()
                        .withParameters(String[].class)
                        .named("from")
                )
                .ensureMethod(m -> m
                        .isPublic()
                        .withParameters(String.class, String.class)
                        .named("where")
                )
                .ensureMethod(m -> m
                        .isPublic()
                        .returns(String.class)
                        .named("build")
                );

        InstanceProxy driver = _Driver
                .concreteClass()
                .intercept("quoteColumn", column -> format("|%s|", column))
                .intercept("quoteTable", table -> format("<%s>", table))
                .intercept("quoteValue", value -> format("--%s--", value))
                .build();

        InstanceProxy sqlBuilder = _SQLBuilder.newInstance(driver);

        sqlBuilder.invoke("select", new Object[]{new String[]{"price", "products.description"}});
        sqlBuilder.invoke("from", new Object[]{new String[]{"sales", "products"}});
        sqlBuilder.invoke("where", "date", "2018-09-07");
        String result = (String) sqlBuilder.invoke("build");

        String expected = "SELECT |price|, |products.description|\n" +
                "FROM <sales>, <products>\n" +
                "WHERE |date| = --2018-09-07--";

        assertEquals(expected, result);

        InstanceProxy mysqlBuilder = _SQLBuilder.newInstance(_MySQLDriver.newInstance());

        mysqlBuilder.invoke("select", new Object[]{new String[]{"price", "products.description"}});
        mysqlBuilder.invoke("from", new Object[]{new String[]{"sales", "products"}});
        mysqlBuilder.invoke("where", "date", "2018-09-07");
        result = (String) mysqlBuilder.invoke("build");

        expected = "SELECT `price`, `products`.`description`\n" +
                "FROM `sales`, `products`\n" +
                "WHERE `date` = \"2018-09-07\"";

        assertEquals(expected, result);

    }
}
