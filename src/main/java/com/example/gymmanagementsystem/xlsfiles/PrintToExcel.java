package com.example.gymmanagementsystem.xlsfiles;

import com.example.gymmanagementsystem.controllers.printer.PrinterToXls;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;

public class PrintToExcel {
    public final static Workbook workbook;
    public final static XSSFSheet sheet;

    static {
        workbook = new XSSFWorkbook();
        sheet = (XSSFSheet) workbook.createSheet("Students");
    }

    public static void exportCustomersToExcel(ObservableList<Customers> customers, File file) {

        sheet.setColumnWidth(0, 8000);

        sheet.setColumnWidth(4, 4500);

        sheet.setColumnWidth(6, 4500);

        sheet.setZoom(150);

        createHeader(true);

        int rowCounter = 2;

        for (Customers customer : customers) {
            createRows(customer, rowCounter);
            rowCounter++;
        }

        saveToXlsFile(file);
    }



    public static void createHeader(boolean customersOnly) {

        XSSFRow header = sheet.createRow(0);

        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeightInPoints((short) 19);
        font.setFamily(FontFamily.ROMAN);
        font.setBold(true);
        style.setFont(font);
        header.setRowStyle(style);

        if (customersOnly) {
            header.createCell(0).setCellValue("Full name");
            header.createCell(1).setCellValue("Phone");
            header.createCell(2).setCellValue("Gender");
            header.createCell(3).setCellValue("Shift");
            header.createCell(4).setCellValue("Address");
            header.createCell(5).setCellValue("Weight");
            header.createCell(6).setCellValue("Who registered");
            header.createCell(7).setCellValue("Fore arm");
            header.createCell(8).setCellValue("Hips");
            header.createCell(9).setCellValue("Chest");
            header.createCell(10).setCellValue("Waist");

        } else {

            header.createCell(0).setCellValue("Full name");
            header.createCell(1).setCellValue("Phone");
            header.createCell(2).setCellValue("Gender");

            header.createCell(3).setCellValue("Started date");
            header.createCell(4).setCellValue("Expired Date");
            header.createCell(5).setCellValue("Amount paid");
            header.createCell(6).setCellValue("Paid by");
            header.createCell(7).setCellValue("Discount");
            header.createCell(8).setCellValue("Poxing");
            header.createCell(9).setCellValue("Online");
            header.createCell(10).setCellValue("Pending");

        }
    }

    private static void createRows(Customers customer, int rowCount) {
        rowSame(customer, rowCount);
    }

    static void rowSame(Customers customer, int rowCount) {
        PrinterToXls.commonRows(sheet, rowCount, customer);
    }
    public static void saveToXlsFile(File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
