package edu.cs3500.spreadsheets.view;


import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents a panel that holds the component panels of a {@link SpreadsheetView}.
 */
public class SpreadsheetComponentsPanel extends JPanel {
  SpreadsheetPanel grid;
  private JScrollBar scrollBarX;
  private JScrollBar scrollBarY;

  private static int barXMaximum = 4994;
  private static int barYMaximum = 4994;

  /**
   * Constructs a new component panel for a {@link SpreadsheetView}.
   * @param modelView a view of the model this view's components will render
   */
  public SpreadsheetComponentsPanel(ModelView<Cell> modelView) {
    setLayout(new BorderLayout());
    grid = new SpreadsheetPanel(modelView);
    scrollBarY = new JScrollBar();
    scrollBarX = new JScrollBar();
    this.setUpLayout();
  }

  /**
   * Sets up the layout for this panel.
   */
  private void setUpLayout() {
    scrollBarY.setMaximum(barYMaximum);

    scrollBarY.addAdjustmentListener(e -> adjustSpreadsheetView(-1, e.getValue()));

    scrollBarX.setOrientation(Adjustable.HORIZONTAL);
    scrollBarX.setMaximum(barXMaximum);
    scrollBarX.addAdjustmentListener(e -> adjustSpreadsheetView(e.getValue(), -1));

    grid.setPreferredSize(new Dimension(SpreadsheetPanel.width, SpreadsheetPanel.height));
    add(grid, BorderLayout.CENTER);
    add(scrollBarY, BorderLayout.EAST);
    add(scrollBarX, BorderLayout.SOUTH);
  }



  /**
   * Adjusts the view window of this panel's {@link SpreadsheetPanel} based on scrollbar change.
   * @param xValue the X value of the scrollbar
   * @param yValue the Y value of the scrollbar
   */
  private void adjustSpreadsheetView(int xValue, int yValue) {

    this.grid.updateXYStart(xValue, yValue);
  }


  /**
   * Adds the given value to the maximum width of this panel's scrollbar.
   * @param x the value to add to the width
   */
  void setMaxWidth(int x) {
    barXMaximum = barXMaximum + x;
    scrollBarX.setMaximum(barXMaximum);
  }

  /**
   * Adds the given value to the maximum height of this panel's scrollbar.
   * @param y the value to add to the height
   */
  void setMaxHeight(int y) {
    barYMaximum = barYMaximum + y;
    scrollBarY.setMaximum(barYMaximum);
  }

  /**
   * Adds a listener to this panel's grid to delete cell contents when the delete key is pressed.
   * @param features the controller this will communicate with
   */
  void addDelKeyListener(Features features) {
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"),
            "delPressed");
    this.getActionMap().put("delPressed", new DelKeyPressed(features));
  }


  /**
   * Class to handle the action of pressing the delete key.
   */
  static class DelKeyPressed extends AbstractAction {
    private Features features;

    /**
     * Creates a new Delete key action handler.
     * @param features the controller to interact with
     */
    DelKeyPressed(Features features) {
      this.features = features;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (SpreadsheetEditView.selectedCoord != null) {
        features.clearCell(new Coord(SpreadsheetEditView.selectedCoord.col,
                SpreadsheetEditView.selectedCoord.row));
        SpreadsheetEditView.selectedCoord = null;
      }
    }
  }



}
