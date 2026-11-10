package ozpasyazilim.utils.core;

import ozpasyazilim.utils.log.Loghelper;

public class FiRunTime {

	Long startTime; // = System.nanoTime();

	Long endTime;   // = System.nanoTime();

	Long totalTime;   // = endTime - startTime;

	String txName;

	public FiRunTime() {
		startTimer();
	}

	public FiRunTime(String txName) {
		setTxName(txName);
		startTimer();
	}


	public void startTimer(){
		setStartTime(System.nanoTime());
	}


	public void endTimer(){
		setEndTime(System.nanoTime());
	}

	public void endTimerAndLog(){
		endTimer();
		Loghelper.get(getClass()).debug("FiRunTime: " + getTxName() + " Miliseconds: " + getRunningTimeMiliseconds() + " ms");
	}

	public Long getRunningTimeMiliseconds(){
		if(getStartTime()==null || getEndTime()==null) return -1L;
		return (getEndTime()-getStartTime())/1000000;
	}

	public Long getDuration(){
		if(getStartTime()==null || getEndTime()==null) return -1L;
		return getEndTime()-getStartTime();
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

	public String getTxName() {
		return txName;
	}

	public String getTxNameNtn() {
		return FiString.orEmpty(getTxName());
	}

	public void setTxName(String txName) {
		this.txName = txName;
	}

}
