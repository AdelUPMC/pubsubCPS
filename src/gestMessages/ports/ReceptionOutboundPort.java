package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import gestMessages.interfaces.ReceptionImplementationI;
import messages.MessageI;

public class ReceptionOutboundPort extends AbstractOutboundPort implements ReceptionImplementationI {

	/**
	 * broker outbound port: require services from ReceptionImplementationI
	 */
	private static final long serialVersionUID = 1L;

	public ReceptionOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ReceptionImplementationI.class, owner);
		assert	uri != null && owner != null ;
	}
	
	public ReceptionOutboundPort( ComponentI owner) throws Exception {
		super(ReceptionImplementationI.class, owner);
		assert	owner != null ;
	}
	

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((ReceptionImplementationI)this.connector).acceptMessage(m);

	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		((ReceptionImplementationI)this.connector).acceptMessages(ms);

	}

}
