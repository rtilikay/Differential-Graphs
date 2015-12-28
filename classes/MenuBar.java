package classes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * This class handles the graph portion of this application, handling the graph and the elements associated with it.
 *
 * @author Reyno Tilikaynen
 * @version 1.0 December 26th 2015
 */
public class MenuBar
{
  /**
   * Text fields for coordinate input and numeric output.
   */
  private JTextField xVal, yVal, out;
  /**
   * Dialog box used for error messages.
   */
  private JDialog d;
  /**
   * Parent JFrame.
   */
  private JFrame parent;
  /**
   * The equation currently inputted.
   */
  private String equation = "x*y";
  /**
   * The panel of buttons and text fields.
   */
  private JPanel menuPanel; 
  /**
   * The graph.
   */
  private Graph graph;
  
  /**
   * Constructs the menu bar and graph.
   * 
   * @param f The frame being added to.
   */
  public MenuBar (JFrame f)
  {
    parent = f;
    menuBar ();
    graph = new Graph ();
  }
  
  /**
   * Constructs the elements of the menu bar. 
   */
  public void menuBar ()
  {
    menuPanel = new JPanel (); //Stored in a panel.
    FlowLayout l = new FlowLayout (); //Using FlowLayout.
    menuPanel.setLayout (l);
    //Creating menu buttons.
    JButton solve = new JButton ("Solve");
    JButton graphEq = new JButton ("Graph Using Point");
    JButton scale = new JButton ("Set Scale");
    JButton field = new JButton ("Graph Field");
    //Creating fields.
    xVal = new JTextField (7);
    yVal = new JTextField (7);
    xVal.setText ("x-value"); 
    yVal.setText ("y-value");
    out = new JTextField (12);
    //Adding components
    menuPanel.add (xVal); 
    menuPanel.add (yVal); 
    menuPanel.add (graphEq);
    menuPanel.add (scale);
    menuPanel.add (field);
    menuPanel.add (solve);
    menuPanel.add (out); 
    
    parent.validate (); 
    parent.repaint ();
    //Adding ActionListeners to buttons.
    graphEq.addActionListener (new ActionListener ()
                                 {
      /**
       * Graphs the differential equation at the point given by the input fields.
       */
      public void actionPerformed (ActionEvent e)
      {
        graph.clear (); 
        try
        {
          boolean b = graph.graphFunction (Equations.result (xVal.getText (), 1, 1), Equations.result (yVal.getText (), 1, 1), equation); //Tries to graph.
          if (!b) //If something went wrong, check if it was at the beginning.
          {
            Double d = Equations.result (equation, Equations.result (xVal.getText (), 1, 1), Equations.result (yVal.getText (), 1, 1)); 
            if (Double.isInfinite (d) || Double.isNaN (d))
              dialog ("Something went wrong!", "Check to make sure your point is in the domain");
          }
        }
        catch (NumberFormatException ee)
        {
          dialog ("Please enter only numbers!", "A formatting error occured");
        }
        catch (Exception ee)
        {
          dialog ("Something went wrong!", "Please check that the point is in the domain"); 
        }
      }
    }
    );
    field.addActionListener (new ActionListener ()
                               {
      /**
       * Graphs the field provided by the differential equation.
       */
      public void actionPerformed (ActionEvent e)
      {
        graph.clear ();
        //Iterate over the entire graph.
        for (double x = -6.5*graph.getXScale (); x <= 6.5*graph.getXScale (); x+=graph.getXScale ()/2.0)
        {
          inner:
            for (double y = -4.5*graph.getYScale (); y <= 4.5*graph.getYScale (); y+=graph.getYScale ()/2.0)
          {
            double slope = 0;
            //Find the slope.
            try
            {
              slope = Equations.result (equation, x, y);
            }
            catch (Exception ee)
            {
              if (ee.getMessage ().equals (" / by zero"))
                slope = Integer.MAX_VALUE;
              else
                continue inner;
            }
            
            //Creates a small line segment based on the slope.
            double xs, xf, ys, yf;
            if (Math.abs (slope) > graph.getYScale ()/graph.getXScale ())
            {
              if (slope > 0)
                ys = y - graph.getYScale ()/4;
              else
                ys = y + graph.getYScale ()/4;
              yf = y + y - ys;
              xs = x -(y-ys)/slope;
              xf = x + x - xs;
            }
            else
            {
              xs = x - graph.getXScale ()/4;
              xf = x + x - xs;
              ys = y - (x-xs)*slope;
              yf = y + y - ys;
            }
            //Draws the segment.
            graph.drawSegment (xs, ys, xf, yf);
          }
        }
      }
    }
    );
    scale.addActionListener (new ActionListener ()
                               {
      /**
       * Updates the scale of the graph.
       */
      public void actionPerformed (ActionEvent e)
      {
        try
        {
          graph.setScale (Equations.result (xVal.getText (), 1, 1), Equations.result (yVal.getText (), 1, 1));
        }
        catch (Exception ee)
        {
          dialog ("Please enter only numbers!", "A formatting error occured");
        }
      }
    }
    );
    solve.addActionListener (new ActionListener ()
                               {
      //Finds the value of the differential equation at a given point.
      public void actionPerformed (ActionEvent e)
      {
        try
        {
          //Parse input fields.
          double x = Equations.result (xVal.getText (), 1, 1);
          double y = Equations.result (yVal.getText (), 1, 1);
          try
          {
            //Calculate the slope.
            double d = Equations.result (equation, x, y);
            if (Double.isNaN (d) || Double.isInfinite (d))
              out.setText ("Result: Undefined");
            else
              out.setText ("Result: " + d);
          }
          catch (Exception ee)
          {
            dialog ("Something went wrong!", "Please check that the point is in the domain");
          }
        }
        catch (Exception ee)
        {
          dialog ("Please enter only numbers!", "A formatting error occured"); 
        }
      }
    }
    );
  }
  
  /**
   * Creates a dialog box when either the "Help" or "About" options are pressed. Uses the JDialog class.
   * 
   * @param title The title of the dialog box. 
   * @param text The message of the dialog box. 
   */
  private void dialog (String title, String text)
  {
    d = new JDialog (parent, title); //Creates the JDialog box.
    d.setSize (340, 100);
    d.setResizable (false);
    d.setLayout (new FlowLayout ()); //Uses a FlowLayout.
    JLabel field1 = new JLabel (text); //Creates a label storing the text to be displayed.
    d.add (field1);
    JButton button = new JButton ("Close"); //Close button.
    button.addActionListener (new ActionListener ()
                                {
      /*
       * Closes the window when the close button is pressed.
       */
      public void actionPerformed (ActionEvent e)
      {
        d.dispose ();
      }
    }
    );
    d.add (button);
    d.setLocationRelativeTo (parent);
    d.setVisible (true);
  }
  
  /**
   * Returns the menuPanel.
   * @return a JPanel containing the menu panel.
   */
  public JPanel getMenuPanel ()
  {
    return menuPanel;
  }
  
  /**
   *  Returns the graph.
   * @return a Graph containing the graph currently used.
   */
  public Graph getGraph ()
  {
    return graph; 
  }
  
  /**
   * Sets the equation to the new value.
   * 
   * @param s The new equation.
   */
  public void setEquation (String s)
  {
    equation = s; 
  }
}
