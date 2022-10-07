package mcheli.eval.eval.exp;

import mcheli.eval.eval.EvalException;
import mcheli.eval.eval.repl.Replace;
import mcheli.eval.eval.var.Variable;







public class FieldExpression
  extends Col2OpeExpression
{
  public FieldExpression()
  {
    setOperator(".");
  }
  
  protected FieldExpression(FieldExpression from, ShareExpValue s) {
    super(from, s);
  }
  
  public AbstractExpression dup(ShareExpValue s) {
    return new FieldExpression(this, s);
  }
  
  public long evalLong() {
    try {
      return this.share.var.evalLong(getVariable());
    } catch (EvalException e) {
      throw e;
    } catch (Exception e) {
      throw new EvalException(2003, toString(), this.string, this.pos, e);
    }
  }
  
  public double evalDouble()
  {
    try {
      return this.share.var.evalDouble(getVariable());
    } catch (EvalException e) {
      throw e;
    } catch (Exception e) {
      throw new EvalException(2003, toString(), this.string, this.pos, e);
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
    
    String word = this.expr.getWord();
    try {
      return this.share.var.getObject(obj, word);
    } catch (EvalException e) {
      throw e;
    } catch (Exception e) {
      throw new EvalException(2301, toString(), this.string, this.pos, e);
    }
  }
  
  protected void let(Object val, int pos)
  {
    Object obj = this.expl.getVariable();
    if (obj == null) {
      throw new EvalException(2104, this.expl.toString(), this.string, pos, null);
    }
    
    String word = this.expr.getWord();
    try {
      this.share.var.setValue(obj, word, val);
    } catch (EvalException e) {
      throw e;
    } catch (Exception e) {
      throw new EvalException(2302, toString(), this.string, pos, e);
    }
  }
  
  protected AbstractExpression replace()
  {
    this.expl = this.expl.replaceVar();
    
    return this.share.repl.replace2(this);
  }
  
  protected AbstractExpression replaceVar() {
    this.expl = this.expl.replaceVar();
    
    return this.share.repl.replaceVar2(this);
  }
  
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(this.expl.toString());
    sb.append('.');
    sb.append(this.expr.toString());
    return sb.toString();
  }
}
