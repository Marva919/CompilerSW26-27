package lispy;

import lispy.Expr.AddExpr;
import lispy.Expr.Expr;
import lispy.Expr.IntLiteral;
import lispy.Expr.SubExpr;
import lispy.Value.IntVal;
import lispy.Value.Value;

public class InterpreterLint {

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
