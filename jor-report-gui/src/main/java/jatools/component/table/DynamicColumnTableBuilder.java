package jatools.component.table;



import jatools.accessor.ProtectPublic;
import jatools.component.Label;
import jatools.component.Text;
import jatools.dataset.Dataset;
import jatools.designer.JatoolsException;


/**
 *
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class DynamicColumnTableBuilder implements ProtectPublic {
    Table table;
    Dataset dataset;
    String fields;
    String titles;
    String classes;

    /**
         * @param table
         * @param dataset
         */
    public DynamicColumnTableBuilder(Table table, Dataset dataset) {
        this.table = table;
        this.dataset = dataset;
    }

    /**
    * DOCUMENT ME!
    *
    * @param table 表格对象,必须为两行,一列,且第一个单元格为标签对象,第二行单元格为文本对象
    * @param dataset 数据集对象
    * @param fields 选中数据集的哪些字段来显示
    * @param titles 表头显示内容
    * @param classes 引用样式类
    * @throws JatoolsException
    */
    public void build() throws JatoolsException {
        if (table == null) {
            throw new JatoolsException("不能创建动态列表格,源表格为空!");
        } else if (table.getRowCount() >2 ||      (table.getColumnCount() != 1)) {
            throw new JatoolsException("不能创建动态列表格,源表格必须为两行一列!");
        } /*else if (!(table.getComponent(0, 0) instanceof Label) ||
            !(table.getComponent(1, 0) instanceof Text)) {
        throw new JatoolsException("不能创建动态列表格,[0,0]单元格不为标签对象,或[1,0]单元格不为文本!");
        }*/
        if (dataset == null) {
            throw new JatoolsException("不能创建动态列表格,数据集为空!");
        }

        String[] fieldsArray = null;

        if (fields != null) {
            fieldsArray = fields.split(";");
        } else {
            // 如果没指定显示列,显示所有dataset中的列
            fieldsArray = new String[dataset.getColumnCount()];

            for (int i = 0; i < fieldsArray.length; i++) {
                fieldsArray[i] = dataset.getColumnName(i);
            }
        }

        String[] titlesArray = null;

        if (titles != null) {
            titlesArray = titles.split(";");

            if (titlesArray.length != fieldsArray.length) {
                throw new JatoolsException("不能创建动态列表格,指定的列数与标题数不一致.");
            }
        } else {
            // 如果没指定抬头,则以列名作标题
            titlesArray = fieldsArray;
        }

        String[] classesArray = null;

        if (classes != null) {
            classesArray = classes.split(";");

            if (classesArray.length != fieldsArray.length) {
                throw new JatoolsException("不能创建动态列表格,指定的样式类个数与显示列数不一致.");
            }
        }

        // 开始创建
        // 重新确定列数
        for (int i = 0; i < (fieldsArray.length - 1); i++) {
            table.insertColumnAfter(0, table.getColumnWidth(0));
        }

        if (table.getRowCount() == 2) {
            Label firstLabel = (Label) table.getComponent(0, 0);
            Text firstText = (Text) table.getComponent(1, 0);
            RowPanel rowPanel = (RowPanel) firstText.getParent();

            try {
                for (int i = 0; i < titlesArray.length; i++) {
                    Label label = null;
                    Text text = null;

                    if (i == 0) {
                        label = firstLabel;
                        text = firstText;
                    } else {
                        label = (Label) firstLabel.clone();
                        table.add(label, 0, i);

                        text = (Text) firstText.clone();
                        rowPanel.add(text, 1, i);
                    }

                    label.setText(titlesArray[i]);
                    text.setVariable("=$." + fieldsArray[i]);

//                    if (classesArray != null) {
//                        text.setStyleClass(classesArray[i]);
//                    }
                }
            } catch (CloneNotSupportedException e) {
                throw new JatoolsException(e);
            }
        } else {
            Text firstText = (Text) table.getComponent(0, 0);
            RowPanel rowPanel = (RowPanel) firstText.getParent();

            try {
                for (int i = 0; i < titlesArray.length; i++) {
                    Text text = null;

                    if (i == 0) {
                        text = firstText;
                    } else {
                        text = (Text) firstText.clone();
                        rowPanel.add(text, 0, i);
                    }

                    text.setVariable("=$." + fieldsArray[i]);

//                    if (classesArray != null) {
//                        text.setStyleClass(classesArray[i]);
//                    }
                }
            } catch (CloneNotSupportedException e) {
                throw new JatoolsException(e);
            }
        }

        table.validate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param fields DOCUMENT ME!
     */
    public void setFields(String fields) {
        this.fields = fields;
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * DOCUMENT ME!
     *
     * @param titles DOCUMENT ME!
     */
    public void setTitles(String titles) {
        this.titles = titles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param classes DOCUMENT ME!
     */
    public void setClasses(String classes) {
        this.classes = classes;
    }
}
