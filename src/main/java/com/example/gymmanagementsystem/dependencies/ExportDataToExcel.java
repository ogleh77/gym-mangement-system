package com.example.gymmanagementsystem.dependencies;

import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.main.Payments;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class ExportDataToExcel {

    public static void exportAllCustomersToXls(ObservableList<Customers> customers, File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Customers");

            sheet.setColumnWidth(0, 8000);

            sheet.setColumnWidth(4, 4500);

            sheet.setColumnWidth(6, 4500);

            sheet.setZoom(150);

            createHeader(true, sheet);

            int rowCount = 2;
            for (Customers customer : customers) {

                commonRows(sheet, rowCount, customer);
                rowCount++;
            }
            saveToXlsFile(file, workbook);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void exportOnlineCustomersToExcel(ObservableList<Customers> customers, File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("online customers");
            sameFieldsOfflineAndOnline(customers, file, sheet, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void exportOfflineCustomersToExcel(ObservableList<Customers> customers, File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("offline customers");
            sameFieldsOfflineAndOnline(customers, file, sheet, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportPendingCustomersToExcel(ObservableList<Customers> customers, File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("pending customers");
            sameFieldsOfflineAndOnline(customers, file, sheet, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-------------------------helpers---------------------

    private static void commonRows(XSSFSheet sheet, int rowCount, Customers customer) {

        XSSFRow row = sheet.createRow(rowCount);

        row.createCell(0).setCellValue(customer.getFirstName() + " " +
                customer.getMiddleName() + " " + customer.getLastName());
        row.createCell(1).setCellValue(customer.getPhone());
        row.createCell(2).setCellValue(customer.getGander());
        row.createCell(3).setCellValue(customer.getShift());
        row.createCell(4).setCellValue(customer.getAddress());
        row.createCell(5).setCellValue(customer.getWeight() + " kg");
        row.createCell(6).setCellValue(customer.getWhoAdded());
        row.createCell(7).setCellValue(customer.getForeArm() + " cm");
        row.createCell(8).setCellValue(customer.getHips() + " cm");
        row.createCell(9).setCellValue(customer.getChest() + " cm");
        row.createCell(10).setCellValue(customer.getWaist() + " cm");
    }
    private static void sameFieldsOfflineAndOnline(ObservableList<Customers> customers, File file, XSSFSheet sheet, boolean isOffline) {
        sheet.setColumnWidth(0, 8000);

        sheet.setColumnWidth(4, 4500);

        sheet.setColumnWidth(6, 4500);

        sheet.setZoom(150);

        createHeader(false, sheet);

        int rowCounter = 2;

        for (Customers customer : customers) {

            for (Payments payment : customer.getPayments()) {
                if (!customer.getPayments().isEmpty()) {
                    XSSFRow row = sheet.createRow(rowCounter);
                    row.createCell(0).setCellValue(customer.getFirstName() + " " +
                            customer.getMiddleName() + " " + customer.getLastName());
                    row.createCell(1).setCellValue(customer.getPhone());
                    row.createCell(2).setCellValue(customer.getGander());

                    row.createCell(3).setCellValue(payment.getStartDate().toString());
                    row.createCell(4).setCellValue(payment.getExpDate().toString());
                    row.createCell(5).setCellValue("  $" + payment.getAmountPaid());
                    row.createCell(6).setCellValue(payment.getPaidBy());
                    row.createCell(7).setCellValue("  $" + payment.getDiscount());
                    row.createCell(8).setCellValue(payment.isPoxing() ? "Yes(√)" : "No(X)");
                    row.createCell(9).setCellValue(payment.isOnline() ? "Yes(√)" : "No(X)");
                    row.createCell(10).setCellValue(payment.isPending() ? "Yes(√)" : "No(X)");
                    rowCounter++;
                }
            }
            if (isOffline) rowCounter++;

        }

        saveToXlsFile(file, sheet.getWorkbook());
    }
    private static void createHeader(boolean customersOnly, Sheet sheet) {

        XSSFRow header = (XSSFRow) sheet.createRow(0);

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
    private static void saveToXlsFile(File file, Workbook workbook) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
