package jatools.designer.variable;

import jatools.classtree.AbstractNode;
import jatools.classtree.ClassNode;
import jatools.classtree.NodeRenderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

public class BeanDialog extends JDialog {

	private ArrayList methods;
	private boolean exitedOk;
	
	private BeanTree beanTree;
	private JTextField nameField;
	private JList methodsList;
	private DefaultListModel listModel;
	
	private JButton okButton,cancelButton;
    public BeanDialog(Dialog owner){
    	super(owner,"类方法名选择");
    	exitedOk=false;
    	setSize(350,450);
    	Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
    	this.setLocation(((int)d.getWidth())/2-175,((int)d.getHeight())/3-225);
    	this.setModal(true);
    	initUI();
//    	setVisible(true);
    }
    public BeanDialog(Dialog owner,String beanName,ArrayList methods){
    	this(owner);
    	nameField.setText(beanName);
    	
    	 try {
				Class class1=Class.forName(beanName);
				
				Method[] ms=class1.getMethods();
				for(int i=0;i<ms.length;i++){
					if(ms[i].getParameterTypes().length==0){
						
					
						Class c1=	ms[i].getReturnType();
	
						if(!c1.equals(void.class)&&ms[i].getModifiers()==1){
							String methodName=ms[i].getName();
							if(methodName.startsWith("get")){
								String s=methodName.substring(3);
								Character.toLowerCase(s.charAt(0));
								
								ListItem li=new ListItem(s,methods.contains(s));
								listModel.addElement(li);
							}
						}
					}
				}
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showConfirmDialog(this, "找不到类名："+beanName,"错误提示",JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE);
				//e1.printStackTrace();
			}
//    	for(int i=0;i<methods.size();i++){
//    		ListItem li=new ListItem(methods.get(i).toString(),true);
//    		listModel.addElement(li);
//    	}
    }
    private void initUI(){
    	this.getContentPane().setLayout(new BorderLayout());
    	Box south=Box.createHorizontalBox();
    	south.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
    	
    	JTabbedPane tab=new JTabbedPane();
    	beanTree=new BeanTree(); 
    	beanTree.setToggleClickCount(2);
    	beanTree.setRowHeight(17);
    	beanTree.setCellRenderer(new NodeRenderer());
    	JScrollPane treePane=new JScrollPane(beanTree);
    	tab.addTab("选择类名", treePane);
    	
    	nameField=new JTextField();
    	listModel=new DefaultListModel();
    	methodsList=new JList();
    	
    	methodsList.addMouseListener(new MouseAdapter(){

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getClickCount()==1){
					if(methodsList.getSelectedValue()!=null){
						ListItem li=(ListItem)methodsList.getSelectedValue();
						li.selected=li.selected?false:true;
						methodsList.repaint();
					}
				}
			}

    	});
    	ListCellCheckRenderer lr=new ListCellCheckRenderer();
    	methodsList.setCellRenderer(lr);
    	methodsList.setModel(listModel);
    	JScrollPane listPane=new JScrollPane(methodsList);
    	
//    	tab.setMinimumSize(new Dimension(100,200));
//    	center.add(tab);
    	
    	
    	JLabel nameLabel=new JLabel("类名：");
    	Box b1=Box.createHorizontalBox();
    	b1.add(nameLabel);
    	b1.add(Box.createHorizontalStrut(5));
    	b1.add(nameField);
    	b1.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
    	
    	
    	
    	JPanel resultPanel=new JPanel(new BorderLayout());
    	resultPanel.add(b1,BorderLayout.NORTH);
    	JPanel listPanel=new JPanel(new BorderLayout());
    	listPanel.add(listPane,BorderLayout.CENTER);
    	resultPanel.add(listPanel,BorderLayout.CENTER);
    	
    	
