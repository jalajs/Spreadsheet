package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * A value supported by our spreadsheet can be a boolean, double, String, Error, or Blank.
 * The value interface extends formula as it is also a type of formula.
 */
public interface Value<T> {

  /**
   * Evaluates a value to whatever generic type it is.
   * @return A type of value.
   */
  T evaluate();


  /**
   * Determines whether a value is a number to help compute math operations.
   * @return whether or not a value is numeric
   */
  boolean isNumber();

  /**
   * Returns the numeric representation of a Value. Non-numeric types default to 0.
   * @return the numeric representation of this value.
   */
  double numberForm();


  List<Value> valuesList();



}
