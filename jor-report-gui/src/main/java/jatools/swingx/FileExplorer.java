/*
 * Created on 2003-12-31
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jatools.swingx;

/**
 * @author zhou
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;



public class FileExplorer extends JPanel implements TreeSelectionListener {

	DetailTable detailTable = new DetailTable();
	JScrollPane sp = new JScrollPane(detailTable);
	JSplitPane split = new JSplitPane();
	FileSystemView fileSystemView = FileSystemView.getFileSystemView();
	FileNode root = new FileNode(fileSystemView.getRoots()[0]);
	DefaultTreeModel treeModel = new DefaultTreeModel(root);
	JTree tree = new JTree(treeModel);

	public static void main(String argv[]) {

		new FileExplorer().showDialog1(null);

	}

	public void acceptFiles(String[] filter) {
		this.filter = filter;

	}
	String[] filter;

	File selectedFile;
	JTextField selectedText;
	public File showDialog1(Frame owner) {
		selectedFile = null;
		JOptionPane.showConfirmDialog(owner, this);
		return selectedFile;
	}
	private FileExplorer() {

		setLayout(new BorderLayout());
		split.setDividerSize(6);
		split.setLeftComponent(new JScrollPane(tree));
		split.setRightComponent(sp);
		split.setDividerLocation(180);
		sp.getViewport().setBackground(Color.white);

		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("ff")); //
		selectedText = new JTextField();
		selectedText.setPreferredSize(new Dimension(250, 22));
		p.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 10));

		p.add(selectedText);
		add(p, BorderLayout.SOUTH);
		add(split, BorderLayout.CENTER);
		tree.addTreeExpansionListener(new MyExpandsionListener());
		tree.setCellRenderer(new MyTreeCellRenderer());
		tree.addTreeSelectionListener(this);
		tree.setSelectionRow(0);
		tree.setComponentOrientation(ComponentOrientation.UNKNOWN);
		detailTable
			.getSelectionModel()
			.addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				hitFile();
			}
		});
	}
	public FileExplorer(int i) {
		
		setLayout(new BorderLayout());
		split.setDividerSize(6);
		split.setLeftComponent(new JScrollPane(tree));
		split.setRightComponent(sp);
		split.setDividerLocation(180);
		sp.getViewport().setBackground(Color.white);

		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("fas")); //
		selectedText = new JTextField();
		selectedText.setPreferredSize(new Dimension(250, 22));
		p.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 10));

		p.add(selectedText);
		add(p, BorderLayout.SOUTH);
		add(split, BorderLayout.CENTER);
		tree.addTreeExpansionListener(new MyExpandsionListener());
		tree.setCellRenderer(new MyTreeCellRenderer());
		tree.addTreeSelectionListener(this);
	}

	public void hitFile() {
		int index = detailTable.getSelectedRow();
		if (index > -1) {
			File f = (File) detailTable.getValueAt(index, 0);
			if (f.isDirectory()) {
				selectedFile = null;
				;
				selectedText.setText(""); //
			} else {

				if (isAcceptable(f.getAbsolutePath())) {
					selectedFile = f;
					selectedText.setText(f.getName());
				}
			}
		}
	}
	boolean isAcceptable(String filePath) {
		if (filter == null)
			return true;

		String lowerPath = filePath.toLowerCase();

		for (int i = 0; i < filter.length; i++) {
			if (lowerPath.endsWith(filter[i]))
				return true;

		}

		return false;

	}
	public void valueChanged(TreeSelectionEvent e) {
		
		Object obj = tree.getLastSelectedPathComponent();
		if (obj == null)
			return;
		else
			detailTable.setParent(((FileNode) obj).getFile());
	}

	class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		public MyTreeCellRenderer() {
		}
		public Component getTreeCellRendererComponent(
			JTree tree,
			Object value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int row,
			boolean hasFocus) {
			super.getTreeCellRendererComponent(
				tree,
				value,
				sel,
				expanded,
				leaf,
				row,
				hasFocus);
			setIcon(fileSystemView.getSystemIcon(((FileNode) value).getFile()));
			return this;
		}
	}
	class MyExpandsionListener implements TreeExpansionListener {
		public MyExpandsionListener() {
		}
		public void treeExpanded(TreeExpansionEvent event) {
			if (tree.getLastSelectedPathComponent() == null) {
				return;
			}
			tree.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			TreePath path = event.getPath();
			FileNode node = (FileNode) path.getLastPathComponent();
			node.explore();
			treeModel.nodeStructureChanged(node);
			tree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		public void treeCollapsed(TreeExpansionEvent event) {
		}
	}
	class FileNode extends DefaultMutableTreeNode {
		private boolean explored = false;
		public FileNode(File file) {
			setUserObject(file);
		}
		public boolean getAllowsChildren() {
			return isDirectory();
		}
		public boolean isLeaf() {
			return !isDirectory();
		}
		public File getFile() {
			return (File) getUserObject();
		}
		public boolean isExplored() {
			return explored;
		}
		public void setExplored(boolean b) {
			explored = b;
		}
		public boolean isDirectory() {
			return getFile().isDirectory();
		}
		public String toString() {
			File file = (File) getUserObject();
			String filename = file.toString();
			int index = filename.lastIndexOf(File.separator);
			return (index != -1 && index != filename.length() - 1)
				? filename.substring(index + 1)
				: filename;
		}
		public void explore() {
			if (!isExplored()) {
				File file = getFile();
				File[] children = file.listFiles();
				if (children == null || children.length == 0)
					return;
				for (int i = 0; i < children.length; ++i) {
					File f = children[i];
					if (f.isDirectory())
						add(new FileNode(children[i]));
				}
				explored = true;
			}
		}
	}
	class DetailTable extends JTable {
		DetailTableModel model = new DetailTableModel();
		public DetailTable() {
			setModel(model);
			setShowGrid(false);
			TableColumnModel colModel = getColumnModel();
			for (int i = 0; i < 3; i++)
				colModel.getColumn(i).setCellRenderer(
					new DetailsTableCellRenderer());
			setRowHeight(18);
			this.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						for (int i = 0; i < getRowCount(); i++) {
							if (getCellRect(i, 0, true)
								.contains(e.getPoint())) {
								openSelect();
								break;
							}
						}
					}
				}
			});
		}
		public void openSelect() {
			Object obj = model.getValueAt(getSelectedRow(), 0);
			if (obj == null)
				return;
			File f = (File) obj;
			if (f.isDirectory()) {

			} else {

			}
		}

		public void setParent(File parent) {
			model.removeAllRows();
			File list[] = parent.listFiles();
			if (list == null)
				return;
			ArrayList vDir = new ArrayList(), vFile = new ArrayList();
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory())
					vDir.add(list[i]);
				else
					vFile.add(list[i]);
			}
			sortElements(vFile);
			sortElements(vDir);
			for (int i = 0; i < vDir.size(); i++)
				model.addFile((File) vDir.get(i));
			for (int i = 0; i < vFile.size(); i++)
				model.addFile((File) vFile.get(i));
		}
		public void sortElements(ArrayList v) {
			for (int i = 0; i < v.size(); i++) {
				int k = i;
				for (int j = i + 1; j < v.size(); j++) {
					File fa = (File) v.get(j);
					File fb = (File) v.get(k);
					if (fileSystemView
						.getSystemDisplayName(fa)
						.toLowerCase()
						.compareTo(
							fileSystemView
								.getSystemDisplayName(fb)
								.toLowerCase())
						< 0)
						k = j;
				}
				if (k != i)
					swap(k, i, v);
			}
		}
		private void swap(int loc1, int loc2, ArrayList v) {
			Object tmp = v.get(loc1);
			v.set(loc1,v.get(loc2));
			v.set(loc2,tmp );
		}
		class DetailTableModel extends DefaultTableModel {
			public DetailTableModel() {
				addColumn("name"); //
				addColumn(App.messages.getString("res.15")); //
				addColumn(App.messages.getString("res.16")); //
			}
			public void addFile(File f) {
				addRow(
					new Object[] {
						f,
						new Double(f.length() / 1024),
						new java.sql.Date(f.lastModified())});
			}
			public void removeAllRows() {
				while (getRowCount() != 0)
					removeRow(0);
			}
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}
		class DetailsTableCellRenderer extends DefaultTableCellRenderer {
			DetailsTableCellRenderer() {
			}
			public Component getTableCellRendererComponent(
				JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column) {
				if (column == 1) {
					setHorizontalAlignment(SwingConstants.TRAILING);
					isSelected = hasFocus = false;
				} else if (column == 2) {
					setHorizontalAlignment(SwingConstants.CENTER);
					isSelected = hasFocus = false;
				} else
					setHorizontalAlignment(SwingConstants.LEADING);
				return super.getTableCellRendererComponent(
					table,
					value,
					isSelected,
					hasFocus,
					row,
					column);
			}
			public void setValue(Object value) {
				setIcon(null);
				if (value instanceof File) {
					File file = (File) value;
					setText(fileSystemView.getSystemDisplayName(file));
					setIcon(fileSystemView.getSystemIcon(file));
				} else {
					super.setValue(value);
				}
			}
		}
	}

}
