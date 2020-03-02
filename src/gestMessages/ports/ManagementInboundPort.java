package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.components.Broker;
import gestMessages.interfaces.ManagementCI;
import gestMessages.interfaces.ReceptionCI;
import messages.MessageFilterI;

public class ManagementInboundPort extends AbstractInboundPort implements ManagementCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManagementInboundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	public ManagementInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	public ManagementInboundPort(String uri, Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(uri, implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		((Broker)this.getOwner()).subscribe(topic,inboundPortUri);
	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception{
		((Broker)this.getOwner()).subscribe(topics,inboundPortUri);
	}

	@Override
	public void subscribe(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception{
		((Broker)this.getOwner()).subscribe(topic,newFilter, inboundPortUri);
		
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception{
		((Broker)this.getOwner()).modifyFilter(topic,newFilter,inboundPortUri);
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception{
		((Broker)this.getOwner()).unsubscribe(topic, inboundPortUri);
		
	}

	@Override
	public void createTopic(String topic) throws Exception{
		((Broker)this.getOwner()).createTopic(topic);
		
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		((Broker)this.getOwner()).createTopics(topics);
		
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		((Broker)this.getOwner()).destroyTopic(topic);
		
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return ((Broker)this.getOwner()).isTopic(topic);
	}

	@Override
	public String[] getTopics() throws Exception {
		return ((Broker)this.getOwner()).getTopics();
	}

}
