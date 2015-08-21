package jatools.dom;

import jatools.dom.src.NodeSource;
import jatools.dom.src.xpath.XPath;

import java.util.List;

import org.w3c.dom.Node;

import bsh.ValueAlways;

public class NodeProxy implements ValueAlways {
	NodeSource src;
	Node root;

	Object node;

	public NodeProxy(NodeSource src, Node root) {
		this.src = src;
		this.root = root;
	}

	public Object value() {
		if (node == null) {
			List nodes = XPath.getDefaults().selectNodes(src.getFullPath(), root);
			if (nodes.size() == 1)
				node = nodes.get(0);
			else
				node = nodes;

		}
		return node;
	}

}
