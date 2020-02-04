package edu.cs3500.spreadsheets.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import edu.cs3500.spreadsheets.controller.Features;

/**
 * Represents a rich graphical view of a {@link SpreadsheetView}. Extends the JFrame class to
 * help create the frame of the view.
 */
public class SpreadsheetGUIView extends JFrame implements SpreadsheetView {
  protected SpreadsheetComponentsPanel componentsPanel;

  protected static int defaultWindowHeight = 694;
  protected static int defaultWindowWidth = 1026;

  /**
   * Constructs a new graphical view of a spreadsheet.
   * @param modelView the spreadsheet model on which to base the graphical view
   */
  public SpreadsheetGUIView(ModelView modelView) {
    super("Beyond gOOD");
    setSize(defaultWindowWidth, defaultWindowHeight);
    setMinimumSize(new Dimension(500, 300));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocation(300, 100);
    setResizable(true);
    ImageIcon img = new ImageIcon("resources/happy-mark.png");
    setIconImage(img.getImage());
    componentsPanel = new SpreadsheetComponentsPanel(modelView);

    // set the layout of our application
    this.setUpLayout();
  }


  /**
   * Sets up the layout of this JFrame.
   */
  private void setUpLayout() {
    setLayout(new BorderLayout());
    componentsPanel.setPreferredSize(new Dimension(defaultWindowWidth, defaultWindowHeight));
    setBackground(Color.darkGray);
    add(componentsPanel);
    pack();
  }

  @Override
  public void render() throws IOException {
    this.repaint();
  }


  @Override
  public void makeVisible(boolean visible) {
    this.setVisible(visible);
  }


  @Override
  public void addFeatures(Features features) {
    // no controller interaction in GUI view so no features to add
  }


}
