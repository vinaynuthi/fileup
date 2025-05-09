package com.example.service;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "file_metadata")
public class FileMetadata {
    @Id
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String uploadDate;
    private String tableName;
    private String baseName;
    private Boolean isTabular;
    private String filePath;

    // Getters
    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getFileType() { return fileType; }
    public Long getFileSize() { return fileSize; }
    public String getUploadDate() { return uploadDate; }
    public String getTableName() { return tableName; }
    public String getBaseName() { return baseName; }
    public Boolean getIsTabular() { return isTabular; }
    public String getFilePath() { return filePath; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    public void setUploadDate(String uploadDate) { this.uploadDate = uploadDate; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    public void setBaseName(String baseName) { this.baseName = baseName; }
    public void setIsTabular(Boolean isTabular) { this.isTabular = isTabular; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}