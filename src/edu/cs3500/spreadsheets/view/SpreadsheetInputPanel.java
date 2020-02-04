package edu.cs3500.spreadsheets.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Represents a panel for inputting new values into a spreadsheet.
 */
public class SpreadsheetInputPanel extends JPanel {
  private JTextField textField;
  JButton confirmButton;
  JButton cancelButton;

  /**
   * Constructs a new panel for inputting values into a spreadsheet.
   */
  public SpreadsheetInputPanel() {
    textField = new JTextField();
    confirmButton = new JButton();
    cancelButton = new JButton();
    this.setUpLayout();
  }

  private void setUpLayout() {
    this.setLayout(new BorderLayout());
    cancelButton.setText("🙅‍");
    cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 25));
    cancelButton.setPreferredSize(new Dimension(65, 40));

    confirmButton.setText("👌");
    confirmButton.setFont(new Font("TimesRoman", Font.PLAIN, 25));
    confirmButton.setPreferredSize(new Dimension(65, 40));

    add(cancelButton, BorderLayout.WEST);
    add(confirmButton, BorderLayout.EAST);
    add(textField);

  }

  /**
   * Returns the text value of this panel's textbox.
   * @return the text contained within this panel's JTextField
   */
  String getInputString() {
    return textField.getText();
  }

  /**
   * Clear's this panel's JTextField's input value.
   */
  void clearInputString() {
    textField.setText("");
  }


  /**
   * Sets this panel's text field to the desired value.
   * @param content the new value to hold in the text field
   */
  void setInputString(String content) {
    this.textField.setText(content);
  }

}
