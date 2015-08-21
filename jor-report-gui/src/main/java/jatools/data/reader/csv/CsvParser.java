package jatools.data.reader.csv;

import jatools.dataset.RowMeta;
import jatools.db.TypeUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;


public class CsvParser {
    public static final char DEFAULT_SEP = ',';

    public static String[] parse(BufferedReader reader)
        throws IOException {
        while (true) {
            String line = reader.readLine();

            if (line == null) {
                return null;
            } else if (!line.trim().equals("")) {
                return parse(line);
            }
        }
    }

    public static Object[] strings2Objects(String[] strValues, RowMeta rowInfo)
        throws Exception {
        Object[] results = new Object[strValues.length];

        if (strValues.length == rowInfo.getColumnCount()) {
            for (int i = 0; i < results.length; i++) {
                if (strValues[i] != null) {
                    if (strValues != null) {
                        try {
                            results[i] = TypeUtil.valueOf(strValues[i].trim(),
                                    TypeUtil.getSqlID(rowInfo.getColumnClass(i)));
                        } catch (Exception e) {
                            throw new Exception("字符串:" + strValues[i] + ",不能转化为类型为:" +
                                rowInfo.getColumnClass(i).getName() + " 的值");
                        }
                    }
                }
            }

            return results;
        } else {
            String s = strValues[0];

            for (int i = 1; i < strValues.length; i++) {
                s += ("," + strValues[i]);
            }

            throw new Exception("csv数据集记录中的值的个数与要求的数目不等:\n" + s);
        }
    }

    public static String[] parse(String line) {
        StringBuffer sb = new StringBuffer();
        ArrayList list = new ArrayList();
        int i = 0;

        do {
            sb.setLength(0);

            if ((i < line.length()) && (line.charAt(i) == '"')) {
                i = advQuoted(line, sb, ++i);
            } else {
                i = advPlain(line, sb, i);
            }

            list.add(sb.toString());

            i++;
        } while (i < line.length());

        return (String[]) list.toArray(new String[0]);
    }

    protected static int advQuoted(String s, StringBuffer sb, int i) {
        int j;
        int len = s.length();

        for (j = i; j < len; j++) {
            if ((s.charAt(j) == '"') && ((j + 1) < len)) {
                if (s.charAt(j + 1) == '"') {
                    j++;
                } else if (s.charAt(j + 1) == DEFAULT_SEP) {
                    j++;

                    break;
                }
            } else if ((s.charAt(j) == '"') && ((j + 1) == len)) {
                break;
            }

            sb.append(s.charAt(j));
        }

        return j;
    }

    protected static int advPlain(String s, StringBuffer sb, int i) {
        int j;

        j = s.indexOf(DEFAULT_SEP, i);

        if (j == -1) {
            sb.append(s.substring(i));

            return s.length();
        } else {
            sb.append(s.substring(i, j));

            return j;
        }
    }
}
