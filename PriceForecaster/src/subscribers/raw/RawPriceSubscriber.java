package subscribers.raw;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataReader;
import com.rti.dds.type.builtin.StringTypeSupport;
import com.rti.dds.subscription.Subscriber;

public class RawPriceSubscriber extends DataReaderAdapter {
	
	// for graceful shutdown sequence
	private static boolean shutdown_flag = false;
	
	public static void main(String[] args){
		
		DomainParticipant participant = DomainParticipantFactory
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
				StringTypeSupport.get_type_name(),
				DomainParticipant.TOPIC_QOS_DEFAULT,
				null, // listener
				StatusKind.STATUS_MASK_NONE);
		
		if(topic == null)
		{
			System.err.println("Unable to create topic");
			return;
		}
		
		StringDataReader reader = (StringDataReader) participant.create_datareader(
				topic, 
				Subscriber.DATAREADER_QOS_DEFAULT, 
				new RawPriceSubscriber(), // listener 
				StatusKind.DATA_AVAILABLE_STATUS);
		
		if(reader == null)
		{
			System.err.println("Unable to create DDS data reader");
			return;
		}
		
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
		System.out.println("Shuting down service...");
		
		participant.delete_contained_entities();
		DomainParticipantFactory.get_instance().delete_participant(participant);
	}
	
	public void on_data_available(DataReader reader){
		
		StringDataReader stringReader = (StringDataReader) reader;
		SampleInfo info = new SampleInfo();
		
		while(true){
			try{
				String sample = stringReader.take_next_sample(info);
				if(info.valid_data){
					System.out.println(sample);
				}
				
				if(sample.equals("")){
					shutdown_flag = true;
				}
			}
			catch(RETCODE_NO_DATA noData){
				// no more data to read
				break;
			}
			catch(RETCODE_ERROR e){
				e.printStackTrace();
			}
		}
	}

}
