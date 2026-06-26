package com.dhanush.certificate.util;

import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;

@Component
public class PdfGeneratorUtil {

    public void generatePdf(String htmlContent, String outputPath) {
        try (OutputStream os = new FileOutputStream(outputPath)) {

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF");
        }
    }
}