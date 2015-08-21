package jatools.designer.property.editor;
import jatools.accessor.PropertyDescriptor;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.formatter.DateFormat;
import jatools.formatter.DecimalFormat;
import jatools.formatter.Format2;
import jatools.swingx.Chooser;
import jatools.swingx.ListEditor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author   java9
 */
public class FormatChooser extends JDialog implements Chooser, ActionListener {
	static final int NULL = 0;
	static final int FIXED = 1;
	static final int CURRENCY = 2;
	static final int PERCENTIGE = 3;
	static final int SCIENTIFIC = 4;
	static final int DATE = 5;
	static final int TIME = 6;

	
	static final String NULL_CARD = "null"; //
	static final String DECIMAL_CARD = "decimal"; //
	static final String DATE_CARD = "date"; //
	static final String TIME_CARD = "time"; //

	
	static final String SELECTABLE = "selectable"; //
	static String[] patterns = new String[]{
			"0", // //
			"0.0", // //
			"0.00", // //
			"0.000", // //
			"0.0000", // //
			"#,##0", // //
			"#,##0.0", // //
			"#,##0.00", // //
			"#,##0.000", // //
			"#,##0.0000", // //
			null, 
			App.messages.getString("res.334"), // //
			App.messages.getString("res.335"), // //
			App.messages.getString("res.336"), // //
			App.messages.getString("res.337"), // //
			App.messages.getString("res.338"), // //
			App.messages.getString("res.339"), // //
			App.messages.getString("res.340"), // //
			App.messages.getString("res.341"), // //
			App.messages.getString("res.342"), // //
			App.messages.getString("res.343"), // //
			null, 
			"0%", // //
			"0.0%", // //
			"0.00%", // //
			"0.000%", // //
			"0.0000%", // //
			null, 
			"0.##E0", "0.00E0", null}; // //$NON-NLS-2$
	static Format2 NULL_FORMAT = new Format2() {
		public String format(Object o) {
			return ""; //
		}

		public PropertyDescriptor[] getRegistrableProperties() {
			return new PropertyDescriptor[0];
		}

		public boolean equals(Object obj) {
			return (obj == null);
		}

		public String toString() {
			return App.messages.getString("res.344"); //
		}

		public Object clone() {
			return this;
		}

		public String toExcel() {
			// TODO Auto-generated method stub
			return null;
		}

		public String toPattern() {
			// TODO Auto-generated method stub
			return null;
		}

		
	};

	static Format2[][] formats;
	boolean exitOK = false;
	ListEditor patternSelector;
	CardLayout card;
	JPanel preview;
	ZFormatPreview activePreview;
	JRadioButton[] options;
	private FormatChooser instance;

	/**
	 * Creates a new ZFormatChooser object.
	 */
	public FormatChooser(Frame owner, boolean needui) {
		super(owner, App.messages.getString("res.170"), true); //
		if (needui) {
			buildUI();
			instance = this;
		}
	}

	public FormatChooser(Frame owner) {
		this(owner, true);

	}


	//
	public void actionPerformed(ActionEvent e) {
		
		int i = Integer.parseInt(e.getActionCommand());
		String cardId = null;

		switch (i) {
			case NULL :
				cardId = NULL_CARD;

				break;

			case FIXED :
			case CURRENCY :
			case PERCENTIGE :
			case SCIENTIFIC :
				cardId = DECIMAL_CARD;

				break;

			case DATE :
				cardId = DATE_CARD;

				break;

			case TIME :
				cardId = TIME_CARD;

				break;
		}

		card.show(preview, cardId);

		Format2[] formats = (Format2[]) ((JComponent) e.getSource())
				.getClientProperty(SELECTABLE);
		boolean nullFormat = e.getSource() == options[0];
		patternSelector.setSelectable(formats);
		preview.setEnabled(!nullFormat);
		patternSelector.setEnabled(!nullFormat);
	}

