package com.example.stokapp.event;

import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.configuration.PdfGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class AnnualSalesReportListener {

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private EmailService emailService;

    @EventListener
    public void handleAnnualSalesReportEvent(AnnualSalesReportEvent event) {
        try {
            File pdfFile = pdfGenerator.generateAnnualSalesReport(event.getOwnerId(), event.getEmail(), event.getYear());
            emailService.sendEmailWithAttachment(event.getEmail(), "Annual Sales Report", "Please find attached the annual sales report.", pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}