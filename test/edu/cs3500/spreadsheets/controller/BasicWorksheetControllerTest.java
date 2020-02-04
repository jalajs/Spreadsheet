package edu.cs3500.spreadsheets.controller;

import org.junit.Before;
import org.junit.Test;

import edu.cs3500.spreadsheets.model.BasicWorkbook;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellReference;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkbookModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.view.ModelView;
import edu.cs3500.spreadsheets.view.SpreadsheetEditView;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * Class for testing the methods in the controller.
 */
public class BasicWorksheetControllerTest {
  Cell c1;
  Cell c2;
  Cell c3;
  Coord a1;
  Coord a2;
  Coord b1;
  WorksheetModel<Cell> spreadsheet;
  ModelView spreadsheetModelView;
  BasicWorksheetController controller;
  SpreadsheetView view;
  WorkbookModel wb;

  @Before
  public void initWorkSheet() {
    a1 = new Coord(1, 1);
    a2 = new Coord(1, 2);
    b1 = new Coord(2, 1);
    wb = new BasicWorkbook();

    c1 = new Cell("\"Hello\"", new CellReference(a1, wb));
    c2 = new Cell("=(SUM 3 7)", new CellReference(a2, wb));
    c3 = new Cell("A1", new CellReference(b1, wb));

    spreadsheet = new BasicWorksheet(wb);
    spreadsheetModelView = new ModelView(wb);
    view = new SpreadsheetEditView(spreadsheetModelView);

    controller = new BasicWorksheetController(wb);
    controller.setView(view);
  }

  @Test
  public void testConfirmInput() {
    assertNull(spreadsheet.getCellAt(a1));
    controller.confirmInput("\"Hello\"", a1);
    assertEquals(c1, spreadsheet.getCellAt(a1));

    assertNull(spreadsheet.getCellAt(a2));
    controller.confirmInput("=(SUM 3 7)", a2);
    assertEquals(c2, spreadsheet.getCellAt(a2));

    assertNull(spreadsheet.getCellAt(b1));
    controller.confirmInput("A1", b1);
    assertEquals(c3, spreadsheet.getCellAt(b1));
  }

  @Test
  public void testSelectCell() {
    assertEquals(a1, controller.selectCell(80, 35));
    assertEquals(b1, controller.selectCell(100, 35));
    assertNull(controller.selectCell(1, 1));
    assertNull(controller.selectCell(600, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadFileBadFileName() {
    this.controller.loadFile("ui\":\\dadw");
  }

  @Test(expected = IllegalStateException.class)
  public void testLoadFileWrongFileType() {
    this.controller.loadFile("README.txt");
  }

  @Test
  public void testLoadFileGoodFile() {
    assertNull(this.spreadsheet.getCellAt(new Coord(1, 1)));
    assertNull(this.spreadsheet.getCellAt(new Coord(3, 1)));
    assertTrue(this.spreadsheet.getGrid().isEmpty());
    this.controller.loadFile("reallySimpleFile.txt");
    assertEquals(3.0, this.spreadsheet.getCellAt(new Coord(1, 1))
            .evaluateCell().numberForm(), 0.001);
    assertEquals(9.0, this.spreadsheet.getCellAt(new Coord(1, 3))
            .evaluateCell().numberForm(), 0.001);
    assertEquals(12, this.spreadsheet.getGrid().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveFileBadName() {
    this.controller.saveFile("daksad:\\#\"dsd");
  }

  @Test
  public void testSaveFileGoodName() {
    this.spreadsheet.setCell(1, 1, "544");
    this.spreadsheet.setCell(4, 1, "\"hello\"");
    this.controller.saveFile("testSaveFile.txt");
    WorksheetModel<Cell> newWM = new BasicWorksheet(wb);

    BasicWorksheetController cont = new BasicWorksheetController(wb);
    ModelView wm = new ModelView(wb);
    SpreadsheetView view = new SpreadsheetEditView(wm);
    cont.setView(view);
    assertNull(newWM.getCellAt(new Coord(1, 1)));
    assertNull(newWM.getCellAt(new Coord(1, 4)));
    cont.loadFile("testSaveFile.txt");
    assertEquals(544.0, newWM.getCellAt(new Coord(1, 1))
            .evaluateCell().numberForm(), 0.001);
    assertEquals("hello", newWM.getCellAt(new Coord(1, 4))
            .evaluateCell().toString());
  }


  @Test
  public void testClearCell() {
    this.spreadsheet.setCell(1, 1, "500");
    assertEquals(500.0, this.spreadsheet.getCellAt(new Coord(1, 1))
            .evaluateCell().numberForm(), 0.001);
    this.controller.clearCell(new Coord(1, 1));
    assertNull(this.spreadsheet.getCellAt(new Coord(1, 1)));
  }


}
