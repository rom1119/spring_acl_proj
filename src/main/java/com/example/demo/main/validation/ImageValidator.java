package com.example.demo.main.validation;


import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

public class ImageValidator implements ConstraintValidator<Image, Object>
{
    private MimeType[] mimeTypes;
    private int minWidth;
    private int maxWidth;
    private int minHeight;
    private int maxHeight;
    private int size;

    private Locale locale;


    @Override
    public void initialize(final Image constraintAnnotation)
    {
        mimeTypes = constraintAnnotation.mimeTypes();
        minWidth = constraintAnnotation.minWidth();
        maxWidth = constraintAnnotation.maxWidth();
        minHeight = constraintAnnotation.minHeight();
        maxHeight = constraintAnnotation.maxHeight();
        size = constraintAnnotation.size();

    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
        boolean toReturn = false;
        MultipartFile multipartFile = (MultipartFile) value;

        if (multipartFile.isEmpty()) {
            return true;
        }
        context.disableDefaultConstraintViolation();

        if (!mimeTypeFileIsCorrect(multipartFile)) {
            context.buildConstraintViolationWithTemplate("validation.user.file.mimeType").addConstraintViolation();
            return false;
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!sizeCorrect(multipartFile)) {
            context.buildConstraintViolationWithTemplate("validation.user.file.size").addConstraintViolation();
            return false;
        }

        if (!maxHeightCorrect(image)) {
            context.buildConstraintViolationWithTemplate("validation.user.file.maxHeight").addConstraintViolation();
            return false;
        }

        if (!minHeightCorrect(image)) {
            context.buildConstraintViolationWithTemplate("validation.user.file.minHeight").addConstraintViolation();
            return false;
        }

        if (!maxWidthCorrect(image)) {
            context.buildConstraintViolationWithTemplate("validation.user.file.maxWidth").addConstraintViolation();
            return false;
        }

        if (!minWidthCorrect(image)) {
            context.buildConstraintViolationWithTemplate("validation.user.file.minWidth").addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean sizeCorrect(MultipartFile multipartFile) {
        return (multipartFile.getSize() / 1000) <= size;
    }

    private boolean mimeTypeFileIsCorrect(MultipartFile multipartFile) {

        for (int i = 0; i < mimeTypes.length; i++) {
            if (mimeTypes[i].mimeType.equals(multipartFile.getContentType())) {
                return true;
            }
        }
        return false;
    }

    private boolean minWidthCorrect(BufferedImage image) {

        return minWidth <= image.getWidth();
    }

    private boolean maxWidthCorrect(BufferedImage image) {

        return maxWidth >= image.getWidth();
    }

    private boolean minHeightCorrect(BufferedImage image) {

        return minHeight <= image.getHeight();
    }

    private boolean maxHeightCorrect(BufferedImage image) {

        return maxHeight >= image.getHeight();
    }
}