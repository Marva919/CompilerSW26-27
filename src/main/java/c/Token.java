package lispy;

public record Token(TokenKind kind, String lexeme) {

  public static Token from(TokenKind kind, String lexeme) {
    return new Token(kind, lexeme);
  }
  public static Token from(TokenKind kind) {
    if (kind.fixedLexeme() == null)
      throw new IllegalArgumentException(kind + " hat kein festes Lexem");
    return new Token(kind, kind.fixedLexeme());
  }
}
