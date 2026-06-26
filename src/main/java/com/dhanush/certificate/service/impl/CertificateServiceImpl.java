package com.dhanush.certificate.service.impl;

import com.dhanush.certificate.dto.CertificateRequestDTO;
import com.dhanush.certificate.dto.CertificateResponseDTO;
import com.dhanush.certificate.entity.Certificate;
import com.dhanush.certificate.repository.CertificateRepository;
import com.dhanush.certificate.service.CertificateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final String UPLOAD_DIR = "uploads/certificates/";

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public CertificateResponseDTO generateCertificate(CertificateRequestDTO request) {

        try {
            String basePath = System.getProperty("user.dir");

            String fileName = "cert_" + System.currentTimeMillis() + ".pdf";
            String filePath = basePath + "/" + UPLOAD_DIR + fileName;

            File file = new File(filePath);
            file.getParentFile().mkdirs();

            // Load HTML
            ClassPathResource resource = new ClassPathResource("templates/certificate.html");
            String html = new String(resource.getInputStream().readAllBytes());

            // Replace values
            String logoPath = "file:///" + basePath.replace("\\", "/") + "/uploads/logo/logo.png";

            html = html.replace("${name}", request.getName());
            html = html.replace("${score}", String.valueOf(request.getScore()));
            html = html.replace("${date}", LocalDate.now().toString());
            html = html.replace("${logoPath}", logoPath);

            // Convert to PDF
            OutputStream os = new FileOutputStream(file);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(os);
            os.close();

            // Save to DB
            Certificate certificate = new Certificate();
            certificate.setName(request.getName());
            certificate.setScore(request.getScore());
            certificate.setFilePath(filePath);
            certificate.setIssueDate(LocalDateTime.now());

            certificate = certificateRepository.save(certificate);
            
            // Response
            CertificateResponseDTO response = new CertificateResponseDTO();
            response.setId(certificate.getId());
            response.setMessage("Certificate generated successfully");
            response.setFilePath(filePath);
            response.setDownloadUrl("/api/certificates/download/" + certificate.getId());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating certificate");
        }
    }

    @Override
    public Resource downloadCertificate(Long id) {

        try {
            Certificate cert = certificateRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Certificate not found"));

            Path path = Paths.get(cert.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            return resource;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error downloading file");
        }
    }
}