package gestMessages.ports;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import gestMessages.interfaces.ManagementCI;
import messages.MessageFilterI;
import gestMessages.plugins.BrokerManagementPlugin;;

/***
 * extends the standard ManagementInboundPort for the case where 
 * the services to be called are implemented by a plug-in
 * ***/
public class ManagementInboundPortForPlugin extends AbstractInboundPortForPlugin implements ManagementCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ManagementInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, pluginURI, owner);
	}
	
	public ManagementInboundPortForPlugin(String pluginURI, ComponentI owner) throws Exception {
		super(ManagementCI.class, pluginURI, owner);
	}
	
	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).subscribe(topic,inboundPortUri);
                        return null;
                    }
                }) ;

	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).subscribe(topics,inboundPortUri);
                        return null;
                    }
                }) ;

	}

	@Override
	public void subscribe(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).subscribe(topic,newFilter,inboundPortUri);
                        return null;
                    }
                }) ;

	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).modifyFilter(topic,newFilter,inboundPortUri);
                        return null;
                    }
                }) ;

	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).unsubscribe(topic,inboundPortUri);
                        return null;
                    }
                }) ;

	}

	@Override
	public void createTopic(String topic) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).createTopic(topic);
                        return null;
                    }
                }) ;

	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).createTopics(topics);
                        return null;
                    }
                }) ;

	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>(this.pluginURI) {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerManagementPlugin)this.getServiceProviderReference()).destroyTopic(topic);
                        return null;
                    }
                }) ;

	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Boolean>(this.pluginURI) {
                    @Override
                    public Boolean call() throws Exception {
                        return ((BrokerManagementPlugin)this.getServiceProviderReference()).isTopic(topic);
                    }
                }) ;
	}

	@Override
	public String[] getTopics() throws Exception {
		return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<String[]>(this.pluginURI) {
                    @Override
                    public String[] call() throws Exception {
                        return ((BrokerManagementPlugin)this.getServiceProviderReference()).getTopics();
                    }
                }) ;
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<String>(this.pluginURI) {
                    @Override
                    public String call() throws Exception {
                        return ((BrokerManagementPlugin)this.getServiceProviderReference()).getPublicationPortURI();
                    }
                }) ;
	}

}
