package org.example.Controller;

import org.example.Model.Calculator;
import org.example.Model.DataHandler;
import org.example.Model.DataExporter;
import org.example.View.MainView;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Controller {
    private MainView view;
    private DataHandler dataHandler;
    private Calculator calculator;
    private DataExporter dataExporter;
    private boolean dataImported = false;
    private boolean calculationDone = false;

    public Controller() {
        this.dataHandler = new DataHandler();
        this.calculator = new Calculator();
        this.dataExporter = new DataExporter();

        this.view = new MainView(this);
    }

    public void startApp() {

        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    public void importData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите файл Excel для импорта");

        int result = fileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                dataHandler.importData(selectedFile.getAbsolutePath());
                view.updateStatus("Данные успешно импортированы.");
                view.enableCalculateButton(true);
                dataImported = true;
                calculationDone = false;
                view.enableExportButton(false);
            } catch (IOException e) {
                view.failed();
            }
        }
    }

    public void calculateData() {
        if (!dataImported) {
            view.failed();
            return;
        }

        try {
            calculator.calculateStats(dataHandler);
            view.updateStatus("Статистика успешно рассчитана.");
            view.enableExportButton(true);
            calculationDone = true;
        } catch (Exception e) {
            view.failed();
        }
    }

    public void exportData() {
        if (!calculationDone) {
            view.failed();
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите файл для экспорта результатов");

        int result = fileChooser.showSaveDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();


            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try {
                dataExporter.exportData(filePath, calculator);
                view.updateStatus("Данные успешно экспортированы в файл: " + filePath);
            } catch (IOException e) {
                view.failed();
            }
        }
    }

    public void exit() {
        System.exit(0);
    }
}
