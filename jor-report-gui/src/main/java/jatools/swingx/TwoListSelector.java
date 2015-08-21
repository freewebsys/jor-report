package jatools.swingx;

import jatools.designer.App;
import jatools.swingx.dnd.DNDManager;
import jatools.swingx.wizard.WizardCellEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.3 $
 * @author $author$
 */

public class TwoListSelector extends JPanel implements WizardCellEditor {
	protected JListX source;
	protected JListX target;
	protected ArrayList lastSelection = new ArrayList();
	protected String errorMessage = App.messages.getString("res.36"); //

	/**
	 * Creates a new ZTwoListSelector object.
	 */
	public TwoListSelector(String sourceTitle, String targetTitle,
			String errorMessage, JListX source, JListX target) {
		this(sourceTitle, targetTitle, errorMessage, source, target,
				MoveController.FULL_OPTIONS);

	}
	public TwoListSelector(String sourceTitle, String targetTitle,
			String errorMessage, JListX source, JListX target, int commandOptions) {
		this.source = (source != null) ? source : new JListX();
		this.target = (target != null) ? target : new JListX();

		this.errorMessage = errorMessage;
		buildUI(sourceTitle, targetTitle, commandOptions);
	}
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Component getEditorComponent() {
		return this;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param listData
	 *            DOCUMENT ME!
	 */
	public void setSelectableList(ArrayList listData) {
		source.setItems(listData);
		target.clearItems();
		lastSelection.clear();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isChanged() {
		ArrayList nowSelection = target.getItems();

		return !(nowSelection.size() == lastSelection.size() && nowSelection
				.containsAll(lastSelection));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void checkAvailable() throws Exception {
		if (target.getItems().isEmpty()) {
			throw new Exception(errorMessage);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public Object getContent() throws Exception {
		return target.getItems();
	}

	/**
	 * DOCUMENT ME!
	 */
	public void applyChange() {
		lastSelection = target.getItems();
	}

	/**
	 * DOCUMENT ME!
	 */
	private void buildUI(String sourceTitle, String targetTitle,
			int commandOptions) {
		this.setLayout(new GridLayout(1, 3));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JScrollPane sp = new JScrollPane(source);
		JPanel leftPane = getLeftPanel();
		leftPane.add(sp, BorderLayout.CENTER);
		if (sourceTitle != null)
			leftPane.add(new JLabel(sourceTitle), BorderLayout.NORTH);

		JPanel controlPane = new MoveController(source, target, commandOptions);

		sp = new JScrollPane(target);

		JPanel rightPane = new JPanel(new BorderLayout());
		rightPane.add(sp, BorderLayout.CENTER);
		if (targetTitle != null)
			rightPane.add(new JLabel(targetTitle), BorderLayout.NORTH);

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.weightx = 0.5f;
		gbc.weighty = 1.0f;
		gbc.fill = GridBagConstraints.BOTH;
		add(leftPane, gbc);

		gbc.weightx = 0.1f;
		gbc.fill = GridBagConstraints.BOTH;
		add(controlPane, gbc);

		gbc.weightx = 0.5f;
		gbc.fill = GridBagConstraints.BOTH;
		add(rightPane, gbc);

		leftPane.setPreferredSize(new Dimension(300, 200));
		rightPane.setPreferredSize(leftPane.getPreferredSize());

		DNDManager manager = new DNDManager();

		manager.registerSource(source);
		manager.registerTarget(source);
		manager.registerSource(target);
		manager.registerTarget(target);

		target.setDoublClickTo(source);
		source.setDoublClickTo(target);
	}
	protected JPanel getLeftPanel() {
		JPanel leftPane = new JPanel(new BorderLayout());
		return leftPane;
	}
}
