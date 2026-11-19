package lispy;

import static lispy.TokenKind.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LispyLexerLint {
  protected String source;
  protected int current;

  public List<Token> scan(String input) {
    source = input;
    current = 0;
    List<Token> tokens = new ArrayList<>();

    do tokens.add(nextToken());
    while (tokens.getLast().kind() != EOF);

    return tokens;
  }

  protected Token nextToken() {
    skipIrrelevant();

    if (isAtEnd()) return Token.from(EOF);

    char c = advance();

    if (Character.isDigit(c)) {
      String lexeme = c + readWhile(Character::isDigit);
      return Token.from(INT, lexeme);
    }

    return switch (c) {
      case '(' -> Token.from(LPAREN);
      case ')' -> Token.from(RPAREN);
      case '+' -> Token.from(PLUS);
      case '-' -> Token.from(MINUS);
      case ';' -> Token.from(SEMI);
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
