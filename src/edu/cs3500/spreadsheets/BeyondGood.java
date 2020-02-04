package edu.cs3500.spreadsheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import edu.cs3500.spreadsheets.controller.BasicWorksheetController;
import edu.cs3500.spreadsheets.model.BasicWorkbook;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorkbookModel;
import edu.cs3500.spreadsheets.model.WorksheetCreator;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.ModelView;
import edu.cs3500.spreadsheets.view.SpreadsheetEditView;
import edu.cs3500.spreadsheets.view.SpreadsheetGUIView;
import edu.cs3500.spreadsheets.view.SpreadsheetTextualView;
import edu.cs3500.spreadsheets.view.SpreadsheetView;

/**
 * The main class for our program.
 */
public class BeyondGood {

  /**
   * Loads the file associated with the given fileName.
   * @param fileName the file's name
   * @return returns the loaded file
   */
  private static FileReader loadFile(String fileName) {
    FileReader file;
    try {
      file = new FileReader(fileName);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Cannot load given file name");
    }
    return file;
  }

  /**
   * Reads in the given file, evaluates it, and prints the result of the requested cell.
    * @param fileName The given file's name
   * @param cellName the name of the cell that needs to be evaluated
   */
  private static void readFileEvalCell(String fileName, String cellName) {
    // command should be:
    //     -in *some-filename* -eval *some-cell*
    FileReader file = loadFile(fileName);
    WorksheetModel<Cell> model = WorksheetReader.read(new WorksheetCreator(), file);
    List<Coord> evalCells = model.updateSheet();
    if (evalCells.size() > 0) {
      // there was an error in a cell
      for (Coord c : evalCells) {
        System.out.println("Error in cell " + Coord.colIndexToName(c.col) + c.row);
      }
    }

    Coord evalCoord = Coord.coordStringToCoord(cellName);

    Cell evalCell = model.getCellAt(evalCoord);
    if (evalCell == null) {
      System.out.print("");
    } else {
      String output = evalCell.evaluateCell().toString();
      System.out.print(output);
    }
  }

  /**
   * Opens the first file, and saves it as the second file using the textual view.
   * @param oldFileName The first file
   * @param newFileName the second file which the first filed is saved as
   */
  private static void readFileSaveNewFile(String oldFileName, String newFileName) {
    // command should be:
    //     -in *some-filename* -save *some-new-filename*
    FileReader file = loadFile(oldFileName);
    WorkbookModel<Cell> model = WorksheetReader.read(new WorksheetCreator(), file);
    ModelView<Cell> modelView = new ModelView<Cell>(model);
    File save = new File(newFileName);
    PrintWriter p;
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
   * Opens the graphical view and loads the requested file and evaluates it.
   * @param fileName the file's name
   */
  private static void loadFileOpenGui(String fileName, boolean isEdit) {
    FileReader file = loadFile(fileName);
    WorkbookModel<Cell> model = WorksheetReader.read(new WorksheetCreator(), file);
    ModelView<Cell> modelView = new ModelView<>(model);
    SpreadsheetView guiRender;
    if (isEdit) {
      BasicWorksheetController spreadsheetController = new BasicWorksheetController(model);
      guiRender = new SpreadsheetEditView(modelView);
      spreadsheetController.setView(guiRender);
    } else {
      guiRender = new SpreadsheetGUIView(modelView);
    }
    guiRender.makeVisible(true);
    try {
      guiRender.render();
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot render gui view");
    }
  }

  /**
   * Opens the graphical view with a blank new spreadsheet.
   */
  private static void openGuiBlankSheet(boolean isEdit) {
    WorkbookModel<Cell> model = new BasicWorkbook();
    ModelView<Cell> modelView = new ModelView<>(model);
    SpreadsheetView guiRender;
    if (isEdit) {
      BasicWorksheetController spreadsheetController = new BasicWorksheetController(model);
      guiRender = new SpreadsheetEditView(modelView);
      spreadsheetController.setView(guiRender);
    } else {
      guiRender = new SpreadsheetGUIView(modelView);
    }
    guiRender.makeVisible(true);
    try {
      guiRender.render();
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot render gui view");
    }
  }

  /**
   * The main entry point.
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    FileReader file;
    /*
      TODO: For now, look in the args array to obtain a filename and a cell name,
      - read the file and build a model from it, 
      - evaluate all the cells, and
      - report any errors, or print the evaluated value of the requested cell.
    */

    switch (args.length) {
      case 4: {
        // command should be one of:
        //     -in *some-filename* -eval *some-cell*
        //  OR
        //     -in *some-filename* -save *some-new-filename*
        if (args[0].equals("-in") && args[2].equals("-eval")) {
          readFileEvalCell(args[1], args[3]);
        } else if (args[0].equals("-in") && args[2].equals("-save")) {
          readFileSaveNewFile(args[1], args[3]);
        } else {
          throw new IllegalArgumentException("Bad command line arguments--" +
                  "okay length but unknown commands");
        }
        break;
      }
      case 3: {
        // command should be:
        //     -in *some-filename* -gui
        //   OR
        //     -in *some-filename* -edit
        if (args[0].equals("-in") && args[2].equals("-gui")) {
          loadFileOpenGui(args[1], false);
        } else if (args[0].equals("-in") && args[2].equals("-edit")) {
          loadFileOpenGui(args[1], true);
        } else {
          throw new IllegalArgumentException("Bad command line arguments--okay length (3) but" +
                  "unknown commands");
        }
        break;
      }
      case 1: {
        // command should be:
        //     -gui
        //  OR
        //     -edit
        if (args[0].equals("-gui")) {
          openGuiBlankSheet(false);
        } else if (args[0].equals("-edit")) {
          openGuiBlankSheet(true);
        } else {
          throw new IllegalArgumentException("Bad command line arguments--okay length (1) but" +
                  "unknown command");
        }
        break;
      }
      default: {
        throw new IllegalArgumentException("Bad command line arguments--invalid number of args");
      }
    }
  }
}
