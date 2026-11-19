package lispy;

import lispy.Expr.Expr;
import lispy.Value.Value;

import java.util.List;
import java.util.Scanner;

/** Pipeline: Text -> Lexer -> Token -> Parser -> AST -> Interpreter -> Value */
public class Main {

  static Value run(String source) {
    List<Token> tokens = new LispyLexerLint().scan(source);
    Expr ast = new LispyParserLint().parse(tokens);
    return new InterpreterLint().eval(ast);
  }

  public static void main(String[] args) {
    if (args.length > 0) { // java Main "(+ 1 2)"
      System.out.println(run(String.join(" ", args)));
      return;
    }
    // einfache REPL: eine L-Int-Zeile pro Eingabe, leere Zeile beendet
    Scanner in = new Scanner(System.in);
    System.out.print("lint> ");
    while (in.hasNextLine()) {
      String line = in.nextLine();
      if (line.isBlank()) break;
      try {
        System.out.println(run(line));
      } catch (RuntimeException e) {
        System.out.println("error: " + e.getMessage());
      }
      System.out.print("lint> ");
    }
  }
}
