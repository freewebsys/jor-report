package jatools.designer.property.editor;

import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.variable.NodeSourceTree;
import jatools.swingx.Chooser;
import jatools.swingx.CommandPanel;
import jatools.swingx.SwingUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class NodePathPropertyEditor extends JDialog implements Chooser {
    private boolean done = false;
    private JTextField pathText = new JTextField();
    String result;
    private NodeSourceTree srcTree;

    /**
     * Creates a new BorderPropertyEditor object.
     */
    public NodePathPropertyEditor() {
        super(Main.getInstance(), App.messages.getString("res.357"), true);

        JPanel p = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;

        srcTree = new NodeSourceTree(false);
        srcTree.setToggleClickCount(0);
        srcTree.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("select.value".equals(evt.getPropertyName())) {
                        String newPath = (String) evt.getNewValue();

                        if (newPath.indexOf(".") == -1) {
                            pathText.setText(newPath);
                        }
                    }
                }
            });

        gbc.weighty = 100;
        p.add(new JScrollPane(srcTree), gbc);
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        p.add(new JLabel(App.messages.getString("res.358")), gbc);
        gbc.weightx = 100;
        p.add(pathText, gbc);

        SwingUtil.setBorder6(p);

        ActionListener oklistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;

                    result = pathText.getText();

                    if ((result != null) && (result.trim().length() == 0)) {
                        result = null;
                    }

                    hide();
                }
            };

        ActionListener cancellistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = false;
                    hide();
                }
            };

        CommandPanel bottomPanel = CommandPanel.createPanel(oklistener, cancellistener);
        bottomPanel.addComponent(App.messages.getString("res.23"),
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;
                    hide();
                }
            });

        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        this.getContentPane().add(p, BorderLayout.CENTER);

        setSize(new Dimension(500, 400));

   
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(JComponent owner, Object value) {
        this.done = false;
        this.result = null;
        this.pathText.setText((String) value);
        this.setLocationRelativeTo(owner);
        srcTree.setEditor(Main.getInstance().getActiveEditor());
        srcTree.expandTree();
        show();

        return this.done;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return result;
    }
}
