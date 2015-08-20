package jatools.engine.css;

import jatools.core.view.StyleAttributes;
import jatools.engine.css.rule.CrossTabRule;
import jatools.engine.css.rule.LayoutRule;
import jatools.engine.css.rule.PageRule;
import jatools.engine.css.rule.RepeatRule;
import jatools.engine.css.rule.TextRule;
import jatools.engine.css.rule.VisibleRule;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PrintStyle {
    public static PrintStyle NULL_STYLE = nullStyle();
    public final static String REPEAT_MODE = "repeat-mode";
    public final static String REPEAT_GAP_X = "repeat-gap-x";
    public final static String REPEAT_GAP_Y = "repeat-gap-y";
    public final static String REPEAT_COUNT_X = "repeat-count-x";
    public final static String REPEAT_COUNT_Y = "repeat-count-y";
    public final static String REPEAT_FLOW_FIRST = "repeat-flow-first";
    public final static String FOOTER = "footer";
    public final static String WEIGHT_X = "weight-x";
    public final static String WEIGHT_Y = "weight-y";
    public final static String WIDTH = "width";
    public final static String HEIGHT = "height";
    public final static String RIGHT = "right";
    public final static String BOTTOM = "bottom";
    public final static String NEW_PAGE_X = "new-page-x";
    public final static String NEW_PAGE_Y = "new-page-y";
    public final static String BREAK_PAGE_NEXT = "break-page-next";
    public final static String PRINT_MODE = "print-mode";
    public final static String VISIBLE = "visible";
    public final static String ALIGN_X = "align-x";
    public final static String UNITED_LEVEL = "united-level";
    public final static String AUTO_SIZE = "auto-size";
    public final static String MAX_WIDTH = "max-width";
    public final static String ALIGN_Y = "align-y";
    public static final String REPEAT_OVERFLOW = "repeat-overflow";
    public static final String REPEAT_MOD_COUNT = "repeat-mod-count";
    public static final String CROSSTAB_TOP_HEADER_VISIBLE = "crosstab-top-header-visible";
    public static final String CROSSTAB_LEFT_HEADER_VISIBLE = "crosstab-left-header-visible";
    public static final String CROSSTAB_HEADER_ROWS = "crosstab-header-rows";
    public static final String CROSSTAB_HEADER_COLUMNS = "crosstab-header-columns";
    public static final String CROSSTAB_PAGE_WRAP = "crosstab-page-wrap";
    public static final String LEFT = "left";
    public static final String TOP = "top";
    StyleAttributes styles;
    private RepeatRule repeatRule;
    private boolean repeatParsed;
    private String styleText;
    private boolean textParsed;
    private TextRule textRule;
    private boolean layoutParsed;
    private LayoutRule layoutRule;
    private boolean pageParsed;
    private PageRule pageRule;
    private boolean visibleParsed;
    private VisibleRule visibleRule;
    private CrossTabRule crosstabRule;
    private boolean crosstabParsed;


    /**
     * Creates a new PrintStyle object.
     *
     * @param styleText DOCUMENT ME!
     */
    public PrintStyle(String styleText) {
        this.styleText = styleText;
    }

    private static PrintStyle nullStyle() {
        PrintStyle style = new PrintStyle(null);
        style.layoutParsed = true;
        style.pageParsed = true;
        style.repeatParsed = true;
        style.textParsed = true;
        style.visibleParsed = true;
        style.crosstabParsed = true;
       

        return style;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RepeatRule getRepeatRule() {
        if (!this.repeatParsed) {
            StyleAttributes _styles = this.getStyles();

            RepeatRule rule = new RepeatRule();

            rule.gapX = _styles.getCSSValue(REPEAT_GAP_X);
            rule.gapY = _styles.getCSSValue(REPEAT_GAP_Y);
            rule.maxCountX = _styles.getCSSValue(REPEAT_COUNT_X);
            rule.maxCountY = _styles.getCSSValue(REPEAT_COUNT_Y);
            rule.flowFirst = _styles.getCSSValue(REPEAT_FLOW_FIRST);
            rule.overflow = _styles.getCSSValue(REPEAT_OVERFLOW);
            rule.modCount = _styles.getCSSValue(REPEAT_MOD_COUNT);

            if (!rule.isNull()) {
                this.repeatRule = rule;
            }

            this.repeatParsed = true;
        }

        return this.repeatRule;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TextRule getTextRule() {
        if (!this.textParsed) {
            StyleAttributes _styles = this.getStyles();

            TextRule rule = new TextRule();

            rule.unitedLevel = _styles.getCSSValue(UNITED_LEVEL);
            rule.autoSize = _styles.getCSSValue(AUTO_SIZE);
            rule.maxWidth = _styles.getCSSValue(MAX_WIDTH);

            if (!rule.isNull()) {
                this.textRule = rule;
            }

            this.textParsed = true;
        }

        return this.textRule;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LayoutRule getLayoutRule() {
        if (!this.layoutParsed) {
            StyleAttributes _styles = this.getStyles();

            LayoutRule rule = new LayoutRule();

            rule.alignX = _styles.getCSSValue(ALIGN_X);
            rule.alignY = _styles.getCSSValue(ALIGN_Y);
            rule.width = _styles.getCSSValue(WIDTH);
            rule.height = _styles.getCSSValue(HEIGHT);
            rule.bottom = _styles.getCSSValue(BOTTOM);
            rule.right = _styles.getCSSValue(RIGHT);
            rule.top = _styles.getCSSValue(TOP);
            rule.left = _styles.getCSSValue(LEFT);
           
            if (!rule.isNull()) {
                this.layoutRule = rule;
            }

            this.layoutParsed = true;
        }

        return this.layoutRule;
    }

    private StyleAttributes getStyles() {
        if (this.styles == null) {
            this.styles = new StyleAttributes(this.styleText);
        }

        return this.styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PageRule getPageRule() {
        if (!this.pageParsed) {
            StyleAttributes _styles = this.getStyles();
            PageRule rule = new PageRule();

            rule.newPageX = _styles.getCSSValue(NEW_PAGE_X);
            rule.newPageY = _styles.getCSSValue(NEW_PAGE_Y);

            rule.forceBreak = _styles.getCSSValue(BREAK_PAGE_NEXT);

            if (!rule.isNull()) {
                this.pageRule = rule;
            }

            this.pageParsed = true;
        }

        return this.pageRule;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public VisibleRule getVisibleRule() {
        if (!this.visibleParsed) {
            StyleAttributes _styles = this.getStyles();
            VisibleRule rule = new VisibleRule();

            rule.printMode = _styles.getCSSValue(PRINT_MODE);
            rule.visible = _styles.getCSSValue(VISIBLE);

            if (!rule.isNull()) {
                this.visibleRule = rule;
            }

            this.visibleParsed = true;
        }

        return this.visibleRule;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CrossTabRule getCrossTabRule() {
        if (!this.crosstabParsed) {
            CrossTabRule rule = new CrossTabRule();
            StyleAttributes _styles = this.getStyles();
            rule.leftHeaderVisible = _styles.getCSSValue(CROSSTAB_LEFT_HEADER_VISIBLE);
            rule.topHeaderVisible = _styles.getCSSValue(CROSSTAB_TOP_HEADER_VISIBLE);
            rule.pageWrap = _styles.getCSSValue(CROSSTAB_PAGE_WRAP);
            rule.headerRows = _styles.getInt(CROSSTAB_HEADER_ROWS, -1);
            rule.headerColumns = _styles.getInt(CROSSTAB_HEADER_COLUMNS, -1);

            if (!rule.isNull()) {
                this.crosstabRule = rule;
            }

            crosstabParsed = true;
        }

        return this.crosstabRule;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
   
}
