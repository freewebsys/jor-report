package jatools.designer;

import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.component.table.GridComponent;
import jatools.designer.layer.Layer;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.property.Selectable;
import jatools.designer.property.editor.NodePathPropertyEditor;
import jatools.designer.undo.PropertyEdit;
import jatools.engine.printer.NodePathTarget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class TagSelector extends JPanel implements ChangeListener, ActionListener, MouseListener {
    static Font f = new Font("Arial", 0, 11);
    static String SRC = "SOURCE.";
    private Selectable sel;
    private Object leaf;

    TagSelector() {
        this.setPreferredSize(new Dimension(1, 20));
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.addMouseListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param sel DOCUMENT ME!
     */
    public void setSelectable(Selectable sel) {
        if (this.sel != null) {
            this.sel.removeSelectionListener(this);
        }

        if (sel != null) {
            sel.addSelectionListener1(this);
        }

        this.sel = sel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (this.sel != null) {
            Object[] sels = this.sel.getSelection();

            if ((sels != null) && (sels.length > 0)) {
                updateSelector(sels[0]);
            }
        }
    }

    private void updateSelector(Object leaf) {
        ComponentPeer p = (ComponentPeer) leaf;

        if (!isAncestor(p, (ComponentPeer) this.leaf)) {
            this.removeAll();

            ButtonGroup g = new ButtonGroup();
            ComponentPeer[] path = p.getAncestorPath();

            for (int i = path.length - 1; i >= 0; i--) {
                String klass = path[i].getComponent().getClass().getName();
                klass = klass.substring(klass.lastIndexOf(".") + 1);

//                if (path[i].getComponent() instanceof NodePathTarget) {
//                    String p2 = ((NodePathTarget) path[i].getComponent()).getNodePath();
//
//                    if (p2 != null) {
//                        klass += (":" + p2);
//                    }
//                }

                JToggleButton label = new JToggleButton("<" + klass + ">");
                
                
                
                label.setFont(f);
                label.setBorder(null);
                label.setMaximumSize(new Dimension(200, 20));
                label.putClientProperty(SRC, path[i]);
                label.addActionListener(this);
                add(label);
                g.add(label);
                label.setRolloverEnabled(true);
                label.setFocusPainted(false);

                if (i == 0) {
                    label.setSelected(true);
                }

                label.addMouseListener(this);
            }

            this.leaf = leaf;

            // this.invalidate();
            //   this.getParent().getLayout().layoutContainer(this.getParent());
            //     this.getParent().validate();
            this.revalidate();
            this.repaint();
        } else {
            for (int i = 0; i < this.getComponentCount(); i++) {
                JToggleButton b = (JToggleButton) this.getComponent(i);
                ComponentPeer peer = (ComponentPeer) b.getClientProperty(SRC);

                if (peer.isSelected()) {
                    b.setSelected(true);

                    return;
                }
            }
        }
    }

    private static boolean isAncestor(ComponentPeer an, ComponentPeer desc) {
        while (desc != null) {
            if (an == desc) {
                return true;
            }

            desc = desc.getParent();
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (this.sel != null) {
            JToggleButton b = (JToggleButton) e.getSource();
            ComponentPeer p = (ComponentPeer) b.getClientProperty(SRC);

            ((ReportPanel) this.sel).unselectAll();
            ((ReportPanel) this.sel).select(p);

            if ((p.getComponent().getCell() == null) || p.getComponent() instanceof GridComponent) {
                Layer layer = ((ReportPanel) this.sel).getGroupPanel().getActiveLayer();

                if (layer != null) {
                    layer.setFocused(false);
                }
            } else if ((p.getComponent().getCell() != null) &&
                    !(p.getComponent() instanceof GridComponent)) {
                Layer layer = ((ReportPanel) this.sel).getGroupPanel().getActiveLayer();

                if (layer != null) {
                    layer.setFocused(true);
                }
            }

            ((ReportPanel) this.sel).repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
            if (e.getSource() == this) {
                Component split = this.getParent();

                while (!"EditorPanel".equals(split.getName())) {
                    split = split.getParent();
                }

                ((EditorPanel) split).toggleSplit();
            } else {
                JToggleButton b = (JToggleButton) e.getSource();
                ComponentPeer p = (ComponentPeer) b.getClientProperty(SRC);
                PropertyDescriptor[] props = p.getComponent().getRegistrableProperties();
                boolean path = false;

                if (props != null) {
                    for (int i = 0; i < props.length; i++) {
                        if (props[i] == ComponentConstants._NODE_PATH) {
                            path = true;

                            break;
                        }
                    }
                }

                if (path) {
                    NodePathPropertyEditor editor = new NodePathPropertyEditor();
                    String oldVal = ((NodePathTarget) p.getComponent()).getNodePath();

                    if (editor.showChooser((JComponent) Main.getInstance().getContentPane(), oldVal)) {
                        String newVal = (String) editor.getValue();

                        ((NodePathTarget) p.getComponent()).setNodePath(newVal);

                        p.getOwner()
                         .addEdit(new PropertyEdit(p, ComponentConstants.PROPERTY_NODE_PATH,
                                oldVal, newVal));
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }
}
