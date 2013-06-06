package champukr;

import java.io.*;

public class MyReaderTest {
	static int ITERATIONS = 300;
	static String FILE_1 = "./testReader.txt";
	static String FILE_2 = "./struts-config.xml";
	
	static String text = null;
	static String getText() {
		if(text != null) return text;
		StringBuffer sb = new StringBuffer();
		try {
			FileReader reader = new FileReader(new File(FILE_2));
			int l = 0;
			char[] bs = new char[128];
			while((l = reader.read(bs)) >= 0) {
				sb.append(new String(bs, 0, l));
			};
		} catch (Exception e) {
		}
		System.out.println("Text length=" + sb.length());
		return text = sb.toString();
	}
	static Reader getReader() {
		return new StringReader(getText());
	}
	static void test1() {
		long t = System.currentTimeMillis();
		try {
			for (int i = 0; i < ITERATIONS; i++) {
				StringBuffer sb = new StringBuffer(100);
				length = 0;
				Reader reader = getReader();
				int k = -1;
				while((k = reader.read()) >= 0) {
					sb.append((char)k);
				}
			}
		} catch (Exception e) {
			
		}
		long dt = System.currentTimeMillis() - t;
		System.out.println("Test 1 done in time=" + dt);
	}

	static void test2() {
		long t = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0; i < ITERATIONS; i++) {
				Reader reader = getReader();
				sb = new StringBuffer(100);
				char[] b = new char[128];
				int l = 0, dl = 0;
				while((dl = reader.read(b)) >= 0) {
					for (int j = 0; j < dl; j++) {
						char c = b[j];
						sb.append(c);
					}
				};
//				System.out.println(sb.toString());
//				System.out.println(" " + l);
			}
		} catch (Exception e) {
			
		}
		long dt = System.currentTimeMillis() - t;
		System.out.println("Test 2 done in time=" + dt);
	}

	static char[] chars = new char[1024];
    static int index = 0;
    static int length = 0;
    
    static int readCharFromStreamBuffer(Reader reader) throws IOException {
    	if(index >= length) {
    		length = reader.read(chars);
    		index = 0;
    	}
    	if(index < length) {
//    		char c = chars[index];
    		index++;
    		return 0;//c;
    	}
    	return -1;
    }

    public static void main(String[] args) {
    	getReader();
		test1();
		test2();
	}
}
