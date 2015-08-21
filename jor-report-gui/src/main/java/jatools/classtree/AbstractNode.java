package jatools.classtree;

import jatools.util.Util;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;


public abstract class AbstractNode extends DefaultMutableTreeNode {
	static final Icon JAR_ICON = Util
			.getIcon("/jatools/icons/jarfile.gif");

	static final Icon CLASS_ICON = Util
			.getIcon("/jatools/icons/class.gif");

	static final Icon LIB_ICON = Util
			.getIcon("/jatools/icons/library.gif");

	static final Icon STATIC_ICON = Util
			.getIcon("/jatools/icons/static.gif");

	static final Icon PUBLIC_ICON = Util
			.getIcon("/jatools/icons/publicmethod.gif");

	public static final Icon CHECKED_ICON = Util
			.getIcon("/jatools/icons/checked.gif");

	public static final Icon NO_CHECK_ICON = Util
			.getIcon("/jatools/icons/nocheck.gif");

	static final int NOT_SELECTED = 0;

	static final int PARTLY_SELECTED = 1;

	static final int SELECTED = 2;

	static final int SELECT_DISABLE = -1;

	private boolean isLeaf = false;

	protected boolean explored = false;

//	boolean withCheck;

//	boolean grayed;

	String memo;



	public AbstractNode() {
		this(null);
	}

	public AbstractNode(Object userObject) {
		this(userObject, true, false);
	}

	public abstract Icon getIcon();

	public AbstractNode(Object userObject, boolean allowsChildren,
			boolean isSelected) {
		super(userObject, allowsChildren);
		
	}



	
	public boolean isExplored() {
		return explored;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	protected void setIsLeaf(boolean yesno) {
		isLeaf = yesno;
	}

	public void explore() {
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	
}
