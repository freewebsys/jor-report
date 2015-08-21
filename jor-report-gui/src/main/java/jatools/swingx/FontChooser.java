package jatools.swingx;

import jatools.designer.App;
import jatools.designer.Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.4 $
 * @author $author$
 */
public class FontChooser extends JPanel implements Chooser, ChangeListener {
    private static FontChooser defaultChooser;
    private final static String SAMPLE_TEXT = "Hello !"; //
    private ListEditor faceSelector;
    private ListEditor styleSelector;
    private ListEditor sizeSelector;
    private String sampleText = SAMPLE_TEXT;
    private JLabel sampleLabel = new JLabel(sampleText);
    Object[] styles = { App.messages.getString("res.17"), App.messages.getString("res.18"), App.messages.getString("res.19"), App.messages.getString("res.20") }; // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    boolean exitOK = false;
    Object result = null;

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    JDialog shareInstance;
	private boolean empty;

    /**
    * Creates a new ZFontChooser object.
    */
    public FontChooser() {
        this(false);
    }
    
    public FontChooser(boolean empty) {
        buildDialog();
        this.empty = empty;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color color) {
        sampleLabel.setForeground(color);
    }

    /**
     * DOCUMENT ME!
     *
     * @param sampleText DOCUMENT ME!
     */
    public void setSampleText(String sampleText) {
        sampleLabel.setText(sampleText);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        sampleLabel.setFont((Font) createFont());
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public static FontChooser getDefault() {
        if (defaultChooser == null) {
            defaultChooser = new FontChooser();
        }

        return defaultChooser;
    }

    /**
    * DOCUMENT ME!
    *
    * @param font DOCUMENT ME!
    */
    public void setValue(Object value) {
        Font f = (Font) value;
        String face = (f == null) ? App.messages.getString("res.21") : f.getName(); //
        int style = (f == null) ? 0 : f.getStyle();
        int size = (f == null) ? 12 : f.getSize();

        faceSelector.setSelectedValue(face);
        styleSelector.setSelectedValue(styles[style]);
        sizeSelector.setSelectedValue(size + ""); //
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Object getValue() {
        return result;
    }
    
    public Object createFont() {
        int size = 16;

        try {
            size = Integer.parseInt((String) sizeSelector.getSelectedValue());
        } catch (NumberFormatException ex) {
            
        }

        return new Font((String) faceSelector.getSelectedValue(),
                        styleSelector.getSelectedIndex(), size);
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean showChooser(JComponent owner) {
        exitOK = false;
        result = null;
        JButton okButton = new JButton(App.messages.getString("res.3")); //
        JButton cancelButton = new JButton(App.messages.getString("res.4")); //
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitOK = true;
                shareInstance.hide();
                result = createFont();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shareInstance.hide();
               
            }
        });
        shareInstance = new JDialog((Frame) Main.getInstance() , App.messages.getString("res.22")); //
        shareInstance.setModal(true);

       
        CommandPanel commandPanel = CommandPanel.createPanel(false);
        commandPanel.addComponent(okButton);
        commandPanel.addComponent(cancelButton);
        
        if(this.empty)
        {
        	JButton emptyButton = new JButton(App.messages.getString("res.23")); //
        	emptyButton.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent e) {
                      exitOK = true;
                      shareInstance.hide();
                  }
              });
        	
        	commandPanel.addComponent(emptyButton);
        }
        
        
        shareInstance.getContentPane().add(this, BorderLayout.CENTER);
        shareInstance.getContentPane().add(commandPanel, BorderLayout.SOUTH);
        shareInstance.pack();
        shareInstance.setLocationRelativeTo(owner);
        shareInstance.show();

        return exitOK;
    }

    /**
    * DOCUMENT ME!
    */
    private void buildDialog() {
        Object[] faces = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                            .getAvailableFontFamilyNames();
        faceSelector = new ListEditor(faces, App.messages.getString("res.24"), false); //

        faceSelector.setPreferredSize(new Dimension(120, 10));

        styleSelector = new ListEditor(styles, App.messages.getString("res.25"), false); //
        styleSelector.setPreferredSize(new Dimension(90, 10));

        Object[] sizes = {
            "6", "8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "32", "48", // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
            "72" //
        };
        sizeSelector = new ListEditor(sizes, App.messages.getString("res.15"), true); //
        sizeSelector.setPreferredSize(new Dimension(60, 10));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0f;
        add(faceSelector, gbc);
        add(Box.createHorizontalStrut(5), gbc);
        add(styleSelector, gbc);
        add(Box.createHorizontalStrut(5), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(sizeSelector, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.0f;
        add(sampleLabel, gbc);
        sampleLabel.setBorder(BorderFactory.createEtchedBorder());
        sampleLabel.setPreferredSize(new Dimension(170, 70));
        sampleLabel.setHorizontalAlignment(JLabel.CENTER);
        faceSelector.addChangeListener(this);
        styleSelector.addChangeListener(this);
        sizeSelector.addChangeListener(this);

        this.setPreferredSize(new Dimension(300, 300));
    }

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv) {
        new FontChooser().showChooser(null);
    }

	/* (non-Javadoc)
	 * @see com.jatools.swing.ZChooser#showChooser(javax.swing.JComponent, com.jatools.core.ZReportDocument, java.lang.Object)
	 */
	public boolean showChooser(JComponent owner, Object value) {
		setValue(value);
		return showChooser(owner);
	
	}
}
