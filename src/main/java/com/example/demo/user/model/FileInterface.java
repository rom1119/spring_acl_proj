package com.example.demo.user.model;

import org.springframework.web.multipart.MultipartFile;

public interface FileInterface {

    void setFile(MultipartFile file);

    MultipartFile getFile();

    void setFileName(String fileName);

    String getFileName();
}
