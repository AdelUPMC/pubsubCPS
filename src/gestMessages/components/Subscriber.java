package gestMessages.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gestMessages.plugins.PubSubManagementPlugin;
import gestMessages.plugins.SubscriberReceptionPlugin;
import messages.MessageFilterI;
import messages.MessageI;
public class Subscriber extends AbstractComponent {

    protected String SUBSCRIBER_MANAGEMENT_PLUGIN_URI = "subscriber_management_URI-" ;
    protected String SUBSCRIBER_RECEPTION_PLUGIN_URI = "subscriber_reception_URI-" ;
    private PubSubManagementPlugin managementPlugin;
    private SubscriberReceptionPlugin receptionPlugin;
    protected String receptionInboundPortURI = "software_developer_URI-";
    private static int nbsubscribers = 0;
    protected Subscriber(String receptionInboundPortURI) throws Exception {
    	this(receptionInboundPortURI, 1,0);
    }
    
	protected Subscriber(String receptionInboundPortURI,int nbThreads,int nbSchedulableThreads) throws Exception {
		//1 thread, 0 schedulable thread
		super(receptionInboundPortURI, 1,0);
		synchronized(this) {
			nbsubscribers++;
			this.SUBSCRIBER_RECEPTION_PLUGIN_URI = this.SUBSCRIBER_RECEPTION_PLUGIN_URI + nbsubscribers;
	        this.SUBSCRIBER_MANAGEMENT_PLUGIN_URI = this.SUBSCRIBER_MANAGEMENT_PLUGIN_URI + nbsubscribers;
	        this.receptionInboundPortURI = this.receptionInboundPortURI + nbsubscribers;
		}
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir"));
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home"));
		}
		this.tracer.setTitle("software_developer-"+nbsubscribers);
		this.tracer.setRelativePosition(nbsubscribers, 2) ;
	}

	@Override
	public void start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting component subscriber "+Subscriber.nbsubscribers);
    }
	
	public void execute() throws Exception{
		//create plugings
		this.receptionPlugin=new SubscriberReceptionPlugin(receptionInboundPortURI,SUBSCRIBER_RECEPTION_PLUGIN_URI) ;
		this.managementPlugin=new PubSubManagementPlugin();
		
		//install them
		receptionPlugin.setPluginURI(this.SUBSCRIBER_RECEPTION_PLUGIN_URI);
		managementPlugin.setPluginURI(this.SUBSCRIBER_MANAGEMENT_PLUGIN_URI);
		this.installPlugin(receptionPlugin);
		this.installPlugin(managementPlugin);
		
		/**
		 * � tester: 3 m�thodes subscribe(), modifyfilter(),unsubscribe(), 2 m�thodes acceptMessage()
		 * sc�nario: un subscriber va s'abonner en utilisant les 3 m�thodes subscribe
		 * 	
		 * **/
		
		 subscribe("C++",receptionPlugin.receptionInboundPortURI);
		 subscribe(new String[] {"Object-oriented programming", "Java"},receptionPlugin.receptionInboundPortURI);
		 //subscribe(new String[] {"Object-oriented programming", "Java"},receptionPlugin.receptionInboundPortURI);

	}

	@Override
	 public void			finalise() throws Exception{
	        this.logMessage("finalising component subscriber "+Subscriber.nbsubscribers) ;
	        super.finalise();
	 }
	
	@Override
	 public void			shutdown()  throws ComponentShutdownException{
	        this.logMessage("shutdown : component subscriber "+Subscriber.nbsubscribers) ;
	        super.shutdown();
	 }
	
	@Override
	 public void			shutdownNow()  throws ComponentShutdownException{
	        this.logMessage("shutdownNow : component subscriber "+Subscriber.nbsubscribers) ;
	        super.shutdownNow();
	 }
	
	//ReceptionCI
	public void acceptMessage(MessageI m) throws Exception {
		this.logMessage("Accept message:"+m.getURI());
	}
	public void acceptMessages(MessageI[] ms) throws Exception {
		for (MessageI m : ms) {
			acceptMessage(m);
	    }
	}
	
	//ManagementCI
	 public void subscribe(String topic, String inboundPortURI)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).subscribe(topic, inboundPortURI);
	  }

	 public void subscribe(String[] topics, String inboundPortURI)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).subscribe(topics, inboundPortURI);
	 }
	 public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).subscribe(topic,filter, inboundPortURI);
	  }
	 public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception{
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).subscribe(topic, newFilter, inboundPortURI);
	  }
	 public void unsubscribe(String topic, String inboundPortUri) throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).unsubscribe(topic, inboundPortUri);
	    }
	 public void createTopic(String topic)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).createTopic(topic);
	  }
	 public void createTopics(String[] topic)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).createTopics(topic);
	  }
	 
	 public void destroyTopic(String topic)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).destroyTopic(topic);
	  }
	 public boolean isTopic(String topic) throws Exception{
	        return  ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).isTopic(topic);
	  }
	 
	 public String[] getTopics() throws Exception{
	        return ((PubSubManagementPlugin)this.getPlugin(this.SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).getTopics();
	  }
	 
	 public String getPublicationPortURI() throws Exception{
	        return ((PubSubManagementPlugin)this.getPlugin(this.SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).getPublicationPortURI();
	  }
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
