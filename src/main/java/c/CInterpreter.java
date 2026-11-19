package c;

import c.Expr.CAddExpr;
import c.Expr.CExpr;
import c.Expr.CIntLiteral;
import c.Expr.CSubExpr;
import c.Value.CIntVal;
import c.Value.CValue;

public class CInterpreter {

  public CValue eval(CExpr expr) {
    return switch (expr) {
      case CIntLiteral(var v) -> new CIntVal(v);

      case CAddExpr(var l, var r) -> {
        int a = CValue.asInt(eval(l));
        int b = CValue.asInt(eval(r));
        yield new CIntVal(a + b);
      }

      case CSubExpr(var l, var r) -> {
        int a = CValue.asInt(eval(l));
        int b = CValue.asInt(eval(r));
        yield new CIntVal(a - b);
      }

      default -> throw new RuntimeException("unexpected expression: " + expr);
    };
  }
}
