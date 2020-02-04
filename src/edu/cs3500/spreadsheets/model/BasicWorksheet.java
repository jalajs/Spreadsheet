package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * A basic implementation of a spreadsheet model with cells. Uses a hashMap to represent a grid of
 * cells because it requires O(n) memory. Also supports basic methods/operations for the data
 * of the model a editor/client may need.
 */
public class BasicWorksheet implements WorksheetModel<Cell> {
  private Map<Coord, Cell> grid;
  private WorkbookModel<Cell> book;

  /**
   * Class Invariants ensured by the constructor and preserved by the methods:
   * The coordinates of the grid are positive and the rows/cols are no greater than 10,000.
   * A formula cannot cause cycles.
   * Modifying an existing cell cannot cause cycles.
   */

  public BasicWorksheet(WorkbookModel<Cell> book) {
    this.grid = new HashMap<>();
    this.book = book;
  }


  @Override
  public void setCell(int row, int col, String value) {
    // this method behaves a bit differently now. Instead of updating the entire sheet when
    // a cell's content is changed, it now only updates the cells that reference this cell.
    // MUCH MORE EFFICIENT :)
    Cell old = this.getCellAt(new Coord(col, row));
    Cell newCell = new Cell(value, new CellReference(new Coord(col, row), this.book, ""));
    if (old != null) {
      // make new cell at position with old cell's referencing list
      newCell.setReferences(old.getReferences());
    }
    Coord coord = new Coord(col, row);
    grid.put(coord, newCell);

    for (Cell c : newCell.getReferences()) {
      c.evaluateCell();
    }
  }
  
  @Override
  public void evalCell(Coord coord) {
    getCellAt(coord).evaluateCell();
  }

  @Override
  public List<Coord> updateSheet() {
    List<Coord> errorCells = new ArrayList<>();

    for (Map.Entry<Coord, Cell> entry : grid.entrySet()) {
      try {
        entry.getValue().evaluateCell();
      } catch (IllegalArgumentException e) {
        errorCells.add(entry.getKey());
      }
    }
    return errorCells;
  }

  @Override
  public void deleteCell(Coord coord) {
    grid.remove(coord);
  }

  @Override
  public Cell getCellAt(Coord coord) {
    Cell cellAt = grid.getOrDefault(coord, null);
    return cellAt == null ? null : new Cell(cellAt.toString(),
            new CellReference(coord, this.book));
  }

  @Override
  public Coord getCoord(Cell cell) {
    for (Map.Entry<Coord, Cell> entry: grid.entrySet()) {
      if (entry.getValue().equals(cell)) {
        return new Coord(entry.getKey().col, entry.getKey().row);
      }
    }
    return null;
  }

  @Override
  public HashMap<Coord, Cell> getGrid() {
    return new HashMap<Coord, Cell>(this.grid);
  }

}
