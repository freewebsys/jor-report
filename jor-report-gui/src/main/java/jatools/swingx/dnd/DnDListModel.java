package jatools.swingx.dnd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractListModel;

public class DnDListModel extends AbstractListModel {
		/**
		 * Inserts a collection of items before the specified index
		 */
		public ArrayList items;
		public DnDListModel(ArrayList items) {
			super();
			// TODO Auto-generated constructor stub
			this.items = items;
		}
		
		public DnDListModel(Object[]  items) {
			super();
			this.items = new ArrayList();
			for (int i = 0; i < items.length; i++) {
				this.items .add( items[i]);
			}
		}

		public void insertItems(int index, Collection objects) {
			// Handle the case where the items are being added to the end of the
			// list
			if (index == -1) {
				// Add the items
				for (Iterator i = objects.iterator(); i.hasNext();) {
				//	String item = (String) i.next();
					addItem( i.next());
				}
			} else {
				// Insert the items
				for (Iterator i = objects.iterator(); i.hasNext();) {
					
					insertItem(index++, i.next());
				}
			}

			// Tell the list to update itself
			this.fireContentsChanged(this, 0, this.items.size() - 1);
		}

		private void addItem(Object item) {
			items.add( item);
			
		}

		private void insertItem(int i, Object item) {
			items.add( i,item);
			
		}

		public void itemsMoved(int newIndex, int[] indicies) {
			// Copy the objects to a temporary ArrayList
			ArrayList objects = new ArrayList();
			for (int i = 0; i < indicies.length; i++) {
				objects.add(this.items.get(indicies[i]));
			}

			// Delete the objects from the list
			for (int i = indicies.length - 1; i >= 0; i--) {
				this.items.remove(indicies[i]);
			}

			// Insert the items at the new location
			insertItems(newIndex, objects);
		}

		public int getSize() {
			// TODO Auto-generated method stub
			return items.size() ;
		}

		public Object getElementAt(int index) {
			// TODO Auto-generated method stub
			return items.get( index);
		}

	}
