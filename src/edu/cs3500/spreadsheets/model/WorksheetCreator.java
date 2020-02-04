package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;


/**
 * A factory for the creation of worksheets. Implements the Builder from the starter code using an
 * instance of our basic implementation of worksheets.
 */
public class WorksheetCreator implements WorksheetReader.WorksheetBuilder<BasicWorkbook> {
  private final BasicWorkbook spreadsheet;

  public WorksheetCreator() {
    this.spreadsheet = new BasicWorkbook();
  }

  @Override
  public WorksheetReader.WorksheetBuilder<BasicWorkbook> createCell(int col, int row,
                                                                     String contents) {
    String tempContents = contents;
    if (contents == null) {
      throw new IllegalArgumentException("Null cell contents.");
    }
    if (contents.substring(0, 1).equals("=")) {
      tempContents = contents.substring(1);
    }
    Sexp sexp;
    try {
      sexp = Parser.parse(tempContents);
    } catch (IllegalArgumentException e) {
      this.spreadsheet.setCell(row, col, tempContents);
      return this;
    }
    String val = sexp.toString();
    this.spreadsheet.setCell(row, col, val);
    return this;
  }

  @Override
  public WorksheetReader.WorksheetBuilder<BasicWorkbook> addNewSheet(String name) {
    this.spreadsheet.addSheet(name);
    this.spreadsheet.changeSheet(name);

    return this;
  }

  @Override
  public BasicWorkbook createWorksheet() {
    return spreadsheet;
  }
}
