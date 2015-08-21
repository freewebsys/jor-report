package jatools.data.sum;


public class CountField extends Sum {

	public CountField(String[] groupField,String calcField) {
		super(groupField, calcField);
	}
	public CountField() {

	}
	/**
	 * DOCUMENT ME!
	 * 
	 * @param values
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object getValue(Object[] values) {
		if (values != null) {

			return  convertToNumber(Integer.class , values.length);
		}
		return null;
	}


}
