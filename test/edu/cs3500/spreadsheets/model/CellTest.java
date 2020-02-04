package edu.cs3500.spreadsheets.model;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for the Cell class. Primarily testing whether or not cells evaluate correctly.
 */
public class CellTest {
  Cell card2;
  Cell card3;
  Cell empty;
  Cell numeric;
  Cell boolCell;
  Cell stringCell;
  Cell invalidFormCell;
  Cell validFormCell;
  Coord a1;
  Coord a2;
  Coord b1;
  Coord b2;
  Coord c1;
  Coord c2;
  Coord d1;

  WorksheetModel spreadsheet;
  WorkbookModel wb;


  @Before
  public void initWorkSheet() {
    a1 = new Coord(1, 1);
    a2 = new Coord(1, 2);
    b1 = new Coord(2, 1);
    b2 = new Coord(2, 2);
    c1 = new Coord(3, 1);
    c2 = new Coord(3, 2);
    d1 = new Coord(4, 1);
    wb = new BasicWorkbook();

    card2 = new Cell("=(SUM 3 7)", new CellReference(a2, wb));
    card3 = new Cell("A1", new CellReference(b1, wb));
    empty = new Cell("", new CellReference(b2, wb));
    numeric = new Cell("543", new CellReference(c1, wb));
    boolCell = new Cell("true", new CellReference(c2, wb));
    stringCell = new Cell("\"HELLO\"", new CellReference(d1, wb));
    invalidFormCell = new Cell("=SUP (3 2)", new CellReference(a1, wb));
    validFormCell = new Cell("=(SUM A2 B1)", new CellReference(c1, wb));

    spreadsheet = new BasicWorksheet(wb);
  }

  @Test
  public void testBlankCell() {
    assertNull(empty.evaluateCell());
  }

  @Test
  public void testNumericCell() {
    assertEquals(new DoubleValue(543), numeric.evaluateCell());
  }

  @Test
  public void testBooleanCell() {
    assertEquals(new BooleanValue(true), boolCell.evaluateCell());
  }

  @Test
  public void testStringCell() {
    assertEquals(new StringValue("HELLO"), stringCell.evaluateCell());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFormulaName() {
    invalidFormCell.evaluateCell();
  }

  @Test
  public void testGoodFormulaName() {
    assertEquals(new DoubleValue(10.0), card2.evaluateCell());
  }

  @Test
  public void testFormulaCell() {
    assertEquals(new DoubleValue(20.0), validFormCell.evaluateCell());
  }

}
