package com.galvanize;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.galvanize.util.ClassProxy;
import com.galvanize.util.InstanceProxy;
import org.junit.jupiter.api.Test;

public class SolutionTest {

    @Test
    public void aTest() {
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

        ClassProxy _SQLBuilder = ClassProxy.interfaceNamed("com.galvanize.SQLBuilder")
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
                );

        InstanceProxy newInstance = _Driver.concreteClass().build();

    }
}
