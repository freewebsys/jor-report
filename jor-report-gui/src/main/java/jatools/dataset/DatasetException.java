package jatools.dataset;

import jatools.accessor.ProtectPublic;

public class DatasetException extends java.lang.Exception implements ProtectPublic {
    private Exception source;
    private Object objectSource;

    public DatasetException() {
    }

    public DatasetException(String msg) {
        super(msg);
    }

    public DatasetException(Exception exception) {
        source = exception;
    }

    public DatasetException(Object object) {
        objectSource = object;
    }

    public DatasetException(Exception exception,
                             Object object) {
        source = exception;
        objectSource = object;
    }

    public DatasetException(String msg,
                             Exception exception) {
        super(msg);
        source = exception;
    }

    public DatasetException(String msg,
                             Object object) {
        super(msg);
        objectSource = object;
    }

    public DatasetException(String msg,
                             Exception exception,
                             Object object) {
        super(msg);
        source = exception;
        objectSource = object;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Exception getSourceException() {
        return source;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSourceObject() {
        return objectSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMessage() {
        super.getMessage();

        if (source != null) {
            return super.getMessage() + "\n " + source.getMessage(); //
        } else {
            return super.getMessage();
        }
    }
}