	Format2[][] getFormats() {
		if (formats == null) {
			formats = new Format2[TIME + 1][];

			
			int j = 0;
			formats[j] = new Format2[]{NULL_FORMAT};
			j++;

			
			ArrayList tmp = new ArrayList();

			for (int i = 0; i < patterns.length; i++) {
				if (patterns[i] != null) {
					tmp.add(new DecimalFormat(patterns[i]));
				}
				else {
					formats[j] = (Format2[]) tmp.toArray(new Format2[0]);
					tmp.clear();
					j++;
				}
			}

			
			for (int i = 0; i < 4; i++) {
				tmp.add(new DateFormat(DateFormat.DATE, i));
			}

			formats[j] = (Format2[]) tmp.toArray(new Format2[0]);
			tmp.clear();
			j++;

			for (int i = 0; i < 4; i++) {
				tmp.add(new DateFormat(DateFormat.TIME, i));
			}

			formats[j] = (Format2[]) tmp.toArray(new Format2[0]);
		}

		return formats;
	}

	/**
	 * DOCUMENT ME!
	 */
	private void buildUI() {
		patternSelector = new ListEditor(new Object[]{NULL_FORMAT}, App.messages.getString("res.345"), false); //
		card = new CardLayout();
		preview = new JPanel(card);

		options = new JRadioButton[]{
				new JRadioButton(App.messages.getString("res.346")), //
				new JRadioButton(App.messages.getString("res.347")), //
				new JRadioButton(App.messages.getString("res.348")), //
				new JRadioButton(App.messages.getString("res.349")), //
				new JRadioButton(App.messages.getString("res.350")), //
				new JRadioButton(App.messages.getString("res.16")), //
				new JRadioButton(App.messages.getString("res.351"))}; //
		JPanel optionPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;

		ButtonGroup group = new ButtonGroup();

		Format2[][] formats = getFormats();

		for (int i = 0; i < options.length; i++) {
			
			options[i].putClientProperty(SELECTABLE, formats[i]);
			options[i].setActionCommand(i + ""); //
			options[i].addActionListener(this);
			optionPane.add(options[i], gbc);
			group.add(options[i]);
		}

		
		JPanel content = new JPanel(new BorderLayout());
		preview.setPreferredSize(new Dimension(10, 70));
		preview.setBorder(BorderFactory.createTitledBorder(App.messages.getString("res.187"))); //
		patternSelector.setBorder(BorderFactory
				.createEmptyBorder(10, 10, 10, 0));
		patternSelector.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int i = patternSelector.getSelectedIndex();
				selectFormat((Format2) patternSelector.getElementAt(i));
			}
		});

		content.add(optionPane, BorderLayout.WEST);
		content.add(patternSelector, BorderLayout.CENTER);

		
		preview.add(NULL_CARD, new ZNullPreview());
		preview.add(DECIMAL_CARD, new ZDecimalPreview());
		preview.add(DATE_CARD, new ZDatePreview());
		preview.add(TIME_CARD, new ZTimePreview());

		///
		content.add(preview, BorderLayout.SOUTH);

		JPanel commandPane = new JPanel(
				new FlowLayout(FlowLayout.RIGHT, 10, 10));
		JButton ok = new JButton(App.messages.getString("res.352")); //
		commandPane.add(ok);

		JButton cancel = new JButton(App.messages.getString("res.4")); //
		commandPane.add(cancel);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitOK = true;
				instance.hide();
			}
		});

		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.hide();
			}
		});

		Border border = BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(10, 10, 10, 10), BorderFactory
				.createEtchedBorder());

		border = BorderFactory.createCompoundBorder(border, BorderFactory
				.createEmptyBorder(10, 10, 5, 10));

		content.setBorder(border);
		getContentPane().add(content, BorderLayout.CENTER);
		getContentPane().add(commandPane, BorderLayout.SOUTH);
		options[0].doClick();
		pack();
		setSize(new Dimension(400, 390));
	}

	private void selectFormat(Format2 format) {
		if (format == null) {
			return;
		}

		
		Component[] c = preview.getComponents();

		for (int i = 0; i < c.length; i++) {
			if (c[i].isVisible()) {
				((ZFormatPreview) c[i]).setFormat(format);

				return;
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getValue() {
		Object value = patternSelector.getElementAt(patternSelector
				.getSelectedIndex());

		if (value == NULL_FORMAT) {
			return null;
		}
		else {
			return value;
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param value
	 *            DOCUMENT ME!
	 */
	public void setValue(Object value) {
		Format2[][] formats = getFormats();

		for (int i = 0; i < formats.length; i++) {
			for (int j = 0; j < formats[i].length; j++) {
				if (formats[i][j].equals(value)) {
					options[i].doClick();
					patternSelector.setSelectedValue(formats[i][j]);

					return;
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean showChooser(JComponent owner) {
		exitOK = false;
		setLocationRelativeTo(owner);
		show();

		return exitOK;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jatools.swing.ZChooser#showChooser(javax.swing.JComponent,
	 *      com.jatools.core.ZReportDocument, java.lang.Object)
	 */
	public boolean showChooser(JComponent owner, Object value) {
		instance = new FormatChooser(Main.getInstance() ,false);

		setValue(value);
		exitOK = false;
		instance.setContentPane(this.getContentPane());
		instance.pack();
		instance.setSize(new Dimension(400, 390));
		instance.setLocationRelativeTo(owner);
		instance.show();
		this.setContentPane(instance.getContentPane());
		return exitOK;

	}
}

/**
 * @author   java9
 */
abstract class ZFormatPreview extends JPanel {
	Format2 format;
	JLabel preview = new JLabel("12345890..14456"); //

	/**
	 * Creates a new ZFormatPreview object.
	 */
	ZFormatPreview() {
		preview.setPreferredSize(new Dimension(150, 25));

		JComponent editor = this.getEditor();
		editor.setPreferredSize(preview.getPreferredSize());
		add(editor);
		add(preview);
	}

	/**
	 * DOCUMENT ME!
	 * @param f   DOCUMENT ME!
	 * @uml.property   name="format"
	 */
	public void setFormat(Format2 f) {
		this.format = f;
		format();
	}

	/**
	 * DOCUMENT ME!
	 */
	public void format() {
		Object value = getValue();

		if (value != null) {
			preview.setText(format.format(value));
		}
		else {
			preview.setText(""); //
		}
	}

	/**
	 * DOCUMENT ME!
	 * @return   DOCUMENT ME!
	 * @uml.property   name="format"
	 */
	public Format2 getFormat() {
		return format;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	abstract public Object getValue();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	abstract public JComponent getEditor();
}

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.6 $
 * @author $author$
 */
class ZDecimalPreview extends ZFormatPreview {
	JTextField text;

	/**
	 * Creates a new ZDecimalPreview object.
	 */
	ZDecimalPreview() {
		text.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				format();
			}
		});
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JComponent getEditor() {
		text = new JTextField("1234567.89"); //

		return text;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getValue() {
		return new Double(text.getText());
	}
}

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.6 $
 * @author $author$
 */
class ZDatePreview extends ZFormatPreview {
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getValue() {
		return new Date();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JComponent getEditor() {
		return new JLabel(App.messages.getString("res.353")); //
	}
}

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.6 $
 * @author $author$
 */
class ZTimePreview extends ZFormatPreview {
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getValue() {
		return new Date();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JComponent getEditor() {
		return new JLabel(App.messages.getString("res.354")); //
	}
}

/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.6 $
 * @author $author$
 */
class ZNullPreview extends ZFormatPreview {
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getValue() {
		return null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public JComponent getEditor() {
		return new JLabel(App.messages.getString("res.355")); //
	}
}
