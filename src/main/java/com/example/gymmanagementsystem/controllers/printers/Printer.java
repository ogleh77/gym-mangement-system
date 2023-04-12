package com.example.gymmanagementsystem.controllers.printers;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Printer {

    private Font font;
    protected final Workbook workbook;
    private CellStyle style;

    public Printer() {
        this.workbook = new HSSFWorkbook();
    }

    protected Font font(boolean isBold, int fontSize) {
        if (font == null) {
            font = workbook.createFont();
            font.setFontHeightInPoints((short) fontSize);
            font.setFontName("Verdana");
            font.setBold(isBold);
        }
        return font;
    }

    protected CellStyle cellStyle(boolean isBold, int fontSize) {
        if (style == null) {
            style = workbook.createCellStyle();
            style.setFont(font(isBold, fontSize));
        }

        return style;
    }

    protected void columnWidth(Sheet sheet) {
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

}
