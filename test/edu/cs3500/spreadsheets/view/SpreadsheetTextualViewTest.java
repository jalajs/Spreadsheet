package edu.cs3500.spreadsheets.view;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import edu.cs3500.spreadsheets.model.BasicWorkbook;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkbookModel;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.ModelView;
import edu.cs3500.spreadsheets.view.SpreadsheetTextualView;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Represent tests for a textual rendering of a spreadsheet.
 */
public class SpreadsheetTextualViewTest {
  SpreadsheetTextualView textualView1;
  SpreadsheetTextualView textualView2;
  SpreadsheetTextualView textualViewEmpty;

  @Before
  public void init() {
    WorkbookModel workbook = new BasicWorkbook();
    WorksheetModel worksheet = new BasicWorksheet(workbook);
    workbook.setCell(1, 1, "\"hello\"");
    workbook.setCell(2, 1, "\"world\"");
    workbook.setCell(2, 2, "420");
    workbook.setCell(2, 3, "true");
    workbook.setCell(3, 5, "=(PRODUCT (SUB C1 A1) (SUB C1 A1))");
    workbook.setCell(6, 2, "=(SQRT (SUM A2:B2))");
    ModelView modelView = new ModelView<Cell>(workbook);
    textualView1 = new SpreadsheetTextualView(modelView);

    textualViewEmpty = new SpreadsheetTextualView(new ModelView<Cell>(new BasicWorkbook()));

    WorkbookModel workbook2 = new BasicWorkbook();
    workbook2.addSheet("Sheet2");
    workbook.changeSheet("Sheet2");
    WorksheetModel worksheet2 = new BasicWorksheet(workbook2);
    workbook2.setCell(6, 3, "53");
    workbook2.setCell(3, 6, "933");
    workbook2.setCell(8, 9, "\"OOD\"");
    workbook2.setCell(3, 5, "false");
    workbook2.setCell(15, 12, "=(SUM A43 A4 (PRODUCT 3 B9))");
    workbook2.setCell(9, 14, "=(STRCAT \"I LOVE\" \"DESIGN\" \"RECIPE\"");
    ModelView modelView2 = new ModelView<>(workbook2);
    textualView2 = new SpreadsheetTextualView(modelView2);
  }

  @Test
  public void testToString() {
    assertEquals("%Sheet1\n", textualViewEmpty.toString());
    assertEquals("%Sheet1\n" + "C2 true\n" +
            "A1 \"hello\"\n" +
            "B2 420\n" +
            "A2 \"world\"\n" +
            "B6 =(SQRT (SUM A2:B2))\n" +
            "E3 =(PRODUCT (SUB C1 A1) (SUB C1 A1))\n", textualView1.toString());

    assertEquals("%Sheet2\n" + "%Sheet1\n" + "I8 \"OOD\"\n" +
            "C6 53\n" +
            "L15 =(SUM A43 A4 (PRODUCT 3 B9))\n" +
            "N9 =(STRCAT \"I LOVE\" \"DESIGN\" \"RECIPE\"\n" +
            "F3 933\n" +
            "E3 false\n", textualView2.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalRender() {
    textualView1.render();
  }

  // completes a round trip test. reads in a file, renders it through this view, and then reads this
  // view’s output back in as a second model, and checks that the two models are equivalent.
  @Test
  public void testRender() {
    WorksheetReader.WorksheetBuilder<BasicWorkbook> builder1 = new WorksheetCreator();
    WorksheetReader.WorksheetBuilder<BasicWorkbook> builder2 = new WorksheetCreator();
    FileReader file1;
    try {
      file1 = new FileReader("reallySimpleFile.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Incorrect file given.");
    }
    BasicWorkbook basic1 = WorksheetReader.read(builder1, file1);
    ModelView<?> modelView = new ModelView<Cell>(basic1);
    PrintWriter pw;
    FileReader file2;
    try {
      pw = new PrintWriter("reallySimpleFileOutput.txt");
      SpreadsheetView view = new SpreadsheetTextualView(modelView, pw);
      view.render();
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Incorrect file given.");
    } catch (IOException e) {
      throw new IllegalArgumentException("IO exception rendering.");
    }
    try {
      file2 = new FileReader("reallySimpleFileOutput.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Incorrect file given.");
    }

    BasicWorkbook basic2 = WorksheetReader.read(builder2, file2);
    assertNotEquals(basic1, basic2); // making sure that we aren't testing the exact same model
    HashMap<Coord, Cell> cells1 = basic1.getGrid();
    HashMap<Coord, Cell> cells2 = basic2.getGrid();
    assertEquals(cells1, cells2);
    assertEquals(basic1.getCellAt(new Coord(1, 1)), basic2.getCellAt(new Coord(1, 1)));
    assertEquals(basic1.getCellAt(new Coord(2, 1)), basic2.getCellAt(new Coord(2, 1)));
    assertEquals(basic1.getCellAt(new Coord(2, 2)), basic2.getCellAt(new Coord(2, 2)));
    assertEquals(basic1.getCellAt(new Coord(6, 8)), basic2.getCellAt(new Coord(6, 8)));
    assertEquals(basic1.getCellAt(new Coord(4, 2)), basic2.getCellAt(new Coord(4, 2)));
    assertEquals(basic1.getCellAt(new Coord(25, 5)), basic2.getCellAt(new Coord(25, 5)));
  }

  @Test
  public void testRender2() {
    WorksheetReader.WorksheetBuilder<BasicWorkbook> builder1 = new WorksheetCreator();
    WorksheetReader.WorksheetBuilder<BasicWorkbook> builder2 = new WorksheetCreator();
    FileReader file1;
    try {
      file1 = new FileReader("cycles.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Incorrect file given.");
    }
    BasicWorkbook basic1 = WorksheetReader.read(builder1, file1);
    ModelView<?> modelView = new ModelView<Cell>(basic1);
    PrintWriter pw;
    FileReader file2;
    try {
      pw = new PrintWriter("cyclesOutput.txt");
      SpreadsheetView view = new SpreadsheetTextualView(modelView, pw);
      view.render();
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Incorrect file given.");
    } catch (IOException e) {
      throw new IllegalArgumentException("IO exception rendering.");
    }
    try {
      file2 = new FileReader("cyclesOutput.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Incorrect file given.");
    }
    BasicWorkbook basic2 = WorksheetReader.read(builder2, file2);
    assertNotEquals(basic1, basic2); // making sure that we aren't testing the exact same model
    HashMap<Coord, Cell> cells1 = basic1.getGrid();
    HashMap<Coord, Cell> cells2 = basic2.getGrid();
    assertEquals(cells1, cells2);
    assertEquals(basic1.getCellAt(new Coord(1, 1)), basic2.getCellAt(new Coord(1, 1)));
    assertEquals(basic1.getCellAt(new Coord(1, 2)), basic2.getCellAt(new Coord(1, 2)));
    assertEquals(basic1.getCellAt(new Coord(1, 3)), basic2.getCellAt(new Coord(1, 3)));
    assertEquals(basic1.getCellAt(new Coord(2, 1)), basic2.getCellAt(new Coord(2, 1)));
    assertEquals(basic1.getCellAt(new Coord(2, 2)), basic2.getCellAt(new Coord(2, 2)));
    assertEquals(basic1.getCellAt(new Coord(3, 1)), basic2.getCellAt(new Coord(3, 1)));
  }

}