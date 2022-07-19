package com.germanovich.springboot.petsitterApp.dao;

import com.germanovich.springboot.petsitterApp.entity.FileDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileDb, Integer> {

}
