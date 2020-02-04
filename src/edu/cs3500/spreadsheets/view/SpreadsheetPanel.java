package edu.cs3500.spreadsheets.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * Represents the spreadsheet view of a {@link SpreadsheetGUIView}. Extends the JPanel class and
 * overrides its paintComponent method.
 */
public class SpreadsheetPanel extends JPanel {
  private ModelView<Cell> modelView;
  private static int xStart = 0;
  static int width = SpreadsheetGUIView.defaultWindowWidth - 50;
  private static int yStart = 0;
  static int height = SpreadsheetGUIView.defaultWindowHeight - 50;
  public static final int CELL_WIDTH = 50;
  public static final int CELL_HEIGHT = 20;
  private static int numCellsInViewX = width / CELL_WIDTH - 1;
  private static int numCellsInViewY = height / CELL_HEIGHT - 4;


  /**
   * Constructs the spreadsheet view of a {@code SpreadsheetGUIView}.
   * @param modelView a view of the model to render in the sheet
   */
  public SpreadsheetPanel(ModelView<Cell> modelView) {
    this.modelView = modelView;
    setBackground(Color.white);
  }


  @Override
  public void paintComponent(Graphics g) {
    width = getWidth();
    height = getHeight();
    numCellsInViewY = (height / CELL_HEIGHT) + CELL_HEIGHT;
    numCellsInViewX = (width / CELL_WIDTH) + CELL_WIDTH;
    this.drawBorders(g);
    this.drawGrid(g);
    this.drawCellVal(g);

  }

  /**
   * Draws the label borders along top and left side of spreadsheet.
   * @param g the <code>Graphics</code> object to protect and update
   */
  private void drawBorders(Graphics g) {
    for (int i = CELL_WIDTH; i < width; i += CELL_WIDTH) {
      g.setColor(Color.GRAY);
      g.fill3DRect(i, 0, CELL_WIDTH, CELL_HEIGHT, true);
      g.setColor(Color.black);
      String cellVal = Coord.colIndexToName((i / CELL_WIDTH) + xStart);
      int offset = (g.getFontMetrics().stringWidth(cellVal));
      g.drawString(cellVal, i + (CELL_WIDTH / 2) - offset / 2, (2 * CELL_HEIGHT) / 3);
    }

    for (int j = CELL_HEIGHT; j < height; j += CELL_HEIGHT) {
      g.setColor(Color.GRAY);
      g.fill3DRect(0, j, CELL_WIDTH, CELL_HEIGHT, true);
      g.setColor(Color.black);
      String cellVal = Integer.toString((j / CELL_HEIGHT) + yStart);
      int offset = g.getFontMetrics().stringWidth(cellVal);
      g.drawString(cellVal, (CELL_WIDTH / 2) - offset / 2, j + ((2 * CELL_HEIGHT) / 3));
    }
  }

  /**
   * Draws the actual grid portion of the spreadsheet.
   * @param g the <code>Graphics</code> object to protect and update
   */
  private void drawGrid(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    g2.setStroke(new BasicStroke(1));
    Coord c = SpreadsheetEditView.selectedCoord;
    System.out.println(c);
    // i = row, j = col
    for (int i = CELL_WIDTH; i < width; i += CELL_WIDTH) {
      for (int j = CELL_HEIGHT; j < height; j += CELL_HEIGHT) {
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.white);

        g2.fillRect(i, j, CELL_WIDTH, CELL_HEIGHT);
        g2.setColor(Color.black);
        if (c != null && c.col == (i / CELL_WIDTH) +
                xStart && c.row == (j / CELL_HEIGHT) + yStart) {
          g2.setColor(Color.red);
          g2.setStroke(new BasicStroke(6));
        }
        g.drawRect(i, j, CELL_WIDTH, CELL_HEIGHT);
      }
    }
  }

  /**
   * Draws the cell values on the spreadsheet.
   * @param g the <code>Graphics</code> object to protect and update
   */
  private void drawCellVal(Graphics g) {
    Set<Map.Entry<Coord, Cell>> gridItems = this.modelView.getGrid().entrySet();
    for (Map.Entry<Coord, Cell> entry: gridItems) {
      // check if entry is within current view (from this.xStart/yStart and this.width+height
      int row = entry.getKey().row;
      int col = entry.getKey().col;
      if (col > xStart && col < xStart + numCellsInViewX
              && row > yStart && row < yStart + numCellsInViewY) {
        if (!entry.getValue().toString().equals("")) {
          drawTruncatedString(g, (col - xStart) * CELL_WIDTH, ((row - yStart - 1) * CELL_HEIGHT)
                  + CELL_HEIGHT, entry.getValue().evaluateCell().toString());
        }
      }
    }
  }

  private void drawTruncatedString(Graphics g, int x, int y, String str) {
    Shape rect = new Rectangle(x, y, CELL_WIDTH, CELL_HEIGHT);
    g.setClip(rect);
    g.drawString(str, x + 5, y + CELL_HEIGHT - 3);
  }

  /**
   * updates this SpreadsheetPanel's starting x and y values to scroll, and refreshes the view.
   * @param xValue the new value for xStart
   * @param yValue the new value for yStart
   */
  void updateXYStart(int xValue, int yValue) {

    xStart = xValue == -1 ? xStart : xValue * 2;
    yStart = yValue == -1 ? yStart : yValue * 2;
    this.paintComponent(this.getGraphics());
  }

  public static int getxStart() {
    return xStart;
  }

  public static int getyStart() {
    return yStart;
  }



}
