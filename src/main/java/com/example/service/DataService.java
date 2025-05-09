package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.repository.FileMetadataRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class DataService {

    private final FileMetadataRepository repository;
    private final JdbcTemplate jdbcTemplate;
    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    public DataService(FileMetadataRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
        // Ensure the upload directory exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    public void importData(MultipartFile file, String tableName, String customName) throws Exception {
        FileMetadata metadata = new FileMetadata();
        String originalFileName = file.getOriginalFilename();
        String fileName = customName != null && !customName.isEmpty() ? customName : originalFileName;

        // Generate a unique file name to avoid overwriting
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
        String filePath = UPLOAD_DIR + uniqueFileName;

        // Save the file to the file system
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());

        metadata.setFileName(fileName);
        metadata.setFileType(file.getContentType());
        metadata.setFileSize(file.getSize());
        metadata.setUploadDate(new java.util.Date().toString());
        metadata.setTableName(tableName);
        metadata.setBaseName(originalFileName);
        metadata.setIsTabular(tableName != null && !tableName.isEmpty());
        metadata.setFilePath(filePath);

        repository.save(metadata);
    }

    public List<FileMetadata> getAllFilesMetadata() {
        return repository.findAll();
    }

    public Map<String, Object> getTableData(String name) throws Exception {
        Optional<FileMetadata> metadataOpt = repository.findByFileName(name);
        if (metadataOpt.isEmpty()) {
            throw new Exception("File not found: " + name);
        }

        FileMetadata metadata = metadataOpt.get();
        if (!metadata.getIsTabular() || metadata.getTableName() == null) {
            return Map.of(
                "metadata", metadata,
                "isTabular", false
            );
        }

        String tableName = metadata.getTableName();
        // Fetch column names
        List<String> columns = jdbcTemplate.queryForList(
            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND TABLE_SCHEMA = '1962mp'",
            String.class,
            tableName
        );

        if (columns.isEmpty()) {
            throw new Exception("Table not found: " + tableName);
        }

        // Fetch data rows
        List<List<Object>> rows = jdbcTemplate.query(
            "SELECT * FROM " + tableName,
            (rs, rowNum) -> {
                List<Object> row = new java.util.ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getObject(i));
                }
                return row;
            }
        );

        return Map.of(
            "metadata", metadata,
            "isTabular", true,
            "columns", columns,
            "rows", rows
        );
    }

    public Optional<FileMetadata> getFileMetadataByName(String fileName) {
        return repository.findByFileName(fileName);
    }

    @Transactional
    public void deleteByFileName(String fileName) {
        Optional<FileMetadata> metadataOpt = repository.findByFileName(fileName);
        if (metadataOpt.isPresent()) {
            FileMetadata metadata = metadataOpt.get();
            // Delete the file from the file system
            if (metadata.getFilePath() != null) {
                File file = new File(metadata.getFilePath());
                if (file.exists()) {
                    file.delete();
                }
            }
            repository.deleteByFileName(fileName);
        }
    }
}