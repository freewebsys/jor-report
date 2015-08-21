package jatools.classtree;

import jatools.swingx.CommandPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class FindClassDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	private JTextField findText;
	private JList classItemList;
	private JScrollPane classScroll;
	private DefaultListModel classItemModel;
	private ArrayList classItemArrayList;
	private JLabel jarName;
	private boolean done=false;
	private String className=null;
	//private JLabel className;
	public FindClassDialog(){
				
	   }
   public FindClassDialog(JDialog owner,String title){
	    super(owner,title);
	    this.setModal(true);
	    Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		findText = new JTextField(40);
		findText.setPreferredSize(new Dimension(100,20));
		jarName=new JLabel();
		jarName.setPreferredSize(new Dimension(20, 20));
		CatchKeyListener catcher=new CatchKeyListener();
		findText.addKeyListener(catcher);
		classItemModel=new DefaultListModel();
		classItemArrayList=new ArrayList();
		classItemList = new JList();
		classItemList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() == classItemList)
				{
					 if(e.getClickCount() >1)
					 {
						  
						    int index= classItemList.locationToIndex(e.getPoint());
							ClassItem item=(ClassItem) classItemModel.getElementAt(index);
							String fullName=item.getFullPath();
							if(item!=null)
							{
							 FindClassDialog.this.setClassName(fullName+";"+item.getJarName());
							  setDone(true);
							}
							//System.out.println("double fullname2: ");
							FindClassDialog.this.dispose();
					 }
				}
			}

		});
		classItemList.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource()==classItemList)
				{
					int index= e.getLastIndex();
					ClassItem item=(ClassItem) classItemModel.getElementAt(index);
					jarName.setText("所在的jar包为: "+item.getJarName());
				}
			}});
		classItemList.setModel(classItemModel);
		classScroll = new JScrollPane();
		classScroll.getViewport().add(classItemList);

		//className = new JLabel("类名");
		JButton okButton = new JButton("确定");
		okButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
                 ClassItem classItem = (ClassItem) classItemList.getSelectedValue();

                 if (classItem != null) {
                	 setClassName(classItem.getFullPath()+";"+classItem.getJarName());
                	 setDone(true);
                 }

                 hide();
			}});
		JButton cancelButton = new JButton("取消");
		cancelButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setDone(false);
                 hide();
			}});
		CommandPanel commandPanel = CommandPanel.createPanel();
		commandPanel.addComponent(okButton);
		commandPanel.addComponent(cancelButton);
		JPanel northPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints northgbc=new GridBagConstraints();
		northgbc.gridwidth=1;
		northgbc.anchor=GridBagConstraints.WEST;
		northPanel.add(new JLabel("类名称:"),northgbc);
		northPanel.add(Box.createHorizontalStrut(5));
		northgbc.fill=GridBagConstraints.HORIZONTAL;
		northgbc.weightx=40;
		northPanel.add(findText,northgbc);
		northgbc.weightx=60;
		contentPane.add(northPanel, "North");
		JPanel centerPanel=new JPanel(new BorderLayout());
		centerPanel.add(classScroll, "Center");		
		centerPanel.add(jarName, "South");
		contentPane.add(centerPanel, "Center");
		contentPane.add(commandPanel, "South");
	
   }
   public void find(String findClass)
   {
	   classItemArrayList.clear();
	   classItemModel.removeAllElements();	   
	   String fullname=null;
	   String name=null;
	   JarFileNode jarname=null;
	   String classPath = System.getProperty("java.class.path");
		String[] jarNames = classPath.split(";");
		for (int i = 1; i < jarNames.length; i++) {
			String string = jarNames[i];
			 jarname = new JarFileNode(string);
			try {
				JarFile jar=new JarFile((String)jarname.getUserObject());
				if(jar==null)
					return;
				Enumeration e = jar.entries();
				while (e.hasMoreElements()) {
					JarEntry entry = (JarEntry) e.nextElement();
					if (entry.getName().endsWith(".class")
							&& entry.getName().indexOf("$") == -1) {
						ClassNode cn = new ClassNode(entry.getName());
						//System.out.println("entry.getName() "+entry.getName());
						 fullname=(String) cn.getUserObject();
						 name=fullname.substring(fullname.lastIndexOf(".")+1);
						 if(name.toLowerCase().startsWith(findClass.toLowerCase()))
						 {							
							 ClassItem item=new ClassItem(name.toLowerCase(),fullname,jarname.toString()/**(String)jarname.getUserObject()*/);							 
							 classItemArrayList.add(item);
							
						}

					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
		Collections.sort(classItemArrayList);
		for(int j=0;j<classItemArrayList.size();j++)
		{
			classItemModel.addElement((ClassItem)classItemArrayList.get(j));
			
		}
		
   }
   public static void main(String args[])
   {
	   try {
           UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
           }
      catch(Exception e)
     {}
    JFrame frame=new JFrame("查找类型");
    //frame.setSize(new Dimension(350,300));//new Dimension(200,100)
	 frame.pack();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
      FindClassDialog fc=new FindClassDialog(null,"查找类型");
       fc.pack();
      fc.addWindowListener(new WindowAdapter()
      {
    	  public void windowClosing(WindowEvent e)
    	  {
    		  if(e.getID()==WindowEvent.WINDOW_CLOSING)
    			  System.exit(0);
    	  }
      });
      fc.setResizable(true);
      fc.setVisible(true);
   }
   
    class   CatchKeyListener implements KeyListener
   {
 	 public void keyTyped(KeyEvent e) 
 	 {

 	 }

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		 String className=findText.getText();
		 if(className==null||className.trim().equals("")){
			 classItemArrayList.clear();
			   classItemModel.removeAllElements();
			   return;
		 }		 
		 find(className);				
	}
   }

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
}
