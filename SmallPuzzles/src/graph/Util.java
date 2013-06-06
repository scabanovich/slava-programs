package graph;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;

public class Util {
	
	public static StringTokenizer read(String uri) {
		try {
			URL url = new URL(uri);
			InputStream is = url.openConnection().getInputStream();
			BufferedInputStream b = new BufferedInputStream(is);
			byte[] bs = new byte[256];
			StringBuilder sb = new StringBuilder();
			while(true) {
                int l = b.read(bs, 0, bs.length);
                if(l < 0) break;
                sb.append(new String(bs, 0, l));
			}
			b.close();
			
			return new StringTokenizer(sb.toString(), "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static int[] parseInt(String s) {
		StringTokenizer st = new StringTokenizer(s, " \t");
		int[] result = new int[st.countTokens()];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(st.nextToken());
		}
		return result;
	}
	
}
