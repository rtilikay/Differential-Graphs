package classes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

/**
 * This class handles the graph used by the program.
 *
 * @author Reyno Tilikaynen
 * @version 1.0 December 26th 2015
 */
public class Graph extends JPanel
{
  /**
   * The scale for the x, y axes.
   */
  private double yScale = 1, xScale = 1;
  /**
   * The number of decimals for the x, y axes to display.
   */
  private int ydecimals = 0, xdecimals = 0;
  /**
   * Graph size.
   */
  private int ySize = 420;
  private int xSize = 600;
  
  /**
   * Spacing between axis bars.
   */
  private int spacing = 40;
  
  /**
   * Origin location.
   */
  private int xCenter = 300; 
  private int yCenter = 210;
  
  /**
   * Size of the ticks of the axes.
   */
  private int markSize = 7;
  
  /**
   * Temporary variables used for storing line segments.
   */
  private int xs = 0; 
  private int ys = 0; 
  private int xf = 0; 
  private int yf = 0;
  
  /**
   * Stores all the lines to be displayed on screen.
   */
  private ArrayList <Line> lines = new ArrayList <Line> ();
  
  /**
   * Override of the getPrefferedSize method to fix size.
   * @return The preferred dimensions of the 
   */
  private Dimension getPrefferedSize ()
  {
    return new Dimension (xSize, ySize);
  }
  
  /**
   *  Clears the graph of all lines drawn on it.
   */
  public void clear ()
  {
    lines = new ArrayList <Line> ();
  }
  
  /**
   * Updates the scale used by the graph.
   * 
   * @param x The new scale for the x axis. 
   * @param y The new scale for the y axis. 
   * @throws NumberFormatException
   */
  public void setScale (double x, double y) throws NumberFormatException
  {
    xScale = x;
    yScale = y;
    if (("" + xScale).indexOf (".") < 0)
      xdecimals = 0; 
    else
      xdecimals = ("" + xScale).substring (("" + xScale).indexOf (".") + 1).length ();
    if (("" + yScale).indexOf (".") < 0)
      ydecimals = 0; 
    else
      ydecimals = ("" + yScale).substring (("" + yScale).indexOf (".") + 1).length ();
    if (xdecimals > 4)
      xdecimals = 4; 
    if (ydecimals > 4)
      ydecimals = 4;
    clear (); 
  }
  
  /**
   * Returns the scale of the x-axis.
   * 
   * @return The x-axis scale in double form.
   */
  public double getXScale ()
  {
   return xScale; 
  }
  
  /**
   * Returns the scale of the y-axis.
   * 
   * @return The y-axis scale in double form.
   */
  public double getYScale ()
  {
   return yScale; 
  }
  
  /**
   *  Graphs a function given the differential equation dy/dx = [equation] starting at the point (x, y).
   *  <p>
   *  Functions are graphed by using Euler's method as long as they within the bounds of the graph and 
   *  the magnitude of the slope is less than a threshhold (this prevents sporatic behaviour near infinities).
   * 
   *  @param x The starting x coordinate.
   *  @param y The starting y coordinate.
   *  @param equation The string expression used for the equation.
   *  @return True if the graph was successful, false otherwise.
   */
  public boolean graphFunction (double x, double y, String equation)
  {
    double xs = x; 
    double ys = y;
    double space = xScale/120.0; //By which amount to increment.
    //Graphs towards the right first, stopping if an invalid slope is reached.
    for (double xf = xs + space; xf <= xScale * 7; xf += space)
    {
      double slope = 0;
      try
      {
        slope = Equations.result (equation, xs, ys); //Finds the slope at the current point.
        if (Double.isNaN (slope) || Double.isInfinite (slope) || Math.abs (slope)*space*3 > yScale) //Checks if the slope is invalid.
          break;
      }
      catch (Exception e) {break;}
      double yf = ys + slope*space; //Calculate the y coordinate.
      drawSegment (xs, ys, xf, yf); //Add a line segment of the current slope.
      xs = xf; //Update the current coordinates.
      ys = yf;
    }
    xs = x; //Reset variables.
    ys = y;
    
    //Graph towards the left (Near identical code to the one above).
    for (double xf = xs - space; xf >= xScale * -7; xf -= space)
    {
      double slope = 0;
      try
      {
        slope = Equations.result (equation, xs, ys);
        if (Double.isNaN (slope) || Double.isInfinite (slope) || Math.abs (slope)*space*3 > yScale)
          return false;
      }
      catch (Exception e) {return false;}
      double yf = ys - slope*space;
      drawSegment (xs, ys, xf, yf);
      xs = xf; 
      ys = yf;
    }
    return true;
  }
  
