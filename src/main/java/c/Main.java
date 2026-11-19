package c;

import c.Expr.Expr;
import c.Value.Value;

import java.util.List;
import java.util.Scanner;

/** Pipeline: Text -> Lexer -> Token -> Parser -> AST -> Interpreter -> Value */
public class Main {

  static Value run(String source) {
    List<Token> tokens = new LispyLexerC().scan(source);
    Expr ast = new LispyParserC().parse(tokens);
    return new InterpreterC().eval(ast);
  }

  public static void main(String[] args) {
    if (args.length > 0) { // java Main "(+ 1 2)"
      System.out.println(run(String.join(" ", args)));
      return;
    }
    // einfache REPL: eine L-Int-Zeile pro Eingabe, leere Zeile beendet
    Scanner in = new Scanner(System.in);
    System.out.print("C> ");
    while (in.hasNextLine()) {
      String line = in.nextLine();
      if (line.isBlank()) break;
      try {
        System.out.println(run(line));
      } catch (RuntimeException e) {
        System.out.println("error: " + e.getMessage());
      }
      System.out.print("C> ");
    }
  }
}
