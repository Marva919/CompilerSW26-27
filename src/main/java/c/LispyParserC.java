package c;

import c.Expr.AddExpr;
import c.Expr.Expr;
import c.Expr.IntLiteral;
import c.Expr.SubExpr;

import javax.accessibility.AccessibleKeyBinding;

import static c.TokenKind.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LispyParserC {
  protected List<Token> tokens;
  protected int current;

  public Expr parse(List<Token> tokenList) {
    tokens = tokenList;
    current = 0;

    Expr expr = parseEform();
    if (!isAtEnd()) throw new RuntimeException("unexpected token: " + peek());

    return expr;
  }

  protected Expr parseEform() {
    if (check(LPAREN)) {
      return switch (peek(1).kind()) {
        case PLUS -> parseAddExpr();
        case MINUS -> parseSubExpr();
        default -> throw new RuntimeException("unexpected token: " + peek(1));
      };
     }
    return parseAtom();
  }

  protected Expr parseAtom() {
    Token t = match(INT);
    try {
      return new IntLiteral(Integer.parseInt(t.lexeme()));
    } catch (NumberFormatException e) {
      throw new RuntimeException("integer literal out of range: " + t.lexeme());
    }
  }

  protected Expr parseAddExpr() {
    match(LPAREN);
    match(PLUS);
    List<Expr> ops = parseOperands();
    return ops.stream().reduce(AddExpr::new).orElseThrow();
  }

  protected Expr parseSubExpr() {
    match(LPAREN);
    match(MINUS);
    List<Expr> ops = parseOperands();
    return ops.stream().reduce(SubExpr::new).orElseThrow();
  }

  /** eform eform+ ')' -> mindestens zwei Operanden, dann die schließende Klammer. */
  protected List<Expr> parseOperands() {
    List<Expr> ops = new ArrayList<>();
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

  protected Token peek() {
    return peek(0);
  }

  protected Token peek(int la) {
    if (la < 0 || current + la >= tokens.size()) return tokens.getLast(); // EOF
    return tokens.get(current + la);
  }

  protected Token advance() {
    return isAtEnd() ? tokens.getLast() : tokens.get(current++);
  }

  protected boolean check(TokenKind... expected) {
    return Arrays.stream(expected).anyMatch(e -> e == peek().kind());
  }

  protected Token match(TokenKind... expected) {
    if (check(expected)) return advance();
    throw new RuntimeException("unexpected token: " + peek());
  }
}
