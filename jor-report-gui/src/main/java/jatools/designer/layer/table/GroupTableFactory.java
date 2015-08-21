package jatools.designer.layer.table;

import jatools.component.Label;
import jatools.component.Text;
import jatools.component.table.Cell;
import jatools.component.table.RowPanel;
import jatools.component.table.Table;
import jatools.core.view.Border;
import jatools.data.reader.DatasetReader;
import jatools.dataset.Dataset;
import jatools.designer.App;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.NodeSource;
import jatools.dom.src.RowNodeSource;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: jatools</p>
 * @author Jiang Dehua
 * @version 1.0
 */
public class GroupTableFactory {
    
    
    
    /**
     * 此方法供选中是groupNodesource展示分组用，暂时弃用
     * 改拖groupNodesource直接生成简单的单元格
     * @param source NodeSource
     * @param containNext boolean   是否包含子groupNodeSource
     * @return Table
     */
    public static Table getTable(NodeSource source, boolean containNext) {
        
        Table table = null;

        if (source instanceof GroupNodeSource) {
            GroupNodeSource groupSource = (GroupNodeSource) source;
            DatasetNodeSource datasetSource = getNearestDatasetSource(groupSource);

            if (datasetSource != null) {
                DatasetReader reader = datasetSource.getReader();
                Dataset dataSet = null;
                String[] fields = dataSet.getFieldNames(reader);

                
                ArrayList groupsList = getGroupList(new ArrayList(), source, containNext);

                //               int groupSourceNumber=getGroupNumber(1,groupSource,containNext);
                int groupSourceNumber = groupsList.size();

                int[] rows = new int[groupSourceNumber + 1];
                Arrays.fill(rows, 20);

                
                int[] columns = new int[fields.length];
                Arrays.fill(columns, 80);
                
                table = new Table(rows, columns);

                
                ArrayList headerVector = new ArrayList();

                for (int i = 0; i < fields.length; i++) {
                    if (!headerVector.contains(fields[i])) {
                        headerVector.add(fields[i]);
                    }
                }

                ArrayList headerList = new ArrayList();
                headerList = newColumnHeader(headerList, headerVector, groupSource);

                for (int i = 0; i < headerList.size(); i++) {
                    Label label = new Label(headerList.get(i).toString());
                    label.setBorder(new Border(1, Color.black));
                    table.add(label, 0, i);
                }

                
                RowPanel rowPanel = new RowPanel();
                table.add(getRowPanel(rowPanel, headerList, groupsList, groupSource, containNext,
                        groupSourceNumber));
            }
        }

        return table;
    }

    /**
     * 返回分组报表
     * @param source NodeSource
     * @return Table
     */
    public static Table getGroupTable(NodeSource source) {
        Table table = null;

        if (source instanceof DatasetNodeSource) {
            DatasetNodeSource datasetSource = (DatasetNodeSource) source;
            GroupNodeSource groupSource = getFirstNodeSource(datasetSource);

            if (groupSource != null) {
                DatasetReader reader = datasetSource.getReader();
                String[] fields = Dataset.getFieldNames(reader);

                
                ArrayList groupsList = getGroupList(new ArrayList(), groupSource, true);
                int groupSourceNumber = groupsList.size();

                int[] rows = new int[groupSourceNumber + 1];
                Arrays.fill(rows, 20);

                
                int[] columns = new int[fields.length];
                Arrays.fill(columns, 80);
                
                table = new Table(rows, columns);

                
                ArrayList headerVector = new ArrayList();

                for (int i = 0; i < fields.length; i++) {
                    headerVector.add(fields[i]);
                }

                ArrayList headerList = new ArrayList();
                headerList = newColumnHeader(headerList, headerVector, groupSource);

                for (int i = 0; i < headerList.size(); i++) {
                    Label label = new Label(headerList.get(i).toString());
                    label.setBorder(new Border(1, Color.black));
                    table.add(label, 0, i);
                }

                
                RowPanel rowPanel = new RowPanel();
                table.add(getRowPanel(rowPanel, headerList, groupsList, groupSource, true,
                        groupSourceNumber));
            }
        }

        return table;
    }

