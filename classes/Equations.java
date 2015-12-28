package classes;

import java.util.*; 

/**
 * This class is able to interpret and evaluate expressions containing two variables x and y as well as well as many functions.
 * This class takes a string expression as input and can check whether it's valid, format it, or find it's result given
 * values for x and y.
 *
 * @author Reyno Tilikaynen
 * @version 1.0 December 26th 2015
 */
public final class Equations
{
  /**
   * Returns the result of an expression given values for x and y.
   * 
   * @param eq A string containing the expression to be evaluated.
   * @param x The value for x.
   * @param y The value for y.
   * @return The result of evaluating the expression with the given values for x and y.
   */
  public static double result (String eq, double x, double y) throws Exception
  {
    ArrayList s = parse (eq, x, y); //Converts the expression to an ArrayList of elements.
    //First, the list is checked for functions.
    for (int i = 0; i < s.size ()-1; i++)
    {
      Object c = s.get (i); //grabs the first element in the expression.
      if (c instanceof String) //If it's a string, then it's a function.
      {
        String ss = (String)c; 
        if (ss.equals ("sin")) //sin
        {
          s.remove (i); //removes the function from the list.
          s.add (i, new Double (Math.sin ((Double)s.remove (i)))); //removes the next element of the list and evaluates it. 
        }
        else if (ss.equals ("cos")) //cos
        {
          s.remove (i);  
          s.add (i, new Double (Math.cos ((Double)s.remove (i))));
        }
        else if (ss.equals ("tan")) //tan
        {
          s.remove (i);  
          s.add (i, new Double (Math.tan ((Double)s.remove (i))));
        }
        if (ss.equals ("sinh")) //hyperbolic sin
        {
          s.remove (i);  
          s.add (i, new Double (Math.sinh ((Double)s.remove (i))));
        }
        else if (ss.equals ("cosh")) //hyperbolic cos
        {
          s.remove (i);  
          s.add (i, new Double (Math.cosh ((Double)s.remove (i))));
        }
        else if (ss.equals ("tanh")) //hyperbolic tan
        {
          s.remove (i);  
          s.add (i, new Double (Math.tanh ((Double)s.remove (i))));
        }
        else if (ss.equals ("arcsin")) //sin inverse
        {
          s.remove (i);  
          s.add (i, new Double (Math.asin ((Double)s.remove (i))));
        }
        else if (ss.equals ("arccos")) //cos inverse
        {
          s.remove (i);  
          s.add (i, new Double (Math.acos ((Double)s.remove (i))));
        }
        else if (ss.equals ("arctan")) //tan inverse
        {
          s.remove (i);  
          s.add (i, new Double (Math.atan ((Double)s.remove (i))));
        }
        else if (ss.equals ("ln")) //natural logarithm
        {
          s.remove (i);  
          s.add (i, new Double (Math.log ((Double)s.remove (i))));
        }
        else if (ss.equals ("log")) //logarithm
        {
          s.remove (i);  
          s.add (i, new Double (Math.log10 ((Double)s.remove (i))));
        }
        else if (ss.equals ("sqrt")) //square root
        {
          s.remove (i);  
          s.add (i, new Double (Math.sqrt ((Double)s.remove (i))));
        }
        else if (ss.equals ("abs")) //absolute value
        {
          s.remove (i);  
          s.add (i, new Double (Math.abs ((Double)s.remove (i))));
        }
      }
    }
    
    //Next, exponents are checked.
    for (int i = 1; i < s.size () - 1; i++)
    {
      Object c = s.get (i);
      if (c instanceof String)
      {
        String ss = (String)c;
        if (ss.equals ("^"))
        {
          s.remove (i);  
          s.add (i-1, new Double (Math.pow ((Double)s.remove (i-1), (Double)s.remove (i-1)))); //Replaces three elements in the list with their result.
        }
      }
    }
    
    //Division/Multiplication
    for (int i = 0; i < s.size () - 1; i++)
    {
      Object c = s.get (i);
      if (c instanceof String)
      {
        String ss = (String)c;
        if (i > 0 && ss.equals ("/"))
        {
          s.remove (i);  
          s.add (i-1, new Double ((Double)s.remove (i-1)/(Double)s.remove (i-1)));
          i--;
        }
        else if (i > 0 && ss.equals ("*"))
        {
          s.remove (i);  
          s.add (i-1, new Double ((Double)s.remove (i-1)*(Double)s.remove (i-1)));
          i--;
        }
        else if (i == 0 && ss.equals ("-"))
        {
          s.remove (i);  
          s.add (0, new Double (-1*(Double)s.remove (i)));
          i--;
        }
      }
    }
    
    //Addition/Subtraction
    for (int i = 1; i < s.size () - 1; i++)
    {
      Object c = s.get (i);
      if (c instanceof String)
      { 
        String ss = (String)c;
        if (ss.equals ("+"))
        {
          s.remove (i);  
          s.add (i-1, new Double ((Double)s.remove (i-1)+(Double)s.remove (i-1)));
          i--;
        }
        else if (ss.equals ("-"))
        {
          s.remove (i);  
          s.add (i-1, new Double ((Double)s.remove (i-1)-(Double)s.remove (i-1)));
          i--;
        }
      }
    }
    return (Double)s.get (0);
  }
  
