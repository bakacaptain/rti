package publishers.future;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.KeyedBytes;
import com.rti.dds.type.builtin.KeyedBytesDataWriter;
import com.rti.dds.type.builtin.KeyedBytesTypeSupport;

import core.ByteSerialzier;

import Price.FuturePriceUpdatedMessage;
import Price.PriceUpdatedMessage;

public class FuturePricePublisher implements Runnable {

	private boolean shutdown_flag = false;
	private BlockingQueue<PriceUpdatedMessage> queue;
	private DomainParticipant participant;
	private KeyedBytesDataWriter dataWriter;
	
	public FuturePricePublisher(BlockingQueue<PriceUpdatedMessage> queue){
		this.queue = queue;
		init();
	}
	
	private void init(){
		participant = DomainParticipantFactory
				.get_instance()
				.create_participant(
						1, // Domain id
						DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
						null, // listener
						StatusKind.STATUS_MASK_NONE);
		
		if(participant == null)
		{
			System.err.println("Unable to create the domain participant");
			return;
		}
		
		Topic topic = participant.create_topic(
				"RawPriceUpdated",
				KeyedBytesTypeSupport.get_type_name(),
				DomainParticipant.TOPIC_QOS_DEFAULT,
				null, // listener
				StatusKind.STATUS_MASK_NONE);
		
		if(topic == null)
		{
			System.err.println("Unable to create topic");
			return;
		}
		
		dataWriter = (KeyedBytesDataWriter) participant.create_datawriter(
					topic,
					Publisher.DATAWRITER_QOS_DEFAULT,
					null, // listener
					StatusKind.STATUS_MASK_NONE);
		
		if(dataWriter == null)
		{
			System.err.println("Cannot create data writer\n");
			return;
		}
	}
	
	@Override
	public void run() {
		try{
			while(!shutdown_flag){
				PriceUpdatedMessage msg = queue.take();
				
				FuturePriceUpdatedMessage f_msg = convert(msg);
				
				byte[] out = ByteSerialzier.serialize(f_msg);
				KeyedBytes bytesOut = new KeyedBytes();
				bytesOut.key = "giraffehelper";
				bytesOut.value = out;
				bytesOut.length = out.length;
				
				dataWriter.write(bytesOut, InstanceHandle_t.HANDLE_NIL);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void stop(){
		shutdown_flag = true;
	}
	
	private FuturePriceUpdatedMessage convert(PriceUpdatedMessage msg){
		return new FuturePriceUpdatedMessage(msg.getOrderId(), msg.getPrice(), msg.getQuote(), (new Date()).getTime());
	}

}
