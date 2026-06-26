package com.dhanush.certificate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CertificateRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Score is required")
    private Integer score;

    // 🔹 Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}