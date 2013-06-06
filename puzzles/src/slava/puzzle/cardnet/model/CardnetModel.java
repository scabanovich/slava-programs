package slava.puzzle.cardnet.model;

import slava.puzzle.template.model.*;

public class CardnetModel extends PuzzleModel {
	CardnetField field;
	CardSet cards;
	CardnetPuzzleInfo puzzle;
	CardnetGeneratorInfo generatorInfo;
	
	boolean isSettingPuzzleModeOn = false;
	boolean isSettingGeneratorInfoModeOn = false;
	
	public CardnetModel() {
		field = new CardnetField();
		setLoader(new CardnetLoader());
		field.setSize(8, 10);
		cards = new CardSet();
		puzzle = new CardnetPuzzleInfo(field, cards);
		generatorInfo = new CardnetGeneratorInfo();
		generatorInfo.init(puzzle);
	}
	
	public CardnetField getField() {
		return field;
	}
	
	public CardSet getCardSet() {
		return cards;
	}
	
	public CardnetPuzzleInfo getPuzzleInfo() {
		return puzzle;
	}
	
	public CardnetGeneratorInfo getGeneratorInfo() {
		return generatorInfo;
	}

	public boolean isShowingSolution() {
		return puzzle.hasSolution();
	}
	
	public void setSettingPuzzleModeOn(boolean b) {
		isSettingPuzzleModeOn = b;
		if(b) {
			puzzle.setProblem();
			isSettingGeneratorInfoModeOn = false;
		} 
	}
	
	public boolean isSettingPuzzleModeOn() {
		return isSettingPuzzleModeOn;
	}
	
	public void setSettingGeneratorInfoModeOn(boolean b) {
		isSettingGeneratorInfoModeOn = b;
		if(b) {
			isSettingPuzzleModeOn = false;
		} 
	}
	
	public boolean isSettingGeneratorInfoModeOn() {
		return isSettingGeneratorInfoModeOn;
	}

}
