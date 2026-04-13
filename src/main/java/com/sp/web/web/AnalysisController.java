package com.sp.web.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    // This endpoint expects a file uploaded from Vue
    @PostMapping("/volcano")
    public ResponseEntity<byte[]> generateVolcanoPlot(@RequestParam("file") MultipartFile file) {
        try {
            // 1. Read the CSV file uploaded by the user into a plain text String
            String csvData = new String(file.getBytes(), StandardCharsets.UTF_8);

            // 2. Prepare to talk to our R server
            RestTemplate restTemplate = new RestTemplate();
            String plumberUrl = "http://localhost:8000/volcano";

            // 3. Send the CSV text to R, and ask for a byte[] (the PNG image) back!
            byte[] imageBytes = restTemplate.postForObject(plumberUrl, csvData, byte[].class);

            // 4. Wrap the image bytes in an HTTP response and send it to Vue
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println("Error talking to R: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}