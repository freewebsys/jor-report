package jatools.designer.variable;

import jatools.swingx.CommandPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ClassSelectDialog extends JDialog implements DocumentListener {
    static List classes;

    /**
     * Creates a new ClassSelector object.
     *
     * @param owner
     *            DOCUMENT ME!
     */
    JTextField patternText = new JTextField();
    JLabel jarLabel = new JLabel("");
    JList matchedList = new JList(new DefaultListModel());
    String selectedClass;
    DefaultListModel model;
    private Matcher lastMatcher;

    /**
     * Creates a new ClassSelectDialog object.
     *
     * @param owner
     *            DOCUMENT ME!
     */
    public ClassSelectDialog(JDialog owner) {
        super(owner, "查找类", true);

        this.model = (DefaultListModel) this.matchedList.getModel();

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p.add(new JLabel("查找类名"), gbc);
        p.add(patternText, gbc);
        p.add(new JLabel("匹配类"), gbc);
        gbc.weighty = 1.0;
        p.add(new JScrollPane(matchedList), gbc);
        gbc.weighty = 0.0;
        p.add(jarLabel, gbc);
        jarLabel.setPreferredSize(new Dimension(0, 25));

        CommandPanel cp = CommandPanel.createPanel(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        confirm();
                    }
                },
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        hide();
                    }
                });

        this.matchedList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    resetJarLabel();
                }
            });

        this.patternText.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    if ((e.getKeyChar() == '\n') && (matchedList.getSelectedIndex() > -1)) {
                        confirm();
                    }
                }
            });

        this.matchedList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        confirm();
                    }
                }
            });

        this.patternText.getDocument().addDocumentListener(this);
        this.getContentPane().add(cp, BorderLayout.SOUTH);
        this.getContentPane().add(p, BorderLayout.CENTER);
        this.setSize(new Dimension(600, 400));
        this.setLocationRelativeTo(owner);
        this.show();
    }

    protected void resetJarLabel() {
        if (this.matchedList.getSelectedIndex() > -1) {
            this.jarLabel.setText(((ClassItem) this.matchedList.getSelectedValue()).jarFile);
        } else {
            this.jarLabel.setText("");
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void confirm() {
        if (this.matchedList.getSelectedIndex() > -1) {
            ClassItem item = (ClassItem) this.matchedList.getSelectedValue();

            if ((item.packageName != null) && (item.packageName.trim().length() > 0)) {
                this.selectedClass = item.packageName + ".";
            } else {
                this.selectedClass = "";
            }

            this.selectedClass += item.className;
        } else {
            this.selectedClass = "";
        }

        hide();
    }

    protected void resetList() {
        if (classes == null) {
            classes = buildIndex();
        }

        if ((lastMatcher != null) && !lastMatcher.over) {
            lastMatcher.stop = true;
        }

        this.lastMatcher = new Matcher();
        this.lastMatcher.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args
     *            DOCUMENT ME!
     */
    public static void main(String[] args) {
        ClassSelectDialog x = new ClassSelectDialog(null);
    }

    /**
     * DOCUMENT ME!
     */
    public List buildIndex() {
        String classPath = System.getProperty("java.class.path");
        if(!classPath.endsWith(";"))
        	classPath += ";";
        
        classPath += System.getProperty("sun.boot.class.path");
        
        String[] jarNames = classPath.split(";");
        ArrayList result = new ArrayList();
        Map uniques = new HashMap();

        for (int i = 0; i < jarNames.length; i++) {
            JarFile jar = null;

            try {
                String jarName = jarNames[i].toLowerCase();

                if (uniques.get(jarName) != null) {
                    continue;
                }

                if (jarName.endsWith(".jar") || jarName.endsWith(".zip")) {
                    jar = new JarFile(jarNames[i]);

                    Enumeration e = jar.entries();

                    while (e.hasMoreElements()) {
                        JarEntry entry = (JarEntry) e.nextElement();

                        if (entry.getName().endsWith(".class") &&
                                (entry.getName().indexOf("$") == -1)) {
                            String fullpath = entry.getName()
                                                   .substring(0, entry.getName().lastIndexOf("."))
                                                   .replace('/', '.');
                            String className = null;
                            String packageName = null;

                            if (fullpath.indexOf('.') == -1) {
                                className = fullpath;
                                packageName = "";
                            } else {
                                packageName = fullpath.substring(0, fullpath.lastIndexOf('.'));
                                className = fullpath.substring(fullpath.lastIndexOf(".") + 1);
                            }

                            ClassItem item = new ClassItem(jarNames[i], className, packageName);
                            ClassItem earlier = (ClassItem) uniques.get(item.className);

                            if (earlier != null) {
                                earlier.unique = false;
                                item.unique = false;
                            } else {
                                uniques.put(item.className, item);
                            }

                            result.add(item);
                        }
                    }

                    uniques.put(jarName, jarName);
                }
            } catch (IOException e1) {
                System.err.println(jarNames[i]);
                e1.printStackTrace();
            }
        }

        Collections.sort(result);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void changedUpdate(DocumentEvent e) {
        resetList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void insertUpdate(DocumentEvent e) {
        resetList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void removeUpdate(DocumentEvent e) {
        resetList();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSelectedClass() {
        return selectedClass;
    }

    class Matcher extends Thread {
        boolean over;
        boolean stop;

        public void run() {
            model.removeAllElements();

            if ((patternText.getText() != null) && (patternText.getText().trim().length() > 0)) {
                Iterator it = classes.iterator();
                String pattern = patternText.getText().toLowerCase();

                while (it.hasNext() && !stop) {
                    ClassItem item = (ClassItem) it.next();

                    if (item.lowerClassName.indexOf(pattern) > -1) {
                        model.addElement(item);
                    }
                }
            }

            if (model.getSize() > 0) {
                matchedList.setSelectedIndex(0);
            }

            over = true;
        }
    }

    class ClassItem implements Comparable {
        String jarFile;
        String className;
        String lowerClassName;
        String packageName;
        boolean unique = true;

        /**
                 * @param jarFile
                 * @param className
                 * @param packageName
                 */
        public ClassItem(String jarFile, String className, String packageName) {
            this.jarFile = jarFile;
            this.className = className;
            this.packageName = packageName;
            this.lowerClassName = className.toLowerCase();
        }

        public int compareTo(Object o) {
            ClassItem a = (ClassItem) o;

            return this.className.compareTo(a.className);
        }

        public String toString() {
            return this.unique ? className : (className + " - " + this.packageName);
        }
    }
}
