package com.example.stokapp.event;

import com.example.stokapp.configuration.PdfGenerator;
import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.sale.domain.SaleService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.List;


@Component
public class SalesReportEventListener implements ApplicationListener<SalesReportEvent> {

    @Autowired
    private SaleService saleService;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(SalesReportEvent event) {
        try {
            String title;
            List<Sale> sales;

            if (event.getMonth() != null && event.getYear() != null) {
                title = "Sales Report for " + Month.of(event.getMonth()).name() + " " + event.getYear();
                sales = saleService.findByOwnerIdAndMonth(event.getOwnerId(), event.getMonth(), event.getYear());
            } else if (event.getYear() != null) {
                title = "Sales Report for " + event.getYear();
                sales = saleService.findByOwnerIdAndYear(event.getOwnerId(), event.getYear());
            } else {
                title = "All Sales Report";
                sales = saleService.findByOwnerId(event.getOwnerId());
            }

            File pdfFile = pdfGenerator.generateSalesPdf("sales_report", sales, title);
            sendEmailWithAttachment(event.getEmail(), "Sales Report", "Please find attached the sales report.", pdfFile);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendEmailWithAttachment(String to, String subject, String text, File file) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addAttachment(file.getName(), file);

        mailSender.send(message);
    }
}