package edu.cs3500.spreadsheets.model;

/**
 * An interface for a generic function object.
 *
 * @param <A> The value parametrized for the input of the function.
 * @param <R> The value parametrized for the output of the function.
 */
public interface Func<A, R> {
  /**
   * Applies the function.
   * @param arg Takes in a type A
   * @return a generic type R
   */
  R apply(A arg);

}
