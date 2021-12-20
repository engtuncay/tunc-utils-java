package ozpasyazilim.utils.core;

// More info: http://blog.efftinge.de/2008/10/multi-line-string-literals-in-java.html


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MultilineStringDemo {

    public static String S() {

        StackTraceElement element = new RuntimeException().getStackTrace()[1];
        String name = element.getClassName().replace('.', '/') + ".java";
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        String s = convertStreamToString(in, element.getLineNumber());

        return s.substring(s.indexOf("/*") + 2, s.indexOf("*/"));
    }

    private static String convertStreamToString(InputStream is, int lineNum) {
        /*
        * To convert the InputStream to String we use the
        * BufferedReader.readLine() method. We iterate until the BufferedReader
        * return null which means there's no more data to read. Each line will
        * appended to a StringBuilder and returned as String.
        */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        int i = 1;
        try {
            while ((line = reader.readLine()) != null) {
                if (i++ >= lineNum) {
                    sb.append(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(S(/*
			<?xml version="1.0"?>
			<root>
				<something />
			</root>
      */));
    }
}