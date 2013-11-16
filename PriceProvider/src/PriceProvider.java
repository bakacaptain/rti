import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.concurrent.LinkedBlockingQueue;

import Price.PriceUpdatedMessage;
import publishers.raw.RawPricePublisher;

public class PriceProvider {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting publisher service...");
		
		// Setup data writer
		
		LinkedBlockingQueue<PriceUpdatedMessage> queue = new LinkedBlockingQueue<PriceUpdatedMessage>();
		RawPricePublisher publisher = new RawPricePublisher(queue);
		Thread publishThread = new Thread(publisher);
		
		publishThread.start();
		
		// Setup UI input
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try{
			while(true){
				System.out.println("Please type a message> ");
				String input = reader.readLine();
				
				long number = Long.parseLong(input);
				
				PriceUpdatedMessage msg = new PriceUpdatedMessage(number, new BigDecimal(26.453), 12);
				
				queue.put(msg);
				if(input.equals(""))
					break;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		publisher.stop();
		
		System.out.println("Stopping publisher service...");
	}
	
}
