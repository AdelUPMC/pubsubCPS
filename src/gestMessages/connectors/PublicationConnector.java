package gestMessages.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gestMessages.interfaces.PublicationCI;
import messages.MessageI;

public class PublicationConnector extends AbstractConnector implements PublicationCI {

	@Override
	public void publish(MessageI m, String topic) throws Exception{
		((PublicationCI)this.offering).publish(m, topic);

	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception{
		((PublicationCI)this.offering).publish(m, topics);
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception{
		((PublicationCI)this.offering).publish(ms, topic);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception{
		((PublicationCI)this.offering).publish(ms, topics);
	}

}
