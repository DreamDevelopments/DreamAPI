package com.github.dreamdevelopments.dreamapi.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark fields in a {@link Config} class to link a local variable to a value in the config file.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ConfigValue {

    /**
     * The path to the value in the config.
     * @return The path to the value in the config.
     */
    String value();

}