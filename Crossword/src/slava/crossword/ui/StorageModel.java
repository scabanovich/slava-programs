package slava.crossword.ui;

import java.io.*;
import java.util.*;

import slava.crossword.runtime.*;
import slava.crossword.preference.*;

public class StorageModel {
    String path;
    String name = "Crossword1";

    public StorageModel() {
        String wd = CrosswordPreference.getInstance().getString("workdir", "");
        File f = null;
        try { f = new File(wd); } catch (Exception e) {}
        if(f == null || !f.isDirectory()) f = new File("");
        path = f.getAbsolutePath().replace('\\', '/');
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return new File(path + "/" + name + ".scr");
    }

    public File getHTMLFile() {
        return new File(path + "/" + name + ".scr.html");
    }

    public File getNumberedFile() {
        return new File(path + "/" + name + "Numbered" + ".html");
    }

	public File getAnswersFile() {
		return new File(path + "/" + name + "Answers" + ".html");
	}

    public Properties getProperties() {
        Properties p = new Properties();
        p.setProperty("path", path);
        p.setProperty("name", name);
        return p;
    }

    public void setProperties(Properties p) {
        boolean ch = !path.equals(p.getProperty("path"));
        path = p.getProperty("path");
        name = p.getProperty("name");
        if(ch) CrosswordPreference.getInstance().setString("workdir", path);
    }

