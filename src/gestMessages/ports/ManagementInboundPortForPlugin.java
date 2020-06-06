package gestMessages.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import gestMessages.interfaces.ManagementCI;
import messages.MessageFilterI;
import gestMessages.interfaces.SubscriptionImplementationI;
import gestMessages.interfaces.ManagementImplementationI;

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
	public void subscribe(String topic, String inboundPortUri) throws Exception
	{
		this.owner.runTask((ignore) -> { 
	        try {
	        	((SubscriptionImplementationI)this.getOwner()).subscribe(topic,inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception 
	{
		this.owner.runTask((ignore) -> { 
	        try {
	        	((SubscriptionImplementationI)this.getOwner()).subscribe(topics,inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void subscribe(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	
	        	((SubscriptionImplementationI)this.getOwner()).subscribe(topic,newFilter,inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception
	{
		this.owner.runTask((ignore) -> { 
	        try {
	        	((SubscriptionImplementationI)this.getOwner()).modifyFilter(topic,newFilter,inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception 
	{
		this.owner.runTask((ignore) -> { 
	        try {
	        	((SubscriptionImplementationI)this.getOwner()).unsubscribe(topic,inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public void createTopic(String topic) throws Exception
	{
		this.owner.runTask((ignore) -> { 
	        try {
	        	((ManagementImplementationI)this.getOwner()).createTopic(topic);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		this.owner.runTask((ignore) -> { 
	        try {
	        	((ManagementImplementationI)this.getOwner()).createTopics(topics);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public void destroyTopic(String topic) throws Exception
	{
		this.owner.runTask((ignore) -> { 
	        try {
	        	((ManagementImplementationI)this.getOwner()).destroyTopic(topic);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });

	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Boolean>(this.pluginURI) {
                    @Override
                    public Boolean call() throws Exception {
                        return ((ManagementImplementationI)this.getServiceProviderReference()).isTopic(topic);
                    }
                }) ;
	}

	@Override
	public String[] getTopics() throws Exception {
		return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<String[]>(this.pluginURI) {
                    @Override
                    public String[] call() throws Exception {
                        return ((ManagementImplementationI)this.getServiceProviderReference()).getTopics();
                    }
                }) ;
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		return this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<String>(this.pluginURI) {
                    @Override
                    public String call() throws Exception {
                        return ((ManagementImplementationI)this.getServiceProviderReference()).getPublicationPortURI();
                    }
                }) ;
	}

}