    /**
     * 通过拖动rowNodeSource节点生成 表格
     * @param rowNodeSource RowNodeSource
     * @return Table
     */
    public static Table getTable(RowNodeSource rowNodeSource) {
        Table table = null;

        if (rowNodeSource == null) {
            return null;
        } else {
            DatasetNodeSource datasetNodeSource = getNearestDatasetSource(rowNodeSource);
            NodeSource parent = datasetNodeSource.getParent();

//            if (!(parent instanceof RootNodeSource)) {
//                return null;
//            } else {
                GroupNodeSource groupSource = getFirstNodeSource(datasetNodeSource, rowNodeSource);

                if (groupSource != null) {
                    ArrayList groupsList = new ArrayList();
                    configGroupList(groupsList, rowNodeSource, groupSource);

                    DatasetReader reader = datasetNodeSource.getReader();

                    String[] fields = Dataset.getFieldNames(reader);

                    int groupSourceNumber = groupsList.size();

                    int[] rows = new int[groupSourceNumber + 2];
                    Arrays.fill(rows, 20);

                    
                    int[] columns = new int[fields.length ];
                    Arrays.fill(columns, 80);
                    
                    table = new Table(rows, columns);
                    table.setNodePath(reader.getName());

                    
                    ArrayList headerVector = new ArrayList();

                    for (int i = 0; i < (groupsList.size() - 1); i++) {
                        headerVector.add(((NodeSource) groupsList.get(i)).getTagName());
                    }

                    for (int i = 0; i < fields.length; i++) {
                    	 if (!headerVector.contains(fields[i])) {
                             headerVector.add(fields[i]);
                         }
                       
                    }

                    ArrayList headerList = new ArrayList();

                    for (int i = 0; i < headerVector.size(); i++) {
                        headerList.add(headerVector.get(i));
                    }

                    //                headerList = newColumnHeader(headerList, headerVector, groupSource);
                    for (int i = 0; i < headerVector.size(); i++) {
                        Label label = new Label(headerVector.get(i).toString());
                        label.setBorder(new Border(1, Color.black));
                        table.add(label, 0, i);
                    }

                    
                    RowPanel rowPanel = new RowPanel();
                    table.add(getRowPanel(rowPanel, headerList, groupsList, groupSource, true,
                            groupSourceNumber));

                    
                    Label zjLabel = new Label(App.messages.getString("res.406"));
                    zjLabel.setBorder(new Border(1, Color.black));
                    table.add(zjLabel, rows.length - 1, 0, groupsList.size() - 1, 1);

                    for (int i = groupsList.size() - 1; i < headerVector.size(); i++) {
                        Label text = new Label();
                        text.setBorder(new Border(1, Color.black));
                        table.add(text, rows.length - 1, i);
                    }

                    return table;
                } else {
                    return getNormalTable(rowNodeSource);
                }
      //      }
        }
    }

    /**
     * 用于groupNodeSource和rowNodeSource返回普通表格
     * @param source NodeSource
     * @return Table
     */
    public static Table getNormalTable(NodeSource source) {
        if (source instanceof GroupNodeSource) {
            DatasetReader reader = getNearestDatasetSource(source).getReader();
            String[] fields = Dataset.getFieldNames(reader);

            
            ArrayList headerVector = new ArrayList();

            for (int i = 0; i < fields.length; i++) {
                headerVector.add(fields[i]);
            }

            GroupNodeSource groupSource = (GroupNodeSource) source;
            String firstColumn = groupSource.getGroup().getField();
            headerVector.remove(firstColumn);

            int[] rows = new int[] {
                    20,
                    20
                };
            int[] columns = new int[fields.length];
            Arrays.fill(columns, 80);

            Table table = new Table(rows, columns);
            table.setNodePath(reader.getName());

            
            GroupNodeSource group = (GroupNodeSource) source;
            Label label = new Label(group.getTagName());
            table.add(label, 0, 0, 1, 1);
            label.setBorder(new Border());

            for (int i = 0; i < headerVector.size(); i++) {
                label = new Label(headerVector.get(i).toString());
                label.setBorder(new Border());
                table.add(label, 0, i + 1, 1, 1);
            }

            
            RowPanel rowPanel = new RowPanel();
            rowPanel.setNodePath(group.getGroup().getField());
            rowPanel.setCell(new Cell(1, 0, fields.length, 1));

            Text text = new Text();
            rowPanel.add(text, 1, 0, 1, 1);
            text.setVariable("=$" + group.getGroup().getField());
            text.setBorder(new Border());

            for (int i = 0; i < headerVector.size(); i++) {
                text = new Text();
                text.setBorder(new Border());
                text.setVariable("=$." + headerVector.get(i));
                rowPanel.add(text, 1, i + 1, 1, 1);
            }

            table.add(rowPanel);

            return table;
        } else if (source instanceof RowNodeSource) {
            DatasetNodeSource datasetSource = getNearestDatasetSource(source);
            DatasetReader reader = datasetSource.getReader();
            String[] fields = Dataset.getFieldNames(reader);

            int[] rows = new int[] {
                    20,
                    20,
                    20
                };
            int[] columns = new int[fields.length];
            Arrays.fill(columns, 80);

            Table table = new Table(rows, columns);
            table.setNodePath(reader.getName());

            Label label = null;
            Text text = null;
            RowPanel rowPanel = new RowPanel();
            rowPanel.setNodePath("Row");
            rowPanel.setCell(new Cell(1, 0, columns.length, 1));

            for (int i = 0; i < columns.length; i++) {
                label = new Label(fields[i]);
                table.add(label, 0, i, 1, 1);
                label.setBorder(new Border());

                text = new Text();
                text.setVariable("=$." + fields[i]);
                text.setBorder(new Border());
                rowPanel.add(text, 1, i, 1, 1);
            }

            table.add(rowPanel);

            
            Label zjLabel = new Label(App.messages.getString("res.406"));
            zjLabel.setBorder(new Border(1, Color.black));
            table.add(zjLabel, rows.length - 1, 0, 1, 1);

            for (int i = 1; i < columns.length; i++) {
                zjLabel = new Label();
                zjLabel.setBorder(new Border(1, Color.black));
                table.add(zjLabel, rows.length - 1, i);
            }

            return table;
        }

        return null;
    }

