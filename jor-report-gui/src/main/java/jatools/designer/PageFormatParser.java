package jatools.designer;

import jatools.PageFormat;

import java.awt.print.Paper;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageFormatParser {
    public static final double DOTS_PER_MM = (double) 96 / (double) 25.4;
    private boolean portrait;
    private int width;
    private int height;
    private int top;
    private int left;
    private int bottom;
    private int right;
    private String line;

    /**
     * Creates a new PageFormatParser object.
     *
     * @param line DOCUMENT ME!
     */
    public PageFormatParser(String line) {
        this.line = line;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PageFormat parse() {
        String[] parts = this.line.split(";");

        for (int i = 0; i < parts.length; i++) {
            parse(parts[i]);
        }

        return new jatools.PageFormat(toAwt());
    }

    private static int toDots(double mmm) {
        try {
            return (int) (((DOTS_PER_MM * mmm) / 10.0));
        } catch (NumberFormatException e) {
        }

        return 0;
    }

    private java.awt.print.PageFormat toAwt() {
        java.awt.print.PageFormat format = new java.awt.print.PageFormat();
        int ori = portrait ? PageFormat.PORTRAIT : PageFormat.LANDSCAPE;

        int pw = toDots(this.width);
        int ph = toDots(this.height);

        int left = toDots(this.left);
        int top = toDots(this.top);
        int right = toDots(this.right);
        int bottom = toDots(this.bottom);

        int x;
        int y;
        int iw;
        int ih;

        x = left;
        y = top;
        iw = pw - right - left;
        ih = ph - top - bottom;

        format.setOrientation(ori);

        Paper p = new Paper();
        p.setImageableArea(x, y, iw, ih);

        p.setSize(pw, ph);

        format.setPaper(p);

        return format;
    }

    private void parse(String np) {
        if (np.startsWith("width:")) {
            this.width = Integer.parseInt(np.substring(np.indexOf(':') + 1));
        } else if (np.startsWith("height:")) {
            this.height = Integer.parseInt(np.substring(np.indexOf(':') + 1));
        } else if (np.startsWith("top:")) {
            this.top = Integer.parseInt(np.substring(np.indexOf(':') + 1));
        } else if (np.startsWith("left:")) {
            this.left = Integer.parseInt(np.substring(np.indexOf(':') + 1));
        } else if (np.startsWith("bottom:")) {
            this.bottom = Integer.parseInt(np.substring(np.indexOf(':') + 1));
        } else if (np.startsWith("right:")) {
            this.right = Integer.parseInt(np.substring(np.indexOf(':') + 1));
        } else if (np.startsWith("orientation:")) {
            this.portrait = "1".equals(np.substring(np.indexOf(':') + 1));
        }
    }
}
