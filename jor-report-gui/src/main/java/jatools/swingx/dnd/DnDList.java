package jatools.swingx.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class DnDList extends JList implements DragSourceListener,
		DragGestureListener, DropTargetListener {
	
	private int overIndex;

	private boolean dragging;

	private int[] selectedIndicies;

	private DragSource dragSource;
	
//	JButton b=new JButton();

	public DnDList(DnDListModel model) {
		super(model);
		
		// Configure ourselves to be a drag source
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this,
				DnDConstants.ACTION_MOVE, this);
		
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );

		// Configure ourselves to be a drop target
		new DropTarget(this, this);
		
//		this.setCellRenderer( new ListCellRenderer(){
//
//			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//				// TODO Auto-generated method stub
//				b.setText( value.toString() );
//				return b;
//			}});
		
//       b.setHorizontalAlignment( b.LEFT );		
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		this.selectedIndicies = this.getSelectedIndices();
		Object[] selectedObjects = this.getSelectedValues();
		if (selectedObjects.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < selectedObjects.length; i++) {
				sb.append(selectedObjects[i].toString() + "\n");
			}

			// Build a StringSelection object that the Drag Source
			// can use to transport a string to the Drop Target
			StringSelection text = new StringSelection(sb.toString());

			// Start dragging the object
			this.dragging = true;
			dragSource.startDrag(dge, DragSource.DefaultMoveDrop, text, this);
		}
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
		this.dragging = false;
	}

	public void dragExit(DropTargetEvent dte) {
		this.overIndex = -1;
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		this.overIndex = this.locationToIndex(dtde.getLocation());
		this.setSelectedIndex(this.overIndex);
	}

	public void dragOver(DropTargetDragEvent dtde) {
		// See who we are over...
		int overIndex = this.locationToIndex(dtde.getLocation());
		if (overIndex != -1 && overIndex != this.overIndex) {
			// If the value has changed from what we were previously over
			// then change the selected object to the one we are over; this
			// is a visual representation that this is where the drop will occur
			this.overIndex = overIndex;
			this.setSelectedIndex(this.overIndex);
		}
	}

	public void drop(DropTargetDropEvent dtde) {
		try {
			Transferable transferable = dtde.getTransferable();
			if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				dtde.acceptDrop(DnDConstants.ACTION_MOVE);

				// Find out where the item was dropped
				int newIndex = this.locationToIndex(dtde.getLocation());

				// Get the items out of the transferable object and build an
				// array out of them...
				String s = (String) transferable
						.getTransferData(DataFlavor.stringFlavor);
				StringTokenizer st = new StringTokenizer(s);
				ArrayList items = new ArrayList();
				while (st.hasMoreTokens()) {
					items.add(st.nextToken());
				}
				DnDListModel model = (DnDListModel) this.getModel();

				// If we are dragging from our this to our list them move the
				// items,
				// otherwise just add them...
				if (this.dragging) {
					// model.itemsMoved( newIndex, items );
					model.itemsMoved(newIndex, this.selectedIndicies);
				} else {
					model.insertItems(newIndex, items);
				}

				// Update the selected indicies
				int[] newIndicies = new int[items.size()];
				for (int i = 0; i < items.size(); i++) {
					newIndicies[i] = newIndex + i;
				}
				this.setSelectedIndices(newIndicies);

				// Reset the over index
				this.overIndex = -1;

				dtde.getDropTargetContext().dropComplete(true);
			} else {
				dtde.rejectDrop();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
			System.err.println("Exception" + exception.getMessage());
			dtde.rejectDrop();
		} catch (UnsupportedFlavorException ufException) {
			ufException.printStackTrace();
			System.err.println("Exception" + ufException.getMessage());
			dtde.rejectDrop();
		}
	}

	public void dragEnter(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	public void dragExit(DragSourceEvent dse) {
		// TODO Auto-generated method stub
		
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
}
