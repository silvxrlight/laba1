package org.example.View;

import org.example.Controller.Controller;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private Controller controller;
    private JButton importButton;
    private JButton calculateButton;
    private JButton exportButton;
    private JButton exitButton;
    private JLabel statusLabel;

    public MainView(Controller controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {

        setTitle("Анализ статистики");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        importButton = new JButton("Импортировать файл");
        calculateButton = new JButton("Расчет");
        exportButton = new JButton("Экспортировать файл");
        exitButton = new JButton("Выход");
        statusLabel = new JLabel("Ожидание импорта данных...");

        // кнопки недоступны, пока нет данных
        calculateButton.setEnabled(false);
        exportButton.setEnabled(false);


        importButton.addActionListener(e -> controller.importData());
        calculateButton.addActionListener(e -> controller.calculateData());
        exportButton.addActionListener(e -> controller.exportData());
        exitButton.addActionListener(e -> controller.exit());


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(importButton);
        buttonPanel.add(calculateButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(exitButton);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.NORTH);

        // добавление панели
        setContentPane(mainPanel);
    }

    public void failed() {
        JOptionPane panel = new JOptionPane("Ошибка");
    }

    ;

    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    public void enableCalculateButton(boolean enable) {
        calculateButton.setEnabled(enable);
    }

    public void enableExportButton(boolean enable) {
        exportButton.setEnabled(enable);
    }
}
