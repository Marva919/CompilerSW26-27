package c;

import java.util.List;
import java.util.Scanner; 

import c.Expr.CExpr;
import c.Value.CValue;

/** Pipeline: Text -> Lexer -> Token -> Parser -> AST -> Interpreter -> Value */
public class Main {

  static CValue run(String source) {
    List<CToken> tokens = new CLexer().scan(source);
    CExpr ast = new CParser().parse(tokens);
    return new CInterpreter().eval(ast);
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
