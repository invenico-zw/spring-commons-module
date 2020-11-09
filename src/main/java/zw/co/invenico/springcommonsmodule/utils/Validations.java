package zw.co.invenico.springcommonsmodule.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import zw.co.invenico.springcommonsmodule.exception.InvalidRequestException;

import javax.validation.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validations {

    public static <T> void checkNonNull(T t, Supplier<? extends RuntimeException> exceptionSupplier) {
        try {
            requireNonNull(t);
        } catch (NullPointerException ex) {
            throw exceptionSupplier.get();
        }
    }

    public static <T> void checkArgumentNonNull(T t, final String fieldName, Supplier<? extends RuntimeException> exceptionSupplier) {
        checkNonNull(t, () -> new IllegalArgumentException("Target object can not be null"));
        try {

            val field = getField(t, fieldName);

            val value = field.get(t);

            checkNonNull(value, exceptionSupplier);

        } catch (IllegalAccessException e) {
            throw new InvalidRequestException(String.format("Failed to access the field value due to %s", e.getMessage()));
        }

    }

    public static <T> void checkArgumentNonNull(T t, final String fieldName, String message) {
        checkArgumentNonNull(t, fieldName, () -> new InvalidRequestException(message));

    }

    public static <T> void checkStringArgumentNonNullAndNonEmpty(T t, final String fieldName, String message) {

        checkNonNull(t, () -> new IllegalArgumentException("Target object can not be null"));
        try {

            val field = getField(t, fieldName);

            val value = field.get(t);

            checkNonNull(value, () -> new InvalidRequestException(message));

            if (value.getClass().isAssignableFrom(String.class)) {

                val string = (String) value;

                if (string.trim().isEmpty())
                    throw new InvalidRequestException(message);

            }

        } catch (IllegalAccessException e) {
            throw new InvalidRequestException(String.format("Failed to access the field value due to %s", e.getMessage()));
        }

    }

    private static <T> Field getField(T t, String fieldName) {

        Class clazz = t.getClass();

        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

        Optional<Field> optionalField = fields.stream()
                .filter(field1 -> field1.getName().equals(fieldName))
                .findFirst();

        while (!(optionalField.isPresent())) {

            log.debug("---> Field with name {} was not found in class {} now searching in {}", fieldName, clazz, clazz.getSuperclass());

            clazz = clazz.getSuperclass();

            if (nonNull(clazz)) {

                fields = Arrays.asList(clazz.getDeclaredFields());

                optionalField = fields.stream()
                        .filter(field1 -> field1.getName().equals(fieldName))
                        .findFirst();
            }

        }

        val field = optionalField
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Field with name %s was not found on %s", fieldName, t.getClass().getSimpleName())));

        if (!field.isAccessible())
            field.setAccessible(true);

        return field;

    }

    public static <T> void validate(T t) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
    }

    public static <T> boolean hasNonNullFields(T t) {

        checkNonNull(t, () -> new IllegalStateException(" Target object should not be null"));

        Class clazz = t.getClass();

        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

        val numberOfNonNullFields = fields.stream().filter(field -> {
            field.setAccessible(true);
            try {
                return nonNull(field.get(t));
            } catch (IllegalAccessException e) {
                log.error("---> Error in accessing fields : {}", e.getMessage());
                log.error("---> Error : ", e);
                throw new IllegalStateException(e.getMessage());
            }
        }).count();

        return numberOfNonNullFields > 0;

    }

    public static <T> boolean hasNonNullOrEmptyStringFields(T t) {

        checkNonNull(t, () -> new IllegalStateException(" Target object should not be null"));

        Class clazz = t.getClass();

        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());

        long numberOfNonNullFields = fields.stream().filter(field -> {

            field.setAccessible(true);

            try {

                val value = field.get(t);

                val fieldNonNull = nonNull(value);

                if (fieldNonNull && value.getClass().isAssignableFrom(String.class)) {

                    val string = value.toString();

                    return !(string.trim().isEmpty());

                } else {

                    return fieldNonNull;

                }

            } catch (IllegalAccessException e) {

                log.error("---> Error in accessing fields : {}", e.getMessage());

                log.error("---> Error : ", e);

                throw new IllegalStateException(e.getMessage());

            }

        }).count();

        return numberOfNonNullFields > 0;

    }

}