  /**
   *  Adds the line segment defined by it's start points and end points to the list (which will cause them to be drawn).
   * 
   *  @param xs The x coordinate of the start point.
   *  @param ys The y coordinate of the start point.
   *  @param xf The x coordinate of the end point.
   *  @param yf The y coordinate of the end point.
   */
  public void drawSegment (double xs, double ys, double xf, double yf)
  {
    //Converts coordinates to coordinates on screen.
    int a = xCenter + (int)((xs/xScale)*spacing);
    int b = yCenter - (int)((ys/yScale)*spacing);
    int c = xCenter + (int)((xf/xScale)*spacing); 
    int d = yCenter - (int)((yf/yScale)*spacing);
    //Adds the line.
    lines.add (new Line (a, b, c, d));
  }
  
  /**
   *  Override of the paintComponent method used to graph functions on screen.
   *  <p>
   *  This method draws the axes of the graph as well as any lines stored in the list.
   * 
   *  @param g The Graphics object used to draw things in the component.
   */
  protected void paintComponent (Graphics g)
  {
    super.paintComponent (g);
    
    //Axes
    g.setColor (Color.black);
    g.drawLine (0, yCenter, xSize, yCenter);
    g.drawLine (xCenter, 0, xCenter, ySize);
    
    //Draws the markers on the axes.
    for (int x = spacing; x <= xCenter; x+= spacing)
    {
      //Draws the ticks on the axes.
      g.drawLine (xCenter + x, yCenter - markSize, xCenter + x, yCenter + markSize); 
      g.drawLine (xCenter - x, yCenter - markSize, xCenter - x, yCenter + markSize);
      g.drawLine (xCenter - markSize, yCenter + x, xCenter + markSize, yCenter + x);
      g.drawLine (xCenter - markSize, yCenter - x, xCenter + markSize, yCenter - x);
      
      //Decimal formatting of x-scale.
      String ps = (x/spacing*xScale) + "00000";
      if (xdecimals == 0)
        ps = ps.substring (0, ps.indexOf ("."));
      else
        ps = ps.substring (0, Math.min (ps.indexOf (".") + xdecimals + 1, ps.length ()));
      if (ps.length () > 6)
        ps = ps.substring (0, 6);
      String ns = "-" + ps;
      if (ns.length () > 6)
        ns = ns.substring (0, 6);
      
      //Drawing the x-scale.
      g.drawString (ps, xCenter + x - (ps.length ()*4), yCenter + markSize + 15);
      g.drawString (ns, xCenter - x - (ns.length ()*4), yCenter + markSize + 15);
      
      //Same for y-scale.
      ps = (x/spacing*yScale) + "00000";
      if (ydecimals == 0)
        ps = ps.substring (0, ps.indexOf ("."));
      else
        ps = ps.substring (0, Math.min (ps.indexOf (".") + ydecimals + 1, ps.length ()));
      if (ps.length () > 6)
        ps = ps.substring (0, 6);
      ns = "-" + ps;
      if (ns.length () > 6)
        ns = ns.substring (0, 6);
      g.drawString (ps, xCenter + markSize + 4, yCenter - x + 4);
      g.drawString (ns, xCenter + markSize + 4, yCenter + x + 4);
    }
    //Draws the lines.
    for (Line i: lines)
      i.paint (g);
    repaint ();
  }
  
  /**
   * This class is used to store line segments.
   * 
   * @author Reyno Tilikaynen
   * @version 1.0 December 26th, 2015
   */
  private class Line
  {
    int xs, ys, xf, yf; //Start and end points of the line.
    
    //Constructor.
    public Line (int a, int b, int c, int d)
    {
      xs = a; 
      ys = b; 
      xf = c; 
      yf = d;
    }
    
    //Draws the line.
    public void paint (Graphics g)
    {
      g.drawLine (xs, ys, xf, yf);
    }
  }
}
