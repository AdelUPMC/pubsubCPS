package gestMessages.ports;


import java.util.ArrayList;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.interfaces.ReceptionImplementationI;
import messages.MessageI;

public class ReceptionInboundPort extends AbstractInboundPort implements ReceptionCI{

	/**
	 * subscribers inbound ports: provide the services of ReceptionCI
	 */
	private static final long serialVersionUID = 1L;

	public ReceptionInboundPort(ComponentI owner) throws Exception {
		super(ReceptionCI.class, owner);
		assert	owner != null ;
	}

	public ReceptionInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, owner);
		assert	uri != null && owner != null ;
	}

	

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((ReceptionImplementationI)this.getOwner()).acceptMessage(m);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
		
	}

	@Override
	public void acceptMessages(ArrayList<MessageI> ms)throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((ReceptionImplementationI)this.getOwner()).acceptMessages(ms);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

}
