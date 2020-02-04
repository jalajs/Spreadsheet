package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * A class representing the types of functions our model can support. Implements the functionality
 * of these functions.
 */
public class Functions {

  /**
   * Enum to help represent the types of functions spreadsheet will support.
   */
  private enum MATHTYPES {
    ADD,
    SUBTRACT,
    TIMES,
    DIVIDE
  }

  /**
   * Computes the sum of some number of values.
   *
   * @param vals A list of values representing the arguments the function will take.
   * @return A doubleValue- sum of the arguments.
   */
  public static DoubleValue sum(List<Value> vals) {
    return Functions.math(vals, MATHTYPES.ADD);
  }

  /**
   * Computes the product of an arbitrary number of arguments.
   *
   * @param vals A list of values representing the arguments the function will take.
   * @return the product of the arguments.
   */
  public static DoubleValue product(List<Value> vals) {
    return Functions.math(vals, MATHTYPES.TIMES);
  }

  /**
   * Computes the difference between two values.
   *
   * @param vals A list of values representing the arguments the function will take.
   * @return the difference of the two values.
   */
  public static DoubleValue difference(List<Value> vals) {
    return Functions.math(vals, MATHTYPES.SUBTRACT);
  }

  /**
   * Computes the quotient between two values.
   *
   * @param vals A list of values representing the arguments the function will take.
   * @return the quotient of the two values.
   */
  public static DoubleValue quotient(List<Value> vals) {
    return Functions.math(vals, MATHTYPES.DIVIDE);
  }


  /**
   * Helper method to compute math operations.
   *
   * @param vals      The arguments the math function needs.
   * @param operation The type of operation of the math operation.
   * @return The result after computing the operation.
   */
  private static DoubleValue math(List<Value> vals, MATHTYPES operation) {
    if (vals.size() == 0) {
      throw new IllegalArgumentException("Must provide at least one argument");
    }
    double ans = vals.get(0).numberForm();
    for (int i = 1; i < vals.size(); i++) {
      if (vals.get(i).isNumber()) {
        switch (operation) {
          case ADD:
            ans += vals.get(i).numberForm();
            break;
          case TIMES:
            ans *= vals.get(i).numberForm();
            break;
          case SUBTRACT:
            ans -= vals.get(i).numberForm();
            break;
          case DIVIDE:
            ans /= vals.get(i).numberForm();
            break;
          default: throw new IllegalArgumentException("Unknown math operation provided");
        }
      }
    }
    return new DoubleValue(ans);
  }

  /**
   * Compares two values and determines if the first is less than the second.
   *
   * @param vals A list of values representing the arguments the function will take.
   * @return whether or not the first arg is less than the second.
   */
  public static BooleanValue lessThan(List<Value> vals) {
    if (vals.size() != 2) {
      throw new IllegalArgumentException("Must only supply two arguments to LESSTHAN");
    }
    if (vals.get(0).isNumber() && vals.get(1).isNumber()) {
      boolean res = vals.get(0).numberForm() < vals.get(1).numberForm();
      return new BooleanValue(res);
    } else {
      throw new IllegalArgumentException("Both arguments must be numbers");
    }
  }

  /**
   * Concatenates an arbitrary number of values into a single string.
   *
   * @param vals A list of values representing the arguments the function will take.
   * @return A single string with all of the values concatenated together
   */
  public static StringValue strCat(List<Value> vals) {
    if (vals.size() == 0) {
      throw new IllegalArgumentException("Must provide at least one argument");
    }
    String concatted = "";
    for (Value val : vals) {
      concatted += val.evaluate().toString();
    }
    return new StringValue(concatted);
  }
}