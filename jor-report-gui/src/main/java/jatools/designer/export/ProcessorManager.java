package jatools.designer.export;

import jatools.designer.App;

import java.awt.Point;
import java.util.ArrayList;


/**
 * @author   java9
 */
public class ProcessorManager implements InputProcessor {
    InputProcessor activeProcessor;
    InputProcessor defaultProcessor;
    boolean processorChanged = false;
    ArrayList processorList; //
    Point lastPoint = new Point();
    Point deltaPoint = new Point();

    /**
    * Creates a new ZProcessorManager object.
    *
    * @param defaultProcessor DOCUMENT ME!
    */
    public ProcessorManager(InputProcessor defaultProcessor) {
        if (defaultProcessor == null) {
            throw new NullPointerException(App.messages.getString("res.439")); //
        }

        this.defaultProcessor = defaultProcessor;
        this.activeProcessor = defaultProcessor;
    }

    /**
	 * DOCUMENT ME!
	 * @uml.property   name="lastPoint"
	 */
    public Point getLastPoint() {
        return lastPoint;
    }

    /**
	 * DOCUMENT ME!
	 * @uml.property   name="deltaPoint"
	 */
    public Point getDeltaPoint() {
        return deltaPoint;
    }

    /**
    * DOCUMENT ME!
    *
    * @param processor DOCUMENT ME!
    */
    public void stopProcessor(InputProcessor processor) {
        if (processor != activeProcessor) {
            return;
        }

        startProcessor(defaultProcessor);
    }

    /**
	 * DOCUMENT ME!
	 * @uml.property   name="activeProcessor"
	 */
    public InputProcessor getActiveProcessor() {
        return activeProcessor;
    }

    /**
    * DOCUMENT ME!
    *
    * @param id DOCUMENT ME!
    * @param modifier DOCUMENT ME!
    * @param x DOCUMENT ME!
    * @param y DOCUMENT ME!
    */
    public void processMouseEvent(int id, int modifier, int x, int y) {
        deltaPoint.x = x - lastPoint.x;
        deltaPoint.y = y - lastPoint.y;
        lastPoint.x = x;
        lastPoint.y = y;
        processorChanged = false;
        activeProcessor.processMouseEvent(id, modifier, x, y);

        if (activeProcessor.isIdle() && (processorList != null)) {
            for (int i = 0; i < processorList.size(); i++) {
                if (processorChanged) {
                    return;
                }

                ((InputProcessor) processorList.get(i)).processMouseEvent(id, modifier, x, y);
            }
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @param processor DOCUMENT ME!
    */
    public void startProcessor(InputProcessor processor) {
        if ((activeProcessor == processor)) {
            return;
        }

        if (processor == null) {
            throw new NullPointerException(App.messages.getString("res.440")); //
        }

        activeProcessor.stopped();
        activeProcessor = processor;

        activeProcessor.started();
        processorChanged = true;
    }

    /**
    * DOCUMENT ME!
    */
    public boolean isIdle() {
        return true;
    }

    /**
    * DOCUMENT ME!
    */
    public void started() {
    }

    /**
    * DOCUMENT ME!
    */
    public void stopped() {
    }

    /**
    * DOCUMENT ME!
    *
    * @param processor DOCUMENT ME!
    */
    public void appendProcessor(InputProcessor processor) {
        if (processor == null) {
            throw new NullPointerException(App.messages.getString("res.441")); //
        }

        if (processorList == null) {
            processorList = new ArrayList();
        } else if (processorList.contains(processor)) {
            return;
        }

        processorList.add(processor);
    }

    /**
    * DOCUMENT ME!
    *
    * @param processor DOCUMENT ME!
    */
    public void removeProcessor(InputProcessor processor) {
        if (processorList == null) {
            return;
        }

        processorList.remove(processor);
    }
}
