package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * A container class for a double values. Implements a Value of type Double as a DoubleValue is
 * one of the types of values supported by our model.
 */
public class DoubleValue extends AbstractValue<Double> {

  public DoubleValue(double value) {
    this.value = value;
  }

  @Override
  public Double evaluate() {
    return this.value;
  }

  @Override
  public boolean isNumber() {
    return true;
  }

  @Override
  public double numberForm() {
    return this.value;
  }

  @Override
  public String toString() {
    return Double.toString(this.value);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof DoubleValue)) {
      return false;
    }
    return ((DoubleValue) other).value - this.value < 0.001;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value);
  }
}
