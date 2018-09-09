package com.example.demo.main.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation annotation to validate image element.
 *
 *
 * Example
 * @Image(width = 150, height = 300m, mimeTypes = {MimeType.IMAGE_JPG, MimeType.IMAGE_PNG})
 */
@Target({TYPE, ANNOTATION_TYPE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
@Documented
public @interface Image
{
    String message() default "file is incorrect";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    MimeType[] mimeTypes() default {MimeType.IMAGE_JPEG, MimeType.IMAGE_JPG, MimeType.IMAGE_PNG};

    /**
     * @return The minimal width field in pixels
     */
    int minWidth() default 10;

    /**
     * @return The maximal width field in pixels
     */
    int maxWidth() default 100;

    /**
     * @return The minimal height field in pixels
     */
    int minHeight() default 10;

    /**
     * @return The maximal height field in pixels
     */
    int maxHeight() default 100;

    /**
     * @return The size of file in kilobytes
     */
    int size() default 1000;

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see Image
     */
    @Target({TYPE, ANNOTATION_TYPE, FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        Image[] value();
    }
}