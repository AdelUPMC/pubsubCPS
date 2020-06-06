package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import gestMessages.interfaces.PublicationCI;
import gestMessages.plugins.BrokerPublicationPlugin;
import messages.MessageI;

public class PublicationInboundPortForPlugin extends AbstractInboundPortForPlugin implements PublicationCI {

	/***
	 * extends the standard PublicationtInboundPort for the case where 
	 * the services to be called are implemented by a plug-in
	 * ***/
	private static final long serialVersionUID = 1L;

	public PublicationInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, pluginURI, owner);
	}
	
	public PublicationInboundPortForPlugin(String pluginURI, ComponentI owner) throws Exception {
		super(PublicationCI.class, pluginURI, owner);
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((BrokerPublicationPlugin)this.getOwner()).publish(m, topic);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((BrokerPublicationPlugin)this.getOwner()).publish(m, topics);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((BrokerPublicationPlugin)this.getOwner()).publish(ms, topic);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {

		this.owner.runTask((ignore) -> { 
	        try {
	        	((BrokerPublicationPlugin)this.getOwner()).publish(ms, topics);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

}
