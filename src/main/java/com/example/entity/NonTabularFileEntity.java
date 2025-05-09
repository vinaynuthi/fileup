package com.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "non_tabular_files")
public class NonTabularFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false, unique = true)
    private String fileName;

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    // Constructors
    public NonTabularFileEntity() {}

    public NonTabularFileEntity(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
}