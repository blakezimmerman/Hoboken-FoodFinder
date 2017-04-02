package com.bzimmerman;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.*;

public class Main {
    // Initialization of variables
    private static boolean isDuckbills;
    private static boolean isWashington;
    private static Restaurant[] restaurants;

    public static void main(String[] args) {
        // Declaration of variables
        isDuckbills = false;
        isWashington = false;
        restaurants = RestaurantArray.get();

        // Draw the UI
        DrawUI();
    }

    private static void DrawUI() {
        // Initialize the window frame
        JFrame mainFrame = new JFrame("Hoboken Food Finder");
        mainFrame.setSize(500, 400); // (width, height)
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        // Initialize bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 1)); // (rows, columns);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 10, 10)); // top, left, bottom, right
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Initialize the result text pane
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        StyledDocument resultText = textPane.getStyledDocument();

        // Add scroll to the pane
        JScrollPane scroll = new JScrollPane(textPane) {
            @Override
            public Dimension getPreferredSize() {
                int size = (int) (mainFrame.getSize().height * 0.65);
                return new Dimension(0, size);
            }
        };
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add resultText to bottom panel
        bottomPanel.add(scroll, BorderLayout.CENTER);

        // Initialize top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2)); // (rows, columns);
        topPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 10, 10)); // top, left, bottom, right
        mainFrame.add(topPanel, BorderLayout.NORTH);

        // Initialize checkbox panel
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new GridLayout(3, 1)); //(rows, columns);

        JLabel requirements = new JLabel("Requirements:");
        checkboxPanel.add(requirements);

        // Initialize check boxes
        JCheckBox duckbillsCB = new JCheckBox("Accepts DuckBills");
        checkboxPanel.add(duckbillsCB);
        duckbillsCB.setSelected(false);

        JCheckBox washingtonCB = new JCheckBox("On Washington Street");
        checkboxPanel.add(washingtonCB);
        washingtonCB.setSelected(false);

        // Add listener to duckbillsCB
        duckbillsCB.addActionListener(e -> {
            Object source = e.getSource();
            if (source instanceof JCheckBox) {
                JCheckBox cb = (JCheckBox) source;
                isDuckbills = cb.isSelected();
            }
        });

        // Add listener to washingtonCB
        washingtonCB.addActionListener(e -> {
            Object source = e.getSource();
            if (source instanceof JCheckBox) {
                JCheckBox cb = (JCheckBox) source;
                isWashington = cb.isSelected();
            }
        });

        // Add checkboxPanel to topPanel
        topPanel.add(checkboxPanel, BorderLayout.WEST);

        // Initialize button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1)); //(rows, columns);

        JLabel padding = new JLabel("");
        buttonPanel.add(padding);

        // Initialize buttons
        JButton randomButton = new JButton("Suggest Me a Restaurant!");
        buttonPanel.add(randomButton);

        // Add listener to randomButton
        randomButton.addActionListener(e -> {
            Object source = e.getSource();
            if (source instanceof JButton) {
                String randResult = getRestaurant(sortRestaurants(restaurants));

                textPane.setText(randResult);
                textPane.setCaretPosition(0);

                int randStartIndex = randResult.indexOf("\n", 0);
                int randStopIndex = randResult.indexOf("\n", (randStartIndex + 2));

                if (randStartIndex != -1) {
                    // Stylize Text
                    SimpleAttributeSet randText = new SimpleAttributeSet();
                    StyleConstants.setAlignment(randText, StyleConstants.ALIGN_CENTER);
                    StyleConstants.setFontFamily(randText, "");
                    StyleConstants.setFontSize(randText, 13);
                    resultText.setParagraphAttributes(0, resultText.getLength(), randText, true);

                    // Sub-string specific styles
                    SimpleAttributeSet randTextBold = new SimpleAttributeSet();
                    StyleConstants.setBold(randTextBold, true);
                    StyleConstants.setFontSize(randTextBold, 16);
                    resultText.setCharacterAttributes(randStartIndex + 2, (randStopIndex - randStartIndex - 1), randTextBold, true);
                } else {
                    textPane.setText("Error");
                }
            }
        });

        JButton viewAllButton = new JButton("View All Restaurants");
        buttonPanel.add(viewAllButton);

        // Add listener to viewAllButton
        viewAllButton.addActionListener(e -> {
            Object source = e.getSource();
            if (source instanceof JButton) {
                String listResult = getList(sortRestaurants(restaurants));

                textPane.setText(listResult);
                textPane.setCaretPosition(0);

                // Center all text
                SimpleAttributeSet allText = new SimpleAttributeSet();
                StyleConstants.setAlignment(allText, StyleConstants.ALIGN_CENTER);
                StyleConstants.setFontFamily(allText, "");
                StyleConstants.setFontSize(allText, 13);
                StyleConstants.setBold(allText, false);
                resultText.setParagraphAttributes(0, resultText.getLength(), allText, true);
                resultText.setCharacterAttributes(0, resultText.getLength(), allText, true);
            }
        });

        // Add buttonPanel to topPanel
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Set frame to visible
        mainFrame.setVisible(true);
    }

    private static List<Restaurant> sortRestaurants(Restaurant[] restaurantArray) {
        List<Restaurant> restaurants = new ArrayList<>();
        Collections.addAll(restaurants, restaurantArray);

        if (isDuckbills)
            restaurants = restaurants.stream().filter(Restaurant::getDuckbills).collect(Collectors.toList());

        if (isWashington)
            restaurants = restaurants.stream().filter(r -> r.getAddress().contains("Washington Street")).collect(Collectors.toList());

        return restaurants;
    }

    private static String getRestaurant(List<Restaurant> applicable) {
        Random rand = new Random();
        Restaurant randRest = applicable.get(rand.nextInt(applicable.size()));

        return ("Consider eating at...\n\n" + randRest.getName() + "\n" + randRest.getAddress());
    }

    private static String getList(List<Restaurant> applicable) {
        StringBuilder all = new StringBuilder();

        for (int i = 0; i < applicable.size(); i++) {
            if (i == (applicable.size() - 1))
                all.append(applicable.get(i).getName()).append(" — ").append(applicable.get(i).getAddress());
            else
                all.append(applicable.get(i).getName()).append(" — ").append(applicable.get(i).getAddress()).append("\n");
        }

        return all.toString();
    }
}