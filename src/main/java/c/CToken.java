package c;

public record CToken(CTokenKind kind, String lexeme) {

  public static CToken from(CTokenKind kind, String lexeme) {
    return new CToken(kind, lexeme);
  }
  public static CToken from(CTokenKind kind) {
    if (kind.fixedLexeme() == null)
      throw new IllegalArgumentException(kind + " hat kein festes Lexem");
    return new CToken(kind, kind.fixedLexeme());
  }
}