  /**
   * Formats the string by putting spaces between all distinct elements.
   * <p>
   * For example, ["2sin(xypi)"] becomes ["2 sin ( x y pi )"].
   * 
   * @param eq The string expression 
   * @return The formatted string
   */
  private static String format (String eq)
  {
    //Iterates through the string
    for (int i = 0; i < eq.length (); i++)
    {
      char c = eq.charAt (i); 
      if (c == '.' || c == ' ') //Not distinct elements
        continue;
      if (c == 'x' || c == 'y' || c == 'e')  //Variables or the constant e.
      {
        eq = eq.substring (0, i) + " " + c + " " + eq.substring (i+1);          
        i+= 2; 
      }
      else if (i != eq.length () - 1 && eq.substring (i, i+2).equals ("pi")) //Pi.
      {
        eq = eq.substring (0, i) + " pi " + eq.substring (i+2);          
        i+= 3; 
      }
      else if (isLetter (c)) //This means it's a function (since the variables were already considered)
      {
        if (i != eq.length () - 1 && isNumber (eq.charAt (i+1)) || isSpecial (eq.charAt (i+1))) //If it's the last character of the function
          eq = eq.substring (0, i + 1) + " " + eq.substring (i+1);
        if (i != 0) //If it's not the first character of the expression
          if (isNumber (eq.charAt (i-1)) || isSpecial (eq.charAt (i-1))) //If it's the first character of the function 
          eq = eq.substring (0, i) + " " + eq.substring (i);
      }
      else if (isSpecial (c)) //This means it's a binary operation symbol.
      {
        if (i != eq.length () - 1 && eq.charAt (i+1) != ' ') //If it's not the last character or already formatted
          eq = eq.substring (0, i + 1) + " " + eq.substring (i+1);
        if (i != 0) //If it's not the first character.
          if (eq.charAt (i-1) != ' ') //Prevents duplicate formatting. 
          eq = eq.substring (0, i) + " " + eq.substring (i);
      }
    }
    return eq;
  }
  
