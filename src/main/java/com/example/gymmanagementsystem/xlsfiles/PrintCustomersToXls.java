package com.example.gymmanagementsystem.xlsfiles;

import com.example.gymmanagementsystem.entities.main.Customers;
import javafx.collections.ObservableList;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;

public class PrintCustomersToXls extends PrintToExcel {

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


    private static void createRows(Customers customer, int rowCount) {
        PrintToExcel.rowSame(customer, rowCount);
    }

}
