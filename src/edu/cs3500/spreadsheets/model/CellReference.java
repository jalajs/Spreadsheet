package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a reference to a single cell or a group of cells.
 */
public class CellReference extends Reference {

  /**
   * Creates a new reference to a rectangular group of cells in a different spreadsheet than the
   * current one.
   * @param c1 the cell in the top-left corner of the reference
   * @param c2 the cell in the bottom-right corner of the reference
   * @param sheet the workbook that this reference pertains to
   * @param sheetName the name of the sheet in this workbook that the reference pertains to
   */
  public CellReference(Coord c1, Coord c2, WorkbookModel<Cell> sheet, String sheetName) {
    super(c1, c2, sheet, sheetName);
  }

  /**
   * Creates a new reference to a single cell in a different spreadsheet than the current one.
   * @param c1 The coordinate to reference
   * @param sheet The workbook that this reference pertains to
   * @param sheetName The name of the sheet to reference
   */
  public CellReference(Coord c1, WorkbookModel<Cell> sheet, String sheetName) {
    super(c1, sheet, sheetName);
  }

  /**
   * Creates a new reference to a rectangular region of cells in the current spreadsheet.
   * @param c1 The coordinate of the top-left corner of the reference
   * @param c2 The coordinate of the bottom-right corner of the reference
   * @param sheet The workbook that this reference pertains to
   */
  public CellReference(Coord c1, Coord c2, WorkbookModel<Cell> sheet) {
    super(c1, c2, sheet, "");
  }

  /**
   * Creates a new reference to a single cell in the current spreadsheet.
   * @param c1 The coordinate to reference
   * @param sheet The workbook that this reference pertains to
   */
  public CellReference(Coord c1, WorkbookModel<Cell> sheet) {
    super(c1, sheet, "");
  }

  @Override
  Value getValue(SexpVisitor visitor) {
    String oldSheet = this.sheet.getCurrentSheetName();

    if (c1 == c2) {
      // only 1 cell to evaluate
      Cell toEval;
      if (!this.sheetName.equals("")) {
        // ref to a different sheet
        if (this.sheet.getSheets().contains(this.sheetName)) {
          this.sheet.changeSheet(this.sheetName);
          toEval = sheet.getCellAt(c1);
        } else {
          return ErrorValue.getInstance();
        }

      } else {
        // ref to this sheet
        toEval = sheet.getCellAt(c1);
      }
      if (toEval == null) {
        this.sheet.changeSheet(oldSheet);
        return BlankValue.getInstance();
      } else {
        List<Cell> thisCell = new ArrayList<>();
        thisCell.add(this.referencing);
        toEval.setReferences(thisCell);
        this.sheet.changeSheet(oldSheet);
        return toEval.evaluateCell(visitor);
      }
    } else {
      // range of cells to evaluate
      List<Value> vals = new ArrayList<Value>();
      if (!this.sheetName.equals("")) {
        // ref to a different sheet
        if (this.sheet.getSheets().contains(this.sheetName)) {
          this.sheet.changeSheet(this.sheetName);
        } else {
          return ErrorValue.getInstance();
        }
      }
      for (int i = c1.col; i <= c2.col; i++) {
        for (int j = c1.row; j <= c2.row; j++) {
          Cell toEval = sheet.getCellAt(new Coord(i, j));

          if (toEval == null) {
            vals.add(BlankValue.getInstance());
          } else {
            List<Cell> thisCell = new ArrayList<>();
            thisCell.add(this.referencing);
            toEval.setReferences(thisCell);
            vals.add(toEval.evaluateCell(visitor));
          }
        }
      }
      this.sheet.changeSheet(oldSheet);
      return new MultiValue(vals);
    }
  }
}
