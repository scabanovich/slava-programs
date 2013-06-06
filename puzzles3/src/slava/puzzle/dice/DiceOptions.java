package slava.puzzle.dice;

public class DiceOptions {
	public static String DISTANCE_OPTION = "-d=";
	public static String ARROW_OPTION = "-p=";
	public static String THROW_OPTION = "-t=";
	public static String FINISH_OPTION = "-f=";
	public static String START_OPTION = "-s=";
	
	public static String USAGE = 
		"-l=number -t=number [-p=number] [-f] [-s]\n" +
		" -l=number  distance from start to finish     (required)\n" + 
		" -t=number  number of dice throws             (required)\n" +
		" -p=number  number of paths, any if not set   (optional)\n" + 
		" -s=yes/no  can jump to start                 (optional)\n" +
		" -f=yes/no  can jump to finish                (optional)\n";
	
	int distance = -1;
	int arrowCount = -1;
	int throwsCount = -1;
	boolean canJumpToFinish = false;
	boolean canJumpToStart = false;
	
	String errorMessage = null;
	
	public void load(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if(args[i].startsWith(DISTANCE_OPTION)) {
				try {
					distance = Integer.parseInt(args[i].substring(3));
				} catch (Exception e) {
					errorMessage = "Cannot parse distance";
				}
			} else if(args[i].startsWith(ARROW_OPTION)) {
				try {
					arrowCount = Integer.parseInt(args[i].substring(3));
				} catch (Exception e) {
					errorMessage = "Cannot parse number of paths";
				}
			} else if(args[i].startsWith(THROW_OPTION)) {
				try {
					throwsCount = Integer.parseInt(args[i].substring(3));
				} catch (Exception e) {
					errorMessage = "Cannot parse number of dice throws";
				}
			} else if(args[i].equals(FINISH_OPTION + "yes")) {
				canJumpToFinish = true;
			} else if(args[i].equals(START_OPTION + "yes")) {
				canJumpToStart = true;
			}
		}
		if(distance <= 0) {
			errorMessage = "Distance option -d is required";
		} else if(throwsCount <= 0) {
			errorMessage = "Dice throws number option -d is required";
		}
	}
	
}
