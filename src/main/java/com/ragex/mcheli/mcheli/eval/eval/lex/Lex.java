package mcheli.eval.eval.lex;

import java.util.List;
import mcheli.eval.eval.exp.AbstractExpression;
import mcheli.eval.eval.exp.ShareExpValue;
import mcheli.eval.util.CharUtil;























public class Lex
{
  protected List[] opeList;
  protected String string;
  protected int pos = 0;
  

  protected int len = 0;
  

  protected int type = -1;
  




  public static final int TYPE_WORD = 2147483632;
  




  public static final int TYPE_NUM = 2147483633;
  




  public static final int TYPE_OPE = 2147483634;
  




  public static final int TYPE_STRING = 2147483635;
  




  public static final int TYPE_CHAR = 2147483636;
  




  public static final int TYPE_EOF = Integer.MAX_VALUE;
  



  public static final int TYPE_ERR = -1;
  



  protected String ope;
  



  protected ShareExpValue expShare;
  




  protected Lex(String str, List[] lists, AbstractExpression paren, ShareExpValue exp)
  {
    this.string = str;
    this.opeList = lists;
    this.expShare = exp;
    if (this.expShare.paren == null) {
      this.expShare.paren = paren;
    }
  }
  





  protected String SPC_CHAR = " \t\r\n";
  











  protected boolean isSpace(int pos)
  {
    if (pos >= this.string.length())
      return true;
    char c = this.string.charAt(pos);
    return this.SPC_CHAR.indexOf(c) >= 0;
  }
  










  protected boolean isNumberTop(int pos)
  {
    if (pos >= this.string.length())
      return false;
    char c = this.string.charAt(pos);
    return ('0' <= c) && (c <= '9');
  }
  








  protected String NUMBER_CHAR = "._";
  












  protected boolean isSpecialNumber(int pos)
  {
    if (pos >= this.string.length())
      return false;
    char c = this.string.charAt(pos);
    return this.NUMBER_CHAR.indexOf(c) >= 0;
  }
  











  protected String isOperator(int pos)
  {
    for (int i = this.opeList.length - 1; i >= 0; i--) {
      if (pos + i < this.string.length())
      {
        List list = this.opeList[i];
        if (list != null) label116:
          for (int j = 0; j < list.size(); j++) {
            String ope = (String)list.get(j);
            for (int k = 0; k <= i; k++) {
              char c = this.string.charAt(pos + k);
              char o = ope.charAt(k);
              if (c != o)
                break label116;
            }
            return ope;
          }
      }
    }
    return null;
  }
  







  protected boolean isStringTop(int pos)
  {
    if (pos >= this.string.length())
      return false;
    char c = this.string.charAt(pos);
    return c == '"';
  }
  







  protected boolean isStringEnd(int pos)
  {
    return isStringTop(pos);
  }
  







  protected boolean isCharTop(int pos)
  {
    if (pos >= this.string.length())
      return false;
    char c = this.string.charAt(pos);
    return c == '\'';
  }
  







  protected boolean isCharEnd(int pos)
  {
    return isCharTop(pos);
  }
  
  public void check()
  {
    for (; 
        




        isSpace(this.pos); this.pos += 1) {
      if (this.pos >= this.string.length())
      {
        this.type = Integer.MAX_VALUE;
        this.len = 0;
        return;
      }
    }
    

    if (isStringTop(this.pos)) {
      processString();
      return;
    }
    
    if (isCharTop(this.pos)) {
      processChar();
      return;
    }
    

    String ope = isOperator(this.pos);
    if (ope != null) {
      this.type = 2147483634;
      this.ope = ope;
      this.len = ope.length();
      return;
    }
    

    boolean number = isNumberTop(this.pos);
    this.type = (number ? 2147483633 : 2147483632);
    for (this.len = 1; 
        !isSpace(this.pos + this.len); this.len += 1)
    {

      if (((!number) || (!isSpecialNumber(this.pos + this.len))) && 
      
        (isOperator(this.pos + this.len) != null))
        break;
    }
  }
  
  protected void processString() {
    int[] ret = new int[1];
    this.type = 2147483635;
    this.len = 1;
    do { this.len += getCharLen(this.pos + this.len, ret);
      if (this.pos + this.len >= this.string.length()) {
        this.type = Integer.MAX_VALUE;
        break;
      }
    } while (!isStringEnd(this.pos + this.len));
    this.len += 1;
    return;
  }
  

  protected void processChar()
  {
    int[] ret = new int[1];
    this.type = 2147483636;
    this.len = 1;
    do { this.len += getCharLen(this.pos + this.len, ret);
      if (this.pos + this.len >= this.string.length()) {
        this.type = Integer.MAX_VALUE;
        break;
      }
    } while (!isCharEnd(this.pos + this.len));
    this.len += 1;
    return;
  }
  

  protected int getCharLen(int pos, int[] ret)
  {
    CharUtil.escapeChar(this.string, pos, this.string.length(), ret);
    return ret[0];
  }
  







  public Lex next()
  {
    this.pos += this.len;
    check();
    return this;
  }
  




  public int getType()
  {
    return this.type;
  }
  







  public String getOperator()
  {
    return this.ope;
  }
  






  public boolean isOperator(String ope)
  {
    if (this.type == 2147483634)
      return this.ope.equals(ope);
    return false;
  }
  




  public String getWord()
  {
    return this.string.substring(this.pos, this.pos + this.len);
  }
  







  public String getString()
  {
    return this.string;
  }
  




  public int getPos()
  {
    return this.pos;
  }
  





  public ShareExpValue getShare()
  {
    return this.expShare;
  }
}
