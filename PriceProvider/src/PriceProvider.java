import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataWriter;
import com.rti.dds.type.builtin.StringTypeSupport;

public class PriceProvider {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting publisher service...");
		
		init_service();
		
		System.out.println("Stopping publisher service...");
		
	}
	
	private static void init_service()
	{
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
		
		StringDataWriter dataWriter = (StringDataWriter) participant.create_datawriter(
					topic,
					Publisher.DATAWRITER_QOS_DEFAULT,
					null, // listener
					StatusKind.STATUS_MASK_NONE);
		
		if(dataWriter == null)
		{
			System.err.println("Cannot create data writer\n");
			return;
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try
		{
			while(true){
				System.out.println("Please type a message> ");
				String input = reader.readLine();
				dataWriter.write(input, InstanceHandle_t.HANDLE_NIL);
				
				if(input.equals("")) break;
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch(RETCODE_ERROR e){
			e.printStackTrace();
		}
		
		participant.delete_contained_entities();
		DomainParticipantFactory.get_instance().delete_participant(participant);
	}

	
}