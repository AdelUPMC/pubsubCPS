package gestMessages.ports;

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
		this.owner.runTask((ignore) -> { 
	        try {
	        	((Broker)this.getOwner()).publish(m, 
	        			topic);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((Broker)this.getOwner()).publish(m, topics);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((Broker)this.getOwner()).publish(ms, topic);	        
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((Broker)this.getOwner()).publish(ms, topics);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

}
