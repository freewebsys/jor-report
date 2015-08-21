package jatools.xml.serializer;

import org.w3c.dom.Element;


public class StringArraySerializer extends ContainerSerializer {
    static final String TOKEN = ",";

    public void write(Element e, Object object) {
        String[] ints = (String[]) object;

        if (ints.length == 0) {
            return;
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < ints.length; i++) {
            buffer.append(encode(ints[i]));

            if (i < (ints.length - 1)) {
                buffer.append(TOKEN);
            }
        }

        setAttribute(e, LENGTH, ints.length + "");
        this.appendText(e, buffer.toString());
    }

    public Object read(Element e, Class type) {
        int len = Integer.parseInt(e.getAttribute(LENGTH));
        String[] result = new String[len];
        int k = 0;
        int i = 0;
        String s = this.getText(e);

        boolean flag = true;

        if (s != null) {
            char[] ac = s.toCharArray();

            for (int j = 0; j < ac.length; j++) {
                if (ac[j] == '"') {
                    flag = !flag;

                    continue;
                }

                if ((ac[j] == ',') && flag) {
                    result[k] = decode(ac, i, j - i);
            		k++;
                    i = j + 1;
                }
            }

            if (flag) {
                if (i < ac.length) {
                    result[k] = decode(ac, i, ac.length - i);
                }
            }
        }

        return result;
    }

    private String encode(String s) {
        String s1 = null;

        if (s == null) {
            s1 = "null";
        } else if (s.length() == 0) {
            s1 = "";
        } else if ((s.indexOf(34) >= 0) || (s.indexOf(44) >= 0) || (s.indexOf(10) >= 0) ||
                Character.isWhitespace(s.charAt(0))) {
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append('"');

            char[] ac = s.toCharArray();

            for (int i = 0; i < ac.length; i++) {
                if (ac[i] == '"') {
                    stringbuffer.append('"');
                }

                stringbuffer.append(ac[i]);
            }

            stringbuffer.append('"');
            s1 = stringbuffer.toString();
        } else {
            s1 = s;
        }

        return s1;
    }

    private static String decode(char[] ac, int i, int j) {
        while (Character.isWhitespace(ac[i]) && (i < ac.length)) {
            i++;
            j--;
        }

        int k = (i + j) - 1;
        StringBuffer stringbuffer = new StringBuffer();

        if ((ac[i] == '"') && (ac[k] == '"')) {
            for (int l = i + 1; l < k; l++) {
                if (ac[l] == '"') {
                    l++;
                }

                if (l < k) {
                    stringbuffer.append(ac[l]);
                }
            }
        } else {
            stringbuffer.append(ac, i, j);
        }

        return stringbuffer.toString();
    }
}
