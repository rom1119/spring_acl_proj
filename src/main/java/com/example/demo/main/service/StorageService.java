package com.example.demo.main.service;

import com.example.demo.user.model.FileInterface;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    String store(FileInterface fileWrapper) throws IOException;

    void deleteFile(FileInterface fileWrapper) throws IOException;

    String updateFile(FileInterface fileWrapper) throws IOException;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
