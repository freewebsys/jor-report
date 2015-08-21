package jatools.swingx.dnd;

import jatools.engine.ProtectClass;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
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
import java.awt.dnd.InvalidDnDOperationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.5 $
 * @author $author$
 */
public class DNDManager implements DragSourceListener, DragGestureListener,
    DropTargetListener, Transferable,ProtectClass {
  public static DataFlavor THIS_FLAVOR = new DataFlavor(DNDManager.class, "class"); //
  public static DataFlavor[] FLAVORS = new DataFlavor[] { THIS_FLAVOR };
  Map sources = new HashMap();
  Map targets = new HashMap();
  JatoolsDragSource currentSource;

  public DNDManager(DataFlavor[] flavors)
  {
    FLAVORS = flavors;
  }
  public DNDManager()
  {


  }
  /**
   * DOCUMENT ME!
   *
   * @param source DOCUMENT ME!
   */
  public void registerSource(JatoolsDragSource source) {
    Component comp = source.getSourceComponent();


    
    DragSource.getDefaultDragSource()
        .createDefaultDragGestureRecognizer(comp, 
        DnDConstants.ACTION_COPY_OR_MOVE, //
        this 
        );

    sources.put(comp, source);
  }

  /**
   * DOCUMENT ME!
   *
   * @param target DOCUMENT ME!
   */
  public void registerTarget(JatoolsDropTarget target) {
    Component comp = target.getTargetComponent();
    new DropTarget(comp, 
                   DnDConstants.ACTION_COPY_OR_MOVE, //
                   this, 
                   true);
    targets.put(comp, target);
  }

  /**
   * 发现有拖动企图
   *
   * @param dge DOCUMENT ME!
   */
  public void dragGestureRecognized(DragGestureEvent dge) {
    if ((dge.getDragAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
      return;
    }

    try {
      currentSource = getSource(dge.getComponent());
      dge.startDrag(DragSource.DefaultLinkDrop, this, this);
    } catch (InvalidDnDOperationException idoe) {
   
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param comp DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  private JatoolsDragSource getSource(Component comp) {
    return (JatoolsDragSource) sources.get(comp);
  }

  /**
   * DOCUMENT ME!
   *
   * @param comp DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  private JatoolsDropTarget getTarget(Component comp) {
    return (JatoolsDropTarget) targets.get(comp);
  }

  /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
  public void dragEnter(DragSourceDragEvent e) {
  }

  /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
  public void dragOver(DragSourceDragEvent e) {
  }

  /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
  public void dropActionChanged(DragSourceDragEvent e) {
  }

  /**
   * DOCUMENT ME!
   *
   * @param dse DOCUMENT ME!
   */
  public void dragExit(DragSourceEvent dse) {
  }

  /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
  public void dragDropEnd(DragSourceDropEvent e) {
    if (e.getDropSuccess()) {
      //    currentSource.complete(false);
    } else {
      //  currentSource.cancel();
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
  public void dragEnter(DropTargetDragEvent e) {

    for(int i = 0;i< FLAVORS.length ;i++)
    {
      if (e.isDataFlavorSupported(FLAVORS[i]) &&
          getTarget(e.getDropTargetContext().getComponent()).canDrop(currentSource)) {
      e.acceptDrag(e.getDropAction());
      return ;
    }

    }
    e.rejectDrag();

  }

  /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
  public void dragOver(DropTargetDragEvent e) {
  }

  /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
  public void dropActionChanged(DropTargetDragEvent e) {
  }

  /**
   * DOCUMENT ME!
   *
   * @param dte DOCUMENT ME!
   */
  public void dragExit(DropTargetEvent dte) {
  }

  /**
   * DOCUMENT ME!
   *
   * @param e DOCUMENT ME!
   */
  public void drop(DropTargetDropEvent e) {
    location =(Point) e.getLocation().clone() ;
    dropEvent = e;


    for(int i = 0;i< FLAVORS.length ;i++)
    {
      if (e.isDataFlavorSupported(FLAVORS[i]) ) {
        usedFlavor = FLAVORS[i];
        JatoolsDropTarget target = getTarget(e.getDropTargetContext().getComponent());
        e.dropComplete(target.drop(currentSource));
        return ;
      }

    }
    e.dropComplete(false);
  }
  DropTargetDropEvent dropEvent;
  DataFlavor usedFlavor ;







  public Point getLocation()
  {
    return location;
  }

  static Point location = new Point();

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public DataFlavor[] getTransferDataFlavors() {
    return FLAVORS;
  }

  /**
   * DOCUMENT ME!
   *
   * @param flavor DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return flavor.equals(THIS_FLAVOR);
  }

  /**
   * DOCUMENT ME!
   *
   * @param flavor DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   *
   * @throws UnsupportedFlavorException DOCUMENT ME!
   * @throws IOException DOCUMENT ME!
   */
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
  IOException {
    return this;
  }
  public DataFlavor getUsedFlavor() {
    return usedFlavor;
  }
  public DropTargetDropEvent getDropEvent() {
    return dropEvent;
  }

}
