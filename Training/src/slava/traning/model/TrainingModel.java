package slava.traning.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class TrainingModel {
	List<TrainingPair> pairs = new ArrayList<TrainingPair>();
	String keyName;
	String valueName;

	public TrainingModel() {		
	}

	public void load(Properties properties) {
		for (Object key: properties.keySet()) {
			String k = (String)key;
			pairs.add(new TrainingPair(k, properties.getProperty(k)));
		}
	}

	public void load(File f) {
		pairs.clear();
		try {
			Properties p = new Properties();
			InputStream s = new FileInputStream(f);
			BufferedInputStream bs = new BufferedInputStream(s);
			p.load(bs);
			load(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<TrainingPair> getPairs() {
		return pairs;
	}

	public void setKeyName(String n) {
		keyName = n;
	}

	public void setValueName(String n) {
		valueName = n;
	}

	public String getKeyName() {
		return keyName;
	}

	public String getValueName() {
		return valueName;
	}

	Random seed = new Random();

	public TrainingPair getRandomPair() {
		return pairs.get(seed.nextInt(pairs.size()));
	}

}
