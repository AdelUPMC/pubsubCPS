package gestMessages.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.components.Broker;
import gestMessages.interfaces.PublicationCI;
import messages.MessageI;

public class PublicationInboundPort extends AbstractInboundPort implements PublicationCI {

	/**
	 * broker inbound port : provide  the services of PublicationCI
	 */
	private static final long serialVersionUID = 1L;

	public PublicationInboundPort(ComponentI owner) throws Exception {
		super(PublicationCI.class, owner);
		assert	owner != null ;
	}
	
	public PublicationInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, owner);
		assert	uri != null && owner != null ;
	}
	
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		//((Broker)this.getOwner()).publish(m, topic);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, 
								topic);
						return null;
						
					}
				});
	
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		//((Broker)this.getOwner()).publish(m, topics);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, 
								topics);
						return null;
						
					}
				});

	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		//((Broker)this.getOwner()).publish(ms, topic);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(ms, 
								topic);
						return null;
						
					}
				});

	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		//((Broker)this.getOwner()).publish(ms, topics);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(ms, 
								topics);
						return null;
						
					}
				});

	}

}
