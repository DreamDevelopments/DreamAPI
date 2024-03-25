package com.github.dreamdevelopments.dreamapi.utils;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Utility class for interacting with objects using reflection.
 */
public class ReflectionUtils {

    /**
     * Returns all the fields from a class including those inherited
     * @param clazz The class
     * @return List of fields
     */
    public static Iterable<Field> getAllFields(@NotNull Class<?> clazz) {

        List<Field> currentClassFields = Lists.newArrayList(clazz.getDeclaredFields());
        Class<?> parentClass = clazz.getSuperclass();

        if (parentClass != null) {
            List<Field> parentClassFields =
                    (List<Field>) getAllFields(parentClass);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }

}