    /**
     * GroupNodeSource 一定在DatasetNodeSource节点下
     * @param dataseNodeSource DatasetNodeSource
     * @return GroupNodeSource
     */
    public static GroupNodeSource getFirstNodeSource(DatasetNodeSource dataseNodeSource) {
        ArrayList children = dataseNodeSource.getChildren();

        for (int i = 0; i < children.size(); i++) {
            Object o = children.get(i);

            if (o instanceof GroupNodeSource) {
                return (GroupNodeSource) o;
            }
        }

        return null;
    }

    /**
     * 从datasetNode节点算起到rowNode 这条路径的上最接近datasetNode 的groupNode节点
     * @param dataseNodeSource DatasetNodeSource
     * @param endNodeSource NodeSource
     * @return GroupNodeSource
     */
    public static GroupNodeSource getFirstNodeSource(DatasetNodeSource dataseNodeSource,
        NodeSource endNodeSource) {
        ArrayList children = dataseNodeSource.getChildren();

        for (int i = 0; i < children.size(); i++) {
            Object o = children.get(i);

            if (o instanceof GroupNodeSource) {
                if (isAncestor((GroupNodeSource) o, endNodeSource)) {
                    return (GroupNodeSource) o;
                }
            }
        }

        return null;
    }

    /**
     * 判断ancestor 是不是 child 的祖先
     * @param ancestor NodeSource
     * @param child NodeSource
     * @return boolean
     */
    private static boolean isAncestor(NodeSource ancestor, NodeSource child) {
        ArrayList v = ancestor.getChildren();

        if (v.size() > 0) {
            boolean b = false;

            for (int i = 0; i < v.size(); i++) {
                NodeSource ns = (NodeSource) v.get(i);

                if (ns.equals(child)) {
                    b = true;

                    break;
                } else {
                    b = isAncestor(ns, child);

                    if (b) {
                        break;
                    }
                }
            }

            return b;
        }

        return false;
    }

    /**
     * 列头名称排序
     * @param list ArrayList
     * @param fields ArrayList
     * @param groupSource GroupNodeSource
     * @return ArrayList
     */
    public static ArrayList newColumnHeader(ArrayList list, ArrayList fields,
        GroupNodeSource groupSource) {
        String field = groupSource.getGroup().getField();
        list.add(groupSource.getGroup().getField());
        fields.remove(field);

        ArrayList child = groupSource.getChildren();
        Iterator it = child.iterator();
        boolean b = false;

        while (it.hasNext()) {
            Object o = it.next();

            if (o instanceof GroupNodeSource) {
                GroupNodeSource _g = (GroupNodeSource) o;
                newColumnHeader(list, fields, _g);
            } else {
                b = true;

                break;
            }
        }

        if (b) {
            for (int i = 0; i < fields.size(); i++) {
                list.add(fields.get(i));
            }
        }

        return list;
    }

    /**
     * 判断GroupNodeSource是否含有Row
     * @param groupNodeSource GroupNodeSource
     * @return boolean
     */
    public static boolean hasRow(GroupNodeSource groupNodeSource) {
        boolean b = false;
        ArrayList v = groupNodeSource.getChildren();

        for (int i = 0; i < v.size(); i++) {
            Object o = v.get(i);

            if (o instanceof RowNodeSource) {
                b = true;

                break;
            }
        }

        return b;
    }

