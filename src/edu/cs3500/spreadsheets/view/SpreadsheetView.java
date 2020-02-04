package edu.cs3500.spreadsheets.view;

import java.io.IOException;

import edu.cs3500.spreadsheets.controller.Features;

/**
 * Represents a view for a {@link edu.cs3500.spreadsheets.model.WorksheetModel WorksheetModel}.
 */
public interface SpreadsheetView {

  /**
   * Renders a view of a spreadsheet.
   * @throws IOException if unable to render view correctly to output device
   */
  void render() throws IOException;

  /**
   * Changes whether this view is visible or not.
   * @param visible whether to make this view visible (true) or hidden (false)
   */
  void makeVisible(boolean visible);

  /**
   * Adds listeners to communicate between this view and the given controller to access the model.
   * @param features the controller to communicate with
   */
  void addFeatures(Features features);

}
