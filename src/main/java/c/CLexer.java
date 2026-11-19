package c;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static c.CTokenKind.EOF;
import static c.CTokenKind.INT;
import static c.CTokenKind.LPAREN;
import static c.CTokenKind.MINUS;
import static c.CTokenKind.PLUS;
import static c.CTokenKind.RPAREN;
import static c.CTokenKind.SEMI;

public class CLexer {
  protected String source;
  protected int current;

  public List<CToken> scan(String input) {
    source = input;
    current = 0;
    List<CToken> tokens = new ArrayList<>();

    do tokens.add(nextToken());
    while (tokens.getLast().kind() != EOF);

    return tokens;
  }

  protected CToken nextToken() {
    skipIrrelevant();

    if (isAtEnd()) return CToken.from(EOF);

    char c = advance();

    if (Character.isDigit(c)) {
      String lexeme = c + readWhile(Character::isDigit);
      return CToken.from(INT, lexeme);
    }

    return switch (c) {
      case '(' -> CToken.from(LPAREN);
      case ')' -> CToken.from(RPAREN);
      case '+' -> CToken.from(PLUS);
      case '-' -> CToken.from(MINUS);
      case ';' -> CToken.from(SEMI);
      default ->
          throw new RuntimeException("unexpected character '" + c + "' at index " + (current - 1));
    };
  }

  // ---- Hilfsmethoden: Zeichenhandling ----

  protected boolean isAtEnd() {
    return current >= source.length();
  }

  protected char peek() {
    return peek(0);
  }

  protected char peek(int la) {
    if (la < 0 || current + la >= source.length()) return '\0';
    return source.charAt(current + la);
  }

  protected char advance() {
    return isAtEnd() ? '\0' : source.charAt(current++);
  }

  /** Überspringt Whitespace und ;;-Kommentare. true, falls etwas übersprungen wurde. */
  protected boolean skipIrrelevant() {
    int start = current;
    while (!isAtEnd()) {
      char c = peek();
      if (Character.isWhitespace(c)) {
        advance();
      } else if (c == '/' && peek(1) == '/') {
        while (!isAtEnd() && peek() != '\n') advance(); // bis Zeilenende
      } else {
        break;
      }
    }
    return current > start;
  }

  protected String readWhile(Predicate<Character> pred) {
    StringBuilder sb = new StringBuilder();
    while (!isAtEnd() && pred.test(peek())) sb.append(advance());
    return sb.toString();
  }
}
