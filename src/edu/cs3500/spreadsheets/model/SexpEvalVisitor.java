package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a visitor function object that help process a Sexp to a value-- implements a Sexp
 * visitor and a function object that takes in a Sexp and returns a Value.
 */
public class SexpEvalVisitor implements SexpVisitor<Value>, Func<Sexp, Value> {
  private Reference ref;
  private List<Coord> visitedSoFar;
  private Cell calledFrom;


  /**
   * Constructs a new visitor for processing S-Expressions to values.
   *
   * @param ref          a reference to another cell
   * @param visitedSoFar the list of cells this visitor has visited so far in this round
   * @param calledFrom   the Cell that is calling upon this visitor
   */
  public SexpEvalVisitor(Reference ref, List<Coord> visitedSoFar, Cell calledFrom) {
    this.ref = ref;
    this.visitedSoFar = visitedSoFar;
    this.calledFrom = calledFrom;
  }


  @Override
  public Value apply(Sexp arg) {
    return arg.accept(this);
  }

  @Override
  public Value visitBoolean(boolean b) {
    return new BooleanValue(b);
  }

  @Override
  public Value visitNumber(double d) {
    return new DoubleValue(d);
  }

  @Override
  public Value visitSList(List<Sexp> l) {
    Sexp firstItem = l.get(0);
    List<Value> vals = new ArrayList<>();
    for (int i = 1; i < l.size(); i++) {
      vals.addAll(l.get(i).accept(this).valuesList());
    }
    switch (firstItem.toString()) {
      case "SUM":
        return Functions.sum(vals);
      case "PRODUCT":
        return Functions.product(vals);
      case "DIFFERENCE":
        return Functions.difference(vals);
      case "QUOTIENT":
        return Functions.quotient(vals);
      case "<":
        return Functions.lessThan(vals);
      case "STRCAT":
        return Functions.strCat(vals);
      default:
        throw new IllegalArgumentException("Bad function given");
    }
  }

  private Value processCellRangeReference(String[] coords, String sheetName) {

    Coord c1 = Coord.coordStringToCoord(coords[0]);
    Coord c2 = Coord.coordStringToCoord(coords[1]);

    for (int i = c1.col; i <= c2.col; i++) {
      for (int j = c1.row; j <= c2.row; j++) {
        Coord temp = new Coord(i, j);
        if (visitedSoFar.contains(temp)) {
          throw new IllegalArgumentException("You cannot have cyclic functions");
        }
      }
    }

    if (c2.row < c1.row || c2.col < c1.col) {
      return ErrorValue.getInstance();
    }

    Reference ref = new CellReference(c1, c2, this.ref.sheet, sheetName);
    ref.setReferencing(this.calledFrom);
    visitedSoFar.add(ref.c1);
    visitedSoFar.add(ref.c2);


    return ref.getValue(this);

  }

  private Value processColumnRangeReference(String[] coords, String sheetName) {
    int col1 = Coord.colNameToIndex(coords[0]);
    int col2 = Coord.colNameToIndex(coords[1]);
    if (col1 > col2) {
      return ErrorValue.getInstance();
    }

    // check if cycle exists
    HashMap<Coord, Cell> sheet = this.ref.sheet.getGrid();
    for (Map.Entry<Coord, Cell> entry : sheet.entrySet()) {
      if ((entry.getKey().col >= col1 && entry.getKey().col <= col2) &&
              visitedSoFar.contains(entry.getKey())) {
        throw new IllegalArgumentException("You cannot have cyclic references!");
      }
    }

    Reference ref = new ColReference(new Coord(col1, 1),
            new Coord(col2, 1), this.ref.sheet, sheetName);

    ref.setReferencing(this.calledFrom);


    return ref.getValue(this);
  }

  @Override
  public Value visitSymbol(String s) {
    Reference ref;
    this.visitedSoFar.add(this.ref.c1);
    this.visitedSoFar.add(this.ref.c2);
    String sheetName;
    if (s.contains("!")) {
      int exIndex = s.indexOf("!");
      sheetName = s.substring(0, exIndex);
      s = s.substring(exIndex + 1);
    } else {
      sheetName = "";
    }

    if (s.contains(":")) {

      String[] coords;
      coords = s.split(":");
      for (String coord : coords) {
        coord = coord.replaceAll("[*a-zA-Z]", "");
        if (coord.length() == 0) {
          return this.processColumnRangeReference(coords, sheetName);
        }
      }
      return this.processCellRangeReference(coords, sheetName);

    } else {
      // Single cell reference
      Coord c1 = Coord.coordStringToCoord(s);

      if (visitedSoFar.contains(c1)) {
        // cycle--we have been here
        //return ErrorValue.getInstance();
        throw new IllegalArgumentException("You cannot have cyclic functions");
      }

      ref = new CellReference(c1, this.ref.sheet, sheetName);
      ref.setReferencing(this.calledFrom);
      visitedSoFar.add(ref.c1);

      return ref.getValue(this);
    }
  }

  @Override
  public Value visitString(String s) {
    return new StringValue(s);
  }
}
