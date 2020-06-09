package server;

public class MyRenderer {

	/**
	 * If open = true - removes tokens and leaves text between.
	 * If open = false - removes tokens with text between.
	 */
	public static StringBuilder cutTokens(StringBuilder text, String startToken, String endToken, boolean open) {
		if (text.indexOf(startToken) < 0) {
			return text;
		}
		StringBuilder sb = new StringBuilder();
		int off = 0;
		while (off < text.length()) {
			int b = text.indexOf(startToken, off);
			if (b < 0) break;
			int e = text.indexOf(endToken, b);
			if (e < 0) break;
			sb.append(text.substring(off, b));
			if (open) {
				sb.append(text.substring(b + startToken.length(), e));
			}
			off = e + endToken.length();
		}
		sb.append(text.substring(off));
		return sb;
	}
}
