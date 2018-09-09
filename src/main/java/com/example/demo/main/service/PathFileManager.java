package com.example.demo.main.service;


import com.example.demo.user.model.FileInterface;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

@Component
@PropertySource("classpath:application.properties")
public class PathFileManager {

    @Value( "${app.fileUpload.folder}" )
    private String UPLOADED_FOLDER;

    @Value( "${app.fileUpload.dir}" )
    private String RESOURCE_UPLOAD_DIR;

    public String generatePath(FileInterface fileWrapper)
    {
        String extensionFile = FilenameUtils.getExtension(fileWrapper.getFile().getOriginalFilename());

        return UPLOADED_FOLDER + getNameClass(fileWrapper) + "/" + getUniqidHash() + "." + extensionFile;

    }

    public String getResourceFilePath(FileInterface fileWrapper)
    {
        return RESOURCE_UPLOAD_DIR + getNameClass(fileWrapper) + "/" + fileWrapper.getFileName();
    }

    public String getServerFilePath(FileInterface fileWrapper)
    {
        return UPLOADED_FOLDER + getNameClass(fileWrapper) + "/" + fileWrapper.getFileName();
    }

    private String getNameClass(FileInterface fileWrapper) {
        String className = fileWrapper.getClass().getName();
        return className.substring(className.lastIndexOf(".") + 1 ).toLowerCase();
    }

    private String getUniqidHash()
    {
        long time = System.currentTimeMillis();
        String uniqid = "";

        SecureRandom sec = new SecureRandom();
        byte[] sbuf = sec.generateSeed(8);
        ByteBuffer bb = ByteBuffer.wrap(sbuf);

        uniqid = String.format("%08x%05x", time/1000, time);
        uniqid += String.format("%.8s", ""+bb.getLong());
        uniqid = uniqid.replace("-", "");

        return uniqid ;

    }

    public String getDir(String path)
    {
        return path.substring(0, path.lastIndexOf('/'));
    }

    public String getFile(String path)
    {
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
