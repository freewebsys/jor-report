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

public interface WizardCellEditor {
  public abstract boolean isChanged() ;
  public abstract void applyChange() ;
  public abstract void checkAvailable() throws Exception ;
  public abstract Object getContent() throws Exception;
  public abstract Component getEditorComponent();

}
