package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * A container class to abstractly represent and handle rendering for spreadsheet errors.
 */
public class ErrorValue extends AbstractValue {

  private static final ErrorValue val = new ErrorValue();

  @Override
  public String toString() {
    return "ERROR";
  }

  public static Value getInstance() {
    return val;
  }

  @Override
  public Object evaluate() {
    return "";
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof ErrorValue;
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }
}
