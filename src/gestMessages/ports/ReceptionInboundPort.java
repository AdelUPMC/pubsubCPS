package gestMessages.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.components.Subscriber;
import gestMessages.interfaces.ReceptionCI;
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
		//((Subscriber)this.getOwner()).acceptMessage(m);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Subscriber)this.getServiceOwner()).acceptMessage(m);
						return null;
						
					}
				});
		
	}

	@Override
	public void acceptMessages(MessageI[] ms)throws Exception {
		//((Subscriber)this.getOwner()).acceptMessages(ms);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Subscriber)this.getServiceOwner()).acceptMessages(ms);
						return null;
						
					}
				});
		
	}

}
