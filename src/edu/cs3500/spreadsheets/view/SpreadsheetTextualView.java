package edu.cs3500.spreadsheets.view;

import java.io.PrintWriter;
import java.util.Map;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents a textual view of a {@link SpreadsheetView}. Is a subclass of the the SpreadsheetView
 * interface.
 */
public class SpreadsheetTextualView implements SpreadsheetView {
  private ModelView<?> modelView;
  private PrintWriter out;

  /**
   * Constructs a new textual view of a spreadsheet.
   * @param modelView the spreadsheet model on which to base the textual view
   * @param out the appendable on which to append the newly generated textual view
   */
  public SpreadsheetTextualView(ModelView<?> modelView, PrintWriter out) {
    this.modelView = modelView;
    this.out = out;
  }

  /**
   * Constructs a new textual view of a spreadsheet.
   * @param modelView the spreadsheet model on which to base the textual view
   */
  SpreadsheetTextualView(ModelView<?> modelView) {
    this.modelView = modelView;
    this.out = null;
  }

  @Override
  public void render() {
    if (this.out == null) {
      throw new IllegalArgumentException("Null appendable given.");
    }
    this.out.print(this.toString());
    this.out.close();
  }


  @Override
  public void makeVisible(boolean visible) {
    // sets listener. Inherited from interface, but does not apply in this implementation.
  }


  @Override
  public void addFeatures(Features features) {
    // No controller interaction in textual view so no features to add
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();

    for (String s: modelView.getSheets()) {
      modelView.changeSheet(s);
      str.append("%" + s + "\n");
      for (Map.Entry<Coord, ?> entry: modelView.getGrid().entrySet()) {
        Object key = entry.getKey();
        Object value = entry.getValue();
        str.append(key.toString());
        str.append(" ");
        String cellContent = value.toString();
        if (cellContent.startsWith("(")) {
          cellContent = "=" + cellContent;
        }
        str.append(cellContent);
        str.append("\n");
      }
    }

    return str.toString();
  }
}
