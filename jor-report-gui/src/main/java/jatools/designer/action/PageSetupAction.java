package jatools.designer.action;


import jatools.PageFormat;
import jatools.ReportDocument;
import jatools.component.ComponentConstants;
import jatools.component.PagePanel;
import jatools.designer.App;
import jatools.designer.PageSetupPanel;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.ComponentPeerFactory;
import jatools.designer.undo.AddEdit;
import jatools.designer.undo.DeleteEdit;

import java.awt.Frame;
import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageSetupAction extends ReportAction {
    private PagePanel oldHeader = null;
    private PagePanel oldFooter = null;

    /**
     * Creates a new PageSetupAction object.
     */
    public PageSetupAction() {
        super(App.messages.getString("res.160"), getIcon("/jatools/icons/pageformat.gif"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        PageSetupPanel chooser = new PageSetupPanel();
        ReportDocument doc = getEditor().getDocument();
        doc.getPage().getPageFormat();

        java.awt.print.PageFormat format = chooser.showDialog((Frame) getEditor()
                                                                          .getTopLevelAncestor(),
                doc.getPage().getPageFormat().toAwtFormat(false), doc.getPage());

        if (format != null) {
            if (doc.getPage().getHeader() != null) {
                oldHeader = doc.getPage().getHeader();

                if (!chooser.hasHeader()) {
                    ComponentPeer parentPeer = getEditor().getReportPanel()
                                                   .getComponentPeer(doc.getPage());
                    ComponentPeer thisPeer = getEditor().getReportPanel()
                                                 .getComponentPeer(doc.getPage().getHeader());

                    DeleteEdit edit = new DeleteEdit(thisPeer, parentPeer.indexOf(thisPeer));
                    getEditor().getReportPanel().addEdit(edit);

                    parentPeer.remove(thisPeer);
                    doc.getPage().invalid();
                }
            } else {
                if (chooser.hasHeader()) {
                    if (oldHeader != null) {
                        doc.getPage().setHeader(oldHeader);

                        ComponentPeer peer = getEditor().getReportPanel().getComponentPeer(oldHeader);
                        ComponentPeer parentPeer = getEditor().getReportPanel()
                                                       .getComponentPeer(doc.getPage());
                        parentPeer.insert(peer, 0);

                        AddEdit edit = new AddEdit(peer, 0, true);
                        getEditor().getReportPanel().addEdit(edit);
                    } else {
                        PagePanel header = new PagePanel();
                        header.setHeight(80);
                        header.setName("header");
                        doc.getPage().setHeader(header);

                        ComponentPeer peer = ComponentPeerFactory.createPeer(getEditor()
                                                                                 .getReportPanel(),
                                header);
                        ComponentPeer parentPeer = getEditor().getReportPanel()
                                                       .getComponentPeer(doc.getPage());
                        parentPeer.insert(peer, 0);

                        AddEdit edit = new AddEdit(peer, 0, true);
                        getEditor().getReportPanel().addEdit(edit);
                    }
                }
            }

            if (doc.getPage().getFooter() != null) {
                oldFooter = doc.getPage().getFooter();

                if (!chooser.hasFooter()) {
                    ComponentPeer parentPeer = getEditor().getReportPanel()
                                                   .getComponentPeer(doc.getPage());
                    ComponentPeer thisPeer = getEditor().getReportPanel()
                                                 .getComponentPeer(doc.getPage().getFooter());
                    DeleteEdit edit = new DeleteEdit(thisPeer, parentPeer.indexOf(thisPeer));
                    getEditor().getReportPanel().addEdit(edit);

                    parentPeer.remove(thisPeer);
                    doc.getPage().invalid();
                }
            } else {
                if (chooser.hasFooter()) {
                    if (oldFooter != null) {
                        ComponentPeer peer = getEditor().getReportPanel().getComponentPeer(oldFooter);
                        ComponentPeer parentPeer = getEditor().getReportPanel()
                                                       .getComponentPeer(doc.getPage());
                        parentPeer.add(peer);

                        AddEdit edit = new AddEdit(peer, true);
                        getEditor().getReportPanel().addEdit(edit);
                    } else {
                        PagePanel footer = new PagePanel();

                        footer.setHeight(280);
                        footer.setName("footer");
                        doc.getPage().setFooter(footer);

                        ComponentPeer peer = ComponentPeerFactory.createPeer(getEditor()
                                                                                 .getReportPanel(),
                                footer);
                        ComponentPeer parentPeer = getEditor().getReportPanel()
                                                       .getComponentPeer(doc.getPage());
                        parentPeer.add(peer);

                        AddEdit edit = new AddEdit(peer, true);
                        getEditor().getReportPanel().addEdit(edit);
                    }
                }
            }

            getEditor().getReportPanel().openEdit();

            ComponentPeer pagePeer = getEditor().getReportPanel().getReportPeer();
            pagePeer.setValue(ComponentConstants.PROPERTY_PAGE_FORMAT, new PageFormat(format),
                PageFormat.class);
            getEditor().getReportPanel().closeEdit();
        }
    }
}
