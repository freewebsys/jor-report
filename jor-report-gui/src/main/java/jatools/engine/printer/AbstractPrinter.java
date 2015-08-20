package jatools.engine.printer;

import jatools.component.BackgroundImageStyle;
import jatools.component.Component;
import jatools.component.ComponentConstants;
import jatools.core.view.AbstractView;
import jatools.core.view.View;
import jatools.data.reader.ArrayCursor;
import jatools.data.reader.Cursor;
import jatools.data.reader.NodeArrayCursor;
import jatools.designer.data.Hyperlink;
import jatools.dom.SimpleNodeList;
import jatools.engine.PrintConstants;
import jatools.engine.Printer;
import jatools.engine.css.CSSValue;
import jatools.engine.css.PrintStyle;
import jatools.engine.css.rule.PageRule;
import jatools.engine.css.rule.VisibleRule;
import jatools.engine.imgloader.ImageLoader;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.script.Context;
import jatools.engine.script.Script;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Node;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision$
 */
public abstract class AbstractPrinter implements Printer,
		PropertyChangeListener {
	private Component c;
	protected boolean done;
	private PrintStyle printStyle;
	private BackgroundImageStyle backgroundImageStyle;
	private boolean columnPrinting;
	private int localID;

	private boolean forcedBreak;

	public boolean isForcedBreak() {
		return forcedBreak;
	}

	public void setForcedBreak(boolean forcedBreak) {
		this.forcedBreak = forcedBreak;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param layout
	 *            DOCUMENT ME!
	 * @param script
	 *            DOCUMENT ME!
	 */
	public void requestFooterReservation(PrinterLayout layout, Script script) {
	}

	/**
	 * DOCUMENT ME!
	 */
	public void reset() {
		done = false;
	}

	protected void printTooltipText(Script script, AbstractView view,
			String tooltip) {
		if (tooltip != null) {
			tooltip = (String) script.evalTemplate(tooltip);

			if (tooltip != null) {
				view.setTooltipText(tooltip);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param script
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isVisible(Script script) {
		if (this.getPrintStyle().getVisibleRule() != null) {
			CSSValue visible = this.getPrintStyle().getVisibleRule().visible;

			if (visible != null) {
				visible.reset(script);

				return visible.isTrue();
			}
		}

		return true;
	}

	protected void printHyperlink(Script script, AbstractView view,
			Hyperlink link) {
		if ((link != null) && (link.getUrl() != null)) {
			Object url = script.evalTemplate(link.getUrl());

			if ((url != null) && !"".equals(url)) {
				String urltext = "target:" + link.getTarget() + ";url:"
						+ url.toString();
				view.setHyperlink(urltext);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param script
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isEveryPagePrint(Script script) {
		VisibleRule rule = getPrintStyle().getVisibleRule();

		if ((rule != null) && (rule.printMode != null)) {
			rule.printMode.reset(script);

			return rule.printMode.is("everypage");
		} else {
			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param script
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isForceBreak2(Script script) {
		PageRule rule = getPrintStyle().getPageRule();

		if ((rule != null) && (rule.forceBreak != null)) {
			rule.forceBreak.reset(script);

			return rule.forceBreak.isTrue();
		} else {
			return false;
		}
	}

	protected void resetLocation(Script script) {
		PageRule rule = this.getPrintStyle().getPageRule();

		if (rule != null) {
			rule.reset(script);

			CSSValue newPageY = rule.newPageY;

			if (newPageY != null) {
				int newY = getComponent().getY();

				if (newPageY.isPercent()) {
					float percent = newPageY.percentValue();
					newY = Math.round(getComponent().getY() * percent);
				} else {
					newY = (int) newPageY.floatValue();
				}

				getComponent().setY(newY);
			}

			CSSValue newPageX = rule.newPageX;

			if (newPageX != null) {
				int newX = getComponent().getX();

				if (newPageX.isPercent()) {
					float percent = newPageX.percentValue();
					newX = Math.round(getComponent().getX() * percent);
				} else {
					newX = (int) newPageX.floatValue();
				}

				getComponent().setX(newX);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param script
	 *            DOCUMENT ME!
	 * @param view
	 *            DOCUMENT ME!
	 * @param layout
	 *            DOCUMENT ME!
	 * @param down
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public View unitView(Script script, View view, PrinterLayout layout,
			boolean down) {
		return view;
	}

	protected Cursor createCursor(Context context, Component comp, int ori)
			throws JaxenException {
		Cursor c = null;

		if ((c == null) && (comp instanceof NodePathTarget)) {
			NodePathTarget var = (NodePathTarget) comp;

			String path = var.getNodePath();

			if (path != null) {
				if (path.startsWith("=")) {
					Object val = context.getScript().eval(path.substring(1));

					if (val instanceof Object[]) {
						c = new ArrayCursor((Object[]) val);
					} else if (val instanceof SimpleNodeList) {
						c = new NodeArrayCursor(
								(Node[]) ((SimpleNodeList) val)
										.toArray(new Node[0]),
								context);
					} else if (val instanceof Collection) {
						c = new ArrayCursor(((Collection) val).toArray());
					}
				} else {
					Node select = context.getScript().getNodeStack(ori)
							.getNode();

					XPath xpath = new DOMXPath(path);
					java.util.List result = xpath.selectNodes(select);
					c = new NodeArrayCursor(
							(Node[]) result.toArray(new Node[0]), context);
				}
			}
		}

		if (c == null) {
			c = new OneStepCursor();
		}

		return c;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param c
	 *            DOCUMENT ME!
	 * @param script
	 *            DOCUMENT ME!
	 */
	public void open(Component c, Script script) {
		this.c = c;

		if (c.getInitPrint() != null) {
			script.set("me", c);
			script.eval(c.getInitPrint());
		}

		createPrintStyle(c.getPrintStyle());

		if (c.getBackgroundImageStyle() != null) {
			this.backgroundImageStyle = new BackgroundImageStyle(
					c.getBackgroundImageStyle());
		}

		c.addPropertyChangeListener(this);
	}

	protected Printer[] prepareChildPrinters(Context context) {
		ArrayList printers = new ArrayList();

		Iterator it = this.c.getChildren().iterator();

		while (it.hasNext()) {
			Component c = (Component) it.next();
			printers.add(context.getPrinter(c));
		}

		return (Printer[]) printers.toArray(new Printer[0]);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Component getComponent() {
		return this.c;
	}

	protected void doBeforePrint(Script script) {
		String beforePrint = c.getBeforePrint();

		if (beforePrint != null) {
			if (script != null) {
				script.set("me", this.c);
			}

			script.eval(beforePrint);
		}
	}

	protected void doAfterPrint(Script script) {
		String afterPrint = c.getAfterPrint();

		if (afterPrint != null) {
			if (script != null) {
				script.set("me", this.c);
			}

			script.eval(afterPrint);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param context
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isDone(Context context) {
		return done;
	}

	protected void setBackgroundImageStyle(Script script, AbstractView view) {
		if (backgroundImageStyle != null) {
			ImageLoader loader = (ImageLoader) script
					.get(PrintConstants.IMAGE_LOADER);
			loader.load(backgroundImageStyle);
		}

		view.setBackgroundImageStyle(backgroundImageStyle);
	}

	protected void setLayoutRule(AbstractView view, Script script) {
		if (getPrintStyle().getLayoutRule() != null) {
			view.setLayoutRule(getPrintStyle().getLayoutRule().clone(script));
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param evt
	 *            DOCUMENT ME!
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == ComponentConstants.PROPERTY_PRINT_STYLE) {
			String style = (String) evt.getNewValue();

			createPrintStyle(style);
		} else if (evt.getPropertyName() == ComponentConstants.PROPERTY_BACKGROUND_IMAGE) {
			String style = (String) evt.getNewValue();

			if (style != null) {
				this.backgroundImageStyle = new BackgroundImageStyle(style);
			} else {
				this.backgroundImageStyle = null;
			}
		}
	}

	private void createPrintStyle(String style) {
		if (style != null) {
			setPrintStyle(new PrintStyle(style));
		} else {
			setPrintStyle(null);
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	public void close() {
		this.getComponent().removePropertyChangeListener(this);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public PrintStyle getPrintStyle() {
		return (printStyle != null) ? printStyle : PrintStyle.NULL_STYLE;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isColumnPrinting() {
		return columnPrinting;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param columnPrinting
	 *            DOCUMENT ME!
	 */
	public void setColumnPrinting(boolean columnPrinting) {
		this.columnPrinting = columnPrinting;
	}

	protected void setPrintStyle(PrintStyle printStyle) {
		this.printStyle = printStyle;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getLocalID() {
		return localID;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param localID
	 *            DOCUMENT ME!
	 */
	public void setLocalID(int localID) {
		this.localID = localID;
	}
}
