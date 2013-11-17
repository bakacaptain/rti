package consumers;

import java.util.concurrent.BlockingQueue;

public class ConsoleConsumer<T> implements Runnable {

	private boolean shutdown_flag = false;
	private BlockingQueue<T> queue;
	
	public ConsoleConsumer(BlockingQueue<T> queue){
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try {
			while(!shutdown_flag){
				T temp = this.queue.take();
				System.out.println("Recieved: "+temp.toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	public void stop(){
		shutdown_flag = true;
	}
}
