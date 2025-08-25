package org.example.Model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    List<List<Double>> selection = new ArrayList<>();  //выборки
    List<List<String>> letters = new ArrayList<>();
    FormulaEvaluator evaluator;// для работы с формулами


    public void importData(String path) throws IOException {
        // Проверка формата файла
        if (path.toLowerCase().endsWith(".xls")) {
            throw new IOException("Обнаружен устаревший формат Excel (.xls). Пожалуйста, используйте формат .xlsx");
        }

        // Проверка на существование файла
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Файл не найден: " + path);
        }

        // очистка предыдущих данных
        selection.clear();
        letters.clear();

        XSSFWorkbook excelWorkbook = new XSSFWorkbook(new FileInputStream(path));
        this.evaluator = excelWorkbook.getCreationHelper().createFormulaEvaluator(); // для работы с формулами

        for (int sheetIt = 0; sheetIt < excelWorkbook.getNumberOfSheets(); sheetIt++){// прохожусь по листам
            Sheet sheet = excelWorkbook.getSheetAt(sheetIt);
            int rowAmount = sheet.getLastRowNum() + 1; // почему-то дает 100, а не 101

            if (rowAmount > 0) { // обрабатываем только непустые листы
                Row firstRow = sheet.getRow(0);
                if (firstRow != null) { // обрабатываем только листы с первой строкой
                    int cellAmount = firstRow.getLastCellNum(); // дает нормально
                    List<String> sheetLetters = new ArrayList<>();

                    for(int cellIt = 0; cellIt < cellAmount; cellIt++){
                        List<Double> columnNumbers = new ArrayList<>();

                        for(int rowIt = 0; rowIt < rowAmount; rowIt++){
                            Row curRow = sheet.getRow(rowIt);
                            if (curRow != null) { // обрабатываем только существующие строки
                                Cell curCell = curRow.getCell(cellIt);
                                if (curCell != null) { // обрабатываем только существующие ячейки
                                    if (rowIt == 0 && curCell.getCellType() == CellType.STRING) {
                                        sheetLetters.add(curCell.getStringCellValue());
                                    } else {
                                        if (curCell.getCellType() == CellType.NUMERIC){
                                            columnNumbers.add(curCell.getNumericCellValue());
                                        }
                                        else if (curCell.getCellType() == CellType.FORMULA){// :)
                                            CellValue evaluatedCell = evaluator.evaluate(curCell);
                                            if (evaluatedCell.getCellType() == CellType.NUMERIC) {
                                                columnNumbers.add(evaluatedCell.getNumberValue());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!columnNumbers.isEmpty()) {
                            selection.add(columnNumbers);
                        }
                    }

                    if(!sheetLetters.isEmpty()){
                        letters.add(sheetLetters);
                    }
                }

                // Проверка на пустые данные после импорта
                if (selection.isEmpty()) {
                    throw new IOException("Файл не содержит числовых данных для анализа");
                }

                excelWorkbook.close();
            }
        }}}