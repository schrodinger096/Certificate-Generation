package com.dhanush.certificate.service;

import com.dhanush.certificate.dto.CertificateRequestDTO;
import com.dhanush.certificate.dto.CertificateResponseDTO;
import org.springframework.core.io.Resource;

public interface CertificateService {

    CertificateResponseDTO generateCertificate(CertificateRequestDTO request);

    Resource downloadCertificate(Long id);
}