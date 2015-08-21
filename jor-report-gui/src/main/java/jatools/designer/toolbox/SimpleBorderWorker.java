package jatools.designer.toolbox;

import jatools.component.ComponentConstants;
import jatools.core.view.Border;
import jatools.core.view.BorderStyle;
import jatools.designer.Main;
import jatools.designer.peer.ComponentPeer;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class SimpleBorderWorker implements BorderWorker {
    private static SimpleBorderWorker instance;

    private SimpleBorderWorker() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param command DOCUMENT ME!
     */
    public void action(String command, String styletext) {
        ComponentPeer[] peers = (ComponentPeer[]) Main.getInstance().getActiveEditor()
                                                      .getReportPanel().getSelection();

        if (peers.length > 0) {
            BorderStyle style = BorderStyle.parse(styletext);

            peers[0].getOwner().openEdit();

            if (command == ALL) {
                doAll(peers, style);
            } else if (command == CLEAR) {
                doClear(peers);
            } else if (command == BorderWorker.LEFT) {
                doLeft(peers, style);
            } else if (command == BorderWorker.TOP) {
                doTop(peers, style);
            } else if (command == BorderWorker.BOTTOM) {
                doBottom(peers, style);
            } else if (command == BorderWorker.RIGHT) {
                doRight(peers, style);
            } else if (command == FRAME) {
                doAll(peers, style);
            } else if (command == BorderWorker.HCENTER) {
                doLeft(peers, style);
                doRight(peers, style);
            } else if (command == BorderWorker.VCENTER) {
                doBottom(peers, style);
                doTop(peers, style);
            } else if (command == BorderWorker.CROSS) {
                doAll(peers, style);
            }

            peers[0].getOwner().closeEdit();
        }
    }

    private void doRight(ComponentPeer[] peers, BorderStyle style) {
        Border fornull = Border.create(null, null, null, style);

        for (int i = 0; i < peers.length; i++) {
            Border old = peers[i].getComponent().getBorder();

            if (old == null) {
                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), fornull,
                    Border.class);
            } else if (!old.equals(fornull)) {
                Border newBorder = Border.create(old.getTopStyle(), old.getLeftStyle(),
                        old.getBottomStyle(), style);

                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newBorder,
                    Border.class);
            }
        }
    }

    private void doBottom(ComponentPeer[] peers, BorderStyle style) {
        Border fornull = Border.create(null, null, style, null);

        for (int i = 0; i < peers.length; i++) {
            Border old = peers[i].getComponent().getBorder();

            if (old == null) {
                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), fornull,
                    Border.class);
            } else if (!old.equals(fornull)) {
                Border newBorder = Border.create(old.getTopStyle(), old.getLeftStyle(), style,
                        old.getRightStyle());

                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newBorder,
                    Border.class);
            }
        }
    }

    private void doTop(ComponentPeer[] peers, BorderStyle style) {
        Border fornull = Border.create(style, null, null, null);

        for (int i = 0; i < peers.length; i++) {
            Border old = peers[i].getComponent().getBorder();

            if (old == null) {
                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), fornull,
                    Border.class);
            } else if (!old.equals(fornull)) {
                Border newBorder = Border.create(style, old.getLeftStyle(), old.getBottomStyle(),
                        old.getRightStyle());

                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newBorder,
                    Border.class);
            }
        }
    }

    private void doLeft(ComponentPeer[] peers, BorderStyle style) {
        Border fornull = Border.create(null, style, null, null);

        for (int i = 0; i < peers.length; i++) {
            Border old = peers[i].getComponent().getBorder();

            if (old == null) {
                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), fornull,
                    Border.class);
            } else if (!old.equals(fornull)) {
                Border newBorder = Border.create(old.getTopStyle(), style, old.getBottomStyle(),
                        old.getRightStyle());

                peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), newBorder,
                    Border.class);
            }
        }
    }

    private void doClear(ComponentPeer[] peers) {
        for (int i = 0; i < peers.length; i++) {
            peers[i].setValue(ComponentConstants._BORDER.getPropertyName(), null, Border.class);
        }
    }

    void doAll(ComponentPeer[] peers, BorderStyle style) {
        doLeft(peers, style);
        doTop(peers, style);
        doBottom(peers, style);
        doRight(peers, style);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SimpleBorderWorker getInstance() {
        if (instance == null) {
            instance = new SimpleBorderWorker();
        }

        return instance;
    }
}
