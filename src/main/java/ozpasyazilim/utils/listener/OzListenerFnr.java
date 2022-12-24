package ozpasyazilim.utils.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.FnResultGen;

public class OzListenerFnr {

	List<Consumer<FnResultGen>> listObservers = new ArrayList<>();

	public void addObserver(Consumer<FnResultGen> fnListener) {
		getListObservers().add(fnListener);
	}

	public void notifyListeners(FnResultGen fnresult) {

		List<Consumer<FnResultGen>> listlisteners = getListObservers();

		if (listlisteners.size() > 0) {

			for (Iterator iterator2 = listlisteners.iterator(); iterator2.hasNext();) {
				Consumer<FnResultGen> consumer = (Consumer<FnResultGen>) iterator2.next();
				consumer.accept(fnresult);
			}

		}
	}


	public List<Consumer<FnResultGen>> getListObservers() {
		return listObservers;
	}


	// Utility methods for Listeners

	public static void notifyObservers(OzListenerFnr listenerAktarim, FnResultGen fnResult) {

		if (listenerAktarim == null) {
			Loghelper.get(OzListenerFnr.class).error("Listener Bulunamadı !!!");
			return;
		}

		List<Consumer<FnResultGen>> listObservers = listenerAktarim.getListObservers();

		if (listObservers.size() > 0) {

			for (Iterator iterator2 = listObservers.iterator(); iterator2.hasNext();) {
				Consumer<FnResultGen> consumer = (Consumer<FnResultGen>) iterator2.next();
				consumer.accept(fnResult);
			}

		}

	}

	public static void notifyObservers(OzListenerFnr listenerAktarim, String string) {

		if (listenerAktarim == null) {
			Loghelper.get(OzListenerFnr.class).error("Listener Bulunamadı !!!");
			return;
		}

		// Loghelper2.getInstance().info("Listener Aktarim null degil");

		List<Consumer<FnResultGen>> listObservers = listenerAktarim.getListObservers();

		if (listObservers.size() > 0) {

			for (Iterator iterator2 = listObservers.iterator(); iterator2.hasNext();) {

				Consumer<FnResultGen> consumer = (Consumer<FnResultGen>) iterator2.next();
				if (consumer == null) Loghelper.get(OzListenerFnr.class).info("consumer null");
				consumer.accept(new FnResultGen(string));
			}

		}


	}

	public static void notifyObservers(OzListenerFnr listenerAktarim, String string, String type) {

		if (listenerAktarim == null) {
			Loghelper.get(OzListenerFnr.class).error("Listener Bulunamadı !!!");
			return;
		}

		// Loghelper2.getInstance().info("Listener Aktarim null degil");

		List<Consumer<FnResultGen>> listObservers = listenerAktarim.getListObservers();

		if (listObservers.size() > 0) {

			// Loghelper2.getInstance().info(" Observar var. Size:" + listObservers.size());

			for (Iterator iterator2 = listObservers.iterator(); iterator2.hasNext();) {

				Loghelper.get(OzListenerFnr.class).info("for döngüsü");

				Consumer<FnResultGen> consumer = (Consumer<FnResultGen>) iterator2.next();
				if (consumer == null) Loghelper.get(OzListenerFnr.class).info("consumer null");
				consumer.accept(new FnResultGen(string, type));


			}



		}


	}

	public static void notifyObserversAndLog(OzListenerFnr listenerAktarim, String message, Class callerClass) {

		if (listenerAktarim == null) {
			Loghelper.get(callerClass).error("Listener Bulunamadı !!!");
			return;
		}

		Loghelper.get(callerClass).info(message);

		List<Consumer<FnResultGen>> listObservers = listenerAktarim.getListObservers();

		if (listObservers.size() > 0) {

			for (Iterator iterator2 = listObservers.iterator(); iterator2.hasNext();) {
				Consumer<FnResultGen> consumer = (Consumer<FnResultGen>) iterator2.next();
				consumer.accept(new FnResultGen(message));
			}

		}

	}

}
