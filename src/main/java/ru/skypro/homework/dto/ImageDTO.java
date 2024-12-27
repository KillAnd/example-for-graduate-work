package ru.skypro.homework.dto;

/**
 * Класс, представляющий данные об изображении.
 * Используется для передачи информации об изображении между слоями приложения.
 */
public class ImageDTO {

    /**
     * Уникальный идентификатор изображения.
     */
    private long id;

    /**
     * Путь к файлу изображения.
     */
    private String filePath;

    /**
     * Размер файла изображения в байтах.
     */
    private Long fileSize;

    /**
     * Тип медиа (MIME-тип) изображения.
     */
    private String mediaType;

    /**
     * Бинарные данные изображения.
     */
    private byte[] data;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
