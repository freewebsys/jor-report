package jatools.swingx.wizard;

import javax.swing.event.ChangeListener;





/**
 * 输入精灵
 *
 * 用于定义精灵的输入特性，输入精灵是输出精灵的动作机，主要用来临听输出精灵的属性变化
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public interface WizardInput extends ChangeListener {
    /**
     * 取得可以接受的输入类型
     *
     * @return 可接受类型
     */
    public int[] getInputTypes();

    /**
     * 连接到输出精灵，以便监听其变化
     *
     * @param output 输出精灵
     *
     * @return true/false 成功/失败
     */
    public boolean connect(WizardOutput output);
}
