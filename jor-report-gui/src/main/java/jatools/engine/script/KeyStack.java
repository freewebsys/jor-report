package jatools.engine.script;


import jatools.dataset.Dataset;
import jatools.dataset.Key;

import java.util.Stack;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class KeyStack {
    /**
     * DOCUMENT ME!
     */
    public final static int ROW = 0;

    /**
     * DOCUMENT ME!
     */
    public final static int COLUMN = 1;
    Stack stack = new Stack();

    /**
     * Creates a new KeyManager object.
     */
    public KeyStack() {
        this.stack.push(Dataset.ANY);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void push(Key key) {
        stack.push(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object pop() {
        return this.stack.pop();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Key getKey() {
        return (Key) stack.peek();
    }
}
