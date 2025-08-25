package org.example.Model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class DataExporter {

    public void exportData(String path, Calculator calculator) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Statistics Results");


        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Статистика", "Значение"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }


        int rowNum = 1;
        for (int i = 0; i < calculator.geometricMean.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Среднее геометрическое (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.geometricMean.get(i));
        }

        for (int i = 0; i < calculator.arithmeticMean.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Среднее арифметическое (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.arithmeticMean.get(i));
        }

        for (int i = 0; i < calculator.stdDeviation.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Стандартное отклонение (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.stdDeviation.get(i));
        }

        for (int i = 0; i < calculator.R.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Размах (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.R.get(i));
        }

        for (int i = 0; i < calculator.amount.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Количество элементов (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.amount.get(i));
        }

        for (int i = 0; i < calculator.varianceRatio.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Коэффициент вариации (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.varianceRatio.get(i));
        }

        for (int i = 0; i < calculator.variance.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Дисперсия (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.variance.get(i));
        }

        for (int i = 0; i < calculator.max.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Максимум (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.max.get(i));
        }

        for (int i = 0; i < calculator.min.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Минимум (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue(calculator.min.get(i));
        }

        for (int i = 0; i < calculator.limits.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Доверительный интервал (выборка " + (i + 1) + ")");
            row.createCell(1).setCellValue("[" + calculator.limits.get(i).getLeft() +
                    "; " + calculator.limits.get(i).getRight() + "]");
        }

        Row covarianceHeader = sheet.createRow(rowNum++);
        covarianceHeader.createCell(0).setCellValue("Матрица ковариации:");

        for (int i = 0; i < calculator.covarianceMatrix.length; i++) {
            Row row = sheet.createRow(rowNum++);
            for (int j = 0; j < calculator.covarianceMatrix[i].length; j++) {
                row.createCell(j).setCellValue(calculator.covarianceMatrix[i][j]);
            }
        }

        // регулировка ширины столбцов
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(path)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}