package jatools.swingx.wizard;

import javax.swing.event.ChangeListener;


/**
 * 输出精灵
 * 主要用于监听来自编辑器组件的变化，并生成正确的输出，并将输出属性的变化，通知给监听器
 * @version $Revision: 1.1 $
 * @author $author$
 */
public interface WizardOutput {
    /**
     * 取得唯一的输出类型
     *
     * @return 输出类型
     */
    public int getOutputType();

    public void checkAvailable() throws Exception;

    /**
     * 取得输出
     *
     * @return 输出对象
     */
    public Object output() throws Exception;

    /**
     * 注册变化监听器
     *
     * @param lst 监听器
     */
    public void addChangeListener(ChangeListener lst);

    /**
     * 注销变化监听器
     *
     * @param lst 监听器
     */
    public void removeChangeListener(ChangeListener lst);
}