    /**
     * 以GroupNodeSource为起点寻找子孙GroupNodeSource以及最后的RowNodeSource
     * @param list ArrayList
     * @param groupSource NodeSource  起始节点为GroupNodeSource
     * @param containNext boolean
     * @return ArrayList
     */
    public static ArrayList getGroupList(ArrayList list, NodeSource groupSource, boolean containNext) {
        list.add(groupSource);

        ArrayList v = groupSource.getChildren();

        for (int i = 0; i < v.size(); i++) {
            NodeSource source = (NodeSource) v.get(i);

            if (source instanceof GroupNodeSource) {
                if (containNext) {
                    list = getGroupList(list, source, containNext);
                }
            } else if (source instanceof RowNodeSource) {
                list.add(source);
            }
        }

        return list;
    }

    /**
     * 以RowNodeSource为起点寻找子孙GroupNodeSource以及最后的GroupNodeSource
     * 由上向上
     * @return ArrayList
     */
    private static void configGroupList(ArrayList list, NodeSource rowNodeSource,
        NodeSource groupNodeSource) {
        if (!rowNodeSource.getParent().equals(groupNodeSource)) {
            configGroupList(list, rowNodeSource.getParent(), groupNodeSource);
        } else {
            list.add(groupNodeSource);
        }

        list.add(rowNodeSource);
    }

    /**
     * 取得构建好的rowPanel,包括row(从选中的groupNodeSource开始构建)
     * 此时不考虑有兄弟groupSource情况,只有父子顺序,所以最多只有一个row(最后的)
     * @param groupSource GroupNodeSource
     * @param containNext boolean
     * @return RowPanel
     */
    public static RowPanel getRowPanel(RowPanel rowPanel, ArrayList headerFields,
        ArrayList groupsList, NodeSource source, boolean containNext, int rowNumber) {
        int index = groupsList.indexOf(source);
        int offIndex = groupsList.size() - index;
        rowPanel.setCell(new Cell(1, 0, headerFields.size(), offIndex));

        if (source instanceof GroupNodeSource) {
            //          String datasetTagName=getNearestDatasetSource(groupSource).getTagName();
            GroupNodeSource groupSource = (GroupNodeSource) source;
            rowPanel.setNodePath(groupSource.getGroup().getField());

            Label text = null;

            Label label = new Label();
            label.setText(App.messages.getString("res.407"));
            label.setBorder(new Border());
            rowPanel.add(label, offIndex, index + 1, groupsList.size() - (index + 1), 1);

            for (int i = groupsList.size(); i < headerFields.size(); i++) {
                text = new Label();
                text.setBorder(new Border());
                rowPanel.add(text, offIndex, i, 1, 1);
            }
        } else if (source instanceof RowNodeSource) {
            rowPanel.setNodePath("Row");

            Text text = null;
            int height = rowNumber;

            for (int i = 0; i < headerFields.size(); i++) {
                text = new Text();

                if (i < (groupsList.size() - 1)) {
                    text.setVariable("=$" + headerFields.get(i).toString());
                    text.setPrintStyle("united-level:" + (i + 1) + ";");
                } else {
                    text.setVariable("=$." + headerFields.get(i).toString());
                }

                text.setBorder(new Border());
                rowPanel.add(text, 1, i, 1, height);

                if (height >= 2) {
                    height = height - 1;
                }
            }
        }

        if (source instanceof GroupNodeSource) {
            ArrayList v = source.getChildren();
            Iterator it = v.iterator();

            while (it.hasNext()) {
                Object o = it.next();

                if (groupsList.contains(o)) {
                    if (o instanceof GroupNodeSource) {
                        if (containNext) {
                            RowPanel _rowPanel = new RowPanel();
                            RowPanel row = getRowPanel(_rowPanel, headerFields, groupsList,
                                    (NodeSource) o, containNext, rowNumber);
                            rowPanel.add(row);
                        }
                    } else {
                        RowPanel _rowPanel = new RowPanel();
                        RowPanel row = getRowPanel(_rowPanel, headerFields, groupsList,
                                (NodeSource) o, false, rowNumber);
                        rowPanel.add(row);
                    }
                }
            }
        }

        return rowPanel;
    }

    /**
     * 从最近父节点中取得DatasetNodeSource
     * @param source NodeSource
     * @return DatasetNodeSource
     */
    public static DatasetNodeSource getNearestDatasetSource(NodeSource gorupSource) {
        NodeSource parent = gorupSource.getParent();

        if (parent instanceof DatasetNodeSource) {
            return (DatasetNodeSource) parent;
        } else {
            return getNearestDatasetSource(parent);
        }
    }
}
