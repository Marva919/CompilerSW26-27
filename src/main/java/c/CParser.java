package c;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static c.CTokenKind.EOF;
import static c.CTokenKind.INT;
import static c.CTokenKind.LPAREN;
import static c.CTokenKind.MINUS;
import static c.CTokenKind.PLUS;
import static c.CTokenKind.RPAREN;
import c.Expr.CAddExpr;
import c.Expr.CExpr;
import c.Expr.CIntLiteral;
import c.Expr.CSubExpr;


public class CParser {
  protected List<CToken> tokens;
  protected int current;

  public CExpr parse(List<CToken> tokenList) {
    tokens = tokenList;
    current = 0;

    CExpr expr = parseEform();
    if (!isAtEnd()) throw new RuntimeException("unexpected token: " + peek());

    return expr;
  }

  /*
  Token:
  exp ::= exp '+' exp
        | exp '-' exp
        | '(' exp ')'
        | INT

  INT ::= [0-9]+

  cint ::= exp EOF

  --> parse: erst check ob "()", dann exp mit rec. Aufruf
   */

  protected CExpr parseEform() {
    CExpr expr = parseFactor();

    while (check(PLUS) || check(MINUS)) {
      if (check(PLUS)) {
        expr = parseAddExpr(expr);
      } else if (check(MINUS)) {
        expr = parseSubExpr(expr);
      }
    }

    return expr;
  }

  protected CExpr parseFactor() {
    if (check(LPAREN)) {
      match(LPAREN);
      CExpr expr = parseEform();
      match(RPAREN);
      return expr;
    }
    return parseAtom();
  }

  protected CExpr parseAtom() {
    CToken t = match(INT);
    try {
      return new CIntLiteral(Integer.parseInt(t.lexeme()));
    } catch (NumberFormatException e) {
      throw new RuntimeException("integer literal out of range: " + t.lexeme());
    }
  }

  protected CExpr parseAddExpr(CExpr left) {
    match(PLUS);
    CExpr right = parseFactor();
    return new CAddExpr(left, right);
  }

  protected CExpr parseSubExpr(CExpr left) {
    match(MINUS);
    CExpr right = parseFactor();
    return new CSubExpr(left, right);
  }

  /** eform eform+ ')' -> mindestens zwei Operanden, dann die schließende Klammer. */
  protected List<CExpr> parseOperands() {
    List<CExpr> ops = new ArrayList<>();
    ops.add(parseEform());
    ops.add(parseEform()); // zweiter Operand ist Pflicht
    while (!check(RPAREN)) ops.add(parseEform());
    match(RPAREN);
    return ops;
  }

  // ---- Hilfsmethoden: Tokenhandling ----

  protected boolean isAtEnd() {
    return peek().kind() == EOF;
  }

  protected CToken peek() {
    return peek(0);
  }

  protected CToken peek(int la) {
    if (la < 0 || current + la >= tokens.size()) return tokens.getLast(); // EOF
    return tokens.get(current + la);
  }

  protected CToken advance() {
    return isAtEnd() ? tokens.getLast() : tokens.get(current++);
  }

  protected boolean check(CTokenKind... expected) {
    return Arrays.stream(expected).anyMatch(e -> e == peek().kind());
  }

  protected CToken match(CTokenKind... expected) {
    if (check(expected)) return advance();
    throw new RuntimeException("unexpected token: " + peek());
  }
}
