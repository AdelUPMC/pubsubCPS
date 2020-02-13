package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.components.Broker;
import gestMessages.interfaces.PublicationCI;
import messages.MessageI;

public class PublicationInboundPort extends AbstractInboundPort implements PublicationCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PublicationInboundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	public PublicationInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	public PublicationInboundPort(String uri, Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(uri, implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
			((Broker)this.getOwner()).publishService(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		((Broker)this.getOwner()).publishService(m, topics);

	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		((Broker)this.getOwner()).publishService(ms, topic);

	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		((Broker)this.getOwner()).publishService(ms, topics);

	}

}
