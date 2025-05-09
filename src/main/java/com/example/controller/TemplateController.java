package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/data")
public class TemplateController {

    @GetMapping("/uploaded-files-page") // Changed mapping
    public String uploadedFilesPage() {
        return "uploadedfiles";
    }

    // Other mappings...
}