  /**
   * Parses the string expression into an ArrayList containing it's elements to be evaluated by the result function.
   * <p>
   * As an example, [parse ("2x", 1, 2)] returns [{2, "*", 1}].
   * 
   * @param eq The string expression to be parsed.
   * @param x The value for x.
   * @param y The value for y.
   * @return An ArrayList containing the elements of the parsed expression.
   */
  private static ArrayList parse (String eq, double x, double y)
  {
    try
    {
      eq = format (eq);
      ArrayList r = new ArrayList ();
      while (eq.length () > 0)
      {
        String s = "";
        eq = eq.trim ();
        if (eq.indexOf (" ") >= 0)
        {
          s = eq.substring (0, eq.indexOf (" "));
          eq = eq.substring (eq.indexOf (" ") + 1);
        }
        else
        {
          s = eq; 
          eq = "";
        }
        try
        {
          if (s.equals ("x"))
            s = "" + x;
          else if (s.equals ("y"))
            s = "" + y;
          Double d = getDouble (s);
          if (r.size () > 0 && r.get (r.size () - 1) instanceof Double)
            r.add (new String ("*")); 
          r.add (d);
        }
        catch (NumberFormatException e)
        {
          if (s.charAt (0) == '(')
          {
            int count = 1;
            int end;
            for (end = 1; count > 0; end++)
            {
              if (eq.charAt (end) == ')')
                count--;
              else if (eq.charAt (end) == '(')
                count++;
            }
            end--;
            if (r.size () > 0 && r.get (r.size () - 1) instanceof Double)
              r.add (new String ("*")); 
            r.add (new Double (result (eq.substring (0, end), x, y))); 
            eq = eq.substring (end + 1);
          }
          else
          {  
            if (r.size () > 0 && r.get (r.size () - 1) instanceof String)
            {
              String t = (String)r.get (r.size () - 1);
              if (isLetter (s.charAt (0)) == isLetter (t.charAt (0)))
                throw new Exception ("Two consecutive letters!");
            }
            if (r.size () > 0 && isLetter (s.charAt (0)) && r.get (r.size () - 1) instanceof Double)
              r.add (new String ("*")); 
            if (r.size () == 0 && !isLetter (s.charAt (0)) && s.charAt (0) != '-')
              continue;
            r.add (s);
          }
        }
      }
      return r;
    }
    catch (Exception e)
    {
      //e.printStackTrace ();
      return null;
    }
  }
  
  /**
   * Checks whether an expression is properly formatted.
   * <p>
   * For example, [check ("2sin(xypi)")] returns true, while [check ("2**3")] returns false.
   * 
   * @param eq The string expression
   * @return True if the string is properly formatted, false otherwise.
   */
  public static boolean check (String eq)
  {
      ArrayList test = parse (eq, 1, 1); //Tries to parse the expression.
      if (test == null) //If parsing didn't work.
        return false;
      return true;
  }
  
  /**
   * Checks whether a character is a lower-case letter.
   * 
   * @param c The character to be checked
   * @return True if it is a lower-case letter, false otherwise
   */
  private static boolean isLetter (char c)
  {
    if (c >= 97 && c <= 122)
      return true; 
    return false;
  }
  
  /**
   * Checks whether a character is a special symbol (not a letter, space or number).
   * 
   * @param c The character to be checked
   * @return True if it is a special symbol, false otherwise
   */
  private static boolean isSpecial (char c)
  {
    if (isNumber (c) || isLetter (c) || c == ' ')
      return false; 
    return true;
  }
  
  /**
   * Checks whether a character is a number.
   * 
   * @param c The character to be checked
   * @return True if it is a number, false otherwise
   */
  private static boolean isNumber (char c)
  {
    if (c >= 48 && c <= 57)
      return true; 
    return false;
  }
  
  /**
   * Checks whether a character is a variable.
   * 
   * @param c The character to be checked
   * @return True if it is a lower-case letter, false otherwise
   */
  private static boolean isVariable (char c)
  {
    if (c == 'x' || c == 'y' || c == 'e' || c == 'p' || c == 'i')
      return true; 
    return false;
  }
  
  /**
   * Converts a string into a double.
   * 
   * @param s The string to be converted.
   * @return The double value of the string.
   * @throws NumberFormatException
   */
  public static double getDouble (String s) throws NumberFormatException
  {
    if (s.equals ("e"))
      return Math.E;
    if (s.equals ("pi"))
      return Math.PI;
    return Double.parseDouble (s);
  }
}