package com.example.gymmanagementsystem.controllers.printers;

import com.example.gymmanagementsystem.entities.main.Customers;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;

public class CustomersPrinter extends Printer {


    public void printCustomers(ObservableList<Customers> customers, File file) {

        Sheet sheet = workbook.createSheet("Customers");

        columnWidth(sheet);
        Row firstRow = sheet.createRow(0);
        createFirstRowCells(firstRow);

        int rowCounter = 2;
        for (Customers customer : customers) {

            Row row = sheet.createRow(rowCounter);
            createCustomerRowCells(row, customer);

            rowCounter++;
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createFirstRowCells(Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue("Full name");
        cell.setCellStyle(cellStyle(true, 15));

        Cell cell1 = row.createCell(1);
        cell1.setCellValue("Phone");
        cell1.setCellStyle(cellStyle(true, 15));

        Cell cell2 = row.createCell(2);
        cell2.setCellValue("Gender");
        cell2.setCellStyle(cellStyle(true, 15));

        Cell cell3 = row.createCell(3);
        cell3.setCellValue("Shift");
        cell3.setCellStyle(cellStyle(true, 15));


        Cell cell4 = row.createCell(4);
        cell4.setCellValue("Address");
        cell4.setCellStyle(cellStyle(true, 15));

        Cell cell5 = row.createCell(5);
        cell5.setCellValue("Weight");
        cell5.setCellStyle(cellStyle(true, 15));

        Cell cell6 = row.createCell(6);
        cell6.setCellValue("Who registared");
        cell6.setCellStyle(cellStyle(true, 15));


        Cell cell7 = row.createCell(7);
        cell7.setCellValue("Fore arm");
        cell7.setCellStyle(cellStyle(true, 15));

        Cell cell8 = row.createCell(8);
        cell8.setCellValue("Hips");
        cell8.setCellStyle(cellStyle(true, 15));

        Cell cell9 = row.createCell(9);
        cell9.setCellValue("Chest");
        cell9.setCellStyle(cellStyle(true, 15));

        Cell cell10 = row.createCell(10);
        cell10.setCellValue("Waist");
        cell10.setCellStyle(cellStyle(true, 15));

    }

    private void createCustomerRowCells(Row row, Customers customer) {
        Cell cell = row.createCell(0);
        cell.setCellValue(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
        cell.setCellStyle(cellStyle(false, 14));

        Cell cell1 = row.createCell(1);
        cell1.setCellValue(customer.getPhone());
        cell1.setCellStyle(cellStyle(false, 14));

        Cell cell2 = row.createCell(2);
        cell2.setCellValue(customer.getGander());
        cell2.setCellStyle(cellStyle(false, 14));

        Cell cell3 = row.createCell(3);
        cell3.setCellValue(customer.getShift());
        cell3.setCellStyle(cellStyle(false, 14));


        Cell cell4 = row.createCell(4);
        cell4.setCellValue(customer.getAddress());
        cell4.setCellStyle(cellStyle(false, 14));

        Cell cell5 = row.createCell(5);
        cell5.setCellValue(customer.getWeight() + " kg");
        cell5.setCellStyle(cellStyle(false, 14));

        Cell cell6 = row.createCell(6);
        cell6.setCellValue(customer.getWhoAdded());
        cell6.setCellStyle(cellStyle(false, 14));


        Cell cell7 = row.createCell(7);
        cell7.setCellValue(customer.getForeArm() + " cm");
        cell7.setCellStyle(cellStyle(false, 14));

        Cell cell8 = row.createCell(8);
        cell8.setCellValue(customer.getHips() + " cm");
        cell8.setCellStyle(cellStyle(false, 14));

        Cell cell9 = row.createCell(9);
        cell9.setCellValue(customer.getChest() + "cm");
        cell9.setCellStyle(cellStyle(false, 14));

        Cell cell10 = row.createCell(10);
        cell10.setCellValue(customer.getWaist() + " cm");
        cell10.setCellStyle(cellStyle(false, 14));

    }


    private void saveTo() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            saveTextToFile(file, workbook);
        }
    }

    private void saveTextToFile(File file, Workbook wb) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
