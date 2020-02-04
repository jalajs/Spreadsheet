package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a reference to one or multiple entire columns of cells.
 */
public class ColReference extends Reference {

  /**
   * Creates a reference to one or multiple entire columns of data.
   * @param c1 The top cell in the first column being referenced
   * @param c2 The top cell in the second (leftmost) column being referenced
   * @param sheet The workbook that this reference exists on
   * @param sheetName The name of the sheet in this workbook the reference pertains to
   */
  ColReference(Coord c1, Coord c2, WorkbookModel<Cell> sheet, String sheetName) {
    super(c1, c2, sheet, sheetName);
  }

  @Override
  Value getValue(SexpVisitor visitor) {
    String oldSheetName = this.sheet.getCurrentSheetName();
    if (!this.sheetName.equals("")) {
      if (this.sheet.getSheets().contains(this.sheetName)) {
        this.sheet.changeSheet(this.sheetName);
      } else {
        return ErrorValue.getInstance();
      }
    }
    HashMap<Coord, Cell> grid = this.sheet.getGrid();
    List<Value> vals = new ArrayList<>();

    for (Map.Entry<Coord, Cell> entry: grid.entrySet()) {
      int curCol = entry.getKey().col;
      if (curCol >= c1.col && curCol <= c2.col) {
        List<Cell> thisCell = new ArrayList<>();
        thisCell.add(this.referencing);
        Cell toEval = entry.getValue();
        toEval.setReferences(thisCell);
        vals.add(toEval.evaluateCell(visitor));
      }
    }
    this.sheet.changeSheet(oldSheetName);
    return new MultiValue(vals);
  }
}
