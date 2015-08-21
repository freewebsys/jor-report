package jatools.dataset;

import java.util.Collection;


final class RowsVector {
    private static final long serialVersionUID = 8683452581122892189L;
    protected transient int modCount = 0;
    boolean paddingFirst;

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer.
     */
    Row[] elementData;

    /**
     * The size of the ArrayList (the number of elements it contains).
     *
     * @serial
     */
    private int size;

    /**
     * @param initialCapacity
     */

    /**
     * Creates a new RowsVector object.
     */
    public RowsVector(Row[] rows) {
        this.elementData = rows;
        this.size = rows.length;
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param   initialCapacity   the initial capacity of the list.
     * @exception IllegalArgumentException if the specified initial capacity
     *            is negative
     */
    public RowsVector(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }

        this.elementData = new Row[initialCapacity];
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public RowsVector() {
        this(10);
    }

    Row[] getObjects() {
        return (Row[]) this.elementData.clone();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isPaddingFirst() {
        return paddingFirst;
    }

    /**
     * DOCUMENT ME!
     *
     * @param paddingFirst DOCUMENT ME!
     */
    public void setPaddingFirst(boolean paddingFirst) {
        this.paddingFirst = paddingFirst;
    }


    /**
     * Increases the capacity of this <tt>ArrayList</tt> instance, if
     * necessary, to ensure  that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     *
     * @param   minCapacity   the desired minimum capacity.
     */
    public void ensureCapacity(int minCapacity) {
        modCount++;

        int oldCapacity = elementData.length;

        if (minCapacity > oldCapacity) {
        	Row[] oldData = elementData;
            int newCapacity = ((oldCapacity * 3) / 2) + 1;

            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }

            elementData = new Row[newCapacity];
            System.arraycopy(oldData, 0, elementData, 0, size);
        }
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return  the number of elements in this list.
     */
    public int size() {
        return size;
    }

    /**
     * Tests if this list has no elements.
     *
     * @return  <tt>true</tt> if this list has no elements;
     *          <tt>false</tt> otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     *
     * @param elem element whose presence in this List is to be tested.
     * @return  <code>true</code> if the specified element is present;
     *                <code>false</code> otherwise.
     */
    public boolean contains(Object elem) {
        return indexOf(elem) >= 0;
    }

    /**
     * Searches for the first occurence of the given argument, testing
     * for equality using the <tt>equals</tt> method.
     *
     * @param   elem   an object.
     * @return  the index of the first occurrence of the argument in this
     *          list; returns <tt>-1</tt> if the object is not found.
     * @see     Object#equals(Object)
     */
    public int indexOf(Object elem) {
        if (elem == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i] == null) {
                    return i;
                }
        } else {
            for (int i = 0; i < size; i++)
                if (elem.equals(elementData[i])) {
                    return i;
                }
        }

        return -1;
    }

    /**
     * Returns a shallow copy of this <tt>ArrayList</tt> instance.  (The
     * elements themselves are not copied.)
     *
     * @return  a clone of this <tt>ArrayList</tt> instance.
     */
    public Object clone() {
        try {
            RowsVector v = (RowsVector) super.clone();
            v.elementData = new Row[size];
            System.arraycopy(elementData, 0, v.elementData, 0, size);
            v.modCount = 0;

            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * Returns an array containing all of the elements in this list
     * in the correct order.
     *
     * @return an array containing all of the elements in this list
     *                in the correct order.
     */
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elementData, 0, result, 0, size);

        return result;
    }

