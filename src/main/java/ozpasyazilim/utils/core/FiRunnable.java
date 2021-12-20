package ozpasyazilim.utils.core;

import java.util.Objects;

@FunctionalInterface
public interface FiRunnable extends Runnable {

	default FiRunnable and(Runnable other) {
		Objects.requireNonNull(other);
		FiRunnable fiRunnable = () -> {
			run();
			other.run();
		};
		return fiRunnable;
	}
}



/**
 * When an object implementing interface <code>Runnable</code> is used
 * to create a thread, starting the thread causes the object's
 * <code>run</code> method to be called in that separately executing
 * thread.
 * <p>
 * The general contract of the method <code>run</code> is that it may
 * take any action whatsoever.
 *
 * @see java.lang.Thread#run()
 */