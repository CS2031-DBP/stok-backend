package com.example.stokapp.event;

import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.sale.domain.SaleService;
import org.springframework.stereotype.Component;
import com.example.stokapp.configuration.EmailService;
import com.example.stokapp.configuration.PdfGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.List;

@Component
public class SalesReportListener {
    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SaleService saleService;

    //MANDAR POR MES
    @EventListener
    public void handleMonthlySalesReportEvent(MonthlySalesReportEvent event) {
        try {
            List<Sale> sales = saleService.findByOwnerIdAndMonth(event.getOwnerId(), event.getMonth(), event.getYear());
            String title = "Sales Report for " + Month.of(event.getMonth()).name() + " " + event.getYear();
            File pdfFile = pdfGenerator.generateSalesPdf("sales_report", sales, title);
            emailService.sendEmailWithAttachment(event.getEmail(), "Monthly Sales Report", "Please find attached the sales report for the month.", pdfFile);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }


    //MANDAR ANUAL SIN GRAFICO
    @EventListener
    public void handleAnnualSalesReportEventWithoutGraphic(AnnualSalesReportEvent event) {
        try {
            File pdfFile = pdfGenerator.generateAnnualSalesReport(event.getOwnerId(), event.getEmail(), event.getYear());
            emailService.sendEmailWithAttachment(event.getEmail(), "Annual Sales Report", "Please find attached the annual sales report.", pdfFile);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    //MANDAR ANUAL CON GRAFICO
    @EventListener
    public void handleAnnualSalesReportEventWithGraphic(AnnualSalesReportEventWithGraphic event) {
        try {
            File pdfFile = pdfGenerator.generateAnnualSalesReportWithGraphics(event.getOwnerId(), event.getEmail(), event.getYear());
            emailService.sendEmailWithAttachment(event.getEmail(), "Annual Sales Report with Graphics", "Please find attached the annual sales report with graphics.", pdfFile);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
