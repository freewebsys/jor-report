package jatools.data.sum;

public class SubstringToAllField  {
    private int start = 0;
    private int end = 0;

    public SubstringToAllField(int fieldId) {
       // super(fieldId);
    }

    public SubstringToAllField(int fieldId,
                                int start,
                                int end) {
      //  super(fieldId);
        this.start = start;
        this.end = end;
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object calculateGroupBy(Object[] values) {
        if (values == null) {
            return null;
        }

        String string = (String) values[0];

        if (end == 0) {
            return string;
        } else {
            int endIndex = end;

            if (string.length() < endIndex) {
                endIndex = string.length();
            }

            return string.substring(start, endIndex);
        }
    }
}
