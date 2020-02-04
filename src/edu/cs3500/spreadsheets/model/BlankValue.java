package edu.cs3500.spreadsheets.model;

/**
 * Represents a blank (empty) value on a spreadsheet.
 */
public class BlankValue extends AbstractValue<String> {

  private static BlankValue val = new BlankValue();

  @Override
  public String evaluate() {
    return "";
  }

  @Override
  public String toString() {
    return "";
  }

  public static Value getInstance() {
    return val;
  }

}
