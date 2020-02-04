package edu.cs3500.spreadsheets.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents a rich graphical view of a {@link SpreadsheetView} with editing capabilities.
 * Extends the JFrame class to help create the frame of the view.
 */
public class SpreadsheetEditView extends SpreadsheetGUIView {
  private SpreadsheetInputPanel spreadsheetInputPanel;
  static Coord selectedCoord = null;
  private ModelView modelView;
  private OptionsPanel optionsPanel;


  /**
   * Constructs a new graphical view of a spreadsheet.
   * @param modelView the spreadsheet model on which to base the graphical view
   */
  public SpreadsheetEditView(ModelView modelView) {
    super(modelView);
    this.modelView = modelView;
    spreadsheetInputPanel = new SpreadsheetInputPanel();
    optionsPanel = new OptionsPanel();

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
    add(spreadsheetInputPanel, BorderLayout.NORTH);
    add(componentsPanel, BorderLayout.CENTER);
    add(optionsPanel, BorderLayout.SOUTH);
    pack();
  }


  @Override
  public void addFeatures(Features features) {
    spreadsheetInputPanel.confirmButton.addActionListener(evt -> {
      if (selectedCoord != null) {
        String cellText = spreadsheetInputPanel.getInputString();
        features.confirmInput(cellText, selectedCoord);
        selectedCoord = null;
        this.spreadsheetInputPanel.clearInputString();
      }
    });

    spreadsheetInputPanel.cancelButton.addActionListener(evt -> {
      Object c = modelView.getCellAt(selectedCoord);
      if (c != null) {
        spreadsheetInputPanel.setInputString(c.toString());
      } else {
        spreadsheetInputPanel.clearInputString();
      }
    });


    componentsPanel.grid.addMouseListener(new GridMouseListener(features));
    componentsPanel.addDelKeyListener(features);


    optionsPanel.addHelpListener();
    optionsPanel.addGridSizeListeners(this.componentsPanel);
    optionsPanel.addFileSaveListener(features);
    optionsPanel.addFileLoadListener(features);
    optionsPanel.addWorkBookListener(features, this);
  }

  /**
   * Represents a mouse listener that checks and reacts to clicks on the spreadsheet.
   */
  class GridMouseListener extends MouseAdapter {
    Features features;

    /**
     * Creates a new listener for listening to mouse clicks on the grid.
     * @param features the controller to communicate with
     */
    GridMouseListener(Features features) {
      this.features = features;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      selectedCoord = features.selectCell(e.getX(), e.getY());
      Object c = modelView.getCellAt(selectedCoord);
      if (c != null) {
        spreadsheetInputPanel.setInputString(c.toString());
      } else {
        spreadsheetInputPanel.clearInputString();
      }
    }
  }

}
