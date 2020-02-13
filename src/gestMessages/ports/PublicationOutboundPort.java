package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gestMessages.interfaces.PublicationCI;
import messages.MessageI;

public class PublicationOutboundPort extends AbstractOutboundPort implements PublicationCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public PublicationOutboundPort( ComponentI owner) throws Exception {
		super(PublicationCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	public PublicationOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	public PublicationOutboundPort(String uri, Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(uri, implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		((PublicationCI)this.connector).publish(m, topic);

	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception{
		((PublicationCI)this.connector).publish(m, topics);

	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception{
		((PublicationCI)this.connector).publish(ms, topic);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception{
		((PublicationCI)this.connector).publish(ms, topics);
	}

}
