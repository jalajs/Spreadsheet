package edu.cs3500.spreadsheets.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for an implementation of the Workbook interface.
 */
public class BasicWorkBookTest {
  Cell c1;
  Cell c2;
  Cell c3;
  Cell c4;
  Cell c5;
  Coord a1;
  Coord a2;
  Coord b1;
  Coord d1;
  WorksheetModel spreadsheet;
  BasicWorksheet basic;
  WorkbookModel<Cell> wb;


  @Before
  public void initWorkSheet() {
    a1 = new Coord(1, 1);
    a2 = new Coord(1, 2);
    b1 = new Coord(2, 1);
    d1 = new Coord(4, 1);

    wb = new BasicWorkbook();
    c1 = new Cell("\"Hello\"", new CellReference(a1, wb));
    c2 = new Cell("=(SUM 3 7)", new CellReference(a2, wb));
    c3 = new Cell("A1", new CellReference(b1, wb));
    c4 = new Cell("FooSheet!A1", new CellReference(a1, wb));
    c5 = new Cell("55", new CellReference(a2, wb));


    spreadsheet = new BasicWorksheet(wb);
    basic = new BasicWorksheet(wb);
    wb.addSheet("FooSheet");
    wb.addSheet("Sheet2");

    wb.changeSheet("Sheet2");
    wb.setCell(1,1,"56");
    wb.changeSheet("FooSheet");
    wb.setCell(3,3,"128");

    wb.changeSheet("FooSheet");
  }

  @Test
  public void testsetCell() {
    assertNull(wb.getCellAt(a1));
    assertNull(wb.getCellAt(a2));
    assertNull(wb.getCellAt(b1));

    wb.setCell(1, 1, "\"Hello\"");

    wb.setCell(2, 1, "A1");

    assertEquals(c1, wb.getCellAt(a1));
    assertEquals(c3, wb.getCellAt(a2));
    assertEquals(c3, wb.getCellAt(b1));
    assertEquals(3, wb.getGrid().size());
  }

  // checking for references between sheets
  @Test
  public void testevalCell() {
    wb.setCell(1, 2, "=(SUM 3 7)");
    Cell c = wb.getCellAt(new Coord(2, 1));
    assertEquals(new DoubleValue(10.0), c.evaluateCell());

    wb.setCell(1, 1, "\"Hello\"");
    wb.setCell(2, 1, "A1");
    Cell cell1 = wb.getCellAt(new Coord(1, 2));
    assertEquals(new StringValue("Hello"), cell1.evaluateCell());

    wb.changeSheet("FooSheet");
    wb.setCell(20, 10, "Sheet2!A1");
    Cell cell2 = wb.getCellAt(new Coord(10, 20));
    assertEquals(new DoubleValue(56.0), cell2.evaluateCell());
  }

  // tests getSheets as well
  @Test
  public void testAddSheet() {
    ArrayList<String> sheets = new ArrayList<>();
    sheets.add("Sheet2");
    sheets.add("Sheet1");
    sheets.add("FooSheet");
    assertEquals(sheets, wb.getSheets());
    wb.addSheet("New Sheet");
    sheets.add("New Sheet");
    assertEquals(sheets, wb.getSheets());
  }

  @Test
  public void testDeleteSheet() {
    ArrayList<String> sheets = new ArrayList<>();
    sheets.add("Sheet2");
    sheets.add("Sheet1");
    sheets.add("FooSheet");
    assertEquals(sheets, wb.getSheets());
    wb.deleteSheet("FooSheet");
    sheets.remove("FooSheet");
    assertEquals(sheets, wb.getSheets());
  }

  // tests changeSheet as well
  @Test
  public void testgetCurrentSheet() {
    assertEquals("FooSheet", wb.getCurrentSheetName());
    wb.changeSheet("Sheet2");
    assertEquals("Sheet2", wb.getCurrentSheetName());
  }
}
