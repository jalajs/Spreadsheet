package edu.cs3500.spreadsheets.controller;

import java.io.IOException;
import java.util.List;


import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkbookModel;
import edu.cs3500.spreadsheets.view.SpreadsheetPanel;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

/**
 * Represents a basic controller for a {@link edu.cs3500.spreadsheets.model.WorksheetModel
 * WorksheetModel}.
 */
public class BasicWorksheetController implements Features {
  private WorkbookModel model;
  private SpreadsheetView view;

  /**
   * Creates a new controller for a basic worksheet.
   *
   * @param model the model for the controller to make calls to
   */
  public BasicWorksheetController(WorkbookModel model) {
    this.model = model;
  }

  /**
   * Sets the view for this controller to render.
   *
   * @param view the view for this controller
   */
  public void setView(SpreadsheetView view) {
    this.view = view;
    view.addFeatures(this);
  }


  @Override
  public void confirmInput(String newValue, Coord coord) {
    model.setCell(coord.row, coord.col, newValue);
    try {
      view.render();
    } catch (IOException e) {
      System.out.println("Could not render view");
    }
  }

  @Override
  public Coord selectCell(int x, int y) {
    final int cellWidth = SpreadsheetPanel.CELL_WIDTH;
    final int cellHeight = SpreadsheetPanel.CELL_HEIGHT;
    int col = x / cellWidth + (SpreadsheetPanel.getxStart());
    int row = y / cellHeight + (SpreadsheetPanel.getyStart());
    try {
      view.render();
    } catch (IOException e) {
      System.out.println("Could not render view");
    }
    if (col >= 1 && row >= 1) {
      return new Coord(col, row);
    } else {
      return null;
    }
  }

  @Override
  public void loadFile(String path) throws IllegalStateException, IllegalArgumentException {
    this.model.loadSheet(path);
    try {
      view.render();
    } catch (IOException e) {
      System.out.println("Could not render view");
    }
  }

  @Override
  public void saveFile(String path) throws IllegalArgumentException {
    this.model.saveSheet(path);
  }

  @Override
  public void clearCell(Coord coord) {
    this.model.deleteCell(coord);
    try {
      view.render();
    } catch (IOException e) {
      System.out.println("Could not render view");
    }
  }

  @Override
  public boolean addWorksheet(String name) {
    return this.model.addSheet(name);
  }

  @Override
  public void changeWorkbook(String wbName) {
    this.model.changeSheet(wbName);
  }

  @Override
  public void deleteWorksheet(String sheetName) {
    this.model.deleteSheet(sheetName);
  }

  @Override
  public List<String> getSheets() {
    return model.getSheets();
  }

  @Override
  public String getCurrentSheet() {
    return model.getCurrentSheetName();
  }

}
