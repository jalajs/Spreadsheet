package edu.cs3500.spreadsheets.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * Represents a cell in a spreadsheet. The cell may be blank or contain a formula/value. This class
 * is final i.e. immutable.
 */
public final class Cell {
  private String content;
  private Reference ref;
  // new field to keep track of cells referencing this
  private List<Cell> cellsThatReferenceThis;

  /**
   * Constructs a new Cell for use in a spreadsheet.
   * @param content raw content of the cell
   * @param ref a reference to this cell
   */
  public Cell(String content, Reference ref) {
    this.content = content;
    this.ref = ref;
    this.cellsThatReferenceThis = new ArrayList<>();
  }

  /**
   * Evaluates this cell, running any formulas and/or references it may contain.
   * @return the Value result of this cell's evaluation
   */
  public Value evaluateCell(SexpVisitor visitor) {
    SexpVisitor vis;
    if (visitor == null) {
      vis = new SexpEvalVisitor(ref, new ArrayList<Coord>(Arrays.asList(ref.c1)), this);
    } else {
      vis = visitor;
    }
    if (this.content == null || this.content.equals("")) {
      return null;
    }
    Sexp sexp;

    if (this.content.substring(0, 1).equals("=")) {
      // it is a formula
      sexp = Parser.parse(content.substring(1));
    }  else {
      sexp = Parser.parse(content);
    }
    try {
      return (Value) sexp.accept(vis);
    } catch (IllegalArgumentException e) {
      return ErrorValue.getInstance();
    }
  }

  /**
   * Evaluates cell with alternative arguments, creating own visitor.
   * @return the Value result of this cell's evaluation
   */
  public Value evaluateCell() {
    try {
      return this.evaluateCell(new SexpEvalVisitor(ref,
              new ArrayList<>(Arrays.asList(ref.c1)), this));
    } catch (IllegalArgumentException e) {
      return ErrorValue.getInstance();
    }
  }


  // New methods here to get this cell's references

  /**
   * Retrieves a copy of the cells that reference this cell.
   * @return the cells referencing this cell
   */
  List<Cell> getReferences() {
    List<Cell> refs = new ArrayList<>();
    for (Cell c : this.cellsThatReferenceThis) {
      refs.add(c.clone());
    }
    return refs;
  }

  /**
   * Adds to this list of cells that reference this cell.
   * @param newRefs the cells to add to this list of references
   */
  void setReferences(List<Cell> newRefs) {
    this.cellsThatReferenceThis.addAll(newRefs);
  }



  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Cell)) {
      return false;
    }

    return ((Cell) other).content.equals(this.content) &&
            ((Cell) other).ref.equals(this.ref);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content, ref);
  }

  @Override
  public String toString() {
    return content;
  }

  @Override
  public Cell clone() {
    Cell c = new Cell(this.content, this.ref);
    c.setReferences(this.cellsThatReferenceThis);
    return c;
  }

}
