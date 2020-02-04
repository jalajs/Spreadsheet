package edu.cs3500.spreadsheets.model;

import org.junit.Test;

import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.StringValue;

import static org.junit.Assert.assertEquals;


/**
 * Class for testing Values, primarily to verify that rendering values as strings works properly.
 */
public class ValueTest {


  @Test
  public void testDoubleValToString() {
    DoubleValue three = new DoubleValue(3.0);
    DoubleValue twelve = new DoubleValue(12.0);
    assertEquals("3.0", three.toString());
    assertEquals("12.0", twelve.toString());
  }


  @Test
  public void testBoolValToString() {
    BooleanValue t = new BooleanValue(true);
    BooleanValue f = new BooleanValue(false);
    assertEquals("true", t.toString());
    assertEquals("false", f.toString());
  }

  @Test
  public void testStringValToString() {
    StringValue hello = new StringValue("hello");
    assertEquals("hello", hello.toString());
  }

}
