package com.example.demo.main.service;

import com.example.demo.user.model.FileInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class StorageManager implements StorageService {

    private FileUploader fileUploader;

    private PathFileManager pathFileManager;

    @Autowired
    public StorageManager(FileUploader fileUploader, PathFileManager pathFileManager) {
        this.fileUploader = fileUploader;
        this.pathFileManager = pathFileManager;
    }

    @Override
    public String store(FileInterface fileWrapper) throws IOException {

        if (fileWrapper.getFile().isEmpty()) {
            return "";
        }

        String filePath = pathFileManager.generatePath(fileWrapper);

        while (fileUploader.fileExist(filePath)) {
            filePath = pathFileManager.generatePath(fileWrapper);
        }

        System.out.println(filePath);

        fileUploader.upload(fileWrapper.getFile(), pathFileManager.getDir(filePath), pathFileManager.getFile(filePath));

        fileWrapper.setFileName(pathFileManager.getFile(filePath));

        return filePath;
    }

    @Override
    public void deleteFile(FileInterface fileWrapper) throws IOException {
        fileUploader.remove(pathFileManager.getServerFilePath(fileWrapper));
        fileWrapper.setFileName(null);
    }

    @Override
    public String updateFile(FileInterface fileWrapper) throws IOException {
        if (fileWrapper.getFile().isEmpty()) {
            return "";
        }

        deleteFile(fileWrapper);

        return store(fileWrapper);

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }
}
