package gestMessages.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.plugins.SubscriberReceptionPlugin;
import messages.MessageI;

public class ReceptionInboundPortForPlugin extends AbstractInboundPortForPlugin implements ReceptionCI {

	/***
	 * extends the standard ReceptiontInboundPort for the case where 
	 * the services to be called are implemented by a plug-in
	 * ***/
	private static final long serialVersionUID = 1L;

	public ReceptionInboundPortForPlugin(String pluginURI, ComponentI owner) throws Exception {
		super(ReceptionCI.class, pluginURI, owner);
		assert owner instanceof ReceptionCI;
	}
	
	public ReceptionInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, pluginURI, owner);
		assert owner instanceof ReceptionCI;
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((SubscriberReceptionPlugin) this.getServiceProviderReference()).acceptMessage(m);
                        return null;
                    }
                });
		
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		 this.getOwner().handleRequestAsync(
	                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
	                    @Override
	                    public Void call() throws Exception {
	                        ((SubscriberReceptionPlugin) this.getServiceProviderReference()).acceptMessages(ms);
	                        return null;
	                    }
	                });
		
	}
	
	
}