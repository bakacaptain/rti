package core;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import Price.PriceUpdatedMessage;

import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.type.builtin.KeyedBytes;
import com.rti.dds.type.builtin.KeyedBytesDataReader;

public class MessageHandler extends DataReaderAdapter {

	private BlockingQueue<PriceUpdatedMessage> queue;
	
	public MessageHandler(BlockingQueue<PriceUpdatedMessage> queue){
		this.queue = queue;
	}
	
	public void on_data_available(DataReader reader){
		
		KeyedBytesDataReader datareader = (KeyedBytesDataReader) reader;
		SampleInfo info = new SampleInfo();
		
		while(true){
			try{
				KeyedBytes sample = new KeyedBytes();
				datareader.take_next_sample(sample,info);
				if(info.valid_data){
					PriceUpdatedMessage msg = (PriceUpdatedMessage) ByteSerialzier.deserialize(sample.value);
					this.queue.put(msg);
					System.out.println("Id:"+msg.getOrderId()+"\nPrice:"+msg.getPrice()+"\nQuote:"+msg.getQuote());
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch(RETCODE_NO_DATA noData){
				// no more data to read
				break;
			}
			catch(RETCODE_ERROR e){
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}
