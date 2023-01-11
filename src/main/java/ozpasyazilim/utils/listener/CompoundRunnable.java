package ozpasyazilim.utils.listener;

import java.util.*;

public final class CompoundRunnable implements Runnable {
	private final Set<Runnable> setRunnables = new LinkedHashSet<>();

	public CompoundRunnable() {
	}

	public CompoundRunnable(Iterable<Runnable> runnables) {
		// From Guava. Easy enough to do by hand if necessary
		for (Runnable runnable : runnables) {
			this.setRunnables.add(runnable);
		}
		//this.runnables. = Lists.newArrayList(runnables);

	}

	public CompoundRunnable(Runnable... runnables) {
		this(Arrays.asList(runnables));
	}

	@Override
	public void run() {
		for (Runnable runnable : setRunnables) {
			runnable.run();
		}
	}

	public void addRunnable(Runnable runnable){
		this.setRunnables.add(runnable);
	}
}