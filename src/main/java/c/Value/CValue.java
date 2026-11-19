package c.Value;

public interface CValue {

  static int asInt(CValue v) {
    if (v instanceof CIntVal(int val)) return val;
    throw new RuntimeException("number expected");
  }
}
