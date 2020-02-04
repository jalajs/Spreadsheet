package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a model for a worksheet for inputting and manipulating data.
 *
 * @param <K> How individual cells will be represented
 */
public interface WorksheetModel<K> {


  /**
   * Sets cell with given value at given row/column coord, whether cell exists there yet or not.
   *
   * @param row   the row component of coordinate of the cell
   * @param col   the column component of coordinate of the cell
   * @param value the value that the cell will hold
   */
  void setCell(int row, int col, String value);

  /**
   * Evaluates the cell at the given coordinate (with may throw an error if it is invalid).
   *
   * @param coord the coordinate at which to evaluate a cell
   */
  void evalCell(Coord coord);


  /**
   * Evaluates every cell in the sheet.
   *
   * @return list of {@link Coord} of cells that produce errors
   */
  List<Coord> updateSheet();

  /**
   * Deletes a cell (i.e. clears its contents) at the given coordinates.
   *
   * @param coord the coordinates of the cell that needs to be removed
   */
  void deleteCell(Coord coord);

  /**
   * Retrieves the generic value at the cell of the given coordinate.
   *
   * @param coord The coordinate of the cell whose value we want
   * @return K The cell's value, depending on how we represent values.
   */
  K getCellAt(Coord coord);


  /**
   * Retrieves the coordinates of the given cell.
   *
   * @param cell The given cell (however we may represent it)
   * @return the coordinates of that cell
   */
  Coord getCoord(K cell);

  /**
   * Retrieve the grid of the spreadsheet.
   *
   * @return the current grid of the spreadsheet.
   */
  HashMap<Coord, K> getGrid();

}