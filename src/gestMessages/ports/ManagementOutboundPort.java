package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gestMessages.interfaces.ManagementCI;
import messages.MessageFilterI;

public class ManagementOutboundPort extends AbstractOutboundPort implements ManagementCI{

	public ManagementOutboundPort(String uri,ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
		/// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).subscribe(topic, inboundPortUri);
		
	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).subscribe(topics, inboundPortUri);
		
	}

	@Override
	public void subscribe(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).subscribe(topic, newFilter,inboundPortUri);
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).subscribe(topic, newFilter,inboundPortUri);
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).subscribe(topic, inboundPortUri);
		
	}

	@Override
	public void createTopic(String topic) throws Exception {
		System.out.println("connector :" + connector);
		((ManagementCI)this.connector).createTopic(topic);
		
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		((ManagementCI)this.connector).createTopics(topics);
		
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		((ManagementCI)this.connector).destroyTopic(topic);
		
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return ((ManagementCI)this.connector).isTopic(topic);

	}

	@Override
	public String[] getTopics() throws Exception {
		return ((ManagementCI)this.connector).getTopics();
	}

}
