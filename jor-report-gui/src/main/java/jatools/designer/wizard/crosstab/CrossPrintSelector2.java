package jatools.designer.wizard.crosstab;

import jatools.designer.property.editor.printstyle.CrossTabRulePanel;
import jatools.designer.wizard.BuilderContext;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @author Jiang Dehua
 * @version 1.0
 */
public class CrossPrintSelector2 extends CrossTabRulePanel{

  public CrossPrintSelector2() {
    super();
  }
  public void apply(BuilderContext context) {
   String css=(super.getRule()==null)?"":super.getRule().toString();
   context.setValue(CrossTabStyler.PRINTCSS, css);
 }
}
