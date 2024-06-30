package com.example.stokapp.configuration;

import com.example.stokapp.inventory.domain.Inventory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class PdfGenerator {

    public static File generatePdf(String fileName, String email, String name) throws IOException {
        File file = new File(fileName);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Email: " + email));
        document.add(new Paragraph("Name: " + name));

        document.close();
        writer.close();

        return file;
    }

    public static File generatePdfLowStock(String fileName, String email, Inventory inventory) throws IOException {
        File file = new File(fileName + ".pdf");
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("CORREO ENVIADO A: " + email + " " + inventory.getOwner().getFirstName()));
        document.add(new Paragraph("NOMBRE DEL PRODUCTO BAJO EN STOCK: " + inventory.getProduct().getName()));
        document.add(new Paragraph("STOCK DEL PRODUCTO: " + inventory.getStock()));
        document.add(new Paragraph("CONTACTAR CON EL SUPPLIER, EL STOCK SE ACABARA PRONTO"));

        document.close();
        writer.close();
        return file;
    }
}