//    	center.add(resultPanel);
    	
    	JSplitPane splitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT,tab,resultPanel);
    	splitPane.setDividerLocation(150);
    	splitPane.setDividerSize(5);
    	this.getContentPane().add(splitPane,BorderLayout.CENTER);
    	this.getContentPane().add(south,BorderLayout.SOUTH);
    	
    	listPanel.setBorder(BorderFactory.createTitledBorder("方法名"));
    	
    	okButton=new JButton("确定");
    	cancelButton=new JButton("取消");
    	south.add(Box.createHorizontalGlue());
    	south.add(okButton);
    	south.add(Box.createHorizontalStrut(20));
    	south.add(cancelButton);
    	south.add(Box.createHorizontalStrut(20));
    	
    	
    	okButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(nameField.getText()==null||nameField.getText().trim().equals("")){
					JOptionPane.showConfirmDialog(BeanDialog.this, "类名不能为空！",
							"错误提示",JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					Class.forName(nameField.getText());
					exitedOk=true;
					dispose();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					JOptionPane.showConfirmDialog(BeanDialog.this, "类名不合法！",
							"错误提示",JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			}
    		
    	});
    	cancelButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				exitedOk=false;
				dispose();
			}
    		
    	});
    	
    
    beanTree.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                int x = e.getX();
                int y = e.getY();
                int row = beanTree.getRowForLocation(x, y);

                if (row != -1) {
                    TreePath path = beanTree.getPathForRow(row);

                    //TreePath path =importScriptPanel.tree.getPathForLocation(e.getX(), e.getY());
                    AbstractNode node = (AbstractNode) path.getLastPathComponent();

                    String name = null;

                   if (node instanceof ClassNode) {
                        name = node.getUserObject().toString();
                        listModel.removeAllElements();
                        try {
							Class class1=Class.forName(name);
							
							Method[] ms=class1.getMethods();
							for(int i=0;i<ms.length;i++){
								if(ms[i].getParameterTypes().length==0){
									/**
									 * java.lang.reflect.Modifier
									 public static final int ABSTRACT 1024 
									 public static final int FINAL 16 
									 public static final int INTERFACE 512 
									 public static final int NATIVE 256 
									 public static final int PRIVATE 2 
									 public static final int PROTECTED 4 
									 public static final int PUBLIC 1 
									 public static final int STATIC 8 
									 public static final int STRICT 2048 
									 public static final int SYNCHRONIZED 32 
									 public static final int TRANSIENT 128 
									 public static final int VOLATILE 64 
									 */
								
									Class c1=	ms[i].getReturnType();
				
									if(!c1.equals(void.class)&&ms[i].getModifiers()==1){
										String methodName=ms[i].getName();
										if(methodName.startsWith("get")){
											String s=methodName.substring(3);
											Character.toLowerCase(s.charAt(0));
											
											ListItem li=new ListItem(s,true);
											listModel.addElement(li);
										}
									}
								}
							}
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                   }

                    if (name != null) {
                    	nameField.setText(name);
//                    	nameField.requestFocus();
                    }
                }
            }
        }
    });
    }
    
	public String getBeanName() {
		return nameField.getText();
	}
	public boolean isExitedOk() {
		return exitedOk;
	}
	public ArrayList getMethods() {
		if(methods==null){
			methods=new ArrayList();
		}
		methods.clear();
		int size=listModel.size();
		for(int i=0;i<size;i++){
		ListItem item=(ListItem)	listModel.get(i);
		if(item.selected){
			methods.add(item.name);
		}
		}
		return methods;
	}
    public static void main(String[] args){
    	BeanDialog bd=new BeanDialog(new JDialog());
    	bd.setVisible(true);
    	}
    class ListCellCheckRenderer extends DefaultListCellRenderer{
     
    	public Component getListCellRendererComponent(JList list, Object value,int index,boolean isSelected,
    			boolean cellHasFocus){
    		    if(value!=null){
    		    	if(value instanceof ListItem){
    		    		ListItem li=(ListItem)value;
    		    		JCheckBox box=new JCheckBox(li.name,li.selected);
    		    		box.setOpaque(false);
    		    		box.setBackground(list.getBackground());
    		    		box.setPreferredSize(new Dimension(150,20));
    		    		
    		    		return box;
    		    	}
    		    }
					return list;
    		
    	}
    	
    }
    class ListItem{
    	String name;boolean selected;
    	public ListItem(String name,boolean selected){
    		this.name=name;
    		this.selected=selected;
    	}
    	public String toString(){
    		return name;
    	}
    	
    }
}
