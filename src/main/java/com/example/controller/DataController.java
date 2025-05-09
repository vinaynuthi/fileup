package com.example.controller;

import com.example.service.DataService;
import com.example.service.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    // GET /data/upload - Render the upload form
    @GetMapping("/upload")
    public String uploadForm(Model model) {
        return "upload";
    }

    // POST /data/upload - Handle file upload
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam(value = "tableName", required = false) String tableName,
                             @RequestParam(value = "customName", required = false) String customName,
                             Model model) {
        try {
            dataService.importData(file, tableName, customName);
            model.addAttribute("message", "File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            model.addAttribute("error", "Error uploading file: " + e.getMessage());
        }
        return "upload";
    }

    // GET /data/uploadedfiles - Render the list of uploaded files
    @GetMapping("/uploadedfiles")
    public String uploadedFiles(Model model) {
        List<FileMetadata> files = dataService.getAllFilesMetadata();
        System.out.println("Number of files retrieved: " + files.size()); // Temporary logging
        model.addAttribute("files", files);
        return "uploadedfiles";
    }

    // GET /data/view - Render the view data page
    @GetMapping("/view")
    public String viewData(Model model) {
        return "view";
    }

    // GET /data/view/{fileName} - Render the view data page with pre-filled file name
    @GetMapping("/view/{fileName}")
    public String viewDataByFileName(@PathVariable("fileName") String fileName, Model model) throws Exception {
        Map<String, Object> data = dataService.getTableData(fileName);
        model.addAllAttributes(data);
        model.addAttribute("name", fileName); // Pre-fill the form
        return "view";
    }

    // POST /data/view - Handle form submission to view data
    @PostMapping("/view")
    public String viewDataPost(@RequestParam("name") String name, Model model) throws Exception {
        Map<String, Object> data = dataService.getTableData(name);
        model.addAllAttributes(data);
        return "view";
    }

    // GET /data/delete - Render the delete page
    @GetMapping("/delete")
    public String deleteForm(Model model) {
        return "delete";
    }

    // GET /data/delete/{fileName} - Redirect to POST /data/delete for compatibility
    @GetMapping("/delete/{fileName}")
    public String deleteFileRedirect(@PathVariable("fileName") String fileName, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("fileName", fileName);
        return "redirect:/data/delete";
    }

    // POST /data/delete - Handle file deletion
    @PostMapping("/delete")
    public String deleteFile(@RequestParam("fileName") String fileName, RedirectAttributes redirectAttributes) {
        try {
            dataService.deleteByFileName(fileName);
            redirectAttributes.addFlashAttribute("message", "File deleted successfully: " + fileName);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting file: " + e.getMessage());
        }
        return "redirect:/";
    }

    // GET /data/details/{fileName} - Render the file details page
    @GetMapping("/details/{fileName}")
    public String fileDetails(@PathVariable("fileName") String fileName, Model model) {
        Optional<FileMetadata> metadata = dataService.getFileMetadataByName(fileName);
        if (metadata.isPresent()) {
            model.addAttribute("metadata", metadata.get());
        } else {
            model.addAttribute("error", "File not found: " + fileName);
        }
        return "details";
    }

    // GET /data/download/{fileName} - Serve the file for download
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) throws Exception {
        Optional<FileMetadata> metadataOpt = dataService.getFileMetadataByName(fileName);
        if (metadataOpt.isEmpty()) {
            throw new Exception("File not found: " + fileName);
        }

        FileMetadata metadata = metadataOpt.get();
        File file = new File(metadata.getFilePath());
        if (!file.exists()) {
            throw new Exception("File not found on server: " + metadata.getFilePath());
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metadata.getBaseName() + "\"")
                .body(resource);
    }
}