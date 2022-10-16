package mcheli.eval.eval.exp;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.repl.Replace;
import mcheli.eval.eval.var.Variable;




public class ArrayExpression
  extends Col2OpeExpression
{
  public ArrayExpression()
  {
    setOperator("[");
    setEndOperator("]");
  }
  
  protected ArrayExpression(ArrayExpression from, ShareExpValue s) {
    super(from, s);
  }
  
  public AbstractExpression dup(ShareExpValue s) {
    return new ArrayExpression(this, s);
  }
  
  public long evalLong() {
    try {
      return this.share.var.evalLong(getVariable());
    } catch (EvalException e) {
      throw e;
    } catch (Exception e) {
      throw new EvalException(2201, toString(), this.string, this.pos, e);
    }
  }
  
  public double evalDouble()
  {
    try {
      return this.share.var.evalDouble(getVariable());
    } catch (EvalException e) {
      throw e;
    } catch (Exception e) {
      throw new EvalException(2201, toString(), this.string, this.pos, e);
    }
  }
  
  public Object evalObject()
  {
    return getVariable();
  }
  
  protected Object getVariable() {
    Object obj = this.expl.getVariable();
    if (obj == null) {
      throw new EvalException(2104, this.expl.toString(), this.string, this.pos, null);
    }
    
    int index = (int)this.expr.evalLong();
    try {
      return this.share.var.getObject(obj, index);
    } catch (EvalException e) {
      throw e;
    } catch (Exception e) {
      throw new EvalException(2201, toString(), this.string, this.pos, e);
    }
  }
  
  protected void let(Object val, int pos)
  {
    Object obj = this.expl.getVariable();
    if (obj == null) {
      throw new EvalException(2104, this.expl.toString(), this.string, pos, null);
    }
    
    int index = (int)this.expr.evalLong();
    try {
      this.share.var.setValue(obj, index, val);
    } catch (EvalException e) {
      throw e;
    } catch (Exception e) {
      throw new EvalException(2202, toString(), this.string, pos, e);
    }
  }
  
  protected AbstractExpression replaceVar()
  {
    this.expl = this.expl.replaceVar();
    this.expr = this.expr.replace();
    return this.share.repl.replaceVar2(this);
  }
  
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(this.expl.toString());
    sb.append('[');
    sb.append(this.expr.toString());
    sb.append(']');
    return sb.toString();
  }
}
