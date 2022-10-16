package mcheli.eval.eval.exp;

import mcheli.eval.eval.oper.Operator;



public class MinusExpression
  extends Col2Expression
{
  public MinusExpression()
  {
    setOperator("-");
  }
  
  protected MinusExpression(MinusExpression from, ShareExpValue s) {
    super(from, s);
  }
  
  public AbstractExpression dup(ShareExpValue s) {
    return new MinusExpression(this, s);
  }
  
  protected long operateLong(long vl, long vr) {
    return vl - vr;
  }
  
  protected double operateDouble(double vl, double vr) {
    return vl - vr;
  }
  
  protected Object operateObject(Object vl, Object vr) {
    return this.share.oper.minus(vl, vr);
  }
}
