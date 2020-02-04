package edu.cs3500.spreadsheets.model;

import java.util.Objects;

import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * A class representing a reference to a rectangular region of cells in the spreadsheet.
 */
public abstract class Reference {
  Coord c1;
  Coord c2;
  WorkbookModel<Cell> sheet;
  protected Cell referencing;
  protected String sheetName;

  /**
   * Creates a new Reference with two coordinates and a worksheet.
   * @param c1 the first (top-left) coordinate of the reference
   * @param c2 the second (bottom-right) coordinate of the reference
   * @param sheet the worksheet of this reference
   */
  Reference(Coord c1, Coord c2, WorkbookModel<Cell> sheet, String sheetName) {
    this.c1 = c1;
    this.c2 = c2;
    this.sheet = sheet;
    this.sheetName = sheetName;
  }

  /**
   * Creates a new Reference with a coordinate and a worksheet.
   * @param c1 the coordinate of this reference
   * @param sheet the worksheet for this Reference
   */
  public Reference(Coord c1, WorkbookModel<Cell> sheet, String sheetName) {
    this.c1 = c1;
    this.c2 = c1;
    this.sheet = sheet;
    this.sheetName = sheetName;
  }

  /**
   * Retrieves/evaluates the value of a reference.
   * @param visitor the visitor to use to evaluate this reference
   * @return the value a reference evaluates to
   */
  abstract Value getValue(SexpVisitor visitor);

  /**
   * A setter method to set the cell that holds this reference.
   * @param referencing the cell that is doing the referencing
   */
  void setReferencing(Cell referencing) {
    this.referencing = referencing;
  }


  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Reference)) {
      return false;
    }

    return ((Reference) other).c1.equals(this.c1) &&
            ((Reference) other).c2.equals(this.c2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(c1, c2);
  }


}
