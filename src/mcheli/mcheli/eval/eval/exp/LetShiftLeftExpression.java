package mcheli.eval.eval.exp;

import mcheli.eval.eval.repl.Replace;



public final class LetShiftLeftExpression
  extends ShiftLeftExpression
{
  public LetShiftLeftExpression()
  {
    setOperator("<<=");
  }
  
  protected LetShiftLeftExpression(LetShiftLeftExpression from, ShareExpValue s)
  {
    super(from, s);
  }
  
  public AbstractExpression dup(ShareExpValue s) {
    return new LetShiftLeftExpression(this, s);
  }
  
  public long evalLong() {
    long val = super.evalLong();
    this.expl.let(val, this.pos);
    return val;
  }
  
  public double evalDouble() {
    double val = super.evalDouble();
    this.expl.let(val, this.pos);
    return val;
  }
  
  public Object evalObject() {
    Object val = super.evalObject();
    this.expl.let(val, this.pos);
    return val;
  }
  
  protected AbstractExpression replace() {
    this.expl = this.expl.replaceVar();
    this.expr = this.expr.replace();
    return this.share.repl.replaceLet(this);
  }
}
