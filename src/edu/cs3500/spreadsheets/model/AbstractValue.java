package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an a {@link Value} with generalized behavior and a field.
 * @param <T> The type of {@code Value} this is
 */
public abstract class AbstractValue<T> implements Value<T> {
  protected T value;

  @Override
  public T evaluate() {
    return null;
  }

  @Override
  public boolean isNumber() {
    return false;
  }

  @Override
  public double numberForm() {
    return 0;
  }

  @Override
  public List<Value> valuesList() {
    List<Value> vals = new ArrayList<>();
    vals.add(this);
    return vals;
  }
}
