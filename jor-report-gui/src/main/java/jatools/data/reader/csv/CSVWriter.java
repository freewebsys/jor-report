package jatools.data.reader.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class CSVWriter {
    //            "姓名",
    //            "行政级别",  // 地厅司局级(0),县处级(1),乡科级(2),办事员及其他(3)
    //            "培训级别",  // 初级培训(0),任职培训(1),出国出境培训(2)
    //            "培训天数",  // 1-100
    //            "培训类型",  // 政治类型(0),专门业务(1),更新知识(2),学历学位教育(3)
    //            "培训渠道"   // 党校(0),行政学院(1),其他培训机构(2)
    static String cls = "string,int,int,int,int,int\n";
    static String names1 = "姓名,行政级别,培训级别,培训天数,培训类型,培训渠道\n";

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            FileReader filereader = new FileReader(new File("d:/names.txt"));
            BufferedReader reader = new BufferedReader(filereader);
            reader.readLine();

            String line = null;
            Map names = new HashMap();

            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 1) {
                    names.put(line.trim(), line.trim());
                }
            }

            reader.close();

            FileWriter writer = new FileWriter(new File("d:/train.txt"));
            writer.write(cls);
            writer.write(names1);
            Math.random() ;
            

            Iterator it = names.keySet().iterator();

            while (it.hasNext()) {
                Object val = it.next();
                writer.write(val+","+random(4)+","+random(3)+","+random(100)+","+random(4)+","+random(3)+"\n");
                System.out.println(val);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static int random(int max)
    {
    	return (int) ((Math.random() * 100) % max) ;
    }
}
