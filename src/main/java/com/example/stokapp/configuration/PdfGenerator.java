package com.example.stokapp.configuration;

import com.example.stokapp.sale.domain.Sale;
import com.example.stokapp.sale.domain.SaleService;
import com.example.stokapp.sale.infrastructure.SaleRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PdfGenerator {


    @Autowired
    private SaleRepository saleRepository;

    public static File generateSalesPdf(String fileName, List<Sale> sales, String title) throws IOException {
        File file = new File(fileName + ".pdf");
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph(title).setBold().setFontSize(20));

        // Crear tabla con ancho del 100%
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 2, 2, 3}));
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados de la tabla
        table.addHeaderCell(new Cell().add(new Paragraph("Sale ID").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Product").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Amount").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Sale Cant").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));

        // Agregar filas a la tabla
        for (Sale sale : sales) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(sale.getId()))));
            table.addCell(new Cell().add(new Paragraph(sale.getInventory().getProduct().getName())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(sale.getAmount()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(sale.getSaleCant()))));
            table.addCell(new Cell().add(new Paragraph(sale.getCreatedAt().toString())));
        }

        document.add(table);
        document.close();
        writer.close();

        return file;
    }


    public File generateAnnualSalesReport(Long ownerId, String email, int year) throws IOException {
        List<Sale> sales = saleRepository.findByOwnerIdAndYear(ownerId, year);

        // Generar PDF
        File pdfFile = new File("AnnualSalesReport_" + year + ".pdf");
        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Email: " + email).setBold().setFontSize(16));
        document.add(new Paragraph("Year: " + year).setBold().setFontSize(16));
        document.add(new Paragraph("Total Sales: " + sales.size()).setBold().setFontSize(16));

        Map<Month, Long> salesByMonth = sales.stream()
                .collect(Collectors.groupingBy(sale -> sale.getCreatedAt().getMonth(), Collectors.counting()));

        Table salesTable = new Table(UnitValue.createPercentArray(new float[]{2, 2}));
        salesTable.setWidth(UnitValue.createPercentValue(100));

        salesTable.addHeaderCell("Month");
        salesTable.addHeaderCell("Sales Count");

        for (Month month : Month.values()) {
            long salesCount = salesByMonth.getOrDefault(month, 0L);
            salesTable.addCell(new Paragraph(month.name()));
            salesTable.addCell(new Paragraph(String.valueOf(salesCount)));
        }
        document.add(salesTable);

        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Sales by Month")
                .xAxisTitle("Month")
                .yAxisTitle("Sales")
                .theme(Styler.ChartTheme.Matlab)
                .build();

        int[] months = IntStream.rangeClosed(1, 12).toArray();
        int[] salesCounts = IntStream.rangeClosed(1, 12)
                .map(m -> salesByMonth.getOrDefault(Month.of(m), 0L).intValue())
                .toArray();

        chart.addSeries("Sales", months, salesCounts);

        byte[] chartBytes = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);

        Image img = new Image(com.itextpdf.io.image.ImageDataFactory.create(chartBytes));
        img.setWidth(UnitValue.createPercentValue(100));
        document.add(img);

        document.close();
        writer.close();

        return pdfFile;
    }
}
