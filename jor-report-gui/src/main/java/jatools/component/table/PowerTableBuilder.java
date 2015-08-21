package jatools.component.table;

import jatools.component.Component;
import jatools.designer.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PowerTableBuilder {
    private static void deleteRowPanel(Component panel) {
        
        Component[] removed = (Component[]) panel.getChildren().toArray(new Component[0]);

        for (int i = 0; i < removed.length; i++) {
            Component child = removed[i];
            panel.remove(child);
            panel.getParent().add(child);
        }

        panel.delete();
    }

    private static int indexOf(Object[] obj, Object key) {
        for (int i = 0; i < obj.length; i++) {
            if (key.equals(obj[i])) {
                return i;
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param groups DOCUMENT ME!
     * @throws Exception
     */
    public static void layoutLeftHeaderGroup(PowerTable p, String[] groups)
        throws Exception {
        
 

        HeaderTable ltable = p.getLeftHeader();
        
        String[] gNames = ltable.getGroupNames();
        int[] gColumns = ltable.getGroupColumns();
        
        

        int gColumn = p.getLeftHeaderColumns();

        //    	boolean[] reservedColumns = new boolean[p.getLeftHeaderColumns()];
        for (int i = gNames.length - 1; i > -1; i--) {
            gColumn -= gColumns[i];

            String g = gNames[i];
            boolean reserved = false;
            boolean normalGroup = false;

            if (g.startsWith("+")) {
                String[] sharedGroups = g.substring(1).split(",");

                for (int j = 0; j < sharedGroups.length; j++) {
                    if (indexOf(groups, sharedGroups[j]) > -1) {
                        reserved = true;

                        break;
                    }
                }
            } else if (g.startsWith("&")) {
                reserved = (groups.length > 1) && g.substring(1).equals(groups[groups.length - 1]);
            } else {
                reserved = indexOf(groups, g) > -1;
                normalGroup = true;
            }

            if (!reserved) {
                
                if (normalGroup) {
                    Component rowPanel = ltable.getComponentByName("_p" + g);

                    if (rowPanel == null) {
                        throw new Exception(App.messages.getString("res.642") + "_p" + g);
                    }

                    deleteRowPanel(rowPanel);
                }

                
                Component totalLabel = ltable.getComponentByName("_t" + g);
                p.removeColumn(gColumn, gColumns[i]);

                
                if (totalLabel == null) {
                    System.out.println(App.messages.getString("res.642") + "_p" + g);
                } else {
                    p.removeRow(totalLabel.getCell().row, totalLabel.getCell().rowSpan);
                }
            }
        }

        boolean shift =  (groups.length > 1) && gNames[0].equals(groups[groups.length - 1]);

        if (shift) {
            List texts = new ArrayList();

            p.getLeftHeader().getComponentsByName("_x" + gNames[0], texts);

            if (texts.isEmpty()) {
                throw new Exception(App.messages.getString("res.642") + "_x" + gNames[0]);
            }

            Component totalLabel = ltable.getComponentByName("_t" + gNames[0]);

            if (totalLabel == null) {
                throw new Exception(App.messages.getString("res.643") + "_t" + gNames[0]);
            }

            Component rowPanel = ltable.getComponentByName("_p" + gNames[0]);

            if (rowPanel == null) {
                throw new Exception(App.messages.getString("res.644") + "_p" + gNames[0]);
            }

            int topRows = p.getTopHeader().getRowCount();

            
            int h = p.getRowHeight(topRows);
            p.insertRowBefore(topRows, 20);

            
            for (int i = ltable.getColumnCount() - 1; i > -1; i--) {
                Component c = p.getComponent(topRows + 1, i);

                if (c != null) {
                    c.getCell().row = topRows;
                    c.getCell().rowSpan++;
                }
            }

         
            deleteRowPanel(rowPanel);

            

            
            rowPanel.clear();

            Iterator it = texts.iterator();

            while (it.hasNext()) {
                Component text = (Component) it.next();
                text.getCell().rowSpan = 1;
                text.getCell().column += (ltable.getColumnCount() - gColumns[0])+2;

                rowPanel.add(text);
            }

          //  rowPanel.getCell().row = 0;
            rowPanel.getCell().rowSpan = 1;

            Component deepPanel = p.getPanelstore().getComponentOver(topRows, 0);

            deepPanel.add(rowPanel);

            
            totalLabel.getCell().row = rowPanel.getCell().row2() + 1;
            totalLabel.getCell().rowSpan = 1;
            totalLabel.getCell().column = ltable.getColumnCount() - gColumns[0];
            totalLabel.getCell().colSpan = gColumns[0];

            totalLabel.delete();
            deepPanel.add(totalLabel);

            
            int topCols = p.getTopHeader().getColumnCount();
            int leftCols = ltable.getColumnCount();
            int bottomRow = p.getRowCount() - 1;

            for (int i = 0; i < topCols; i++) {
                Component c = p.getCellstore().getComponent(bottomRow, leftCols + i);

                if (c != null) {
                    c.getCell().row = topRows;
                }
            }

            
            p.removeColumn(0, gColumns[0]);
            
            p.removeRow(p.getRowCount()-1);

        }

        //        boolean[] keptColumns = getKeptColumns(p, groups);
        //        Table leftTable = createLeftTable(p, groups);
        //
        //        int[] cols = leftTable.getColumnWidths();
        //
        //        int topRows = p.getTopHeader().getCell().getRowSpan();
        //
        //        leftTable.translate(topRows, 0);
        //
        //        HeaderTable leftHeader = HeaderTable.create(leftTable);
        //
        //        leftHeader.setCell(new Cell(topRows, 0, leftTable.getColumnCount(), leftTable.getRowCount()));
        //
        //        while (p.getRowCount() > (leftTable.getRowCount() + topRows)) {
        //            p.removeRow(p.getRowCount() - 1);
        //        }
        //
        //        String np = p.getLeftHeader().getNodePath();
        //        p.remove(p.getLeftHeader());
        //
        //        for (int i = keptColumns.length - 1; i > -1; i--) {
        //            if (!keptColumns[i]) {
        //                p.removeColumn(i);
        //            }
        //        }
        //
        //        p.add(leftHeader);
        //
        //        p.getLeftHeader().setNodePath(np);
        //
        //        for (int i = 0; i < cols.length; i++) {
        //            p.setColumnWidth(i, cols[i]);
        //        }
    }

    private static boolean[] getKeptColumns(PowerTable p, String[] groups)
        throws Exception {
        HeaderTable ltable = p.getLeftHeader();

        boolean[] keptColumns = new boolean[ltable.getColumnCount()];

        for (int lev = 0; lev < groups.length; lev++) {
            String lName = groups[lev];
            List texts = new ArrayList();

            p.getLeftHeader().getComponentsByName("_x" + lName, texts);

            if (texts.isEmpty()) {
                throw new Exception(App.messages.getString("res.642") + "_x" + lName);
            }

            getKeptColumns(keptColumns, texts);

            //            List shared = new ArrayList();
            //            
            //            p.getLeftHeader().getComponentsBySharedName("_" + lName+"_", shared);
            //            getKeptColumns(keptColumns, texts);
        }

        return keptColumns;
    }

    private static void getKeptColumns(boolean[] keptColumns, List texts) {
        Iterator it = texts.iterator();

        while (it.hasNext()) {
            Cell cell = ((Component) it.next()).getCell();

            for (int i = cell.column; i <= cell.column2(); i++) {
                keptColumns[i] = true;
            }
        }
    }

    static Table createLeftTable(PowerTable p, String[] groups)
        throws Exception {
        HeaderTable ltable = p.getLeftHeader();

        Map cc = new HashMap();

        int[] groupCols = new int[groups.length];

        for (int lev = 0; lev < groups.length; lev++) {
            String lName = groups[lev];

            Component totalLabel = ltable.getComponentByName("_t" + lName);

            if (totalLabel == null) {
                throw new Exception(App.messages.getString("res.643") + "_t" + lName);
            }

            cc.put("_t" + lName, totalLabel);

            List texts = new ArrayList();

            ltable.getComponentsByName("_x" + lName, texts);

            if (texts.isEmpty()) {
                throw new Exception(App.messages.getString("res.642") + "_x" + lName);
            }

            cc.put("_x" + lName, texts);

            groupCols[lev] = getGroupCols(texts);

            Component panel = ltable.getComponentByName("_p" + lName);

            if (panel == null) {
                throw new Exception(App.messages.getString("res.644") + "_p" + lName);
            }

            cc.put("_p" + lName, panel);
        }

        int totalGroupCols = 0;

        for (int i = 0; i < groupCols.length; i++) {
            totalGroupCols += groupCols[i];
        }

        
        int[] cols = new int[totalGroupCols];
        Arrays.fill(cols, 80);

        int col = 0;

        for (int i = 0; i < groups.length; i++) {
            Component c = ltable.getComponentByName("_t" + groups[i]);

            if (c != null) {
                for (int j = 0; j < groupCols[i]; j++) {
                    cols[col] = p.getColumnWidth(c.getCell().column + j);
                    col++;
                }
            }
        }

        int topRows = p.getTopHeader().getCell().getRowSpan();

        
        int[] rows = new int[groups.length + 1];
        Arrays.fill(rows, p.getRowHeight(topRows));

        Table leftTable = new Table(rows, cols);

        for (int i = 0; i < groups.length; i++) {
            Component c = ltable.getComponentByName(groups[i]);

            if (c != null) {
                cols[i] = c.getWidth();
            }
        }

        // _t  total label
        // _p  panel
        // _x  table
        
        
        
        
        
        
        
        Component childPanel = null;
        Component childTotal = null;

        //   List childTotal2 = null;
        int groupCol = totalGroupCols;
        int groupRows = 0;

        for (int lev = groups.length - 1; lev > -1; lev--) {
            groupCol -= groupCols[lev];
            groupRows++;

            String lName = groups[lev];

            Component total = (Component) cc.get("_t" + lName);
            Cell totalCell = total.getCell();
            totalCell.row = groupRows;

            
            int offCol = groupCol - totalCell.column;

            totalCell.column = groupCol;
            totalCell.colSpan = totalGroupCols - totalCell.column;

            Component panel = (Component) cc.get("_p" + lName);
            Cell panelCell = (panel).getCell();
            panelCell.row = 0;
            panelCell.rowSpan = groupRows;
            panelCell.column = 0;
            panelCell.colSpan = totalGroupCols;

            panel.clear();

            
            List texts = (List) cc.get("_x" + lName);
            Iterator it = texts.iterator();

            while (it.hasNext()) {
                Component text = (Component) it.next();
                Cell textCell = text.getCell();
                textCell.row = 0;
                textCell.rowSpan = groupRows;

                textCell.column += offCol;

                
                String style = text.getPrintStyle();

                if (style == null) {
                    style = "";
                }

                style += (";united-level:" + (lev + 1) + ";");
                text.setPrintStyle(style);

                panel.add(text);
            }

            
            //            List totals = (List) cc.get("_t2" + lName);
            //            it = totals.iterator();
            //
            //            while (it.hasNext()) {
            //                Component total2 = (Component) it.next();
            //                Cell total2Cell = total2.getCell();
            //                total2Cell.row = totalCell.row;
            //                // total2Cell.rowSpan = 
            //                total2Cell.column += offCol;
            //
            //                //panel.add(text);
            //            }
            if (childPanel != null) {
                //                if (childPanel.getParent() != null) {
                //                    childPanel.getParent().remove(childPanel);
                //                }
                panel.setParent(null);
                panel.add(childPanel);
                //                if (childTotal.getParent() != null) {
                //                	childTotal.getParent().remove(childTotal);
                //                }
                panel.add(childTotal);

                //                it = childTotal2.iterator();
                //
                //                while (it.hasNext()) {
                //                    Component total2 = (Component) it.next();
                ////                    if (total2.getParent() != null) {
                ////                    	total2.getParent().remove(total2);
                ////                    }
                //                    panel.add(total2);
                //                }
            }

            childTotal = total;
            //    childTotal2 = totals;
            childPanel = panel;
        }

        leftTable.add(childPanel);
        leftTable.add(childTotal);

        return leftTable;
    }

    private static int getGroupCols(List texts) {
        int col = Integer.MAX_VALUE;
        int col2 = 0;

        Iterator it = texts.iterator();

        while (it.hasNext()) {
            Component c = (Component) it.next();

            col = Math.min(c.getCell().column, col);
            col2 = Math.max(c.getCell().column2(), col2);
        }

        return col2 - col + 1;
    }
}
