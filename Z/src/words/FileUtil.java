package words;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FileUtil {
    public static ReadBytes readBytes(File f) {
        if(!f.isFile()) return null;
        BufferedInputStream br = null;
        try {
            FileInputStream fr = new FileInputStream(f);
            br = new BufferedInputStream(fr);
            int l = (int)f.length();
            byte[] bs = new byte[l];
            l = br.read(bs, 0, l);
            br.close();
            fr.close();
            return new ReadBytes(bs, l);
        } catch (IOException e) {
        	return null;
        } finally {
        	if(br!=null) {
        		try {
					br.close();
				} catch (IOException e) {
					//ignore
				}
        	}
        }
    }
    
    static class ReadBytes {
    	byte[] bs;
    	int length;
    	
    	ReadBytes(byte[] bs, int l) {
    		this.bs = bs;
    		length = l;
    	}
    }

    public static String readFile(File f, String encoding) {
    	ReadBytes bs = readBytes(f);
    	if(bs == null) return null;
        try {
            return new String(bs.bs, 0, bs.length, encoding);
        } catch (UnsupportedEncodingException e) {
        	return null;
        }
    }

}
