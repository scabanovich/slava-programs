package slava.crossword.runtime;

import slava.crossword.preference.CrosswordPreference;

public class LetterCoder {
    int LENGTH = 256;
    byte[] numbers = new byte[LENGTH];
    byte[] letters = new byte[LENGTH];
	byte[] smallLetters = new byte[LENGTH];
    char[] chars = new char[]{'À','Á','Â','Ã','Ä','Å','¨','Æ','Ç','È','É','Ê','Ë','Ì','Í',
                              'Î','Ï','Ð','Ñ','Ò','Ó','Ô','Õ','Ö','×','Ø','Ù','Ú','Û','Ü',
                              'Ý','Þ','ß'};
    char[] smallChars = new char[]{'à','á','â','ã','ä','å','¸','æ','ç','è','é','ê','ë','ì','í',
    		'î','ï','ð','ñ','ò','ó','ô','õ','ö','÷','ø','ù','ú','û','ü',
    		'ý','þ','ÿ'};
    char[] wchars = new char[]{
        '\u0430', '\u0431', '\u0432', '\u0433', '\u0434',
        '\u0435', '\u0451', '\u0436', '\u0437', '\u0438',
        '\u0439', '\u043a', '\u043b', '\u043c', '\u043d',
        '\u043e', '\u043f', '\u0440', '\u0441', '\u0442',
        '\u0443', '\u0444', '\u0445', '\u0446', '\u0447',
        '\u0448', '\u0449', '\u044a', '\u044b', '\u044c',
        '\u044d', '\u044e', '\u044f'};
    String abc;

    public LetterCoder() {
        init();
    }

    public int getLetterCount() {
        return 33;
    }

    public byte number(byte letter) {
        byte q = letter;
        return (q < 0) ? numbers[256 + q] : numbers[q];
    }

    public byte[] toNumbers(byte[] s) {
        byte[] w = new byte[s.length];
        for (int i = 0; i < s.length; i++) {
          int q = s[i];
          if(q < 0) q += 256;
          w[i] = numbers[q];
        }
        return w;
    }

    public byte getChar(byte number) {
        int i = number;
        if(i < 0) i += 256;
        return (i == 255) ? (byte)32 : letters[number];
    }

	public byte getSmallChar(byte number) {
		int i = number;
		if(i < 0) i += 256;
		return (i == 255) ? (byte)32 : smallLetters[i];
	}

    public char getDrawChar(byte number) {
        int i = number;
        if(i < 0) i += 256;
        return (i == 255) ? ' ' : chars[number];
    }
    
    public char getDrawCharSmall(byte number) {
        int i = number;
        if(i < 0) i += 256;
        return (i == 255) ? ' ' : smallChars[number];
    }
    
    public byte getCharIndex(char c) {
        for (int i = 0; i < chars.length; i++) if(chars[i] == c) return (byte)i;
        return (byte)255;
    }

    public char getWindowsChar(byte number) {
        int i = number;
        if(i < 0) i += 256;
        return (i == 255) ? ' ' : wchars[number];
    }
    
    public char getSmallFileChar(byte number) {
		int i = number;
		if(i < 0) i += 256;
		return (i == 255) ? ' ' : abc.charAt(i);
    }

    public byte getWindowsCharIndex(char c) {
        for (int i = 0; i < chars.length; i++) if(wchars[i] == c) return (byte)i;
        return (byte)255;
    }

    public boolean isCorrectWord(byte[] w) {
        if(w == null || w.length == 0) return false;
        for (int i = 0; i < w.length; i++)
          if(w[i] < 0 || w[i] >= LENGTH || letters[w[i]] == -1) return false;
        return true;
    }

    private byte b(int i) {
        return (byte)i;
    }

    public void init() {
        CrosswordPreference cp = CrosswordPreference.getInstance();

        for (int i = 0; i < LENGTH; i++) {
            numbers[i] = -1;
            letters[i] = -1;
            smallLetters[i] = -1;
        }
        String abc = cp.getString("abc", null);
        this.abc = abc;
        for (int i = 0; i < abc.length(); i++) {
            char c = abc.charAt(i);
            byte b = (byte)c;
            int bi = (int)b;
            if(bi < 0) bi += 256;
            numbers[bi] = b(i);
            letters[i] = b;
            smallLetters[i] = b;
        }
        abc = cp.getString("ABC", null);
        for (int i = 0; i < abc.length(); i++) {
            char c = abc.charAt(i);
            byte b = (byte)c;
            int bi = (int)b;
            if(bi < 0) bi += 256;
            numbers[bi] = b(i);
            letters[i] = b;
        }
    }

}