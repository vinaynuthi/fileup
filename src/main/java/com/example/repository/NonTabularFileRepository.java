package com.example.repository;

import com.example.entity.NonTabularFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NonTabularFileRepository extends JpaRepository<NonTabularFileEntity, Long> {
    Optional<NonTabularFileEntity> findByFileNameIgnoreCase(String fileName);
}