package com.example.demo.main.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUploader {

    public void updateFile()
    {

    }

    public void updateFiles()
    {

    }

    public void upload(MultipartFile file, String dirToFile, String fileName) throws IOException {

        createDirIfNotExist(dirToFile);

        byte[] bytes = file.getBytes();
        Path path = Paths.get(dirToFile + "/" + fileName);
        Files.write(path, bytes);

    }

    public boolean remove(String filePath) throws IOException {
        File fileToDelete = FileUtils.getFile(filePath);
        boolean success = FileUtils.deleteQuietly(fileToDelete);

        return success;
    }

    public boolean fileExist(String filePath)
    {
        File file = new File(filePath);

        return file.exists();
    }

    private void createDirIfNotExist(String dirName)
    {
        File directory = new File(dirName);
        if (! directory.exists()){
            directory.mkdirs();
        }
    }

}
