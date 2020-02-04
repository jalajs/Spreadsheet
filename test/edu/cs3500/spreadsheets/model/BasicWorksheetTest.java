package edu.cs3500.spreadsheets.model;

import org.junit.Before;
import org.junit.Test;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for our implementation of a spreadsheet.
 */
public class BasicWorksheetTest {
  Cell c1;
  Cell c2;
  Cell c3;
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

    spreadsheet = new BasicWorksheet(wb);
    basic = new BasicWorksheet(wb);
    wb.addSheet("FooSheet");
    wb.changeSheet("FooSheet");

  }


  // test for confirming that all the cells added to a spreadsheet are in fact there
  @Test
  public void testSetCells() {

    assertNull(wb.getCellAt(a1));
    assertNull(wb.getCellAt(a2));
    assertNull(wb.getCellAt(b1));

    wb.setCell(1, 1, "\"Hello\"");
    wb.setCell(1, 2, "=(SUM 3 7)");
    wb.setCell(2, 1, "A1");

    assertEquals(c1, wb.getCellAt(a1));
    assertEquals(c3, wb.getCellAt(a2));
    assertEquals(c3, wb.getCellAt(b1));
    assertEquals(3, wb.getGrid().size());

  }

  @Test
  public void testEmptySpreadsheet() {
    assertTrue(spreadsheet.getGrid().isEmpty());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testNoSelfRefFormulas() {
    spreadsheet.setCell(1, 1, "=(SUM A1 3");

  }

  @Test (expected = IllegalArgumentException.class)
  public void testNoSelfRefFormulasIndirectly() {
    spreadsheet.setCell(1,1, "B1");
    spreadsheet.setCell(2, 1, "=(PROD A1 3)");
  }

  // a test of formulas that refer to the same cell multiple times
  @Test
  public void testSameCell() {
    basic.setCell(1,1, "2");
    basic.setCell(2, 1, "=(SUM A1 A1)");
    Cell c = basic.getCellAt(b1);
    assertEquals(new DoubleValue(4.0), c.evaluateCell());
  }

  @Test
  public void testRegionRefArgs() {
    basic.setCell(1, 1, "1");
    basic.setCell(1, 2, "4");
    basic.setCell(2, 1, "5");
    basic.setCell(2, 2, "8");
    basic.setCell(3,1, "=(SUM A1:B2)");
    Cell c = basic.getCellAt(d1);
    assertEquals(new DoubleValue(18.0), c.evaluateCell());
  }

  // tests for column references

  @Test
  public void testColRef1() {
    wb.setCell(1, 1, "1");
    wb.setCell(2, 1, "5");
    wb.setCell(3, 1, "6");
    wb.setCell(4, 1, "7");
    wb.setCell(5, 1, "8");
    wb.setCell(5, 5, "=(SUM A:A)");
    Cell c = wb.getCellAt(new Coord(5, 5));
    assertEquals(new DoubleValue(27.0), c.evaluateCell());
  }

  @Test
  public void testColRefs() {
    wb.setCell(1, 1, "1");
    wb.setCell(2, 1, "5");
    wb.setCell(3, 1, "6");
    wb.setCell(4, 1, "7");
    wb.setCell(5, 1, "8");
    wb.setCell(1, 2, "B3");
    wb.setCell(3, 2, "3");
    wb.setCell(50, 2, "-3");
    wb.setCell(5, 5, "=(SUM A:B)");
    Cell c = wb.getCellAt(new Coord(5, 5));
    assertEquals(new DoubleValue(30.0), c.evaluateCell());
  }


  @Test
  public void testColRefsCyclicError() {
    wb.setCell(1, 1, "1");
    wb.setCell(2, 1, "5");
    wb.setCell(3, 1, "6");
    wb.setCell(1, 2, "7");
    wb.setCell(3, 3, "3");
    wb.setCell(50, 3, "-3");
    wb.setCell(3, 2, "=(SUM A:C)");
    Cell c = wb.getCellAt(new Coord(2, 3));
    assertEquals(new ErrorValue(), c.evaluateCell());
  }

  @Test
  public void testColRefsCylicError2() {
    wb.setCell(1, 5, "=(SUM A:A)");
    Cell c = wb.getCellAt(new Coord(5, 1));
    assertEquals(new ErrorValue(), c.evaluateCell()); // returns an error
  }











}
