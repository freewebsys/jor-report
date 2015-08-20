package jatools.swingx.wizard;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EZSoft.</p>
 * @author 周文军
 * @version 1.0
 */
import java.awt.Component;


/**
 * 编辑精灵
 *
 * <p>用于定义编辑器的组件特性，如取编辑器组件及焦点管理</p>
 * @version $Revision: 1.1 $
 * @author $author$
 */
public interface WizardEditor {
    /**
     * 得到编辑器组件
     *
     * @return 编辑器组件
     */
    public Component getEditorComponent();

    /**
     * 通知编辑器焦点要离开了
     *
     * @throws Exception 如果本编辑器还没有准备好数据
     */
    public void leave() throws Exception;

    /**
     * 通知编辑器焦点要进入了
     *
     * @throws Exception 如果本编辑器还没准备好进入
     */
    public void enter() throws Exception;
}
