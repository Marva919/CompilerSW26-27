package lispy;

public enum TokenKind {
  PLUS("+"),
  MINUS("-"),
  LPAREN("("),
  RPAREN(")"),
  SEMI(";"),
  INT(null), // [0-9]+  -> Lexem variabel
  EOF(""); // synthetisch, kein Lexem im Quelltext

  private final String fixedLexeme;

  TokenKind(String fixedLexeme) {
    this.fixedLexeme = fixedLexeme;
  }

  /** Festes Lexem (null bei INT). */
  public String fixedLexeme() {
    return fixedLexeme;
  }
}
