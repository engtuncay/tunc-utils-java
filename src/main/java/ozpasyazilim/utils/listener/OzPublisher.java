package ozpasyazilim.utils.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * 
 * 
 * Publisher - Subscriber Java 8 Pattern
 * 
 * @author Tuncay Orak
 *
 * @param <T>
 */
public class OzPublisher<T> {

	List<Consumer<T>> listSubscribers = new ArrayList<>();

	/**
	 * You can add subscriber (consumer<T> function) for this publisher
	 * 
	 * @param subcriber
	 */
	public void addSubscriber(Consumer<T> subcriber) {
		getListSubscribers().add(subcriber);
	}

	public void notifySubscribers(T message) {

		List<Consumer<T>> listsubscribers = getListSubscribers();

		if (listsubscribers.size() > 0) {

			for (Iterator iterator2 = listsubscribers.iterator(); iterator2.hasNext();) {
				Consumer<T> subscriber = (Consumer<T>) iterator2.next();
				subscriber.accept(message);
			}

		}
	}

	public List<Consumer<T>> getListSubscribers() {
		return listSubscribers;
	}

	public static void main(String[] args) {

		OzPublisher<String> publisherString = new OzPublisher<>();

		publisherString.addSubscriber(e -> System.out.println(e + " World"));
		publisherString.addSubscriber(System.out::println);

		publisherString.notifySubscribers("Hello");

	}

}