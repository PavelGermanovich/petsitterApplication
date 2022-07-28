package com.germanovich.springboot.petsitterApp.service;

import com.germanovich.springboot.petsitterApp.dao.FileRepository;
import com.germanovich.springboot.petsitterApp.entity.FileDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    @Autowired
    FileRepository fileRepository;

    public FileDb store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDb fileDb = new FileDb(fileName, file.getContentType(), file.getBytes());
        return fileRepository.save(fileDb);
    }

    public FileDb updateImage(int id, MultipartFile updatedFile) throws IOException {
        String fileName = StringUtils.cleanPath(updatedFile.getOriginalFilename());
        FileDb fileDb = new FileDb(fileName, updatedFile.getContentType(), updatedFile.getBytes());
        fileDb.setId(id);
        return fileRepository.save(fileDb);
    }

    public FileDb getFile(Integer id) {
        return fileRepository.findById(id).get();
    }

    public Stream<FileDb> getAllFiles() {
        return fileRepository.findAll().stream();
    }

}
