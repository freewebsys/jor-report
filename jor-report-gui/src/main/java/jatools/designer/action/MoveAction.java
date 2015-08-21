package jatools.designer.action;

import jatools.designer.App;
import jatools.designer.SelectionState;
import jatools.designer.peer.ComponentPeer;

import java.awt.event.ActionEvent;



class MoveAction extends ReportAction {
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int DEFAULT_DELTA = 1;
    private int orientation;


    /**
     * Creates a new MoveAction object.
     *
     * @param orientation DOCUMENT ME!
     */
    public MoveAction(int orientation) {
        super(App.messages.getString("res.585"));
        this.orientation = orientation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 0) && state.isMoveable());
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ComponentPeer[] peers = (ComponentPeer[]) getEditor().getReportPanel().getSelection();

        if (peers.length == 0) {
            return;
        }

        int deltaX = getDeltaX();
        int deltaY = getDeltaY();

        for (int i = 0; i < peers.length; i++) {
            peers[i].move(deltaX, deltaY);
        }

        getEditor().repaint();
    }

    private int getDeltaX() {
        if (orientation == LEFT) {
            return -1;
        } else if (orientation == RIGHT) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getDeltaY() {
        if (orientation == UP) {
            return -1;
        } else if (orientation == DOWN) {
            return 1;
        } else {
            return 0;
        }
    }
}
