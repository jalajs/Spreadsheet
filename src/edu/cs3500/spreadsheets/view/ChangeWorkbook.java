package edu.cs3500.spreadsheets.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.cs3500.spreadsheets.controller.Features;

/**
 * Represents a view to add and delete worksheets as well as switch the one currently in focus.
 */
public class ChangeWorkbook extends JFrame {
  private final int windowWidth = 300;
  private final int windowHeight = 500;

  private JTextField newWorkbookNameField;
  private JButton addNewWorkbookButton;
  private JPanel sheetButtonPanel;
  private SpreadsheetView parentWindow;
  private JLabel currentSheetLabel;

  /**
   * Creates a new view to allow the user to create, switch, and destroy worksheets.
   */
  ChangeWorkbook() {
    setSize(windowWidth, windowHeight);
    setMinimumSize(new Dimension(100, 50));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocation(500, 300);
    setResizable(false);

    this.setUpView(this.getContentPane());
  }

  /**
   * Sets up the view layout and various components of this window.
   * @param container The container to which these elements should be added
   */
  private void setUpView(Container container) {
    setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    JLabel title = new JLabel("Change/Add Worksheet", SwingConstants.CENTER);
    title.setFont(new Font("TimesRoman", Font.PLAIN, 20));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel hint1 = new JLabel("New worksheet name...");
    hint1.setFont(new Font("TimesRoman", Font.PLAIN, 12));
    hint1.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel hint2 = new JLabel("OR choose an existing worksheet...");
    hint2.setFont(new Font("TimesRoman", Font.PLAIN, 14));
    hint2.setAlignmentX(Component.CENTER_ALIGNMENT);

    currentSheetLabel = new JLabel();
    currentSheetLabel.setFont(new Font("TimesRoman", Font.PLAIN, 15));
    currentSheetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    newWorkbookNameField = new JTextField();
    addNewWorkbookButton = new JButton("Create Worksheet");
    addNewWorkbookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    sheetButtonPanel = new JPanel();
    sheetButtonPanel.setLayout(new BoxLayout(sheetButtonPanel, BoxLayout.Y_AXIS));


    add(title);
    add(Box.createVerticalStrut(5));
    add(currentSheetLabel);
    add(Box.createVerticalStrut(5));
    add(new JSeparator(SwingConstants.HORIZONTAL));
    add(Box.createVerticalStrut(5));
    add(hint1);
    add(newWorkbookNameField);
    add(Box.createVerticalStrut(5));
    add(addNewWorkbookButton);
    add(Box.createVerticalStrut(10));
    add(new JSeparator(SwingConstants.HORIZONTAL));
    add(hint2);
    add(Box.createVerticalStrut(10));
    add(sheetButtonPanel);
    pack();
  }

  /**
   * Grabs the list of sheets currently in the workbook and updates the list of them accordingly.
   * @param features The controller with which to interact to get available sheets
   */
  private void updateAvailableSheets(Features features) {
    List<String> sheetNames = features.getSheets();
    sheetButtonPanel.removeAll();
    for (String s : sheetNames) {
      JPanel duoButtonPanel = new JPanel();
      JButton sheetButton = new JButton(s);
      JButton deleteSheetButton = new JButton("Delete " + s);
      duoButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
      this.addChangeWorksheetListener(sheetButton, features);
      this.addDeleteWorksheetListener(deleteSheetButton, s, features);
      duoButtonPanel.add(sheetButton);
      duoButtonPanel.add(deleteSheetButton);
      this.sheetButtonPanel.add(duoButtonPanel);
      this.sheetButtonPanel.add(Box.createVerticalStrut(10));
      this.sheetButtonPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
      this.sheetButtonPanel.add(Box.createVerticalStrut(10));

    }
    this.currentSheetLabel.setText("Current sheet: " + features.getCurrentSheet());
    pack();
  }

  /**
   * Adds a listener to the buttons for each sheet to change the one currently in focus.
   * @param sheetButton The button to add the listener to
   * @param features The controller that the button callback should interact with
   */
  void addChangeWorksheetListener(JButton sheetButton, Features features) {
    sheetButton.addActionListener(e -> {
      features.changeWorkbook(sheetButton.getText());
      this.updateAvailableSheets(features);
      try {
        this.parentWindow.render();
      } catch (IOException ex) {
        System.out.println("Couldn't render window");
      }
    });
  }

  /**
   * Adds a listener to the given button to make it delete its corresponding spreadsheet.
   * @param delButton The button to add the listener to
   * @param sheetName The name of this sheet that clicking this button should delete
   * @param features The controller that this button's callback will interact with to delete a sheet
   */
  void addDeleteWorksheetListener(JButton delButton, String sheetName, Features features) {
    delButton.addActionListener(e -> {
      features.deleteWorksheet(sheetName);
      this.updateAvailableSheets(features);
      try {
        this.parentWindow.render();
      } catch (IOException ex) {
        System.out.println("Couldn't render window");
      }
    });
  }

  /**
   * Adds a listener to a button in this window to add a new spreadsheet when clicked.
   * @param features The controller to interact with to add the new sheet
   * @param window The parent window of this view, which will get refreshed when the sheet changes
   */
  void addButtonListeners(Features features, SpreadsheetView window) {
    this.parentWindow = window;
    this.addNewWorkbookButton.addActionListener(e -> {
      String sheetName = this.newWorkbookNameField.getText().replace(" ", "");
      this.newWorkbookNameField.setText("");
      boolean success = features.addWorksheet(sheetName);
      if (success) {
        this.updateAvailableSheets(features);
      } else {
        JOptionPane.showMessageDialog(this, "Could not create sheet--" +
                "invalid name given or worksheet with name already exists");
      }
    });
    this.updateAvailableSheets(features);
  }
}
