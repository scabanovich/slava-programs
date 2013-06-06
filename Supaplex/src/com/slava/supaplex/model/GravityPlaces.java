package com.slava.supaplex.model;

import java.util.ArrayList;

public class GravityPlaces {
	SupaplexLevel level;
	ArrayList gravityPlaces = new ArrayList();
	
	public void setLevel(SupaplexLevel level) {
		this.level = level;
	}

	public GravityPlace[] getGravityPlaces() {
		return (GravityPlace[])gravityPlaces.toArray(new GravityPlace[0]);
	}
	
	public void setGravityPlace(int place, int mode) {
		GravityPlace g = findGravityPlace(place);
		if(g == null) {
			if(mode < 0) return;
			addGravityPlace(place, mode);
		} else {
			if(mode < 0) {
				removeGravityPlace(g); 
			} else if(g.getMode() != mode) {				
				g.bytes[2] = (byte)mode;
				apply();
			}
		}
	}
	
	public GravityPlace findGravityPlace(int place) {
		for (int i = 0; i < gravityPlaces.size(); i++) {
			GravityPlace g = (GravityPlace)gravityPlaces.get(i); 
			if(g.getPlace() == place) return g;
		}
		return null;
	}
	
	private void addGravityPlace(int place, int mode) {
		GravityPlace g = new GravityPlace();
		g.setPlace(place);
		g.bytes[2] = (byte)mode;
		gravityPlaces.add(g);
		apply();
	}

	private void removeGravityPlace(GravityPlace g) {
		gravityPlaces.remove(g);
		apply();		
	}
	
	private void apply() {
		for (int i = 0; i < gravityPlaces.size(); i++) {
			GravityPlace g = (GravityPlace)gravityPlaces.get(i);
			g.apply(level.distribution, 6 * i);
		}
	}
	
	public void load(byte quantity, byte[] distribution) {
		gravityPlaces = new ArrayList();
		int max = (int)quantity;
		if(max > distribution.length / 6) max = distribution.length / 6;
		for (int i = 0; i < max; i++) {
			GravityPlace g = GravityPlace.create(distribution, i * 6);
			gravityPlaces.add(g);
		}
	}

}
