package edu.cs3500.spreadsheets.view;

import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkbookModel;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * Represents a read-only interface of a {@link WorksheetModel}.
 * @param <T> The type of cell the {@code WorksheetModel} holds
 */
public class ModelView<T> implements WorkbookModel<T> {
  private WorkbookModel model;

  /**
   * Constructs a new read-only interact of a {@link WorksheetModel}.
   * @param model the model from which to construct the read-only view
   */
  public ModelView(WorkbookModel model) {
    this.model = model;
  }

  @Override
  public void setCell(int row, int col, String value) {
    throw new UnsupportedOperationException("Cannot set cell from view");
  }

  @Override
  public void evalCell(Coord coord) {
    model.evalCell(coord);
  }

  @Override
  public List<Coord> updateSheet() {
    return model.updateSheet();
  }

  @Override
  public void deleteCell(Coord coord) {
    throw new UnsupportedOperationException("Cannot delete cell from view");
  }

  @Override
  public T getCellAt(Coord coord) {
    return (T) model.getCellAt(coord);
  }

  @Override
  public Coord getCoord(T cell) {
    return model.getCoord(cell);
  }

  @Override
  public HashMap<Coord, T> getGrid() {
    return model.getGrid();
  }

  @Override
  public void changeSheet(String sheetName) {
    this.model.changeSheet(sheetName);
  }

  @Override
  public boolean addSheet(String newSheetName) {
    return false;
  }

  @Override
  public void deleteSheet(String sheetName) {
    throw new UnsupportedOperationException("Cannot delete a sheet from the view");
  }

  @Override
  public List<String> getSheets() {
    return model.getSheets();
  }

  @Override
  public String getCurrentSheetName() {
    return model.getCurrentSheetName();
  }

  @Override
  public void loadSheet(String path) {
    throw new UnsupportedOperationException("Should not load from view");
  }

  @Override
  public void saveSheet(String path) {
    throw new UnsupportedOperationException("Should not save from view");
  }
}
