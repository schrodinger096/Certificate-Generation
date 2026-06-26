package com.dhanush.certificate.controller;

import com.dhanush.certificate.dto.CertificateRequestDTO;
import com.dhanush.certificate.dto.CertificateResponseDTO;
import com.dhanush.certificate.service.CertificateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/certificates")
@CrossOrigin("*")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    //  Generate Certificate
    @PostMapping("/generate")
    public ResponseEntity<CertificateResponseDTO> generateCertificate(
            @Valid @RequestBody CertificateRequestDTO request) {

        CertificateResponseDTO response = certificateService.generateCertificate(request);
        return ResponseEntity.ok(response);
    }

    //  Download Certificate (BY ID ✅)
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable Long id) {

        Resource resource = certificateService.downloadCertificate(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate_" + id + ".pdf")
                .body(resource);
    }

    //  Preview Certificate (open in browser)
    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> previewCertificate(@PathVariable Long id) {

        Resource resource = certificateService.downloadCertificate(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=certificate_" + id + ".pdf")
                .body(resource);
    }
}