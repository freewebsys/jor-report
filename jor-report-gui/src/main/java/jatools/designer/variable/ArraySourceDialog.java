package jatools.designer.variable;

import jatools.designer.Main;
import jatools.dom.src.ArrayNodeSource;
import jatools.swingx.CommandPanel;
import jatools.swingx.SwingUtil;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class ArraySourceDialog extends JDialog implements ActionListener {
    //    static final String NEWACTION = "new";
    //static final String INSERTACTION = "insert";

    //  static final String VALIDATEACTION="validate";
    static final Icon icon = Util.getIcon("/jatools/icons/class.gif");
    static final String OKACTION = "ok";
    static final String CANCELACTION = "cancel";

    //    static final String BEANACTION = "bean";
    private static ArrayNodeSource nodeSource;
    private static boolean exitedOk;
    private JTextField tagField;
    private JTextArea expressionArea;
    private JTextField elementClass;
    private Component c;
    ActionListener okListener;
    ActionListener cancelListener;

    /**
     * Creates a new ArraySourceDialog object.
     *
     * @param c DOCUMENT ME!
     */
    public ArraySourceDialog(Component c) {
        super((Frame) javax.swing.SwingUtilities.getWindowAncestor(c));
        this.setTitle("java数组定义");
        this.c = c;
        exitedOk = false;
        initUI();
        setModal(true);
        setSize(450, 420);

        //        if (c != null) {
        this.setLocationRelativeTo(Main.getInstance());

        //        } else {
        //            setLocation(250, 250);
        //        }
    }

    private Window getRootContainer(Component c) {
        if (c != null) {
            if (c.getParent() instanceof Window) {
                return (Window) c.getParent();
            } else {
                return getRootContainer(c);
            }
        }

        return null;
    }

    private void setSource(ArrayNodeSource source) {
        this.nodeSource = source;

        if (nodeSource != null) {
            tagField.setText(source.getTagName());
            expressionArea.setText(source.getExpression());
            elementClass.setText(source.getElementClass());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayNodeSource getSource(ArrayNodeSource source, Component c) {
        ArraySourceDialog dialog = new ArraySourceDialog(c);
        dialog.setSource(source);
        dialog.setVisible(true);

        if (exitedOk) {
            return nodeSource;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     */
    public void initUI() {
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(center(), BorderLayout.CENTER);
        this.getContentPane().add(south2(), BorderLayout.SOUTH);
    }

    private JPanel center() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));

        Box tagBox = Box.createHorizontalBox();
        tagBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel tagLabel = new JLabel("名称：");
        tagField = new JTextField();
        tagField.setPreferredSize(new Dimension(150, 25));
        tagField.setMaximumSize(new Dimension(150, 25));
        tagBox.add(tagLabel);
        tagBox.add(tagField);

        panel.add(tagBox, BorderLayout.NORTH);
        panel.add(editor(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel editor() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel west = new JPanel(new BorderLayout());
        JPanel center = new JPanel(new BorderLayout());
        west.add(new JLabel("公式："), BorderLayout.NORTH);
        //        panel.setBorder(BorderFactory.createTitledBorder(null, "数组公式编辑", TitledBorder.CENTER,
        //                TitledBorder.CENTER));

        //        JToolBar bar = new JToolBar();
        //        bar.add(createButton(Util.getIcon("/com/jatools/icons/new.gif"), "新建", NEWACTION));
        //        bar.add(createButton(Util.getIcon("/com/jatools/icons/new.gif"), "插入常用数组", INSERTACTION));
        //     //   bar.add(createButton(Util.getIcon("/com/jatools/icons/new.gif"), "合法性检测", VALIDATEACTION));
        //        bar.add(createButton(Util.getIcon("/com/jatools/icons/new.gif"), "高级", BEANACTION));
        expressionArea = new JTextArea();

        JScrollPane pane = new JScrollPane(expressionArea);

        //       bar.setFloatable(false);
        //     panel.add(bar, BorderLayout.NORTH);
        center.add(pane, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = gbc.BOTH;

        gbc.gridwidth = gbc.REMAINDER;

        south.add(new JLabel("返回的对象类型可以为java数组,集合,Iterator,ResultSet,TableModel对象."), gbc);
        south.add(Box.createVerticalStrut(30), gbc);

        south.add(new JLabel("数组元素的javaBean路径(用于自动抽取javaBean的属性列表):"), gbc);
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        elementClass = new JTextField();
        south.add(elementClass, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = gbc.REMAINDER;

        JButton b = new JButton(icon);
        b.setFocusPainted(false);
        b.setMargin(null);
        SwingUtil.setSize(b, new Dimension(23, 23));

        south.add(b, gbc);

        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    findClass();
                }
            });

        south.add(Box.createVerticalStrut(30), gbc);

        center.add(south, BorderLayout.SOUTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(west, BorderLayout.WEST);

        JPanel p = new JPanel(new BorderLayout());
        p.add(panel, BorderLayout.CENTER);

        return p;
    }

    protected void findClass() {
        String cls = new ClassSelectDialog(this).getSelectedClass();

        if (cls != null) {
            this.elementClass.setText(cls);
        }
    }

    private CommandPanel south2() {
        okListener = new OKActionListener();
        cancelListener = new CancelActionListener();

        CommandPanel cp = CommandPanel.createPanel(okListener, cancelListener);

        return cp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(OKACTION)) {
            if ((tagField.getText() == null) || tagField.getText().trim().equals("")) {
                JOptionPane.showConfirmDialog(this, "名称不能为空！", "错误提示", JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE);

                return;
            }

            String cls = ((this.elementClass.getText() != null) &&
                (this.elementClass.getText().trim().length() > 0)) ? this.elementClass.getText()
                                                                   : null;

            if (nodeSource == null) {
                nodeSource = new ArrayNodeSource(tagField.getText(), expressionArea.getText(), cls);
            } else {
                nodeSource.setTagName(tagField.getText());
                nodeSource.setExpression(expressionArea.getText());
                nodeSource.setElementClass(cls);
            }

            exitedOk = true;
            dispose();
        } else if (e.getActionCommand().equals(CANCELACTION)) {
            exitedOk = false;
            dispose();
        } /* else if (e.getActionCommand().equals(NEWACTION)) {
        expressionArea.setText("");
        }
        else if (e.getActionCommand().equals(INSERTACTION)) {
        ArrayPopop ap = new ArrayPopop();
        
        ap.show((JButton) e.getSource(), 0, ((JButton) e.getSource()).getHeight());
        }
        else if(e.getActionCommand().equals(VALIDATEACTION)){
        System.out.println("content:"+expressionArea.getText().replaceAll("\n"," "));
        expressionArea.getDocument().addDocumentListener(new DocumentListener(){
        public void changedUpdate(DocumentEvent e) {
        }
        
        public void insertUpdate(DocumentEvent e) {
        }
        
        public void removeUpdate(DocumentEvent e) {
        }
        });
        }*/
        /*  else if (e.getActionCommand().equals(BEANACTION)) {
              BeanDialog beanDialog = null;
        
              if (nodeSource == null) {
                  nodeSource = new ArrayNodeSource(tagField.getText(), expressionArea.getText());
              }
        
              if ((nodeSource.getBeanName() != null) && (nodeSource.getMethods() != null)) {
                  beanDialog = new BeanDialog(this, nodeSource.getBeanName(), nodeSource.getMethods());
              } else {
                  beanDialog = new BeanDialog(this);
              }
        
              beanDialog.setVisible(true);
        
              if (beanDialog.isExitedOk()) {
                  String beanName = beanDialog.getBeanName();
                  Vector vector = beanDialog.getMethods();
                  nodeSource.setMethods(vector);
                  nodeSource.setBeanName(beanName);
              }
          }*/
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InstantiationException ex) {
        } catch (ClassNotFoundException ex) {
        }

        ArraySourceDialog d = new ArraySourceDialog(null);
        d.setVisible(true);
    }

    class OKActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if ((tagField.getText() == null) || tagField.getText().trim().equals("")) {
                JOptionPane.showConfirmDialog(ArraySourceDialog.this, "名称不能为空！", "错误提示",
                    JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);

                return;
            }

            String cls = ((elementClass.getText() != null) &&
                    (elementClass.getText().trim().length() > 0)) ? elementClass.getText()
                                                                       : null;

                if (nodeSource == null) {
                    nodeSource = new ArrayNodeSource(tagField.getText(), expressionArea.getText(), cls);
                } else {
                    nodeSource.setTagName(tagField.getText());
                    nodeSource.setExpression(expressionArea.getText());
                    nodeSource.setElementClass(cls);
                }

            exitedOk = true;
            dispose();
        }
    }

    class CancelActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            exitedOk = false;
            dispose();
        }
    }

    class ArrayPopop extends JPopupMenu {
        private JMenuItem monthlyItem;
        private JMenuItem weeklyItem;
        private JMenuItem quaterItem;
        private JMenuItem continentItem;
        private JMenuItem provinceItem;

        ArrayPopop() {
            JMenu timeMenu = new JMenu("时间类数组");
            JMenu areaMenu = new JMenu("地区类数组");

            quaterItem = new JMenuItem("按季度");
            monthlyItem = new JMenuItem("按月");
            weeklyItem = new JMenuItem("按周");

            continentItem = new JMenuItem("按洲");
            provinceItem = new JMenuItem("按中国省份");

            timeMenu.add(quaterItem);
            timeMenu.add(monthlyItem);
            timeMenu.add(weeklyItem);

            areaMenu.add(continentItem);
            areaMenu.add(provinceItem);

            this.add(timeMenu);
            this.add(areaMenu);
        }
    }
}
