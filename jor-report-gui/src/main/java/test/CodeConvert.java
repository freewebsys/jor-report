package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


/**
 * 编码转换
 *
 * @author
 * @version
  */
public class CodeConvert {
    /**
    *
    *
    * @param args
    */
    public static void main(String[] args) {
         convert(new File("E:\\java\\JOR\\src.secret"), ".java","GB2312","UTF-8");
    }

    static void convert(File f, String ext,String from,String to) {
        if (f.isDirectory()) {
            for (File fs : f.listFiles()) {
                convert(fs, ext,from,to);
            }
        } else if (f.getName().endsWith(ext)) {
            try {
                System.out.println(f.getName() + "...");

                FileInputStream fis = new FileInputStream(f);
                byte[] buffers = new byte[fis.available()];
                fis.read(buffers);
                fis.close();

                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(f), to); //
                writer.write(new String(buffers, from));

                writer.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
