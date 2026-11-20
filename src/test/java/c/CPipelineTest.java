package c;

import c.Expr.CExpr;
import c.Expr.CIntLiteral;
import c.Expr.CSubExpr;
import c.Value.CValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CPipelineTest {

  private static int run(String src) {
    return CValue.asInt(Main.run(src));
  }

  @Test
  void literal() {
    assertEquals(42, run("42"));
  }

  @Test
  void add() {
    assertEquals(3, run("(1 + 2)"));
  }

  @Test
  void addVariadic() {
    assertEquals(10, run("(1 + 2 + 3 + 4)"));
  }

  @Test
  void subNotSwapped() {
    assertEquals(7, run("(10 - 3)"));
  }

  @Test
  void subLeftAssoc() {
    assertEquals(12, run("(20 - 5 - 3)"));
  }

  @Test
  void nested() {
    assertEquals(4, run("((1 + 2) - (3 - 4))"));
  }

  @Test
  void comment() {
    assertEquals(3, run("// demo\n(1 + // x\n 2)"));
  }

  @Test
  void astSubLeftAssoc() {
    CExpr ast = new CParser().parse(new CLexer().scan("(10 - 3 - 2)"));
    assertEquals(
        new CSubExpr(new CSubExpr(new CIntLiteral(10), new CIntLiteral(3)), new CIntLiteral(2)), ast);
  }

  @Test
  void oneOperandRejected() {
    assertThrows(RuntimeException.class, () -> run("(+ 1)"));
  }

  @Test
  void noOperandRejected() {
    assertThrows(RuntimeException.class, () -> run("(+)"));
  }

  @Test
  void trailingInput() {
    assertThrows(RuntimeException.class, () -> run("(+ 1 2) 3"));
  }

  @Test
  void unknownChar() {
    assertThrows(RuntimeException.class, () -> run("(+ 1 @)"));
  }
}
