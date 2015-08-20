package jatools.swingx.scripteditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;

public class DragFileDemo extends JPanel implements ActionListener {
	  JTextArea fileArea;

	 // JFileChooser fc;

	  JButton clear;

	  TabbedPaneController tpc;

	  public DragFileDemo() {
	    super(new BorderLayout());

	

	 
	 

	    //The TabbedPaneController manages the panel that
	    //contains the tabbed pane. When there are no files
	    //the panel contains a plain text area. Then, as
	    //files are dropped onto the area, the tabbed panel
	    //replaces the file area.
	    JTabbedPane tabbedPane = new JTabbedPane();
	    JPanel tabPanel = new JPanel(new BorderLayout());
	    tabPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	    tpc = new TabbedPaneController(tabbedPane, tabPanel);


	    add(tabPanel, BorderLayout.CENTER);
	  }

	  public void setDefaultButton() {
	    getRootPane().setDefaultButton(clear);
	  }

	  public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == clear) {
	      tpc.clearAll();
	    }
	  }

	  /**
	   * Create the GUI and show it. For thread safety, this method should be
	   * invoked from the event-dispatching thread.
	   */
	  private static void createAndShowGUI() {
	    //Make sure we have nice window decorations.
	    JFrame.setDefaultLookAndFeelDecorated(true);

	    //Create and set up the window.
	    JFrame frame = new JFrame("DragFileDemo");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    //Create and set up the menu bar and content pane.
	    DragFileDemo demo = new DragFileDemo();
	    demo.setOpaque(true); //content panes must be opaque
	    frame.setContentPane(demo);

	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	    demo.setDefaultButton();
	  }

	  public static void main(String[] args) {
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	        createAndShowGUI();
	      }
	    });
	  }
	}

	/*
	 * TabbedPaneController.java is used by the 1.4 DragFileDemo.java example.
	 */

	/*
	 * Class that manages area where the contents of files are displayed. When no
	 * files are present, there is a simple JTextArea instructing users to drop a
	 * file. As soon as a file is dropped, a JTabbedPane is placed into the window
	 * and each file is displayed under its own tab. When all the files are removed,
	 * the JTabbedPane is removed from the window and the simple JTextArea is again
	 * displayed.
	 */

	class TabbedPaneController {
	  JPanel tabbedPanel = null;

	  JTabbedPane tabbedPane;

	  JPanel emptyFilePanel = null;

	  JTextArea emptyFileArea = null;

	  FileAndTextTransferHandler transferHandler;

	  boolean noFiles = true;

	  String fileSeparator;

	  public TabbedPaneController(JTabbedPane tb, JPanel tp) {
	    tabbedPane = tb;
	    tabbedPanel = tp;
	    transferHandler = new FileAndTextTransferHandler(this);
	    fileSeparator = System.getProperty("file.separator");
	    //The split method in the String class uses
	    //regular expressions to define the text used for
	    //the split. The forward slash "\" is a special
	    //character and must be escaped. Some look and feels,
	    //such as Microsoft Windows, use the forward slash to
	    //delimit the path.
	    if ("\\".equals(fileSeparator)) {
	      fileSeparator = "\\\\";
	    }
	    init();
	  }

	  public JTextArea addTab(String filename) {
	    if (noFiles) {
	      tabbedPanel.remove(emptyFilePanel);
	      tabbedPanel.add(tabbedPane, BorderLayout.CENTER);
	      noFiles = false;
	    }
	    String[] str = filename.split(fileSeparator);
	    return makeTextPanel(str[str.length - 1], filename);
	  }

	  //Remove all tabs and their components, then put the default
	  //file area back.
	  public void clearAll() {
	    if (noFiles == false) {
	      tabbedPane.removeAll();
	      tabbedPanel.remove(tabbedPane);
	    }
	    init();
	  }

	  private void init() {
	    String defaultText = "Select one or more files from the file chooser and drop here...";
	    noFiles = true;
	    if (emptyFilePanel == null) {
	      emptyFileArea = new JTextArea(20, 15);
	      emptyFileArea.setEditable(false);
	      emptyFileArea.setDragEnabled(true);
	      emptyFileArea.setTransferHandler(transferHandler);
	      emptyFileArea.setMargin(new Insets(5, 5, 5, 5));
	      JScrollPane fileScrollPane = new JScrollPane(emptyFileArea);
	      emptyFilePanel = new JPanel(new BorderLayout(), false);
	      emptyFilePanel.add(fileScrollPane, BorderLayout.CENTER);
	    }
	    tabbedPanel.add(emptyFilePanel, BorderLayout.CENTER);
	    tabbedPanel.repaint();
	    emptyFileArea.setText(defaultText);
	  }

	  protected JTextArea makeTextPanel(String name, String toolTip) {
	    JTextArea fileArea = new JTextArea(20, 15);
	    fileArea.setDragEnabled(true);
	    fileArea.setTransferHandler(transferHandler);
	    fileArea.setMargin(new Insets(5, 5, 5, 5));
	    JScrollPane fileScrollPane = new JScrollPane(fileArea);
	    tabbedPane.addTab(name, null, (Component) fileScrollPane, toolTip);
	    tabbedPane.setSelectedComponent((Component) fileScrollPane);
	    return fileArea;
	  }
	}

	/*
	 * FileAndTextTransferHandler.java is used by the 1.4 DragFileDemo.java example.
	 */

	class FileAndTextTransferHandler extends TransferHandler {
	  private DataFlavor fileFlavor, stringFlavor;

	  private TabbedPaneController tpc;

	  private JTextArea source;

	  private boolean shouldRemove;

	  protected String newline = "\n";

	  //Start and end position in the source text.
	  //We need this information when performing a MOVE
	  //in order to remove the dragged text from the source.
	  Position p0 = null, p1 = null;

	  FileAndTextTransferHandler(TabbedPaneController t) {
	    tpc = t;
	    fileFlavor = DataFlavor.javaFileListFlavor;
	    stringFlavor = DataFlavor.stringFlavor;
	  }

	  public boolean importData(JComponent c, Transferable t) {
	    JTextArea tc;

	    if (!canImport(c, t.getTransferDataFlavors())) {
	      return false;
	    }
	    //A real application would load the file in another
	    //thread in order to not block the UI. This step
	    //was omitted here to simplify the code.
	    try {
	      if (hasFileFlavor(t.getTransferDataFlavors())) {
	        String str = null;
	        java.util.List files = (java.util.List) t
	            .getTransferData(fileFlavor);
	        for (int i = 0; i < files.size(); i++) {
	          File file = (File) files.get(i);
	          //Tell the tabbedpane controller to add
	          //a new tab with the name of this file
	          //on the tab. The text area that will
	          //display the contents of the file is returned.
	          tc = tpc.addTab(file.toString());

	          BufferedReader in = null;

	          try {
	            in = new BufferedReader(new FileReader(file));

	            while ((str = in.readLine()) != null) {
	              tc.append(str + newline);
	            }
	          } catch (IOException ioe) {
	            System.out
	                .println("importData: Unable to read from file "
	                    + file.toString());
	          } finally {
	            if (in != null) {
	              try {
	                in.close();
	              } catch (IOException ioe) {
	                System.out
	                    .println("importData: Unable to close file "
	                        + file.toString());
	              }
	            }
	          }
	        }
	        return true;
	      } else if (hasStringFlavor(t.getTransferDataFlavors())) {
	        tc = (JTextArea) c;
	        if (tc.equals(source)
	            && (tc.getCaretPosition() >= p0.getOffset())
	            && (tc.getCaretPosition() <= p1.getOffset())) {
	          shouldRemove = false;
	          return true;
	        }
	        String str = (String) t.getTransferData(stringFlavor);
	        tc.replaceSelection(str);
	        return true;
	      }
	    } catch (UnsupportedFlavorException ufe) {
	      System.out.println("importData: unsupported data flavor");
	    } catch (IOException ieo) {
	      System.out.println("importData: I/O exception");
	    }
	    return false;
	  }

	  protected Transferable createTransferable(JComponent c) {
	    source = (JTextArea) c;
	    int start = source.getSelectionStart();
	    int end = source.getSelectionEnd();
	    Document doc = source.getDocument();
	    if (start == end) {
	      return null;
	    }
	    try {
	      p0 = doc.createPosition(start);
	      p1 = doc.createPosition(end);
	    } catch (BadLocationException e) {
	      System.out
	          .println("Can't create position - unable to remove text from source.");
	    }
	    shouldRemove = true;
	    String data = source.getSelectedText();
	    return new StringSelection(data);
	  }

	  public int getSourceActions(JComponent c) {
	    return COPY_OR_MOVE;
	  }

	  //Remove the old text if the action is a MOVE.
	  //However, we do not allow dropping on top of the selected text,
	  //so in that case do nothing.
	  protected void exportDone(JComponent c, Transferable data, int action) {
	    if (shouldRemove && (action == MOVE)) {
	      if ((p0 != null) && (p1 != null)
	          && (p0.getOffset() != p1.getOffset())) {
	        try {
	          JTextComponent tc = (JTextComponent) c;
	          tc.getDocument().remove(p0.getOffset(),
	              p1.getOffset() - p0.getOffset());
	        } catch (BadLocationException e) {
	          System.out.println("Can't remove text from source.");
	        }
	      }
	    }
	    source = null;
	  }

	  public boolean canImport(JComponent c, DataFlavor[] flavors) {
	    if (hasFileFlavor(flavors)) {
	      return true;
	    }
	    if (hasStringFlavor(flavors)) {
	      return true;
	    }
	    return false;
	  }

	  private boolean hasFileFlavor(DataFlavor[] flavors) {
	    for (int i = 0; i < flavors.length; i++) {
	      if (fileFlavor.equals(flavors[i])) {
	        return true;
	      }
	    }
	    return false;
	  }

	  private boolean hasStringFlavor(DataFlavor[] flavors) {
	    for (int i = 0; i < flavors.length; i++) {
	      if (stringFlavor.equals(flavors[i])) {
	        return true;
	      }
	    }
	    return false;
	  }
	}
