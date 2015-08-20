package jatools.viewer;

import jatools.core.view.PageView;
import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class Viewer extends JApplet {
    /**
     * DOCUMENT ME!
     *
     * @param pageViewer DOCUMENT ME!
     */

    
    int loadedPages;
    int totalPages;
    rvr v = new rvr();
    String engine1;
    String report1;
    String sessionID;
    private String files;
    private ed e;
    JFrame frame = new JFrame(App.messages.getString("res.7"));
	private String call_cache;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * this is a try
     */
    public ArrayList getPages() {
        ArrayList pages = new ArrayList();

        String root = this.getParameter("root"); //
        files = this.getParameter("files"); //

        java.util.StringTokenizer token = new StringTokenizer(files, ";"); //

        while (token.hasMoreTokens()) {
            pages.add(root + token.nextToken());
        }

        return pages;
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
    	v.enabledFitPageWidth() ;
        JButton exp = exportButton();
        v.toolbar().add(exp, 0);

        int w = Integer.parseInt(this.getParameter("frame_width"));
        int h = Integer.parseInt(this.getParameter("frame_height"));
        call_cache = this.getParameter( "call_cache");
        
        

        frame.getContentPane().add(v, BorderLayout.CENTER);
        frame.setSize(new Dimension(w, h));
        frame.setLocationRelativeTo(null);
        frame.show();
        
        frame.addComponentListener( new ComponentAdapter(){
        	public void componentResized(ComponentEvent e) {
        		v.fitPageWidth();
        		
        	}
        	
        	
        	
        });

        loadReport();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JButton exportButton() {
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/jatools/icons/export.gif")); //
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(25, 25));
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    export();
                }
            });

        return button;
    }

    /**
         *
         */
    protected void export() {
        try {
            if (e == null) {
                e = new ed();
            }

            
            e.show();

            if (e.xok) {
                String type = e.getType();
                URL url = this.getCodeBase();

                String port = (url.getPort() == -1) ? "" : (":" +
                    url.getPort());
                String codebase = "http://" + url.getHost() + port +
                    url.getPath();

                codebase = codebase.substring(0, codebase.lastIndexOf("/"));

                String _url = codebase + "/server?call_cache=" + call_cache +
                    "&as=" + type+"&do_export=1";
                getAppletContext().showDocument(new URL(_url));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @param pageViewer DOCUMENT ME!
    */
    private void loadReport() {
        loadedPages = 0;
        totalPages = 0;

        ArrayList pages = getPages();

        for (int i = 0; i < pages.size(); i++) {
            try {
                URL url = new URL((String) pages.get(i));
                PageCanvas pc = v.addPage((PageView) null);
                ZPageLoader loader = new ZPageLoader(pc, url);
                loader.start();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
    }

    class ZPageLoader extends Thread {
        PageCanvas canvas;
        URL url;
        PageView pp;

        ZPageLoader(PageCanvas canvas, URL url) {
            this.canvas = canvas;
            this.url = url;
        }

        public void run() {
            try {
                URLConnection conn = url.openConnection();
                conn.setUseCaches(false);

                ObjectInputStream ois = new ObjectInputStream(conn.getInputStream());
                pp = (PageView) ois.readObject();
                ois.close();

                Runnable set = new Runnable() {
                        public void run() {
                            canvas.setPageView(pp);
                            canvas.setScale( v.toScale() );
                            //canvas.setScale(rvr.to)
                        }
                    };

                SwingUtilities.invokeLater(set);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
