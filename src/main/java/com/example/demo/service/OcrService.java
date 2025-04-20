package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class OcrService {
    @Value("classpath:static/bg.PNG")
    private Resource resource;

    @Autowired
    OllamaChatService ollamaChatService;

//    @Value("${tesseract.datapath:C:\\Program Files\\Tesseract-OCR\\tessdata}")
//    private String tesseractDataPath;

    @Value("${tesseract.language:eng}")
    private String tesseractLanguage;

    @Value("${tesseract.dpi:300}")
    private String tesseractDpi;

    private Tesseract tesseract;

    @PostConstruct
    public void init() {
        // Initialize Tesseract once during service startup
        tesseract = new Tesseract();
        // In your OCR service
       tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
        tesseract.setLanguage(tesseractLanguage);
        tesseract.setTessVariable("user_defined_dpi", tesseractDpi);
    }

    public String extractTextFromImage() throws IOException, TesseractException {
        Path tempFile = null;
        try {
            // Create a temporary file
            tempFile = Files.createTempFile("tempImage", getFileExtension(resource.getFilename()));

            // Copy the resource content to the temporary file
            try (InputStream inputStream = resource.getInputStream()) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Perform OCR on the image
            String result = tesseract.doOCR(tempFile.toFile());
            System.out.println(result);
            ollamaChatService.promptTemplate(result);

            return result;
        } finally {
            // Always clean up temporary files
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
        }
    }

    /**
     * Extract text from a specific resource
     */
    public String extractTextFromResource(Resource imageResource) throws IOException, TesseractException {
        Path tempFile = null;
        try {
            // Create a temporary file
            tempFile = Files.createTempFile("tempImage", getFileExtension(imageResource.getFilename()));

            // Copy the resource content to the temporary file
            try (InputStream inputStream = imageResource.getInputStream()) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Perform OCR on the image
            return tesseract.doOCR(tempFile.toFile());
        } finally {
            // Always clean up temporary files
            if (tempFile != null) {
                Files.deleteIfExists(tempFile);
            }
        }
    }

    /**
     * Helper method to extract file extension
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return ".tmp";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex);
        }
        return ".tmp";
    }
}
