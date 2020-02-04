package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a workbook in a spreadsheet application that can hold and manipulate multiple sheets.
 * @param <K> The type of the individual cells in this workbook
 */
public interface WorkbookModel<K> extends WorksheetModel<K> {

  /**
   * Changes the current sheet being viewed and manipulated to the one with the given name.
   * @param sheetName The name of the spreadsheet to switch to and make the current sheet
   */
  void changeSheet(String sheetName);

  /**
   * Adds a new spreadsheet to this workbook.
   * @param newSheetName The name of the spreadsheet to add
   * @return whether the sheet could be successfully added. Generally fails if name is invalid
   */
  boolean addSheet(String newSheetName);

  /**
   * Deletes the sheet in this workbook with the given name.
   * @param sheetName The name of the spreadsheet to remove
   */
  void deleteSheet(String sheetName);

  /**
   * Gets a list of the names of all the spreadsheets contained within this workbook.
   * @return A list of all the spreadsheet names
   */
  List<String> getSheets();

  /**
   * Retrieves the name of the sheet that is currently in focus in this workbook.
   * @return The name of the current sheet
   */
  String getCurrentSheetName();

  /**
   * Loads sheets from a given file path into this workbook, replacing all previous contents.
   * @param path The file path to load sheets from
   */
  void loadSheet(String path);

  /**
   * Saves the current workbook to file, writing each sheet out in different file sections.
   * @param path The file path to which this should be saved
   */
  void saveSheet(String path);
}
