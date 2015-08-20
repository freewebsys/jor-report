package jatools.designer.wizard.crosstab;

import jatools.data.reader.DatasetReader;
import jatools.designer.wizard.BuilderContext;

import java.awt.BorderLayout;

import javax.swing.JPanel;



/**
 * <p>Title:CrossHeaderSelector </p>
 * <p>Description: 交叉报表行列表头选择</p
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Jatools</p>
 * @author Jiang Dehua
 * @version 1.0
 */
public class CrossHeaderSelector extends JPanel {
    private DatasetReader oldReader;
    private CrossReaderPanel crossPanel;

    /**
     * Creates a new CrossHeaderSelector object.
     */
    public CrossHeaderSelector() {
        super();
        crossPanel = new CrossReaderPanel();
        this.setLayout(new BorderLayout());
        this.add(crossPanel, BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     */
    public void setReader(DatasetReader reader) {
        if (oldReader == null) {
            oldReader = reader;
            setModel(oldReader);
        } else {
            if (oldReader.equals(reader)) {
                return;
            } else {
                this.oldReader = reader;
                setModel(oldReader);
            }
        }
    }

    private void setModel(DatasetReader reader) {
        crossPanel.setCachedReader(reader);
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void apply(BuilderContext context) {
        crossPanel.apply(context);
    }
}
