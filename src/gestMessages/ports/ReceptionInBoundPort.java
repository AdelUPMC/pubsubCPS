package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.interfaces.ReceptionCI;
import messages.MessageI;

public class ReceptionInBoundPort extends AbstractInboundPort implements ReceptionCI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReceptionInBoundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	public ReceptionInBoundPort(String uri, Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(uri, implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void acceptMessage(MessageI m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptMessages(MessageI[] ms) {
		// TODO Auto-generated method stub
		
	}

}
