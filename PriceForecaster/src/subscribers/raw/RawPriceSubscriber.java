package subscribers.raw;

import java.util.concurrent.BlockingQueue;

import Price.PriceUpdatedMessage;
import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.KeyedBytesDataReader;
import com.rti.dds.type.builtin.KeyedBytesTypeSupport;
import com.rti.dds.subscription.Subscriber;

import core.MessageHandler;

public class RawPriceSubscriber implements Runnable {
	
	// for graceful shutdown sequence
	private static boolean shutdown_flag = false;
	private BlockingQueue<PriceUpdatedMessage> queue;
	private DomainParticipant participant;
	
	public RawPriceSubscriber(BlockingQueue<PriceUpdatedMessage> queue){
		this.queue = queue;
		
		this.init();
	}
	
	
	
	private void init(){
		participant = DomainParticipantFactory
				.get_instance()
				.create_participant(
						0, // Domain id
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
			
		KeyedBytesDataReader reader = (KeyedBytesDataReader) participant.create_datareader(
				topic, 
				Subscriber.DATAREADER_QOS_DEFAULT, 
				new MessageHandler(queue), // listener 
				StatusKind.DATA_AVAILABLE_STATUS);
			
		if(reader == null)
		{
			System.err.println("Unable to create DDS data reader");
			return;
		}
	}
	
	public void run(){
		
		System.out.println("Data reader started...");
		try{
			while(true){
				Thread.sleep(2000);
				if(shutdown_flag) 
					break;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		System.out.println("Stopped reading data...");
	}
	
	public void stop(){
		System.out.println("Shuting down service...");
		
		shutdown_flag = true;
		
		participant.delete_contained_entities();
		DomainParticipantFactory.get_instance().delete_participant(participant);
	}
	
	

}
