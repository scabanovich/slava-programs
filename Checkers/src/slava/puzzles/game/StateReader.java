package slava.puzzles.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class StateReader implements Iterator, GameConstants {
	File f;
	BufferedReader reader;
	String line;
	
	String currentCode = null;
	Result currentResult = null;

	public void setFile(File f) {
		if(!f.isFile()) throw new RuntimeException();
		try {
			reader = new BufferedReader(new FileReader(f));
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasNext() {
		return line != null;
	}

	@Override
	public Object next() {
		if(line == null) {
			currentCode = null;
			currentResult = null;
			return null;
		}
		String[] s = line.split(" ");
		byte r = INDEFINITE;
		if(s.length > 1) {
			if(s[1].equals("+")) r = WHITE_WIN;
			else if(s[1].equals("=")) r = DRAW;
			else if(s[1].equals("-")) r = BLACK_WIN;
		}
		String bestMove = s.length > 2 ? s[2] : null;
		currentCode = s[0];
		currentResult = new Result(r, bestMove, 0);
		try {
			line = reader.readLine();
			while(line != null && line.trim().length() == 0) {
				line = reader.readLine();
			}
			if(line == null) {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			line = null;
		}
		return currentCode;
	}

	public String getCurrentCode() {
		return currentCode;
	}

	public Result getCurrentResult() {
		return currentResult;
	}

	@Override
	public void remove() {
	}
	
}
