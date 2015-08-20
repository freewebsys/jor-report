package jatools.designer.toolbox;

import jatools.component.Component;
import jatools.component.ComponentConstants;
import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.TableBase;
import jatools.core.view.Border;
import jatools.core.view.BorderStyle;
import jatools.designer.Main;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.TablePeer;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class TableBorderWorker implements BorderWorker {
    final static int TOP = 1;
    final static int VCENTER = 2;
    final static int BOTTOM = 4;
    final static int LEFT = 8;
    final static int HCENTER = 16;
    final static int RIGHT = 32;
    private static TableBorderWorker instance;

    private TableBorderWorker() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param command DOCUMENT ME!
     */
    public void action(String command, String styletext) {
        TablePeer peer = getTablePeer();

        if (peer != null) {
            BorderStyle style = BorderStyle.parse(styletext);

            peer.getOwner().openEdit();

            if (command == ALL) {
                doAll(peer, style);
            } else if (command == CLEAR) {
                doClear(peer);
            } else if (command == BorderWorker.LEFT) {
                doLeft(peer, style);
            } else if (command == BorderWorker.TOP) {
                doTop(peer, style);
            } else if (command == BorderWorker.BOTTOM) {
                doBottom(peer, style);
            } else if (command == BorderWorker.RIGHT) {
                doRight(peer, style);
            } else if (command == FRAME) {
                doTop(peer, style);
                doLeft(peer, style);
                doBottom(peer, style);
                doRight(peer, style);
            } else if (command == BorderWorker.HCENTER) {
                doHCenter(peer, style);
            } else if (command == BorderWorker.VCENTER) {
                doVCenter(peer, style);
            } else if (command == BorderWorker.CROSS) {
                doVCenter(peer, style);
                doHCenter(peer, style);
            }

            peer.getOwner().closeEdit();
        }
    }

    private void doHCenter(TablePeer peer, BorderStyle style) {
        int left = peer.getSelection().x;
        int right = peer.getSelection().x + peer.getSelection().width;
        ComponentPeer[] peers = (ComponentPeer[]) peer.getOwner().getSelection();

        for (int i = 0; i < peers.length; i++) {
            BorderStyle topStyle = null;
            BorderStyle leftStyle = null;
            BorderStyle bottomStyle = null;
            BorderStyle rightStyle = null;

            Border old = peers[i].getComponent().getBorder();

            if (old != null) {
                topStyle = old.getTopStyle();
                leftStyle = old.getLeftStyle();
                bottomStyle = old.getBottomStyle();
                rightStyle = old.getRightStyle();
            }

            int col = peers[i].getComponent().getCell().column;
            int col2 = peers[i].getComponent().getCell().column2() + 1;
            boolean changed = false;

            if ((col > left) && (col < right) && !style.equals(leftStyle)) {
                leftStyle = style;
                changed = true;
            }

            if ((col2 > left) && (col2 < right) && !style.equals(rightStyle)) {
                rightStyle = style;
                changed = true;
            }

            if (changed) {
                Border newborder = Border.create(topStyle, leftStyle, bottomStyle, rightStyle);
                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newborder,
                    Border.class);
            }
        }
    }

    private void doVCenter(TablePeer peer, BorderStyle style) {
        int top = peer.getSelection().y;
        int bottom = peer.getSelection().y + peer.getSelection().height;
        ComponentPeer[] peers = (ComponentPeer[]) peer.getOwner().getSelection();

        for (int i = 0; i < peers.length; i++) {
            BorderStyle topStyle = null;
            BorderStyle leftStyle = null;
            BorderStyle bottomStyle = null;
            BorderStyle rightStyle = null;

            Border old = peers[i].getComponent().getBorder();

            if (old != null) {
                topStyle = old.getTopStyle();
                leftStyle = old.getLeftStyle();
                bottomStyle = old.getBottomStyle();
                rightStyle = old.getRightStyle();
            }

            int row = peers[i].getComponent().getCell().row;
            int row2 = peers[i].getComponent().getCell().row2() + 1;
            boolean changed = false;

            if ((row > top) && (row < bottom) && !style.equals(leftStyle)) {
                topStyle = style;
                changed = true;
            }

            if ((row2 > top) && (row2 < bottom) && !style.equals(rightStyle)) {
                bottomStyle = style;
                changed = true;
            }

            if (changed) {
                Border newborder = Border.create(topStyle, leftStyle, bottomStyle, rightStyle);
                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newborder,
                    Border.class);
            }
        }
    }

    private void doLeft(TablePeer peer, BorderStyle style) {
        clear(peer, style, LEFT);

        ComponentPeer[] peers = (ComponentPeer[]) peer.getOwner().getSelection();

        Border fornull = Border.create(null, style, null, null);

        for (int i = 0; i < peers.length; i++) {
            if (peers[i].getComponent().getCell().column == peer.getSelection().x) {
                Border old = peers[i].getComponent().getBorder();

                if (old == null) {
                    peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), fornull,
                        Border.class);
                } else if (!old.equals(fornull)) {
                    Border newBorder = Border.create(old.getTopStyle(), style,
                            old.getBottomStyle(), old.getRightStyle());

                    peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newBorder,
                        Border.class);
                }
            }
        }
    }

    private void doTop(TablePeer peer, BorderStyle style) {
        clear(peer, style, TOP);

        ComponentPeer[] peers = (ComponentPeer[]) peer.getOwner().getSelection();

        Border fornull = Border.create(style, null, null, null);

        for (int i = 0; i < peers.length; i++) {
            if (peers[i].getComponent().getCell().row == peer.getSelection().y) {
                Border old = peers[i].getComponent().getBorder();

                if (old == null) {
                    peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), fornull,
                        Border.class);
                } else if (!old.equals(fornull)) {
                    Border newBorder = Border.create(style, old.getLeftStyle(),
                            old.getBottomStyle(), old.getRightStyle());

                    peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newBorder,
                        Border.class);
                }
            }
        }
    }

    private void doBottom(TablePeer peer, BorderStyle style) {
        clear(peer, style, BOTTOM);

        ComponentPeer[] peers = (ComponentPeer[]) peer.getOwner().getSelection();

        Border fornull = Border.create(null, null, style, null);

        for (int i = 0; i < peers.length; i++) {
            if (peers[i].getComponent().getCell().row2() == ((peer.getSelection().y +
                    peer.getSelection().height) - 1)) {
                Border old = peers[i].getComponent().getBorder();

                if (old == null) {
                    peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), fornull,
                        Border.class);
                } else if (!old.equals(fornull)) {
                    Border newBorder = Border.create(old.getTopStyle(), old.getLeftStyle(), style,
                            old.getRightStyle());

                    peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newBorder,
                        Border.class);
                }
            }
        }
    }

    private void doRight(TablePeer peer, BorderStyle style) {
        clear(peer, style, RIGHT);

        ComponentPeer[] peers = (ComponentPeer[]) peer.getOwner().getSelection();

        Border fornull = Border.create(null, null, null, style);

        for (int i = 0; i < peers.length; i++) {
            if (peers[i].getComponent().getCell().column2() == ((peer.getSelection().x +
                    peer.getSelection().width) - 1)) {
                Border old = peers[i].getComponent().getBorder();

                if (old == null) {
                    peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), fornull,
                        Border.class);
                } else if (!old.equals(fornull)) {
                    Border newBorder = Border.create(old.getTopStyle(), old.getLeftStyle(),
                            old.getBottomStyle(), style);

                    peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newBorder,
                        Border.class);
                }
            }
        }
    }

    private void doClear(TablePeer peer) {
        clear(peer, null, TOP | BOTTOM | LEFT | RIGHT);

        ComponentPeer[] peers = (ComponentPeer[]) peer.getOwner().getSelection();

        for (int i = 0; i < peers.length; i++) {
            peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), null, Border.class);
        }
    }

    private void doAll(TablePeer peer, BorderStyle style) {
        clear(peer, style, TOP | BOTTOM | LEFT | RIGHT);

        ComponentPeer[] peers = (ComponentPeer[]) peer.getOwner().getSelection();

        Border newborder = new Border("border:" + style.toString());

        for (int i = 0; i < peers.length; i++) {
            peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newborder, Border.class);
        }
    }

    private void clear(TablePeer peer, BorderStyle style, int where) {
        Cell cell = new Cell(peer.getSelection());
        CellStore cellstore = ((TableBase) peer.getComponent()).getCellstore();

        if ((where & TOP) != 0) {
            clearTop(peer, style, cell, cellstore);
        }

        if ((where & BOTTOM) != 0) {
            clearBottom(peer, style, cell, cellstore);
        }

        if ((where & RIGHT) != 0) {
            clearRight(peer, style, cell, cellstore);
        }

        if ((where & LEFT) != 0) {
            clearLeft(peer, style, cell, cellstore);
        }
    }

    private void clearTop(TablePeer peer, BorderStyle style, Cell cell, CellStore cellstore) {
        if (cell.row > 0) {
            for (int i = cell.column; i <= cell.column2(); i++) {
                Component c = cellstore.getComponentOver(cell.row - 1, i);
                Border old_border = c.getBorder();

                if (old_border != null) {
                    Cell _cell = (Cell) c.getCell().clone();
                    _cell.row = cell.row;
                    _cell.rowSpan = 1;

                    if (cell.contains(_cell)) {
                        BorderStyle old_style = old_border.getBottomStyle();

                        if ((old_style != null) && !old_style.equals(style)) {
                            Border new_border = Border.create(old_border.getTopStyle(),
                                    old_border.getLeftStyle(), null, old_border.getRightStyle());

                            ComponentPeer childPeer = peer.getOwner().getComponentPeer(c);
                            childPeer.setValue(ComponentConstants._BORDER.getPropertyName(),
                                new_border, Border.class);
                        }
                    }
                }

                //                i += (c.getCell().colSpan - 1);
            }
        }
    }

    private void clearLeft(TablePeer peer, BorderStyle style, Cell cell, CellStore cellstore) {
        if (cell.column > 0) {
            for (int i = cell.row; i <= cell.row2(); i++) {
                Component c = cellstore.getComponentOver(i, cell.column - 1);
                Border old_border = c.getBorder();

                if (old_border != null) {
                    Cell _cell = (Cell) c.getCell().clone();
                    _cell.column = cell.column;
                    _cell.colSpan = 1;

                    if (cell.contains(_cell)) {
                        BorderStyle old_style = old_border.getRightStyle();

                        if ((old_style != null) && !old_style.equals(style)) {
                            Border new_border = Border.create(old_border.getTopStyle(),
                                    old_border.getLeftStyle(), old_border.getBottomStyle(), null);

                            ComponentPeer childPeer = peer.getOwner().getComponentPeer(c);
                            childPeer.setValue(ComponentConstants._BORDER.getPropertyName(),
                                new_border, Border.class);
                        }
                    }
                }

                //                i += (c.getCell().colSpan - 1);
            }
        }
    }

    private void clearBottom(TablePeer peer, BorderStyle style, Cell cell, CellStore cellstore) {
        TableBase table = (TableBase) peer.getComponent();

        if (cell.row2() < (table.getRowCount() - 1)) {
            for (int i = cell.column; i <= cell.column2(); i++) {
                Component c = cellstore.getComponentOver(cell.row2() + 1, i);
                if(c == null)
                	continue;
                Border old_border = c.getBorder();

                if (old_border != null) {
                    Cell _cell = (Cell) c.getCell().clone();
                    _cell.row = cell.row2();
                    _cell.rowSpan = 1;

                    if (cell.contains(_cell)) {
                        BorderStyle old_style = old_border.getTopStyle();

                        if ((old_style != null) && !old_style.equals(style)) {
                            Border new_border = Border.create(null, old_border.getLeftStyle(),
                                    old_border.getBottomStyle(), old_border.getRightStyle());

                            ComponentPeer childPeer = peer.getOwner().getComponentPeer(c);
                            childPeer.setValue(ComponentConstants._BORDER.getPropertyName(),
                                new_border, Border.class);
                        }
                    }
                }

                //                i += (c.getCell().colSpan - 1);
            }
        }
    }

    private void clearRight(TablePeer peer, BorderStyle style, Cell cell, CellStore cellstore) {
        TableBase table = (TableBase) peer.getComponent();

        if (cell.column2() < (table.getColumnCount() - 1)) {
            for (int i = cell.row; i <= cell.row2(); i++) {
                Component c = cellstore.getComponentOver(i, cell.column2() + 1);
                Border old_border = c.getBorder();

                if (old_border != null) {
                    Cell _cell = (Cell) c.getCell().clone();
                    _cell.column = cell.column2();
                    _cell.colSpan = 1;

                    if (cell.contains(_cell)) {
                        BorderStyle old_style = old_border.getLeftStyle();

                        if ((old_style != null) && !old_style.equals(style)) {
                            Border new_border = Border.create(old_border.getTopStyle(), null,
                                    old_border.getBottomStyle(), old_border.getRightStyle());

                            ComponentPeer childPeer = peer.getOwner().getComponentPeer(c);
                            childPeer.setValue(ComponentConstants._BORDER.getPropertyName(),
                                new_border, Border.class);
                        }
                    }
                }

                //                i += (c.getCell().colSpan - 1);
            }
        }
    }

    private TablePeer getTablePeer() {
        ComponentPeer[] peers = (ComponentPeer[]) Main.getInstance().getActiveEditor()
                                                      .getReportPanel().getSelection();

        if (peers.length > 0) {
            if (peers[0].getComponent().getCell() != null) {
                TableBase t = getRootTable(peers[0].getComponent());
                TablePeer tablePeer = (TablePeer) peers[0].getOwner().getComponentPeer(t);

                return tablePeer;
            }
        }

        return null;
    }

    TableBase getRootTable(Component c) {
        while (c.getCell() != null) {
            c = c.getParent();
        }

        if (c instanceof TableBase) {
            return (TableBase) c;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static TableBorderWorker getInstance() {
        if (instance == null) {
            instance = new TableBorderWorker();
        }

        return instance;
    }
}
