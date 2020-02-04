package edu.cs3500.spreadsheets.controller;

import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents features to communicate between a view and model in a spreadsheet.
 */
public interface Features {

  /**
   * Updates the model by changing the value at a cell with content sent from the view.
   * @param newValue the new value to add to the board
   * @param coord the coordinate at which to add the new value
   */
  void confirmInput(String newValue, Coord coord);

  /**
   * Selects a cell on the model's board with a mouse position from the view.
   * @param x the mouse X position
   * @param y the mouse Y position
   * @return the cell coordinate at which the mouse's cursor was placed
   */
  Coord selectCell(int x, int y);

  /**
   * Loads a new file from the OS to display in a spreadsheet.
   */
  void loadFile(String path) throws IllegalStateException;

  /**
   * Saves the current state of the model as a text file.
   */
  void saveFile(String path) throws IllegalArgumentException;

  /**
   * Clears the contents of the cell at the given coordinates.
   * @param coord the coord to clear the contents from
   */
  void clearCell(Coord coord);

  // TODO javadoc
  boolean addWorksheet(String name);

  // TODO javadoc
  void changeWorkbook(String wbName);

  // TODO javadoc
  void deleteWorksheet(String sheetName);

  List<String> getSheets();

  String getCurrentSheet();
}
