package jatools.designer.variable;

import jatools.designer.App;

import jatools.dom.src.ArrayNodeSource;
import jatools.dom.src.CrossIndexNodeSource;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.IndexNodeSource;

import jatools.util.Util;

import org.apache.commons.lang.ArrayUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class NodeSourceTreeRenderer extends JPanel implements TreeCellRenderer {
    static final Object NULL = new Object();
    static final String NODE_LABEL = "NODELabel";
    static Icon root_icon = Util.getIcon("/jatools/icons/datatree/root.gif");
    static Icon dataset_icon = Util.getIcon("/jatools/icons/datatree/dataset.gif");
    static Icon index_icon = Util.getIcon("/jatools/icons/datatree/index.gif");
    static Icon cross_icon = Util.getIcon("/jatools/icons/datatree/cross.gif");
    static Icon row_icon = Util.getIcon("/jatools/icons/datatree/row.gif");
    static Icon group_icon = Util.getIcon("/jatools/icons/datatree/group.gif");
    static Icon xml_icon = Util.getIcon("/jatools/icons/datatree/xml.gif");
    static Icon array_icon = Util.getIcon("/jatools/icons/datatree/array.gif");
    static Color selectedBackGround = UIManager.getColor("Tree.selectionBackground");
    static final String NODE = "node";
    static final Color labelColor = UIManager.getColor("TableHeader.background");
    static final Border labelBorder = UIManager.getBorder("TableHeader.cellBorder");

    /**
     * Creates a new SourceTreeRenderer object.
     */
    public NodeSourceTreeRenderer() {
        super();

        this.setOpaque(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tree DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param selected DOCUMENT ME!
     * @param expanded DOCUMENT ME!
     * @param leaf DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param hasFocus DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
        boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.removeAll();

        if (value != null) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
            Object nodeValue = treeNode.getUserObject();

            setBackground(Color.white);

            if (nodeValue instanceof Boolean) {
                return new JLabel(App.messages.getString("res.258"));
            }

            if (nodeValue instanceof TreeNodeValue) {
                TreeNodeValue tnv = (TreeNodeValue) nodeValue;
                putClientProperty(NODE, tnv);

                int type = tnv.getSourceType();

                IconPanel nodeLabel = null;

                switch (type) {
                case TreeNodeValue.ROOT_NODE_SOURCE:
                    setLayout(new BorderLayout());
                    nodeLabel = new IconPanel(tnv.toString(), root_icon);

                    add(nodeLabel, BorderLayout.WEST);

                    break;

                case TreeNodeValue.CROSS_DATASET_NODE_SOURCE:
                    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

                    CrossIndexNodeSource crossIndexDataset = (CrossIndexNodeSource) tnv.getNodeSource();
                    String[] index = crossIndexDataset.getIndexFields();
                    String[] index2 = crossIndexDataset.getIndexFields2();
                    String tagName = crossIndexDataset.getTagName();
                    nodeLabel = new IconPanel(tagName, cross_icon);

                    add(nodeLabel);

                    add(new JLabel("{"));

                    for (int i = 0; i < index.length; i++) {
                        String field = index[i];
                        boolean sel = selected && tnv.isSelected(field);
                        Component button = createFieldButton(field, sel);
                        add(button);
                    }

                    add(new JLabel("}"));

                    add(new JLabel("{"));

                    for (int i = 0; i < index2.length; i++) {
                        String field = index2[i];
                        boolean sel = selected && tnv.isSelected(field);
                        Component button = createFieldButton(field, sel);
                        add(button);
                    }

                    add(new JLabel("}"));

                    break;

                case TreeNodeValue.INDEX_DATASET_NODE_SOURCE:
                    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

                    IndexNodeSource indexDataset = (IndexNodeSource) tnv.getNodeSource();
                    index = indexDataset.getIndexFields();

                    tagName = indexDataset.getTagName();
                    nodeLabel = new IconPanel(tagName, index_icon);

                    add(nodeLabel);

                    add(new JLabel("{"));

                    for (int i = 0; i < index.length; i++) {
                        String field = index[i];
                        boolean sel = selected && tnv.isSelected(field);
                        Component button = createFieldButton(field, sel);
                        add(button);
                    }

                    add(new JLabel("}"));

                    break;

                case TreeNodeValue.DATASET_NODE_SOURCE: {
                    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

                    DatasetNodeSource dataSource = (DatasetNodeSource) tnv.getNodeSource();

                    String[] fields = ((NodeSourceTree) tree).getColumns(treeNode);
                    index = dataSource.getIndexFields();
                    index2 = dataSource.getIndexFields2();

                    tagName = dataSource.getTagName();
                    nodeLabel = new IconPanel(tagName, dataset_icon);

                    add(nodeLabel);

                    if (fields != null) {
                        boolean[] indexed = new boolean[fields.length];

                        if ((index != null) && (index.length > 0)) {
                            add(new JLabel("{"));

                            for (int i = 0; i < index.length; i++) {
                                String field = index[i];
                                boolean sel = selected && tnv.isSelected(field);
                                Component button = createFieldButton(field, sel);
                                add(button);

                                int pos = ArrayUtils.indexOf(fields, index[i]);

                                if (pos != -1) {
                                    indexed[pos] = true;
                                }
                            }

                            add(new JLabel("}"));

                            if ((index2 != null) && (index2.length > 0)) {
                                add(new JLabel("{"));

                                for (int i = 0; i < index2.length; i++) {
                                    String field = index2[i];
                                    boolean sel = selected && tnv.isSelected(field);
                                    Component button = createFieldButton(field, sel);
                                    add(button);

                                    int pos = ArrayUtils.indexOf(fields, index2[i]);

                                    if (pos != -1) {
                                        indexed[pos] = true;
                                    }
                                }

                                add(new JLabel("}"));
                            }
                        }

                        for (int i = 0; i < fields.length; i++) {
                            if (!indexed[i]) {
                                String field = fields[i];
                                boolean sel = selected && tnv.isSelected(field);
                                Component button = createFieldButton(field, sel);
                                add(button);
                            }
                        }
                    }

                    break;
                }

                case TreeNodeValue.GROUP_NODE_SOURCE:
                    setLayout(new BorderLayout());
                    nodeLabel = new IconPanel(tnv.toString(), group_icon);

                    add(nodeLabel, BorderLayout.WEST);

                    break;

                case TreeNodeValue.ROW_NODE_SOURCE:
                    setLayout(new BorderLayout());
                    nodeLabel = new IconPanel(tnv.toString(), row_icon);
                    add(nodeLabel, BorderLayout.WEST);

                    break;

                case TreeNodeValue.XML_NODE_SOURCE:
                    setLayout(new BorderLayout());
                    nodeLabel = new IconPanel(tnv.toString(), xml_icon);
                    add(nodeLabel, BorderLayout.WEST);

                    break;

                case TreeNodeValue.ARRAY_NODE_SOURCE:

                    ArrayNodeSource arrayNodeSource = (ArrayNodeSource) tnv.getNodeSource();
                    String[] v = arrayNodeSource.getFields();
                    setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    nodeLabel = new IconPanel(tnv.toString(), array_icon);
                    add(nodeLabel);

                    if (v != null) {
                        for (int i = 0; i < v.length; i++) {
                            String field = v[i].toString();
                            boolean sel = selected && tnv.isSelected(field);
                            Component button = createFieldButton(field, sel);
                            add(button);
                        }
                    }

                    break;
                }

                if (nodeLabel != null) {
                    nodeLabel.setName(NODE_LABEL);

                    if (selected && tnv.isLastPressedNodeSource()) {
                        nodeLabel.setBackground(selectedBackGround);
                        nodeLabel.setForeground(Color.white);
                    }
                }
            } else {
                this.putClientProperty(NODE, NULL);
            }
        }

        return this;
    }

    private Component createFieldButton(String name, boolean selected) {
        JLabel label = new JLabel(name);
        label.setName(name);
        label.setOpaque(true);

        label.setFont(new Font("宋体", Font.PLAIN, 12));
        label.setBorder(labelBorder);

        if (selected) {
            label.setBackground(selectedBackGround);
            label.setForeground(Color.white);
        } else {
            label.setBackground(labelColor);
        }

        return label;
    }

    /**
     * DOCUMENT ME!
     */
    public void validate() {
        super.validate();

        TreeNodeValue tnv = (TreeNodeValue) this.getClientProperty(NODE);

        if (tnv != null) {
            Map<String, Rectangle> rects = new HashMap<String, Rectangle>();

            Component[] components = this.getComponents();

            for (Component c : components) {
                if (c.getName() != null) {
                    rects.put(c.getName(), c.getBounds());
                }
            }

            tnv.setFieldRects(rects);
        }
    }

    class IconPanel extends JPanel {
        JLabel iconLabel;
        JLabel textLabel;

        IconPanel(String text, Icon icon) {
            super();
            this.setLayout(new BorderLayout(0, 0));
            iconLabel = new JLabel(icon, JLabel.LEFT);

            textLabel = new JLabel(text, JLabel.CENTER) {
                        public Dimension getPreferredSize() {
                            Dimension p = super.getPreferredSize();
                            p.width = Math.max(30, p.width);

                            return p;
                        }
                    };

            textLabel.setFont(new Font(App.messages.getString("res.21"), Font.PLAIN, 12));
            textLabel.setOpaque(true);
            textLabel.setBackground(Color.white);
            this.add(iconLabel, BorderLayout.WEST);
            this.add(textLabel, BorderLayout.CENTER);
        }

        public void setBackground(Color c) {
            super.setBackground(Color.white);

            if (textLabel != null) {
                textLabel.setBackground(c);
            }
        }

        public void setForeground(Color c) {
            super.setForeground(c);

            if (textLabel != null) {
                textLabel.setForeground(c);
            }
        }
    }
}
