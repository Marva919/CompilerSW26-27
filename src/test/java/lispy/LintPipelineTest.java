package lispy;

import static org.junit.jupiter.api.Assertions.*;

import lispy.Expr.Expr;
import lispy.Expr.IntLiteral;
import lispy.Expr.SubExpr;
import lispy.Value.Value;
import org.junit.jupiter.api.Test;

class LintPipelineTest {

  private static int run(String src) {
    return Value.asInt(Main.run(src));
  }

  @Test
  void literal() {
    assertEquals(42, run("42"));
  }

  @Test
  void add() {
    assertEquals(3, run("(+ 1 2)"));
  }

  @Test
  void addVariadic() {
    assertEquals(10, run("(+ 1 2 3 4)"));
  }

  @Test
  void subNotSwapped() {
    assertEquals(7, run("(- 10 3)"));
  }

  @Test
  void subLeftAssoc() {
    assertEquals(12, run("(- 20 5 3)"));
  }

  @Test
  void nested() {
    assertEquals(4, run("(- (+ 1 2) (- 3 4))"));
  }

  @Test
  void comment() {
    assertEquals(3, run(";; demo\n(+ 1 ;; x\n 2)"));
  }

  @Test
  void astSubLeftAssoc() {
    Expr ast = new LispyParserLint().parse(new LispyLexerLint().scan("(- 10 3 2)"));
    assertEquals(
        new SubExpr(new SubExpr(new IntLiteral(10), new IntLiteral(3)), new IntLiteral(2)), ast);
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
