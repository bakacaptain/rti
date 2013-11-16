import java.util.concurrent.LinkedBlockingQueue;

import Price.PriceUpdatedMessage;

import subscribers.raw.RawPriceSubscriber;


public class PriceForecaster {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		LinkedBlockingQueue<PriceUpdatedMessage> queue = new LinkedBlockingQueue<PriceUpdatedMessage>();
		
		RawPriceSubscriber subscriber = new RawPriceSubscriber(queue);
		
		Thread subscriberThread = new Thread(subscriber);
		
		subscriberThread.start();

	}

}
