package edu.cs3500.spreadsheets.model;

import org.junit.Test;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.WorksheetCreator;

import static org.junit.Assert.assertEquals;

/**
 * Test class for a worksheet creator factory method.
 */
public class WorksheetCreatorTest {
  WorksheetCreator creator = new WorksheetCreator();
  WorkbookModel wb = new BasicWorkbook();
  BasicWorksheet basic = new BasicWorksheet(wb);

  @Test(expected = IllegalArgumentException.class)
  public void testCreateCellNullContents() {
    creator.createCell(2,5,null);
  }

  @Test
  public void testCreateCell() {
    basic.setCell(1, 1, "5");
    assertEquals(basic, creator.createCell(1, 1, "5"));
  }

  @Test
  public void testCreateWorkSheet() {
    assertEquals(basic, creator.createWorksheet());
  }
}