package gestMessages.ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import gestMessages.components.Broker;
import gestMessages.interfaces.ManagementCI;
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
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		//((Broker)this.getOwner()).subscribe(topic,inboundPortUri);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).subscribe(topic, inboundPortUri);
						return null;
						
					}
				});
	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception{
		//((Broker)this.getOwner()).subscribe(topics,inboundPortUri);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).subscribe(topics, inboundPortUri);
						return null;
						
					}
				});
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortUri) throws Exception{
		//((Broker)this.getOwner()).subscribe(topic,filter, inboundPortUri);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).subscribe(topic,filter,inboundPortUri);
						return null;
						
					}
				});
		
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception{
		//((Broker)this.getOwner()).modifyFilter(topic,newFilter,inboundPortUri);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).modifyFilter(topic,newFilter,inboundPortUri);
						return null;
						
					}
				});
	}

	@Override
	public void unsubscribe(String topic, String inboundPortUri) throws Exception{
		//((Broker)this.getOwner()).unsubscribe(topic, inboundPortUri);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).unsubscribe(topic, inboundPortUri);
						return null;
						
					}
				});
		
	}

	@Override
	public void createTopic(String topic) throws Exception{
		//((Broker)this.getOwner()).createTopic(topic);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).createTopic(topic);
						return null;
						
					}
				});
		
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		//((Broker)this.getOwner()).createTopics(topics);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).createTopics(topics);
						return null;
						
					}
				});
		
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		//((Broker)this.getOwner()).destroyTopic(topic);
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).destroyTopic(topic);
						return null;
						
					}
				});
		
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		//return ((Broker)this.getOwner()).isTopic(topic);
		return this.owner.handleRequestSync(
				new AbstractComponent.AbstractService<Boolean>(){
					@Override
					public Boolean call() throws Exception {
						return ((Broker)this.getServiceOwner()).isTopic(topic);
					}
			});
	}

	@Override
	public String[] getTopics() throws Exception {
		//return ((Broker)this.getOwner()).getTopics();
		return this.owner.handleRequestSync(
				new AbstractComponent.AbstractService<String[]>() {
					@Override
					public String[] call() throws Exception {
						return ((Broker)this.getServiceOwner()).getTopics();
						
					}
				});
	}

}
