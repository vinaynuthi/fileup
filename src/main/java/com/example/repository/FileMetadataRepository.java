package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.service.FileMetadata;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    @Modifying
    @Query("DELETE FROM FileMetadata fm WHERE fm.fileName = :fileName")
    void deleteByFileName(String fileName);

    Optional<FileMetadata> findByFileName(String fileName);
}