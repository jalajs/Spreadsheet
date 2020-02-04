package edu.cs3500.spreadsheets.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cs3500.spreadsheets.view.ModelView;
import edu.cs3500.spreadsheets.view.SpreadsheetTextualView;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

/**
 * Represents a simple implementation of a spreadsheet application workbook that contains multiple
 * distinct spreadsheets.
 */
public class BasicWorkbook implements WorkbookModel {
  private HashMap<String, WorksheetModel> worksheets;
  private String currentSheet;
  private static final int MAX_SHEET_NAME_LEN = 10;

  /**
   * Creates a new workbook that initially contains no spreadsheets.
   */
  public BasicWorkbook() {
    this.worksheets = new HashMap<>();
    this.currentSheet = "";
  }

  @Override
  public void setCell(int row, int col, String value) {
    this.checkAddDefaultSheet();
    this.worksheets.get(currentSheet).setCell(row, col, value);
  }

  @Override
  public void evalCell(Coord coord) {
    this.checkAddDefaultSheet();
    this.worksheets.get(currentSheet).evalCell(coord);
  }

  @Override
  public List<Coord> updateSheet() {
    this.checkAddDefaultSheet();
    return worksheets.get(currentSheet).updateSheet();
  }

  @Override
  public void deleteCell(Coord coord) {
    this.checkAddDefaultSheet();
    worksheets.get(currentSheet).deleteCell(coord);
  }

  @Override
  public Object getCellAt(Coord coord) {
    this.checkAddDefaultSheet();
    return worksheets.get(currentSheet).getCellAt(coord);
  }

  @Override
  public Coord getCoord(Object cell) {
    this.checkAddDefaultSheet();
    return worksheets.get(currentSheet).getCoord(cell);
  }

  @Override
  public HashMap getGrid() {
    this.checkAddDefaultSheet();
    return worksheets.get(currentSheet).getGrid();
  }

  @Override
  public void changeSheet(String sheetName) {
    this.currentSheet = sheetName;
  }

  @Override
  public boolean addSheet(String newSheetName) {
    if (newSheetName.equals("") || newSheetName.length() > MAX_SHEET_NAME_LEN ||
            this.worksheets.containsKey(newSheetName)) {
      return false;
    }

    this.worksheets.put(newSheetName, new BasicWorksheet(this));
    return true;
  }

  @Override
  public void deleteSheet(String sheetName) {
    this.worksheets.remove(sheetName);
    if (this.currentSheet.equals(sheetName)) {
      if (this.worksheets.isEmpty()) {
        String fillerSheetName = "Sheet1";
        this.addSheet(fillerSheetName);
        this.changeSheet(fillerSheetName);
      } else {
        this.changeSheet(this.worksheets.keySet().toArray()[0].toString());
      }
    }
  }

  @Override
  public List<String> getSheets() {
    List<String> sheets = new ArrayList<>();
    for (Map.Entry<String, WorksheetModel> entry : this.worksheets.entrySet()) {
      sheets.add(entry.getKey());
    }
    return sheets;
  }

  @Override
  public String getCurrentSheetName() {
    return this.currentSheet;
  }

  @Override
  public void loadSheet(String path) {
    FileReader file = loadFileFromPath(path);
    WorkbookModel<Cell> loadModel;
    loadModel = WorksheetReader.read(new WorksheetCreator(), file);
    List<String> sheets = loadModel.getSheets();

    this.worksheets = new HashMap<>();

    for (String s : sheets) {
      loadModel.changeSheet(s);
      this.addSheet(s);
      this.changeSheet(s);
      Set<Map.Entry<Coord, Cell>> newEntries = loadModel.getGrid().entrySet();
      for (Map.Entry<Coord, Cell> entry : newEntries) {
        this.setCell(entry.getKey().row, entry.getKey().col, entry.getValue().toString());
      }
    }


  }

  @Override
  public void saveSheet(String path) {
    this.checkAddDefaultSheet();
    File save = new File(path);
    PrintWriter p;
    ModelView<Cell> modelView = new ModelView<>(this);
    try {
      p = new PrintWriter(save);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Bad file name to save");
    }
    SpreadsheetView textRender = new SpreadsheetTextualView(modelView, p);
    try {
      textRender.render();
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot append to file");
    }
  }

  /**
   * Loads the file with the given file name and returns a FileReader for it.
   *
   * @param fileName the path to the file to read
   * @return a FileReader corresponding to the given fileName
   */
  private FileReader loadFileFromPath(String fileName) {
    FileReader file;
    try {
      file = new FileReader(fileName);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot load given file name");
    }
    return file;
  }

  /**
   * Checks to see if any sheets have been added yet from file, otherwise creates a default sheet.
   */
  private void checkAddDefaultSheet() {
    if (this.currentSheet.equals("")) {
      this.currentSheet = "Sheet1";
      this.worksheets.put(currentSheet, new BasicWorksheet(this));
    }
  }

}