    /**
     * Returns an array containing all of the elements in this list in the
     * correct order; the runtime type of the returned array is that of the
     * specified array.  If the list fits in the specified array, it is
     * returned therein.  Otherwise, a new array is allocated with the runtime
     * type of the specified array and the size of this list.<p>
     *
     * If the list fits in the specified array with room to spare (i.e., the
     * array has more elements than the list), the element in the array
     * immediately following the end of the collection is set to
     * <tt>null</tt>.  This is useful in determining the length of the list
     * <i>only</i> if the caller knows that the list does not contain any
     * <tt>null</tt> elements.
     *
     * @param a the array into which the elements of the list are to
     *                be stored, if it is big enough; otherwise, a new array of the
     *                 same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list.
     * @throws ArrayStoreException if the runtime type of a is not a supertype
     *         of the runtime type of every element in this list.
     */
    public Object[] toArray(Object[] a) {
        if (a.length < size) {
            a = (Object[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }

        System.arraycopy(elementData, 0, a, 0, size);

        if (a.length > size) {
            a[size] = null;
        }

        return a;
    }

    // Positional Access Operations

    /**
     * Returns the element at the specified position in this list.
     *
     * @param  index index of element to return.
     * @return the element at the specified position in this list.
     * @throws    IndexOutOfBoundsException if index is out of range <tt>(index
     *                   &lt; 0 || index &gt;= size())</tt>.
     */
    public Object get(int index) {
     //   RangeCheck(index);

        return elementData[index];
    }

    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *
     * @param index index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws    IndexOutOfBoundsException if index out of range
     *                  <tt>(index &lt; 0 || index &gt;= size())</tt>.
     */
    public Object set(int index, Row element) {
        RangeCheck(index);

        Object oldValue = elementData[index];
        elementData[index] = element;

        return oldValue;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param o element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of Collection.add).
     */
    public boolean add(Row o) {
        ensureCapacity(size + 1); // Increments modCount!!
        elementData[size++] = o;

        return true;
    }

    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted.
     * @param element element to be inserted.
     * @throws    IndexOutOfBoundsException if index is out of range
     *                  <tt>(index &lt; 0 || index &gt; size())</tt>.
     */
    public void add(int index, Row element) {
        if ((index > size) || (index < 0)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        ensureCapacity(size + 1); // Increments modCount!!
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the element to removed.
     * @return the element that was removed from the list.
     * @throws    IndexOutOfBoundsException if index out of range <tt>(index
     *                   &lt; 0 || index &gt;= size())</tt>.
     */
    public Object remove(int index) {
        RangeCheck(index);

        modCount++;

        Object oldValue = elementData[index];

        int numMoved = size - index - 1;

        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }

        elementData[--size] = null; // Let gc do its work

        return oldValue;
    }

    /**
     * Removes all of the elements from this list.  The list will
     * be empty after this call returns.
     */
    public void clear() {
        modCount++;

        // Let gc do its work
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }

    /**
     * Appends all of the elements in the specified Collection to the end of
     * this list, in the order that they are returned by the
     * specified Collection's Iterator.  The behavior of this operation is
     * undefined if the specified Collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified Collection is this list, and this
     * list is nonempty.)
     *
     * @param c the elements to be inserted into this list.
     * @return <tt>true</tt> if this list changed as a result of the call.
     * @throws    NullPointerException if the specified collection is null.
     */
    public boolean addAll(RowsVector c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacity(size + numNew); // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;

        return numNew != 0;
    }

    /**
     * Inserts all of the elements in the specified Collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified Collection's iterator.
     *
     * @param index index at which to insert first element
     *                    from the specified collection.
     * @param c elements to be inserted into this list.
     * @return <tt>true</tt> if this list changed as a result of the call.
     * @throws    IndexOutOfBoundsException if index out of range <tt>(index
     *                  &lt; 0 || index &gt; size())</tt>.
     * @throws    NullPointerException if the specified Collection is null.
     */
    public boolean addAll(int index, Collection c) {
        if ((index > size) || (index < 0)) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacity(size + numNew); // Increments modCount

        int numMoved = size - index;

        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
        }

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;

        return numNew != 0;
    }

    /**
     * Removes from this List all of the elements whose index is between
     * fromIndex, inclusive and toIndex, exclusive.  Shifts any succeeding
     * elements to the left (reduces their index).
     * This call shortens the list by <tt>(toIndex - fromIndex)</tt> elements.
     * (If <tt>toIndex==fromIndex</tt>, this operation has no effect.)
     *
     * @param fromIndex index of first element to be removed.
     * @param toIndex index after last element to be removed.
     */
    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;

        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex, numMoved);

        // Let gc do its work
        int newSize = size - (toIndex - fromIndex);

        while (size != newSize)
            elementData[--size] = null;
    }

    /**
     * Check if the given index is in range.  If not, throw an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     */
    private void RangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Save the state of the <tt>ArrayList</tt> instance to a stream (that
     * is, serialize it).
     *
     * @serialData The length of the array backing the <tt>ArrayList</tt>
     *             instance is emitted (int), followed by all of its elements
     *             (each an <tt>Object</tt>) in the proper order.
     */
}
