package mcheli.eval.util;

public class CharUtil
{
  public static String escapeString(String str)
  {
    return escapeString(str, 0, str.length());
  }
  
  public static String escapeString(String str, int pos, int len)
  {
    StringBuffer sb = new StringBuffer(len);
    int end_pos = pos + len;
    int[] ret = new int[1];
    while (pos < end_pos)
    {
      char c = escapeChar(str, pos, end_pos, ret);
      if (ret[0] <= 0) {
        break;
      }
      sb.append(c);
      pos += ret[0];
    }
    return sb.toString();
  }
  
  public static char escapeChar(String str, int pos, int end_pos, int[] ret)
  {
    if (pos >= end_pos)
    {
      ret[0] = 0;
      return '\000';
    }
    char c = str.charAt(pos);
    if (c != '\\')
    {
      ret[0] = 1;
      return c;
    }
    pos++;
    if (pos >= end_pos)
    {
      ret[0] = 1;
      return c;
    }
    ret[0] = 2;
    c = str.charAt(pos);
    switch (c)
    {
    case '0': 
    case '1': 
    case '2': 
    case '3': 
    case '4': 
    case '5': 
    case '6': 
    case '7': 
      long code = c - '0';
      for (int i = 1; i < 3; i++)
      {
        pos++;
        if (pos >= end_pos) {
          break;
        }
        c = str.charAt(pos);
        if ((c < '0') || (c > '7')) {
          break;
        }
        ret[0] += 1;
        code *= 8L;
        code += c - '0';
      }
      return (char)(int)code;
    case 'b': 
      return '\b';
    case 'f': 
      return '\f';
    case 'n': 
      return '\n';
    case 'r': 
      return '\r';
    case 't': 
      return '\t';
    case 'u': 
      code = 0L;
      for (int i = 0; i < 4; i++)
      {
        pos++;
        if (pos >= end_pos) {
          break;
        }
        c = str.charAt(pos);
        if (('0' <= c) && (c <= '9'))
        {
          ret[0] += 1;
          code *= 16L;
          code += c - '0';
        }
        else if (('a' <= c) && (c <= 'f'))
        {
          ret[0] += 1;
          code *= 16L;
          code += c - 'a' + 10;
        }
        else
        {
          if (('A' > c) || (c > 'F')) {
            break;
          }
          ret[0] += 1;
          code *= 16L;
          code += c - 'A' + 10;
        }
      }
      return (char)(int)code;
    }
    return c;
  }
}