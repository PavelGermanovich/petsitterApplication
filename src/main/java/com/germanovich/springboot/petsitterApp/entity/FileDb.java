package com.germanovich.springboot.petsitterApp.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity(name = "file")
public class FileDb {
    @Id
    @GenericGenerator(name="auto" , strategy="increment")
    @GeneratedValue(generator = "auto")
    @Column(name = "file_pk")
    private int id;

    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String type;

    @Lob
    @Type(type="org.hibernate.type.ImageType")
    private byte[] data;

    public FileDb() {
    }

    public FileDb(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
