package gestMessages.ports;

import java.util.ArrayList;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import gestMessages.interfaces.ReceptionImplementationI;
import messages.MessageI;

public class ReceptionInboundPortForPlugin extends AbstractInboundPortForPlugin implements ReceptionImplementationI {

	/***
	 * extends the standard ReceptiontInboundPort for the case where 
	 * the services to be called are implemented by a plug-in
	 * ***/
	private static final long serialVersionUID = 1L;

	public ReceptionInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
		super(uri, ReceptionImplementationI.class, pluginURI, owner);
		assert owner instanceof ReceptionImplementationI;
	}

	public ReceptionInboundPortForPlugin(String pluginURI, ComponentI owner) throws Exception {
		super(ReceptionImplementationI.class, pluginURI, owner);
		assert owner instanceof ReceptionImplementationI;
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
	public void acceptMessages(ArrayList<MessageI> ms) throws Exception {
		 this.owner.runTask((ignore) -> { 
		        try {
		        	((ReceptionImplementationI) this.getOwner()).acceptMessages(ms);
		        } catch (Exception e) {	
		            e.printStackTrace();
		        }
		    });
		
	}
}