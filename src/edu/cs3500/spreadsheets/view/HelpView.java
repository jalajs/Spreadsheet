package edu.cs3500.spreadsheets.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Represents a window that shows guidance for using built-in functions in this spreadsheet.
 */
class HelpView extends JFrame {
  private final int windowWidth = 300;
  private final int windowHeight = 150;

  /**
   * Creates a new window to show help.
   */
  HelpView() {
    setSize(windowWidth, windowHeight);
    setMinimumSize(new Dimension(100, 50));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocation(300, 300);
    setResizable(true);
    this.setUpView(this.getContentPane());
  }

  /**
   * Sets up the layout of view of this window, including adding all its components.
   * @param container The container to add components to
   */
  private void setUpView(Container container) {
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    JLabel title = new JLabel();
    title.setText("BUILT-IN FUNCTIONS");
    title.setFont(new Font("TimesRoman", Font.PLAIN, 40));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    container.add(title);
    addALabel("SUM", container);
    addAnArgumentType("Number", container);
    addADescription("Adds the given arguments together", container);

    addALabel("PRODUCT", container);
    addAnArgumentType("Number", container);
    addADescription("Multiplies the given arguments together", container);

    addALabel("DIFFERENCE", container);
    addAnArgumentType("Number", container);
    addADescription("Subtracts the given arguments from one another", container);

    addALabel("QUOTIENT", container);
    addAnArgumentType("Number", container);
    addADescription("Multiplies the given arguments with one another", container);

    addALabel("<", container);
    addAnArgumentType("Number", container);
    addADescription("Takes in only 2 arguments. Returns whether the first one is less than " +
            "the second argument", container);

    addALabel("STRCAT", container);
    addAnArgumentType("String", container);
    addADescription("Returns the given arguments concatenated together", container);

    pack();
  }

  /**
   * Adds a new text label that acts as a header to the given container.
   * @param text the text for the label
   * @param container the container to add the label to
   */
  private void addALabel(String text, Container container) {
    JLabel label = new JLabel(text);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    label.setFont(new Font("TimesRoman", Font.PLAIN, 25));
    container.add(label);
  }

  /**
   * Adds a label with much smaller font that will show argument types to the given container.
   * @param text the text to use for the label
   * @param container the container to add the label to
   */
  private void addAnArgumentType(String text, Container container) {
    JLabel label = new JLabel("Argument Type(s): " + text);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    label.setFont(new Font("TimesRoman", Font.PLAIN, 10));
    container.add(label);
  }

  /**
   * Adds a label with mid-sized font to the given container that will show a function description.
   * @param text the text to use for the label
   * @param container the container to add the label to
   */
  private void addADescription(String text, Container container) {
    JLabel label = new JLabel(text);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    label.setFont(new Font("TimesRoman", Font.PLAIN, 15));
    container.add(label);
  }
}
