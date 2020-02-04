package edu.cs3500.spreadsheets.model;


import java.util.Objects;

/**
 * A container class for a string values. Implements a Value of type String as a StringValue is
 * one of the types of values supported by our model.
 */
public class StringValue extends AbstractValue<String> {

  public StringValue(String value) {
    this.value = value;
  }

  @Override
  public String evaluate() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
    //return "\"" + this.value + "\"";
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof StringValue)) {
      return false;
    }
    return ((StringValue) other).value.equals(this.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value);
  }
}
