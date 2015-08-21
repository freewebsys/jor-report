package jatools.designer.toolbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class SwingBorderEditor extends SwingEditorSupport {
	  
    private JComboBox borderCombo;
    private JButton  borderButton;
    private BorderDialog borderDialog;

    private Border etched = BorderFactory.createEtchedBorder();
    private Border bevelLowered = BorderFactory.createLoweredBevelBorder();
    private Border bevelRaised =BorderFactory.createRaisedBevelBorder();
    private Border line = BorderFactory.createLineBorder(Color.black);
    private Border borders[] = { etched, bevelLowered, bevelRaised, line };

    private Border border;

    public SwingBorderEditor(){
	createComponents();
	addComponentListeners();
    }

    private void createComponents() {
	panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	panel.setAlignmentX(Component.LEFT_ALIGNMENT);
	UIDefaults table = UIManager.getDefaults();
	table.put("beaninfo.BorderIcon", 
		  LookAndFeel.makeIcon(getClass(), "resources/BorderIcon.gif"));
        table.put("beaninfo.BorderBevelLowered", 
		  LookAndFeel.makeIcon(getClass(), "resources/BorderBevelLowered.gif"));
        table.put("beaninfo.BorderBevelRaised", 
		  LookAndFeel.makeIcon(getClass(), "resources/BorderBevelRaised.gif"));
        table.put("beaninfo.BorderEtched", 
		  LookAndFeel.makeIcon(getClass(), "resources/BorderEtched.gif"));
        table.put("beaninfo.BorderLine", 
		  LookAndFeel.makeIcon(getClass(), "resources/BorderLine.gif"));
	Icon buttonIcon = UIManager.getIcon("beaninfo.BorderIcon");

        borderCombo = createComboBox();

	// need rigid area match up
	borderButton = new JButton(buttonIcon);
	Dimension d = new Dimension(buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
	borderButton.setMargin(SwingEditorSupport.BUTTON_MARGIN);

	setAlignment(borderCombo);
	setAlignment(borderButton);
	panel.add(borderCombo);
	panel.add(Box.createRigidArea(new Dimension(5,0)));
	panel.add(borderButton);
	panel.add(Box.createHorizontalGlue());
    }

    private void addComponentListeners() {
	borderButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    if (borderDialog == null)
			borderDialog = new BorderDialog(panel.getParent(), "Border Chooser");
		    border = borderDialog.showDialog();
		    if (!(borderDialog.isCancelled()))
			setValue(border);
		}
	    });
    }

    public void setValue(Object value){
	super.setValue(value);
	// update our GUI state
	// set ComboBox to any equal border value
	//	borderCombo.setSelectedItem(value);
	// set BorderChooser - setSelectedBorder to any equal value as well
    }
  
    private JComboBox createComboBox(){
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	for (int i = 0; i < 4; i++){
	    model.addElement(new Integer(i));
	}
          
	JComboBox c = new JComboBox(model); // borders);
	c.setRenderer(new TestCellRenderer(c));
	c.setPreferredSize(SwingEditorSupport.MEDIUM_DIMENSION); // new Dimension(120,20));
	c.setMinimumSize(SwingEditorSupport.MEDIUM_DIMENSION);
	c.setMaximumSize(SwingEditorSupport.MEDIUM_DIMENSION);
	c.setSelectedIndex(-1);
	c.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    JComboBox cb = (JComboBox)e.getSource();
		    border = borders[cb.getSelectedIndex()];
		    setValue(border);
		}
	    }); 
	return c;
    }


    class TestCellRenderer extends JLabel implements ListCellRenderer   {
	JComboBox combobox;
	Icon images[] = {
	    UIManager.getIcon("beaninfo.BorderEtched"),                  
	    UIManager.getIcon("beaninfo.BorderBevelLowered"),
	    UIManager.getIcon("beaninfo.BorderBevelRaised"),
	    UIManager.getIcon("beaninfo.BorderLine") };

	String desc[] = {
	    "Etched",                  
	    "BevelLowered",
	    "BevelRaised",
	    "Line" };
          
	public TestCellRenderer(JComboBox x) {
	    this.combobox = x;
	    setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
						      int modelIndex,
						      boolean isSelected,
						      boolean cellHasFocus) {
	    if (value == null) {
		setText("");
		setIcon(null);
	    } else {
		int index = ((Integer)value).intValue();
		if (index < 0){
		    setText("");
		    setIcon(null);
		} else {
		    String text = " " + desc[index];
		    setIcon(images[index]);                          
		    setText(text);
		    if (isSelected){
			setBackground(UIManager.getColor("ComboBox.selectionBackground"));
			setForeground(UIManager.getColor("ComboBox.selectionForeground"));
		    } else {
			setBackground(UIManager.getColor("ComboBox.background"));
			setForeground(UIManager.getColor("ComboBox.foreground"));
		    }
		}
	    }
	    return this;
	}
    }

    class BorderDialog extends JDialog {
	JPanel pane;
	JButton okButton;
	BorderChooser borderChooser;
	Border border = null;
	boolean cancel = false;

	public BorderDialog(Component c, String title){
	    super(JOptionPane.getFrameForComponent(c), title, true);
	    Container contentPane = getContentPane();
	    pane = new JPanel();

	    contentPane.setLayout(new BorderLayout());
	    okButton = new JButton("OK"); // new BorderTracker(pane);
	    ActionListener okListener = new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			// get the Border from the pane
			border = getBorder();
		    }
		};

	    getRootPane().setDefaultButton(okButton);
	    okButton.setActionCommand("OK");
	    if (okListener != null) {
		okButton.addActionListener(okListener);
	    }
	    okButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			hide();
		    }
		});
	    JButton cancelButton = new JButton("Cancel");
	    cancelButton.setActionCommand("cancel");
	    cancelButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			cancel = true;
			hide();
		    }
		});
	    // borderlayout
	    addBorderChooser(pane);      
	    pane.add(okButton);
	    pane.add(cancelButton);
	    contentPane.add(pane, BorderLayout.CENTER);
	    pack();
	    this.addWindowListener(new Closer());
	    this.addComponentListener(new DisposeOnClose());
	}

	public void addBorderChooser(JPanel panel){
	    borderChooser = new BorderChooser();
	    panel.add(borderChooser);
	}

	public void setBorder(){ // called from pane
	}

	public Border getBorder(){
	    return borderChooser.getSelectedBorder();
	    //      return this.border;
	}

	public Border showDialog(){
	    this.cancel = false;
	    this.show();
	    return getBorder(); // border should be ok
	}

	public boolean isCancelled(){
	    return this.cancel;
	}

	class Closer extends WindowAdapter {
	    public void windowClosing(WindowEvent e) {
		Window w = e.getWindow();
		w.hide();
	    }
	}

	class DisposeOnClose extends ComponentAdapter {
	    public void componentHidden(ComponentEvent e) {
		Window w = (Window)e.getComponent();
		w.dispose();
	    }
	}
    }

    public static void main(String args[]){
	JFrame f = new JFrame();
	f.addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent e){
		    System.exit(0);
		}
	    });
	SwingBorderEditor editor = new SwingBorderEditor();
	f.getContentPane().add(editor.getCustomEditor());
	f.pack();
	f.show();
    }
}
class SwingEditorSupport extends PropertyEditorSupport {

    /** 
     * Component which holds the editor. Subclasses are responsible for
     * instantiating this panel.
     */
    protected JPanel panel;
    
    protected static final Dimension LARGE_DIMENSION = new Dimension(150,20);
    protected static final Dimension MEDIUM_DIMENSION = new Dimension(120,20);
    protected static final Dimension SMALL_DIMENSION = new Dimension(50,20);
    protected static final Insets BUTTON_MARGIN = new Insets(0,0,0,0);

    /** 
     * Returns the panel responsible for rendering the PropertyEditor.
     */
    public Component getCustomEditor() {
        return panel;
    }

    public boolean supportsCustomEditor() {
        return true;
    }
    
    // layout stuff
    protected final void setAlignment(JComponent c){
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.setAlignmentY(Component.CENTER_ALIGNMENT);
    }
    
    /** 
     * For property editors that must be initialized with values from
     * the property descriptor.
     */
    public void init(PropertyDescriptor descriptor)  {  
    }
}
