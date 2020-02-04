package edu.cs3500.spreadsheets.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.cs3500.spreadsheets.controller.Features;

/**
 * Represents a panel for holding options and settings for a spreadsheet.
 */
class OptionsPanel extends JPanel {
  private JButton addRowsButton;
  private JTextField rowNumBox;
  private JButton addColsButton;
  private JTextField colNumBox;
  private JButton helpButton;
  private JButton loadButton;
  private JButton saveButton;
  private JFileChooser fc;
  private JButton changeBook;

  /**
   * Creates a new panel that holds spreadsheet options.
   */
  OptionsPanel() {
    this.addRowsButton = new JButton();
    this.rowNumBox = new JTextField();
    this.addColsButton = new JButton();
    this.colNumBox = new JTextField();
    this.helpButton = new JButton();
    this.loadButton = new JButton();
    this.saveButton = new JButton();
    this.fc = new JFileChooser();
    this.changeBook = new JButton();
    this.setUpLayout();
  }

  /**
   * Sets up the layout of this panel, including setting layout type and adding components.
   */
  private void setUpLayout() {
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    Dimension buttonSize = new Dimension(110, 40);
    Dimension smallerButtonSize = new Dimension(80, 40);
    Dimension textBoxSize = new Dimension(100, 40);

    addRowsButton.setText("Add # Rows");
    addRowsButton.setPreferredSize(buttonSize);

    addColsButton.setText("Add # Cols");
    addColsButton.setPreferredSize(buttonSize);

    helpButton.setText("Help");
    helpButton.setPreferredSize(buttonSize);

    loadButton.setText("Load");
    loadButton.setPreferredSize(smallerButtonSize);

    saveButton.setText("Save");
    saveButton.setPreferredSize(smallerButtonSize);

    rowNumBox.setPreferredSize(textBoxSize);

    colNumBox.setPreferredSize(textBoxSize);

    changeBook.setPreferredSize(buttonSize);
    changeBook.setText("Workbook...");


    add(Box.createHorizontalStrut(5));
    add(changeBook);
    add(Box.createHorizontalStrut(5));
    add(new JSeparator(SwingConstants.VERTICAL));
    add(Box.createHorizontalStrut(5));
    add(addRowsButton);
    add(Box.createHorizontalStrut(5));
    add(rowNumBox, BorderLayout.LINE_START);
    add(Box.createHorizontalStrut(5));
    add(new JSeparator(SwingConstants.VERTICAL));
    add(Box.createHorizontalStrut(5));

    add(addColsButton);
    add(Box.createHorizontalStrut(5));
    add(colNumBox);
    add(Box.createHorizontalStrut(5));
    add(new JSeparator(SwingConstants.VERTICAL));
    add(Box.createHorizontalStrut(5));

    add(loadButton);
    add(Box.createHorizontalStrut(5));
    add(saveButton);
    add(Box.createHorizontalStrut(5));
    add(helpButton);
    add(Box.createHorizontalStrut(5));
  }

  /**
   * Shows a popup help window using a HelpView.
   */
  private void showHelp() {
    JFrame help = new HelpView();
    help.setVisible(true);
  }

  /**
   * Adds a click listener to the help button of this view.
   */
  void addHelpListener() {
    this.helpButton.addActionListener(e -> showHelp());
  }

  /**
   * Adds click listeners to the buttons to add row+cols in this views.
   * @param componentsPanel the panel to update the row+col count for
   */
  void addGridSizeListeners(SpreadsheetComponentsPanel componentsPanel) {
    this.addRowsButton.addActionListener(e ->
            componentsPanel.setMaxHeight(Integer.parseInt(rowNumBox.getText()) / 2));

    this.addColsButton.addActionListener(e ->
            componentsPanel.setMaxWidth(Integer.parseInt(colNumBox.getText()) / 2));
  }

  /**
   * Adds a click listener to the button to save the current sheet.
   * @param features the controller to interact with to save this file
   */
  void addFileSaveListener(Features features) {
    this.saveButton.addActionListener(evt -> {
      int returnVal = fc.showSaveDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        System.out.println("Saving: " + file.getAbsolutePath());
        try {
          features.saveFile(file.getAbsolutePath());
        } catch (IllegalArgumentException e) {
          JOptionPane.showMessageDialog(this, e.getMessage());
        }
      } else {
        JOptionPane.showMessageDialog(this, "Failed to open file");
      }
    });

  }

  void addFileLoadListener(Features features) {
    this.loadButton.addActionListener(evt -> {
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        System.out.println("Opening: " + file.getName());
        try {
          features.loadFile(file.getAbsolutePath());
        } catch (IllegalStateException e) {
          JOptionPane.showMessageDialog(this, "Could not load " +
                  "the selected file\nPlease ensure it is a valid spreadsheet file");
        }
      } else {
        System.out.println("Failed to open file");
      }
    });

  }

  private void showChangeWorkBook(Features features, SpreadsheetView window) {
    ChangeWorkbook changeBook = new ChangeWorkbook();
    changeBook.addButtonListeners(features, window);
    changeBook.setVisible(true);
  }

  void addWorkBookListener(Features features, SpreadsheetView window) {
    this.changeBook.addActionListener(e -> showChangeWorkBook(features, window));
  }

}

