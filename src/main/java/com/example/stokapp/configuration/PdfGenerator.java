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
import org.knowm.xchart.*;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Month;
import java.util.Arrays;
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

        // Producto más vendido
        Map.Entry<String, Long> mostSoldProduct = sales.stream()
                .collect(Collectors.groupingBy(sale -> sale.getInventory().getProduct().getName(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (mostSoldProduct != null) {
            document.add(new Paragraph("Producto más vendido: " + mostSoldProduct.getKey() + " - " + mostSoldProduct.getValue() + " ventas"));
        }

        // Tabla de categorías
        Map<String, Long> categorySales = sales.stream()
                .collect(Collectors.groupingBy(sale -> String.valueOf(sale.getInventory().getProduct().getCategory()), Collectors.counting()));

        Table categoryTable = new Table(UnitValue.createPercentArray(new float[]{3, 3}));
        categoryTable.setWidth(UnitValue.createPercentValue(100));

        categoryTable.addHeaderCell(new Cell().add(new Paragraph("Categoría").setBold()));
        categoryTable.addHeaderCell(new Cell().add(new Paragraph("Cantidad de Productos Vendidos").setBold()));

        for (Map.Entry<String, Long> entry : categorySales.entrySet()) {
            categoryTable.addCell(new Cell().add(new Paragraph(entry.getKey())));
            categoryTable.addCell(new Cell().add(new Paragraph(entry.getValue().toString())));
        }

        document.add(categoryTable);

        // Gráfico de categorías
        PieChart categoryChart = new PieChartBuilder().width(800).height(600).title("Ventas por Categoría").build();
        categoryChart.getStyler().setLegendVisible(false);
        categoryChart.getStyler().setAnnotationType(PieStyler.AnnotationType.LabelAndValue);

        categorySales.forEach(categoryChart::addSeries);

        byte[] categoryChartBytes = BitmapEncoder.getBitmapBytes(categoryChart, BitmapEncoder.BitmapFormat.PNG);
        Image categoryImg = new Image(com.itextpdf.io.image.ImageDataFactory.create(categoryChartBytes));
        categoryImg.setWidth(UnitValue.createPercentValue(100));
        document.add(categoryImg);

        // Tabla de ventas
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 2, 2, 3}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("SaleID").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Producto").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Cantidad Vendida").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Dinero de la Venta").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha").setBold()));

        double totalSaleCant = 0;

        for (Sale sale : sales) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(sale.getId()))));
            table.addCell(new Cell().add(new Paragraph(sale.getInventory().getProduct().getName())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(sale.getAmount()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(sale.getSaleCant()))));
            table.addCell(new Cell().add(new Paragraph(sale.getCreatedAt().toString())));

            totalSaleCant += sale.getSaleCant();
        }

        document.add(table);

        document.add(new Paragraph("Total de Ganancias: " + totalSaleCant).setBold().setFontSize(16));

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

        // Producto más vendido
        Map.Entry<String, Long> mostSoldProduct = sales.stream()
                .collect(Collectors.groupingBy(sale -> sale.getInventory().getProduct().getName(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (mostSoldProduct != null) {
            document.add(new Paragraph("Producto más vendido: " + mostSoldProduct.getKey() + " - " + mostSoldProduct.getValue() + " ventas"));
        }

        // Tabla de categorías
        Map<String, Long> categorySales = sales.stream()
                .collect(Collectors.groupingBy(sale -> String.valueOf(sale.getInventory().getProduct().getCategory()), Collectors.counting()));

        Table categoryTable = new Table(UnitValue.createPercentArray(new float[]{3, 3}));
        categoryTable.setWidth(UnitValue.createPercentValue(100));

        categoryTable.addHeaderCell(new Cell().add(new Paragraph("Categoría").setBold()));
        categoryTable.addHeaderCell(new Cell().add(new Paragraph("Cantidad de Productos Vendidos").setBold()));

        for (Map.Entry<String, Long> entry : categorySales.entrySet()) {
            categoryTable.addCell(new Cell().add(new Paragraph(entry.getKey())));
            categoryTable.addCell(new Cell().add(new Paragraph(entry.getValue().toString())));
        }

        document.add(categoryTable);

        Map<Month, Long> salesByMonth = sales.stream()
                .collect(Collectors.groupingBy(sale -> sale.getCreatedAt().getMonth(), Collectors.counting()));

        Map<Month, Double> earningsByMonth = sales.stream()
                .collect(Collectors.groupingBy(sale -> sale.getCreatedAt().getMonth(), Collectors.summingDouble(Sale::getSaleCant)));

        Table salesTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2}));
        salesTable.setWidth(UnitValue.createPercentValue(100));

        salesTable.addHeaderCell("Month");
        salesTable.addHeaderCell("Sales Count");
        salesTable.addHeaderCell("Ganancias");

        double totalEarnings = 0;

        for (Month month : Month.values()) {
            long salesCount = salesByMonth.getOrDefault(month, 0L);
            double earnings = earningsByMonth.getOrDefault(month, 0.0);
            totalEarnings += earnings;

            salesTable.addCell(new Paragraph(month.name()));
            salesTable.addCell(new Paragraph(String.valueOf(salesCount)));
            salesTable.addCell(new Paragraph(String.valueOf(earnings)));
        }

        document.add(salesTable);

        document.add(new Paragraph("Total de Ganancias: " + totalEarnings).setBold().setFontSize(16));

        document.close();
        writer.close();

        return pdfFile;
    }


    public File generateAnnualSalesReportWithGraphics(Long ownerId, String email, int year) throws IOException {
        List<Sale> sales = saleRepository.findByOwnerIdAndYear(ownerId, year);

        // Generar PDF
        File pdfFile = new File("AnnualSalesReportWithGraphics_" + year + ".pdf");
        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Email: " + email).setBold().setFontSize(16));
        document.add(new Paragraph("Year: " + year).setBold().setFontSize(16));
        document.add(new Paragraph("Total Sales: " + sales.size()).setBold().setFontSize(16));

        Map<Month, Long> salesByMonth = sales.stream()
                .collect(Collectors.groupingBy(sale -> sale.getCreatedAt().getMonth(), Collectors.counting()));

        Map<Month, Double> earningsByMonth = sales.stream()
                .collect(Collectors.groupingBy(sale -> sale.getCreatedAt().getMonth(), Collectors.summingDouble(Sale::getSaleCant)));

        Table salesTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2}));
        salesTable.setWidth(UnitValue.createPercentValue(100));

        salesTable.addHeaderCell("Month");
        salesTable.addHeaderCell("Sales Count");
        salesTable.addHeaderCell("Ganancias");

        double totalEarnings = 0;

        for (Month month : Month.values()) {
            long salesCount = salesByMonth.getOrDefault(month, 0L);
            double earnings = earningsByMonth.getOrDefault(month, 0.0);
            totalEarnings += earnings;

            salesTable.addCell(new Paragraph(month.name()));
            salesTable.addCell(new Paragraph(String.valueOf(salesCount)));
            salesTable.addCell(new Paragraph(String.valueOf(earnings)));
        }

        document.add(salesTable);

        document.add(new Paragraph("Total de Ganancias: " + totalEarnings).setBold().setFontSize(16));

        XYChart salesChart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Ventas por Mes")
                .xAxisTitle("Mes")
                .yAxisTitle("Ventas")
                .theme(Styler.ChartTheme.Matlab)
                .build();

        XYChart earningsChart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Ganancias por Mes")
                .xAxisTitle("Mes")
                .yAxisTitle("Ganancia")
                .theme(Styler.ChartTheme.Matlab)
                .build();

        List<Double> months = IntStream.rangeClosed(1, 12).boxed().map(Double::valueOf).collect(Collectors.toList());
        List<Double> salesCounts = IntStream.rangeClosed(1, 12)
                .mapToObj(m -> salesByMonth.getOrDefault(Month.of(m), 0L).doubleValue())
                .collect(Collectors.toList());
        List<Double> monthlyEarnings = IntStream.rangeClosed(1, 12)
                .mapToDouble(m -> earningsByMonth.getOrDefault(Month.of(m), 0.0))
                .boxed()
                .collect(Collectors.toList());

        salesChart.addSeries("Sales", months, salesCounts);
        earningsChart.addSeries("Earnings", months, monthlyEarnings);

        byte[] salesChartBytes = BitmapEncoder.getBitmapBytes(salesChart, BitmapEncoder.BitmapFormat.PNG);
        byte[] earningsChartBytes = BitmapEncoder.getBitmapBytes(earningsChart, BitmapEncoder.BitmapFormat.PNG);

        Image salesImg = new Image(com.itextpdf.io.image.ImageDataFactory.create(salesChartBytes));
        salesImg.setWidth(UnitValue.createPercentValue(100));
        document.add(salesImg);

        Image earningsImg = new Image(com.itextpdf.io.image.ImageDataFactory.create(earningsChartBytes));
        earningsImg.setWidth(UnitValue.createPercentValue(100));
        document.add(earningsImg);

        // Agregar gráfico de pastel para las categorías
        Map<String, Double> salesByCategory = sales.stream()
                .collect(Collectors.groupingBy(sale -> String.valueOf(sale.getInventory().getProduct().getCategory()),
                        Collectors.summingDouble(Sale::getSaleCant)));

        PieChart pieChart = new PieChartBuilder().width(800).height(600).title("Ventas por Categoría").build();

        salesByCategory.forEach(pieChart::addSeries);

        byte[] pieChartBytes = BitmapEncoder.getBitmapBytes(pieChart, BitmapEncoder.BitmapFormat.PNG);

        Image pieChartImg = new Image(com.itextpdf.io.image.ImageDataFactory.create(pieChartBytes));
        pieChartImg.setWidth(UnitValue.createPercentValue(100));
        document.add(pieChartImg);

        document.close();
        writer.close();

        return pdfFile;
    }
}

