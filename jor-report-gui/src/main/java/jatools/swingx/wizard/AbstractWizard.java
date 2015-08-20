package jatools.swingx.wizard;

import jatools.designer.App;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public abstract class AbstractWizard implements WizardInput, WizardOutput, WizardEditor {
    public static final int NULL_TYPE = -1;
    public static final int JDBC_CONNECTION = 0;
    public static final int JDBC_TABLE_NAME_STRING_LIST = 1;
    public static final int JDBC_TABLE_NAME_VECTOR = 2;
    public static final int JDBC_RESULT_LIST_VECTOR = 3;
    public static final int JDBC_SQL_SELECT_WHERE_EXPRESSION = 4;
    public static final int JDBC_TOTAL_FIELD = 5;
    public static final int JDBC_SQL_ORDER_BY_EXPRESSION = 6;
    public static final int JDBC_SQL_FULL_EXPRESSION = 6;

    
    private ArrayList listeners = new ArrayList();
    private Map wizard_type = new HashMap();
    protected boolean upperLinkChanged = true;
    protected WizardOutput lastUpperLink;



    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract WizardCellEditor getCellEditor();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected WizardOutput[] getOutputWizard() {
        return (WizardOutput[]) wizard_type.keySet().toArray(new WizardOutput[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRequiredInput(int type) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param output DOCUMENT ME!
     */
    protected abstract void initEditor();

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public final void enter() throws Exception {
        
        WizardOutput[] wos = getOutputWizard();

        String error = null;

        for (int i = 0; i < wos.length; i++) {
            if (this.isRequiredInput(wos[i].getOutputType())) {
                try {
                    wos[i].checkAvailable();
                } catch (Exception ex) {
                    error = ex.getMessage();

                    try {
                        ((AbstractWizard) wos[i]).enter();
                    } catch (Exception e) {
                        error = e.getMessage();
                    }
                }
            }
        }

        if (error != null) {
            throw new Exception(error);
        }

        
        if (upperLinkChanged) {
            initEditor();
            getCellEditor().applyChange();
        }

        upperLinkChanged = false;
    }

    /**
     * 注册变化监听器
     *
     * @param lst 监听器
     */
    public void addChangeListener(ChangeListener lst) {
        listeners.add(lst);
    }

    /**
     * 注销变化监听器
     *
     * @param lst 监听器
     */
    public void removeChangeListener(ChangeListener lst) {
        listeners.remove(lst);
    }

    /**
     * DOCUMENT ME!
     */
    public void fireChangeListener() {
        ChangeEvent evt = new ChangeEvent(this);

        for (int i = 0; i < listeners.size(); i++) {
            ChangeListener lst = (ChangeListener) listeners.get(i);
            lst.stateChanged(evt);
        }
    }

    /**
     * 连接到输出精灵，以便监听其变化
     *
     * @param output 输出精灵
     *
     * @return true/false 成功/失败
     */
    public boolean connect(WizardOutput output) {
        int[] its = getInputTypes();

        int ot = output.getOutputType();

        boolean acceptable = false;

        for (int i = 0; i < its.length; i++) {
            if (its[i] == ot) {
                acceptable = true;

                break;
            }
        }

        if (acceptable) {
            output.addChangeListener(this);
            wizard_type.put(output, new Integer(ot));
            lastUpperLink = output;
        }

        return acceptable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param outputWizard DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int getConnectionType(Object outputWizard) {
        Object type = wizard_type.get(outputWizard);

        if (type == null) {
            return -1;
        } else {
            return ((Integer) type).intValue();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public final void leave() throws Exception {
        if (getCellEditor().isChanged()) {
            fireChangeListener();
            getCellEditor().applyChange();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object output() throws Exception {
        return getCellEditor().getContent();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public final void checkAvailable() throws Exception {
        if (upperLinkChanged) {
            throw new Exception(App.messages.getString("res.39")); //
        }

        getCellEditor().checkAvailable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent evt) {
        if (evt.getSource() instanceof WizardOutput) {
            upperLinkChanged = true;
        }

        this.fireChangeListener();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getEditorComponent() {
        return getCellEditor().getEditorComponent();
    }

    /** 没有实现的方法
    public boolean isTypeAvailable(int type);
    public Object output(int type);
    public Component getEditor();
    **/
}
