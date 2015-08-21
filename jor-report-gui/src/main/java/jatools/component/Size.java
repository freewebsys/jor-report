package jatools.component;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Size {
    private static int[] emptyArray = new int[0];
    private int[] sizes;
    private int[] positions;
    private boolean valid;

    /**
     * Creates a new Size object.
     */
    public Size() {
        sizes = emptyArray;
    }

    /**
     * Creates a new Size object.
     *
     * @param _sizes DOCUMENT ME!
     */
    public Size(int[] _sizes) {
        setSizes(_sizes);
    }

    /**
     * DOCUMENT ME!
     *
     * @param _sizes DOCUMENT ME!
     */
    public void setSizes(int[] _sizes) {
        sizes = new int[_sizes.length];
        System.arraycopy(_sizes, 0, sizes, 0, sizes.length);
        valid = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getSizes() {
        int[] result = new int[sizes.length];
        System.arraycopy(sizes, 0, result, 0, sizes.length);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPosition(int index) {
        if (!valid) {
            validate();
        }
//        if(index >= positions.length || index <0)
//    		System.out.println();
        
        return positions[index];
    }

    private void validate() {
        this.positions = new int[sizes.length + 1];

        int pos = 0;

        for (int i = 1; i < positions.length; i++) {
            pos += sizes[i - 1];
            this.positions[i] = pos;
        }
        
        valid = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize(int index) {
        return sizes[index];
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param size DOCUMENT ME!
     */
    public void setSize(int index, int size) {
        if (sizes[index] != size) {
            sizes[index] = size;
            valid = false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int length() {
        return this.sizes.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param start DOCUMENT ME!
     * @param length DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void insertEntries(int start, int length, int value) {
        int[] result = new int[sizes.length + length];
        System.arraycopy(sizes, 0, result, 0, start);
        System.arraycopy(sizes, start, result, start + length, sizes.length - start);

        for (int i = 0; i < length; i++) {
            result[i + start] = value;
        }

        sizes = result;
        valid = false;
    }
    
    

    /**
     * Removes a contiguous group of entries
     * from this <code>SizeSequence</code>.
     * Note that the values of <code>start</code> and
     * <code>length</code> must satisfy the following
     * conditions:  <code>(0 <= start < getSizes().length)
     * AND (length >= 0)</code>.  If these conditions are
     * not met, the behavior is unspecified and an exception
     * may be thrown.
     *
     * @param start   the index of the first entry to be removed
     * @param length  the number of entries to be removed
     */
    public void removeEntries(int start, int length) {
        int[] result = new int[sizes.length - length];
        System.arraycopy(sizes, 0, result, 0, start);
        System.arraycopy(sizes, start + length, result, start, sizes.length - start - length);
        sizes = result;
        valid = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param width DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSize(int index, int width) {
    	
        return getPosition(index + width) - getPosition(index);
    }
}
