package edu.cs3500.spreadsheets.model;

import org.junit.Assert;
import org.junit.Test;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Test class to test a new method we added to the Coord class.
 */
public class CoordTest {

  @Test
  public void coordStringToCoord() {
    Assert.assertEquals(new Coord(2, 5), Coord.coordStringToCoord("B5"));
    Assert.assertEquals(new Coord(3, 15), Coord.coordStringToCoord("C15"));
    Assert.assertEquals(new Coord(27, 34), Coord.coordStringToCoord("AA34"));
    Assert.assertEquals(new Coord(29, 100), Coord.coordStringToCoord("AC100"));
  }
}