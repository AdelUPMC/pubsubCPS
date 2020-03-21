package gestMessages.ports;

import fr.sorbonne_u.components.AbstractComponent;
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
		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((BrokerPublicationPlugin)this.getServiceProviderReference()).publish(m, topic);
						return null;
					}
					
				});
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((BrokerPublicationPlugin)this.getServiceProviderReference()).publish(m, topics);
						return null;
					}
				});
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((BrokerPublicationPlugin)this.getServiceProviderReference()).publish(ms, topic);
						return null;
					}
				});
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.getOwner().handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((BrokerPublicationPlugin)this.getServiceProviderReference()).publish(ms, topics);
						return null;
					}
				});
	}

}
