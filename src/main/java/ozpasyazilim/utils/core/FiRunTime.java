package ozpasyazilim.utils.core;

import mark.utils.formatter.DoubleFormatter;
import mark.utils.formatter.DoubletoStringFormatter;

public class FiRunTime {

	Long startTime; // = System.nanoTime();

	Long endTime;   // = System.nanoTime();

	Long totalTime;   // = endTime - startTime;

	public FiRunTime(Boolean b) {
		startTime();
	}

	public FiRunTime() {
		startTime();
	}


	public void startTime(){
		setStartTime(System.nanoTime());
	}


	public void  endTime(){
		setEndTime(System.nanoTime());
	}

	public Long getRunningTimeMiliseconds(){

		if(getStartTime()==null || getEndTime()==null) return 0L;

		//Double dbl= Double.valueOf((getEndTime()-getStartTime())/1000000);

		return (getEndTime()-getStartTime())/1000000;
	}


	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
}
