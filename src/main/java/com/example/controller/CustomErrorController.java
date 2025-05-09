package com.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        Object message = request.getAttribute("jakarta.servlet.error.message");
        Object uri = request.getAttribute("jakarta.servlet.error.request_uri");

        // Skip logging for favicon.ico 404 errors
        if (uri != null && uri.toString().equals("/favicon.ico") && status != null && status.equals(HttpStatus.NOT_FOUND.value())) {
            return "error"; // Return the error page without logging
        }

        // Log other errors
        logger.error("Error occurred: Status={}, URI={}, Message={}", status, uri, message);
        logger.error("Exception details: ", new Exception());

        model.addAttribute("status", status);
        model.addAttribute("uri", uri);
        model.addAttribute("message", message);

        return "error"; // Assumes you have an error.html template
    }
}