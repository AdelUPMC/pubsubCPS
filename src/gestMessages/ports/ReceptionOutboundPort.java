package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gestMessages.interfaces.ReceptionCI;
import messages.MessageI;

public class ReceptionOutboundPort extends AbstractOutboundPort implements ReceptionCI {

	/**
	 * broker outbound port: require services from ReceptionCI
	 */
	private static final long serialVersionUID = 1L;

	public ReceptionOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, owner);
		assert	uri != null && owner != null ;
	}
	
	public ReceptionOutboundPort( ComponentI owner) throws Exception {
		super(ReceptionCI.class, owner);
		assert	owner != null ;
	}
	

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((ReceptionCI)this.connector).acceptMessage(m);

	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		((ReceptionCI)this.connector).acceptMessages(ms);

	}

}
