package com.example.gymmanagementsystem.xlsfiles;

import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import javafx.collections.ObservableList;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;

public class PrintOnlineCustomersXls extends PrintToExcel {

    public static void exportOnlineCustomersToExcel(ObservableList<Customers> customers, File file) {

        sheet.setColumnWidth(0, 8000);

        sheet.setColumnWidth(4, 4500);

        sheet.setColumnWidth(6, 4500);

        sheet.setZoom(150);

        createHeader(true);

        int rowCounter = 2;

        for (Customers customer : customers) {
            createOnlineRows(customer, rowCounter);
            rowCounter++;
        }

        saveToXlsFile(file);
    }

    private static void createOnlineRows(Customers customer, int rowCount) {
        PrintOfflineCustomersXls.sameCode(customer, rowCount, sheet.createRow(rowCount));
    }


}
