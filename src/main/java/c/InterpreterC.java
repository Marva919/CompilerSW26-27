package c;

import c.Expr.AddExpr;
import c.Expr.Expr;
import c.Expr.IntLiteral;
import c.Expr.SubExpr;
import c.Value.IntVal;
import c.Value.Value;

public class InterpreterC {

  public Value eval(Expr expr) {
    return switch (expr) {
      case IntLiteral(var v) -> new IntVal(v);

      case AddExpr(var l, var r) -> {
        int a = Value.asInt(eval(l));
        int b = Value.asInt(eval(r));
        yield new IntVal(a + b);
      }

      case SubExpr(var l, var r) -> {
        int a = Value.asInt(eval(l));
        int b = Value.asInt(eval(r));
        yield new IntVal(a - b);
      }

      default -> throw new RuntimeException("unexpected expression: " + expr);
    };
  }
}
