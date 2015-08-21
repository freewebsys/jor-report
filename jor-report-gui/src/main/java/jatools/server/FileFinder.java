package jatools.server;

import jatools.io.ByteArrayResourceOutput;
import jatools.io.SessionedFileResourceOutputFactory.TempFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FileFinder {
    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     */
    public static void service(HttpServletRequest request, HttpServletResponse response) {
        String file = request.getParameter("file");

        if (file != null) {
            Object f = request.getSession().getAttribute(file);

            byte[] bytes = null;

            if (f instanceof ByteArrayResourceOutput) {
                ByteArrayResourceOutput res = (ByteArrayResourceOutput) f;
                bytes = res.getBytes();
            } else if (f instanceof TempFile) {
                TempFile tf = (TempFile) f;

                if (tf != null) {
                    try {
                        InputStream is = new FileInputStream(tf.getFile());

                        bytes = new byte[is.available()];
                        is.read(bytes);
                        is.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            if (bytes != null) {
                try {
                    ServletOutputStream os = response.getOutputStream();
                    os.write(bytes);
                    os.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            request.getSession().setAttribute(file, null);
        }
    }
}
