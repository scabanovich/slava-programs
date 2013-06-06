package slava.traning.enging;

public interface TrainingListener {

	public void started();

	public void setPair(String key, String value);

	public void setTimer(int sec);

	public void checkValue(String value);

}
