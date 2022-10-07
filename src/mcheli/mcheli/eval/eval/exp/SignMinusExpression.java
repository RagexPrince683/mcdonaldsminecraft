package mcheli.eval.eval.exp;

import mcheli.eval.eval.oper.Operator;



public class SignMinusExpression
  extends Col1Expression
{
  public SignMinusExpression()
  {
    setOperator("-");
  }
  
  protected SignMinusExpression(SignMinusExpression from, ShareExpValue s) {
    super(from, s);
  }
  
  public AbstractExpression dup(ShareExpValue s) {
    return new SignMinusExpression(this, s);
  }
  
  protected long operateLong(long val) {
    return -val;
  }
  
  protected double operateDouble(double val) {
    return -val;
  }
  
  public Object evalObject() {
    return this.share.oper.signMinus(this.exp.evalObject());
  }
}
