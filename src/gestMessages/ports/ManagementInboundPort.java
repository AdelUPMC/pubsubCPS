package gestMessages.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.interfaces.ManagementCI;
import gestMessages.interfaces.SubscriptionImplementationI;
import gestMessages.interfaces.ManagementImplementationI;
import messages.MessageFilterI;

public class ManagementInboundPort extends AbstractInboundPort implements ManagementCI {

	/**
	 * broker inbound port : provide  the services of ManagementCI
	 */
	private static final long serialVersionUID = 1L;

	public ManagementInboundPort(ComponentI owner) throws Exception {
		super(ManagementCI.class, owner);
		assert	owner != null ;
	}

	public ManagementInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
		assert	uri != null && owner != null ;
	}
	
	

	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception
	{
		this.owner.runTask((ignore) -> { 
	        try {
	        	((SubscriptionImplementationI)this.getOwner()).subscribe(topic, inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception{
		this.owner.runTask((ignore) -> { 
	        try {	        	
	        	((SubscriptionImplementationI)this.getOwner()).subscribe(topics, inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortUri) throws Exception{
		this.owner.runTask((ignore) -> { 
	        try {	        	
	        	((SubscriptionImplementationI)this.getOwner()).subscribe(topic,filter,inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception{
		this.owner.runTask((ignore) -> { 
	        try {	        	
	        	((SubscriptionImplementationI)this.getOwner()).modifyFilter(topic,newFilter,inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception{
		this.owner.runTask((ignore) -> { 
	        try {
	        	
	        	((SubscriptionImplementationI)this.getOwner()).unsubscribe(topic, inboundPortUri);
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void createTopic(String topic) throws Exception{
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
	public void destroyTopic(String topic) throws Exception {
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
		return this.owner.handleRequestSync(
				new AbstractComponent.AbstractService<Boolean>(){
					@Override
					public Boolean call() throws Exception {
						return ((ManagementImplementationI)this.getServiceOwner()).isTopic(topic);
					}
			});
	}

	@Override
	public String[] getTopics() throws Exception {
		return this.owner.handleRequestSync(
				new AbstractComponent.AbstractService<String[]>() {
					@Override
					public String[] call() throws Exception {
						return ((ManagementImplementationI)this.getServiceOwner()).getTopics();
						
					}
				});
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		return this.owner.handleRequestSync(
				new AbstractComponent.AbstractService<String>() {
					@Override
					public String call() throws Exception {
						return ((ManagementImplementationI)this.getServiceOwner()).getPublicationPortURI();
						
					}
				});
	}

}
