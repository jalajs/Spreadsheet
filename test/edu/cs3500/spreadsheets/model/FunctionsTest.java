package edu.cs3500.spreadsheets.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;

/**
 * Class for testing that our functions and formulas operate correctly.
 */
public class FunctionsTest {
  DoubleValue fours;
  DoubleValue two;
  BooleanValue boolT;
  BooleanValue boolF;
  StringValue hello;

  List<Value> mathArgs;
  List<Value> mathArgsRev;
  List<Value> concatArgs;
  List<Value> incorrectArgs;

  @Before
  public void initFunc() {
    fours = new DoubleValue(4.0);
    two = new DoubleValue(2.0);
    boolT = new BooleanValue(true);
    boolF = new BooleanValue(false);
    hello = new StringValue("hello");

    mathArgs = new ArrayList<>();
    mathArgs.add(fours);
    mathArgs.add(two);

    mathArgsRev = new ArrayList<>();
    mathArgsRev.add(two);
    mathArgsRev.add(fours);

    concatArgs = new ArrayList<>();
    concatArgs.add(hello);
    concatArgs.add(hello);

    incorrectArgs = new ArrayList<>();
    incorrectArgs.add(two);
    incorrectArgs.add(boolF);

  }

  @Test
  public void testSum() {
    assertEquals(new DoubleValue(6.0), Functions.sum(mathArgs));
  }

  @Test
  public void testProduct() {
    assertEquals(new DoubleValue(8.0), Functions.product(mathArgs));
  }

  @Test
  public void testLessThan() {
    assertEquals(boolF, Functions.lessThan(mathArgs));
    assertEquals(boolT, Functions.lessThan(mathArgsRev));
  }

  //test fourth function we make
  @Test
  public void testStringConcat() {
    assertEquals(new StringValue("hellohello"), Functions.strCat(concatArgs));
    concatArgs.add(fours);
    assertEquals(new StringValue("hellohello4.0"), Functions.strCat(concatArgs));
  }

  @Test
  public void testIncorrectArgTypes() {
    assertEquals(two, Functions.sum(incorrectArgs));
    assertEquals(two, Functions.product(incorrectArgs));
  }

}