    public void save(CrosswordComponentModel model, boolean saveAnswers) {
        File f = getFile();
        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
            FileOutputStream pw = new FileOutputStream(f);
            saveText(pw, model);
            pw.close();
        } catch (Exception e) {}
        f = getHTMLFile();
        try {
            f.createNewFile();
            PrintWriter pw = new PrintWriter(new FileWriter(f));
            saveHTML(pw, model);
            pw.close();
        } catch (Exception e) {}
        if(saveAnswers) {
			f = getAnswersFile();
			try {
				f.createNewFile();
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				saveAnswers(pw, model);
				pw.close();
			} catch (Exception e) {}
        }
    }

    private void saveText(OutputStream stream, CrosswordComponentModel model) throws Exception {
        int[] n = model.getRuntimeNet();
        byte[] b = model.getRuntimeCharacters();
        int w = model.getWidth();
        for (int i = 0; i < n.length; i++) {
            stream.write((byte)'-');
            if(n[i] == 0) stream.write((byte)'*');
            else if(b[i] == (byte)255) stream.write((byte)' ');
            else stream.write(WordBase.instance.getLetterCoder().getChar(b[i]));
            if((i % w) == w - 1) {
                stream.write((byte)'-');
                stream.write((byte)'\n');
            }
        }
    }

    private void saveHTML(PrintWriter stream, CrosswordComponentModel model) throws Exception {
        int[] n = model.getRuntimeNet();
        byte[] b = model.getRuntimeCharacters();
        int w = model.getWidth();
        stream.print("<html>\n");
        stream.print("  <table>");
        for (int i = 0; i < n.length; i++) {
            if((i % w) == 0) stream.print("\n    <tr>\n");
            stream.print("<td width=\"30\" height=\"30\" align=\"center\"");
            if(n[i] == 0) stream.write(" bgcolor=\"7f7f7f\"");
            stream.print(">");
            if(n[i] == 1) {
                if(b[i] != (byte)255)
                  stream.print(WordBase.instance.getLetterCoder().getDrawChar(b[i]));
            }
            stream.print("</td>\n");
            if((i % w) == w - 1) stream.print("    </tr>");
        }

        stream.print("\n  </table>\n");
        stream.print("</html>\n");
    }

	private void saveAnswers(PrintWriter stream, CrosswordComponentModel model) throws Exception {
		int[] n = model.getRuntimeNet();
		byte[] b = model.getRuntimeCharacters();
		int w = model.getWidth();
		StringBuffer sbh = new StringBuffer();
		StringBuffer sbv = new StringBuffer();
		int wc = 0;

		stream.print("<html>\n");
		stream.print("  <table>");
		for (int i = 0; i < n.length; i++) {
			if((i % w) == 0) stream.print("\n    <tr>\n");
			stream.print("<td width=\"30\" height=\"30\" align=\"center\"");
			if(n[i] == 0) stream.write(" bgcolor=\"7f7f7f\"");
			stream.print(">");
			if(n[i] == 1) {
				boolean h = isHWord(n, w, i);
				boolean v = isVWord(n, w, i);
				if(!h && !v) {
					stream.print(' ');
				} else {
					++wc;
					stream.print(wc);					
					if(h) {
						sbh.append(" " + wc + ". ");
						readHWord(n, b, w, i, sbh);
					}
					if(v) {
						sbv.append(" " + wc + ". ");
						readVWord(n, b, w, i, sbv);
					}
				}
//				if(b[i] != (byte)255)
//				  stream.print(WordBase.instance.getLetterCoder().getDrawChar(b[i]));
			}
			stream.print("</td>\n");
			if((i % w) == w - 1) stream.print("    </tr>");
		}

		stream.print("\n  </table>\n");
		stream.print("<h5>Horizontal</h5>\n");
		stream.print(sbh.toString() + '\n' + '\n');
		stream.print("<h5>Vertical</h5>\n");
		stream.print(sbv.toString() + '\n' + '\n');
		stream.print("</html>\n");


	}
	
	boolean isHWord(int[] n, int w, int i) {
		if((i % w) == w - 1) return false;
		int ie = i + 1;
		if(n[ie] == 0) return false;
		if(i % w == 0) return true;
		return n[i - 1] == 0;		
	}

	boolean isVWord(int[] n, int w, int i) {
		if(i + w >= n.length) return false;
		return (n[i + w] != 0) && (i < w || n[i - w] == 0);		
	}
	
	void readHWord(int[] n, byte[] b, int w, int i, StringBuffer sb) {
		LetterCoder lc = WordBase.instance.getLetterCoder();
		boolean first = true;
		while(true) {
			char c = (first) ? lc.getDrawChar(b[i]) : lc.getDrawCharSmall(b[i]);
			first = false;
			sb.append(c);
			if((i % w) == w - 1) break;
			i = i + 1;
			if(n[i] == 0) break;
		}
	}

	void readVWord(int[] n, byte[] b, int w, int i, StringBuffer sb) {
		LetterCoder lc = WordBase.instance.getLetterCoder();
		boolean first = true;
		while(true) {
			char c = (first) ? lc.getDrawChar(b[i]) : lc.getDrawCharSmall(b[i]);
			first = false;
			sb.append(c);
			if(i + w >= n.length) break;
			i = i + w;
			if(n[i] == 0) break;
		}
	}
	public void saveAsNumbers(CrosswordComponentModel model, Map letterToNumber, byte[] numberToLetter, int tipSize) {
        File f = getNumberedFile();
        try {
            f.createNewFile();
            PrintWriter pw = new PrintWriter(new FileWriter(f));
            saveAsNumbers(pw, model, letterToNumber, numberToLetter, tipSize);
            pw.close();
        } catch (Exception e) {}
	}
	private void saveAsNumbers(PrintWriter stream, CrosswordComponentModel model, Map letterToNumber, byte[] numberToLetter, int tipSize) {
		int[] n = model.getRuntimeNet();
		byte[] b = model.getRuntimeCharacters();
        int w = model.getWidth();
        stream.print("<html>\n");
        stream.print("  <table>");
        for (int i = 0; i < n.length; i++) {
            if((i % w) == 0) stream.print("\n    <tr>\n");
            stream.print("<td width=\"30\" height=\"30\" align=\"center\"");
            if(n[i] == 0) stream.write(" bgcolor=\"7f7f7f\"");
            stream.print(">");
            if(n[i] == 1) {
                if(b[i] != (byte)255) {
                	Integer v = (Integer)letterToNumber.get(new Byte(b[i]));
                	stream.print((v.intValue() + 1));
                }
            }
            stream.print("</td>\n");
            if((i % w) == w - 1) stream.print("    </tr>");
        }

        stream.print("\n  </table>\n");

        stream.print("<hr>\n");
        saveNumberedKey(stream, letterToNumber, numberToLetter, tipSize);
        stream.print("<hr>\n");
        stream.print("\n  <p>Answers:</p>\n");
        saveNumberedKey(stream, letterToNumber, numberToLetter, -1);
        stream.print("</html>\n");
	}
	private void saveNumberedKey(PrintWriter stream, Map letterToNumber, byte[] numberToLetter, int tipSize) {
        stream.print("  <table>\n");
        stream.print("  <tr>");
        for (int i = 0; i < numberToLetter.length; i++) {
        	stream.print("<td>" + (i + 1) + "</td>");
        }
        stream.print("  </tr>\n");
        stream.print("  <tr>");
        for (int i = 0; i < numberToLetter.length; i++) {
        	byte l = numberToLetter[i];
        	if(tipSize >= 0 && i >= tipSize) continue;
        	char c = WordBase.instance.getLetterCoder().getDrawChar(l);
        	stream.print("<td>" + c + "</td>");
        }
        stream.print("  </tr>\n");
        stream.print("  </table>\n");
	}

    public void load(CrosswordComponentModel model) {
        File f = getFile();
        if(!f.isFile()) return;
        ArrayList l = new ArrayList();
        byte[] bs = null;
        try {
          InputStream is = new FileInputStream(f);
          int length = is.available();
          bs = new byte[length];
          is.read(bs);
        } catch (Exception e) {}
        int k = 0;
        for (int i = 0; i < bs.length; i++) {
          if(bs[i] == (byte)'\n') {
            int wl = i - k - 1;
            byte[] w = new byte[wl];
            System.arraycopy(bs, k, w, 0, wl);
            l.add(w);
            k = i + 1;
          }
        }
        if(k < bs.length) {
          int wl = bs.length - k - 1;
          byte[] w = new byte[wl];
          System.arraycopy(bs, k, w, 0, wl);
          l.add(w);
        }

        byte[][] s = (byte[][])l.toArray(new byte[0][]);
        if(s.length == 0) return;
        int w = s[0].length;
        for (int i = 0; i < s.length; i++) if(s[i].length != w) return;

        int width = w / 2, height = s.length;
        model.setDimensions(width, height);
        k = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                byte c = s[i][2 * j + 1];
                if(c == (byte)'*') {
                  model.setNetAt(k, 0);
                } else {
                    byte b = WordBase.instance.getLetterCoder().number((byte)c);
                    model.setCharAt(k, b);
                }
                ++k;
            }
        }
    }

}
