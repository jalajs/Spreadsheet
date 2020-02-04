package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * A container class to abstractly represent a spreadsheet value that contains multiple values, as
 * in a ranged cell reference.
 */
public class MultiValue extends AbstractValue<Value> {
  private List<Value> values;

  public MultiValue(List<Value> values) {
    this.values = values;
  }

  @Override
  public String toString() {
    return "RANGE";
  }

  @Override
  public List<Value> valuesList() {
    return this.values;
  }
}
