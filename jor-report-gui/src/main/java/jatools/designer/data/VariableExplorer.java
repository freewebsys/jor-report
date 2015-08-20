package jatools.designer.data;


import jatools.designer.App;
import jatools.designer.Main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class VariableExplorer extends JDialog {
    public final static int CANCEL = 0;
    public final static int OK = 1;
    public final static int NULL = 2;
    public int xok = CANCEL;
    VariableTree variableTree = new VariableTree();
    private JButton okCommand;

    /**
     * Creates a new VariableExplorer object.
     *
     * @param owner DOCUMENT ME!
     * @param title DOCUMENT ME!
     */
    public VariableExplorer(Frame owner, String title) {
        super(owner, title, true);
        buildUI(owner);
        variableTree.setModel(Main.getInstance().getActiveEditor().getVariableModel());
        setSize(new Dimension(380, 450));
        setLocationRelativeTo(owner);
    }

    private void buildUI(Frame owner) {
        JPanel wrapper = new JPanel(new GridLayout());
        wrapper.add(variableTree);

        variableTree.setOwner(owner);
        variableTree.setDoubleClickAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        variableTree.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    okCommand.setEnabled(variableTree.isSettable());
                }
            });

        JButton newCommand = new JButton(variableTree.getAddAction());
        JButton editCommand = new JButton(variableTree.getEditAction());
        JButton deleteCommand = new JButton(variableTree.getDeleteAction());

        okCommand = new JButton(App.messages.getString("res.3"));
        okCommand.setEnabled(false);

        JButton cancelCommand = new JButton(App.messages.getString("res.4"));

        JButton applyNull = new JButton(App.messages.getString("res.287"));

        applyNull.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    nullApply();
                }
            });

        newCommand.setPreferredSize(new Dimension(75, 25));
        editCommand.setPreferredSize(newCommand.getPreferredSize());
        deleteCommand.setPreferredSize(newCommand.getPreferredSize());
        okCommand.setPreferredSize(newCommand.getPreferredSize());
        cancelCommand.setPreferredSize(newCommand.getPreferredSize());
        applyNull.setPreferredSize(newCommand.getPreferredSize());
        okCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        cancelCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();
                }
            });

        JPanel commandPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        Component strut = Box.createVerticalStrut(150);

        commandPanel.add(strut, gbc);

        gbc.insets = new Insets(2, 20, 2, 20);

        commandPanel.add(newCommand, gbc);
        commandPanel.add(editCommand, gbc);
        commandPanel.add(deleteCommand, gbc);
        gbc.insets = new Insets(20, 20, 2, 20);
        commandPanel.add(okCommand, gbc);

        gbc.insets = new Insets(2, 20, 2, 20);

        commandPanel.add(applyNull, gbc);
        commandPanel.add(cancelCommand, gbc);

        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 0));
        getContentPane().add(wrapper, BorderLayout.CENTER);
        getContentPane().add(commandPanel, BorderLayout.EAST);

        this.addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    variableTree.scrollSelectedRowToVisible();
                }
            });
    }

    protected void done() {
        if (variableTree.isSettable()) {
            xok = OK;

            hide();
        }
    }

    protected void nullApply() {
        xok = NULL;
        hide();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVariable() {
        return variableTree.getVariable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     */
    public void setVariable(String var) {
        variableTree.setVariable(var);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        JDialog d = new JDialog();
        d.getContentPane().add(new _Label(), BorderLayout.CENTER);
        d.show();
    }
}


class _Label extends JLabel {
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        int step = 4;
        int count = 5;

        int size = step * count;

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D biGraphics = image.createGraphics();
        biGraphics.setPaint(Color.black);

        int x = step;

        for (int i = 0; i < (count * 2); i++) {
            biGraphics.drawLine(0, x, x, 0);
            x += step;
        }

        Rectangle rect = new Rectangle(0, 0, size, size);
        TexturePaint paint = new TexturePaint(image, rect);
        ((Graphics2D) g).setPaint(paint);

        ((Graphics2D) g).setStroke(new BasicStroke(6));

        g.drawRect(10, 10, this.getWidth() - 20, this.getHeight() - 20);
    }
}
