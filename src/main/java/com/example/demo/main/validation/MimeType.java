package com.example.demo.main.validation;

public enum MimeType {
    IMAGE_JPG("image/jpg"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png");

    String mimeType;

    private MimeType(String mimeTypeArg)
    {
        mimeType = mimeTypeArg;
    }
}
