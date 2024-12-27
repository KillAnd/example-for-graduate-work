package ru.skypro.homework.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private String filePath; //путь
    private long fileSize; //размер файла в байтах
    private String mediaType; //тип медиа

    @Lob
    @Column(name = "data")
    private byte[] data;

}
