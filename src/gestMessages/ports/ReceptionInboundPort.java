package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.components.Subscriber;
import gestMessages.interfaces.ReceptionCI;
import messages.MessageI;

public class ReceptionInboundPort extends AbstractInboundPort implements ReceptionCI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReceptionInboundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	public ReceptionInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, owner);
		// TODO Auto-generated constructor stub
	}
	public ReceptionInboundPort(String uri, Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(uri, implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((Subscriber)this.getOwner()).acceptMessageService(m);
		
	}

	@Override
	public void acceptMessages(MessageI[] ms)throws Exception {
		((Subscriber)this.getOwner()).acceptMessagesService(ms);
		
	}

}
