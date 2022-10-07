package mcheli.eval.eval.exp;

import mcheli.eval.eval.oper.Operator;



public class MultExpression
  extends Col2Expression
{
  public MultExpression()
  {
    setOperator("*");
  }
  
  protected MultExpression(MultExpression from, ShareExpValue s) {
    super(from, s);
  }
  
  public AbstractExpression dup(ShareExpValue s) {
    return new MultExpression(this, s);
  }
  
  protected long operateLong(long vl, long vr) {
    return vl * vr;
  }
  
  protected double operateDouble(double vl, double vr) {
    return vl * vr;
  }
  
  protected Object operateObject(Object vl, Object vr) {
    return this.share.oper.mult(vl, vr);
  }
}
