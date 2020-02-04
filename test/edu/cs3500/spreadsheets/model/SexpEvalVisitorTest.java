package edu.cs3500.spreadsheets.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;

import static org.junit.Assert.assertEquals;

/**
 * Test class for SexpEvalVisitor functionality.
 */
public class SexpEvalVisitorTest {
  Sexp arg;
  SexpEvalVisitor visitor;
  List<Coord> vals;

  @Before
  public void initVisitor() {
    arg = new SNumber(4.0);
    vals =  new ArrayList<>();
    WorkbookModel wb = new BasicWorkbook();
    visitor = new SexpEvalVisitor(new CellReference(new Coord(1, 2), wb),
            vals, null);

  }

  @Test
  public void testApply() {
    assertEquals(new DoubleValue(4.0), visitor.apply(arg));
  }

  @Test
  public void testVisitBoolean() {
    assertEquals(new BooleanValue(true), visitor.visitBoolean(true));
  }

  @Test
  public void testVisitNumber() {
    assertEquals(new DoubleValue(4.0), visitor.visitNumber(4.0));
  }

  @Test
  public void testVisitSList() {
    List<Sexp> slist = new ArrayList<>();
    SSymbol symbol = new SSymbol("=(SUM ");
    SNumber first = new SNumber(2);
    SNumber second = new SNumber(4);
    slist.add(symbol);
    slist.add(first);
    slist.add(second);
    assertEquals(new DoubleValue(6.0), visitor.visitSList(slist));
  }


}
