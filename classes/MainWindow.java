package classes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is the main class of the program, and is responsible for creating the window and organizing the elements inside of it.
 *
 * @author Reyno Tilikaynen
 * @version 1.0 December 26th 2015
 */
public class MainWindow extends JFrame implements ActionListener
{
  /**
   * The dialog box used for custom messages.
   */
  private JDialog d;
  /**
   * The field where equations are inputted.
   */
  private JTextField fieldEq;
  /**
   * Stores the current equation.
   */
  private JLabel cur;
  /**
   * Stores the menu bar of buttons.
   */
  private MenuBar sb;
  
  /**
   * Constructs a new application window and adds a JMenuBar menu to it.
   */
  public MainWindow ()
  {
    super ("Differential Graphs");
    JMenuItem quitItem = new JMenuItem ("Quit");
    JMenu fileMenu = new JMenu ("File");
    fileMenu.add (quitItem);
    
    JMenuItem helpItem = new JMenuItem ("Help");
    JMenuItem aboutItem = new JMenuItem ("About");
    JMenu helpMenu = new JMenu ("Help");
    helpMenu.add (helpItem);
    helpMenu.add (aboutItem);
    
    JMenuBar myMenus = new JMenuBar ();
    helpItem.addActionListener (this); 
    aboutItem.addActionListener (this);
    quitItem.addActionListener (this); 
    myMenus.add (fileMenu);
    myMenus.add (helpMenu);
    setJMenuBar (myMenus);
    
    setSize (800, 600);
    setResizable (false);
    setVisible (true);
    setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
    mainMenu ();
  }
  
  /**
   * Initializes the body of the application window. Adds elements which allow the user to input equations as well as
   * an instance of MenuBar, which contains the graph and elements associated with it. The elements are organized
   * using SpringLayout.
   */
  private void mainMenu ()
  {
    SpringLayout layout = new SpringLayout (); //Layout
    setLayout (layout);
    JLabel enterEq = new JLabel ("Enter Equation: dy/dx = "); //Label instructing what to do.
    fieldEq = new JTextField (28); //Equation field
    fieldEq.setText ("x*y"); //Default message
    JButton input = new JButton ("Input"); //Input button
    cur = new JLabel ("Current: x*y"); //Label storing the current equation loaded.
    sb = new MenuBar (this); //Instance of MenuBar.
    add (enterEq);
    add (fieldEq);
    add (input); 
    add (cur);
    add (sb.getMenuPanel ());
    add (sb.getGraph ());
    layout.putConstraint (SpringLayout.WEST, enterEq,80, SpringLayout.WEST, this); //enterEq, top left of the window.
    layout.putConstraint (SpringLayout.NORTH, enterEq, 20, SpringLayout.NORTH, this);
    layout.putConstraint (SpringLayout.WEST, fieldEq, 10, SpringLayout.EAST, enterEq); //fieldEq, right of enterEq
    layout.putConstraint (SpringLayout.NORTH, fieldEq, 0, SpringLayout.NORTH, enterEq);
    layout.putConstraint (SpringLayout.WEST, input, 10, SpringLayout.EAST, fieldEq); //input, right of fieldEq
    layout.putConstraint (SpringLayout.NORTH, input, 0, SpringLayout.NORTH, fieldEq);
    layout.putConstraint (SpringLayout.WEST, cur, 10, SpringLayout.EAST, input); //cur, right of input
    layout.putConstraint (SpringLayout.NORTH, cur, 5, SpringLayout.NORTH, input);
    layout.putConstraint (SpringLayout.WEST, sb.getMenuPanel (), 30, SpringLayout.WEST, this); //menuPanel, below the above elements.
    layout.putConstraint (SpringLayout.NORTH, sb.getMenuPanel (), 50, SpringLayout.NORTH, this); 
    layout.putConstraint (SpringLayout.WEST, sb.getGraph (), 100, SpringLayout.WEST, this); //graph, below menuPanel.
    layout.putConstraint (SpringLayout.NORTH, sb.getGraph (), 100, SpringLayout.NORTH, this);
    layout.putConstraint (SpringLayout.SOUTH, sb.getGraph (), 420, SpringLayout.NORTH, sb.getGraph ()); //Forces size to be 600*420.
    layout.putConstraint (SpringLayout.EAST, sb.getGraph (), 600, SpringLayout.WEST, sb.getGraph ());
    validate ();
    repaint ();
    
    input.addActionListener (new ActionListener ()
                               {
      /*
       * Responsible for loading the equation currently inputted. 
       */
      public void actionPerformed (ActionEvent e)
      {
        if (!Equations.check (fieldEq.getText ())) //If the equation isn't valid, show an error message.
          dialog ("Formatting Error!", "Please fix any errors in the equation.");
        else //Otherwise, load it.
        {
          sb.setEquation (fieldEq.getText ());
          cur.setText ("Current: " + fieldEq.getText ());
        }
      }
    }
    );
  }
  
  /**
   * This method is part of the ActionListener implementation and dictates what will happen when an action occurs. 
   * If the "Quit" item is pressed, the java application will exit. 
   * If the "Help" item is pressed, a JDialog window called "Help" will appear. 
   * If the "About" item is pressed, a JDialog window called "About" will appear. 
   * 
   * @param ae This stores the ActionEvent object that contains information on the action performed
   *
   */
  public void actionPerformed (ActionEvent ae)
  {
    if (ae.getActionCommand ().equals ("Quit"))
      System.exit (0);
    if (ae.getActionCommand ().equals ("Help"))
      dialog ("Help", "Enter Equation into top field, enter x/y values in left fields");
    else
    {
      if (ae.getActionCommand ().equals ("About"))
        dialog ("About", "By: Reyno Tilikaynen");
    }
  }
  
  /**
   * Creates a dialog box when either the "Help" or "About" options are pressed. Uses the JDialog class.
   * 
   * @param title The title of the dialog box. 
   * @param text The message of the dialog box. 
   */
  private void dialog (String title, String text)
  {
    d = new JDialog (this, title); //Creates the JDialog box.
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
    d.setLocationRelativeTo (this);
    d.setVisible (true);
  }
  
  /**
   * Creates an instance of MainWindow, which runs the entire program.
   * 
   * @param args The arguments passed in when the class is run.
   * 
   */
  public static void main (String [] args)
  {
    MainWindow m = new MainWindow ();
  }
} // MainWindow class.
