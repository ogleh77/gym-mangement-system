package com.example.gymmanagementsystem.xlsfiles;

import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import javafx.collections.ObservableList;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;

public class PrintOfflineCustomersXls extends PrintToExcel{
    public static void exportOnlineCustomersToExcel(ObservableList<Customers> customers, File file) {

        sheet.setColumnWidth(0, 8000);

        sheet.setColumnWidth(4, 4500);

        sheet.setColumnWidth(6, 4500);

        sheet.setZoom(150);

        createHeader(true);

        int rowCounter = 2;

        for (Customers customer : customers) {
            createOfflineRows(customer, rowCounter);
            rowCounter++;
        }

        saveToXlsFile(file);
    }


    private static void createOfflineRows(Customers customer, int rowCount) {
        sameCode(customer, rowCount, sheet.createRow(rowCount));
    }

    static void sameCode(Customers customer, int rowCount, XSSFRow row2) {
        for (Payments payment : customer.getPayments()) {
            if (!customer.getPayments().isEmpty()) {

                row2.createCell(0).setCellValue(customer.getFirstName() + " " +
                        customer.getMiddleName() + " " + customer.getLastName());
                row2.createCell(1).setCellValue(customer.getPhone());
                row2.createCell(2).setCellValue(customer.getGander());

                row2.createCell(3).setCellValue(payment.getStartDate().toString());
                row2.createCell(4).setCellValue(payment.getExpDate().toString());
                row2.createCell(5).setCellValue("  $" + payment.getAmountPaid());
                row2.createCell(6).setCellValue(payment.getPaidBy());
                row2.createCell(7).setCellValue("  $" + payment.getDiscount());
                row2.createCell(8).setCellValue(payment.isPoxing() ? "Yes(√)" : "No(X)");
                row2.createCell(9).setCellValue(payment.isOnline() ? "yyy(√)" : "No(X)");
                row2.createCell(10).setCellValue(payment.isPending() ? "Yes(√)" : "No(X)");
                rowCount++;
            }

        }
    }
}
