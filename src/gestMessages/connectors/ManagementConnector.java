package gestMessages.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gestMessages.interfaces.ManagementCI;
import messages.MessageFilterI;

public class ManagementConnector extends AbstractConnector implements ManagementCI {

	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		((ManagementCI)this.offering).subscribe(topic, inboundPortUri);
		
	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		((ManagementCI)this.offering).subscribe(topics, inboundPortUri);

	}

	@Override
	public void subscribe(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		((ManagementCI)this.offering).subscribe(topic, newFilter, inboundPortUri);

	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		((ManagementCI)this.offering).modifyFilter(topic, newFilter, inboundPortUri);


	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		((ManagementCI)this.offering).unsubscribe(topic, inboundPortUri);

	}

	@Override
	public void createTopic(String topic) throws Exception {
		((ManagementCI)this.offering).createTopic(topic);

	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		((ManagementCI)this.offering).createTopics(topics);
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		((ManagementCI)this.offering).destroyTopic(topic);
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return ((ManagementCI)this.offering).isTopic(topic);
	}

	@Override
	public String[] getTopics() throws Exception {
		return((ManagementCI)this.offering).getTopics();
	}

}
