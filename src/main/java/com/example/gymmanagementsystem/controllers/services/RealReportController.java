package com.example.gymmanagementsystem.controllers.services;

import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.fxml.Initializable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.net.URL;
import java.util.ResourceBundle;

public class RealReportController extends CommonClass implements Initializable {

    private Font font;
    private final Workbook workbook;
    private CellStyle style;

    public RealReportController() {
        this.workbook = new HSSFWorkbook();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    private Font font(boolean isBold, int fontSize) {
        if (font == null) {
            font = workbook.createFont();
            font.setFontHeightInPoints((short) fontSize);
            font.setFontName("Verdana");
            font.setBold(isBold);
        }
        return font;
    }

    private CellStyle cellStyle(boolean isBold, int fontSize) {
        if (style == null) {
            style = workbook.createCellStyle();
            style.setFont(font(isBold, fontSize));
        }

        return style;
    }

    private void columnWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 6000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
    }


    private void createCell(Row row, boolean customersOnly, Sheet sheet) {

createCell(row,customer);
    }


    private void createCell(Row row, Customers customer) {

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


}
