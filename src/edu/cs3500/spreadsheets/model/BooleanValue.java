package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * A container class for a boolean values. Implements a Value of type boolean as a BooleanValue is
 * one of the types of values supported by our model.
 */
public class BooleanValue extends AbstractValue<Boolean> {

  public BooleanValue(boolean value) {
    this.value = value;
  }

  @Override
  public Boolean evaluate() {
    return this.value;
  }

  @Override
  public String toString() {
    return Boolean.toString(this.value).toUpperCase();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof BooleanValue)) {
      return false;
    }
    return ((BooleanValue) other).value == this.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value);
  }
}
