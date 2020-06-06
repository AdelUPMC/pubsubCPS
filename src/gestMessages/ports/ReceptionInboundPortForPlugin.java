package gestMessages.ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.interfaces.ReceptionImplementationI;
import messages.MessageI;

public class ReceptionInboundPortForPlugin extends AbstractInboundPortForPlugin implements ReceptionCI {

	/***
	 * extends the standard ReceptiontInboundPort for the case where 
	 * the services to be called are implemented by a plug-in
	 * ***/
	private static final long serialVersionUID = 1L;

	public ReceptionInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, pluginURI, owner);
		assert owner instanceof ReceptionCI;
	}

	public ReceptionInboundPortForPlugin(String pluginURI, ComponentI owner) throws Exception {
		super(ReceptionCI.class, pluginURI, owner);
		assert owner instanceof ReceptionCI;
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((ReceptionImplementationI) this.getOwner()).acceptMessage(m);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		 this.owner.runTask((ignore) -> { 
		        try {
		        	((ReceptionImplementationI) this.getOwner()).acceptMessages(ms);
		        } catch (Exception e) {	
		            e.printStackTrace();
		        }
		    });
		
	}
}