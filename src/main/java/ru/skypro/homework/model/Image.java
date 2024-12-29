package ru.skypro.homework.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Сущность, представляющая изображение (Image).
 * Содержит информацию о изображении, включая его идентификатор, путь к файлу, размер файла, тип медиа и данные изображения.
 */
@Data
@Entity
public class Image {

    /**
     * Уникальный идентификатор изображения.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Путь к файлу изображения.
     */
    private String filePath;

    /**
     * Размер файла изображения в байтах.
     */
    private long fileSize;

    /**
     * Тип медиа (MIME-тип) изображения.
     */
    private String mediaType;

    /**
     * Данные изображения в виде массива байтов.
     */
    @Column(name = "data")
    @Type(type="org.hibernate.type.BinaryType")
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

}
