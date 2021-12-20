package ozpasyazilim.utils.core;

public class FiRunnable2 {

	@Deprecated
	public void runnableIfNotNull(Object objectToCheck, Runnable runnable){


		if(objectToCheck!=null){
			runnable.run();
		}

	}

}
