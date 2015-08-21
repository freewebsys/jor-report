package jatools.designer.undo;

import jatools.component.chart.Chart;
import jatools.data.reader.DatasetReader;
import jatools.designer.peer.ComponentPeer;

import java.util.ArrayList;
import java.util.Properties;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class ChartSetEdit extends AbstractUndoableEdit {
	ComponentPeer peer;

	Properties props;

	DatasetReader reader;

	String label;

	ArrayList data;

	Properties _props;

	DatasetReader _reader;

	String _label;

	ArrayList _data;

	public ChartSetEdit(ComponentPeer peer, Properties props,
			DatasetReader reader, String label, ArrayList data) {

		this.peer = peer;
		this.props = props;
		this.reader = reader;
		this.label = label;
		this.data = data;

		Chart chart = (Chart) peer.getComponent();
		this._props = chart.getProperties();
		this._reader = chart.getReader();
		this._label = chart.getLabelField();
		this._data = chart.getPlotData();

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws CannotRedoException
	 *             DOCUMENT ME!
	 */
	public void redo() throws CannotRedoException {
		super.redo();
		Chart chart = (Chart) peer.getComponent();
		chart.setProperties(this._props);
		chart.setReader(this._reader);
		chart.setLabelField(this._label);
		chart.setPlotData(this._data);
	//	chart.chart = null;

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws CannotUndoException
	 *             DOCUMENT ME!
	 */
	public void undo() throws CannotUndoException {
		super.undo();

		Chart chart = (Chart) peer.getComponent();
		chart.setProperties(this.props);
		chart.setReader(this.reader);
		chart.setLabelField(this.label);
		chart.setPlotData(this.data);
	//	chart.chart = null;
	}
}