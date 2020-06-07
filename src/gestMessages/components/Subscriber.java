package gestMessages.components;

import java.util.ArrayList;
import java.util.Arrays;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gestMessages.TestScenario;
import gestMessages.interfaces.ReceptionImplementationI;
import gestMessages.plugins.PubSubManagementPlugin;
import gestMessages.plugins.SubscriberReceptionPlugin;
import messages.MessageFilterI;
import messages.MessageI;
import test.SubscriberScenario;
public class Subscriber extends AbstractComponent implements ReceptionImplementationI {

    protected String SUBSCRIBER_MANAGEMENT_PLUGIN_URI = "subscriber_management_URI-" ;
    protected String SUBSCRIBER_RECEPTION_PLUGIN_URI = "subscriber_reception_URI-" ;
    protected String receptionInboundPortURI = "software_developer_URI-";
   
    private final String scenario;
    private PubSubManagementPlugin managementPlugin;
    private SubscriberReceptionPlugin receptionPlugin;
    
    private static int nbsubscribers = 0;
    private int subscriberId;
    
    protected Subscriber(String reflexionURI) throws Exception {
    	this(reflexionURI, 1,0, null);
    }
    
	protected Subscriber(String reflectionURI,int nbThreads,int nbSchedulableThreads, String scenario) throws Exception {
		super(reflectionURI + nbsubscribers, nbThreads, nbSchedulableThreads);
		//1 thread, 0 schedulable thread
		synchronized(this) {
			nbsubscribers++;
			subscriberId = nbsubscribers;
			this.SUBSCRIBER_RECEPTION_PLUGIN_URI = this.SUBSCRIBER_RECEPTION_PLUGIN_URI + nbsubscribers;
	        this.SUBSCRIBER_MANAGEMENT_PLUGIN_URI = this.SUBSCRIBER_MANAGEMENT_PLUGIN_URI + nbsubscribers;
	        this.receptionInboundPortURI = this.receptionInboundPortURI + nbsubscribers;
		}
		this.scenario = scenario;
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir"));
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home"));
		}
		
		this.tracer.setTitle("software_developer-"+nbsubscribers);
		this.tracer.setRelativePosition(nbsubscribers, 3) ;
		System.out.println("[Subsrciber:Subscriber]Subscriber crée");
	}

	@Override
	public void start() throws ComponentStartException{
		super.start() ;
		this.logMessage("starting component subscriber "+Subscriber.nbsubscribers);
    }
	
	
	public void execute() throws Exception{
		//create plugings
		this.receptionPlugin = new SubscriberReceptionPlugin(receptionInboundPortURI, SUBSCRIBER_RECEPTION_PLUGIN_URI) ;
		this.managementPlugin = new PubSubManagementPlugin();
		
		//install them
		receptionPlugin.setPluginURI(this.SUBSCRIBER_RECEPTION_PLUGIN_URI);
		managementPlugin.setPluginURI(this.SUBSCRIBER_MANAGEMENT_PLUGIN_URI);
		this.installPlugin(managementPlugin);
		this.installPlugin(receptionPlugin);	
		if (scenario == null)
		{
			System.out.println("[Subscriber:execute]Aucun scenario lancer");
			return;
		}
		switch (scenario)
		{
		case TestScenario.SCENARIO_BASIC1:	
			SubscriberScenario.testBasic1(this);
			break;
		case TestScenario.SCENARIO_BASIC2:	
			SubscriberScenario.testBasic2(this);
			break;
		case TestScenario.SCENARIO_NORMAL1:	
			SubscriberScenario.testNormal1(this);
			break;
		case TestScenario.SCENARIO_NORMAL2:	
			SubscriberScenario.testNormal2(this);
			break;

		case TestScenario.TestCompletTopSub:
			SubscriberScenario.testCompletTopicSubcribe(this);
			break;
		default:
			SubscriberScenario.testBasic1(this);
			break;
		}
	}
	
	
	@Override
	 public void			finalise() throws Exception{
	        this.logMessage("finalising component subscriber "+Subscriber.nbsubscribers) ;
	        super.finalise();
	 }
	
	@Override
	 public void			shutdown()  throws ComponentShutdownException{
	        this.logMessage("shutdown : component subscriber "+Subscriber.nbsubscribers) ;
	       // super.shutdown();
	 }
	
	@Override
	 public void			shutdownNow()  throws ComponentShutdownException{
	        this.logMessage("shutdownNow : component subscriber "+Subscriber.nbsubscribers) ;
	        super.shutdownNow();
	 }
	
	//ReceptionCI
	public void acceptMessage(MessageI m) throws Exception
	{	
		System.out.println("[Subsrciber:Accept message] " + m.getURI() + " a recu ce message => " + m.getPayload());
		this.logMessage("[Accept message] " + m.getURI() + " a recu ce message => " + m.getPayload());
	}
	public void acceptMessages(ArrayList<MessageI> ms) throws Exception
	{
		this.logMessage("[Subscriber:acceptMessages] bloc de " + ms.size() + " messages recus" );
		System.out.println("[Subscriber:acceptMessages] bloc de " + ms.size() + " messages recus");
		/*
		 * for (MessageI m : ms) {
		 *	acceptMessage(m);
	     *     }
		 */
	}
	
	//ManagementCI
	 public void subscribe(String topic, String inboundPortURI)throws Exception {
		 	this.logMessage("Subscriber"+Subscriber.nbsubscribers+" is subscribing to topic "+topic);
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).subscribe(topic, inboundPortURI);
	  }

	 public void subscribe(String[] topics, String inboundPortURI)throws Exception {
		 	this.logMessage("Subscriber"+Subscriber.nbsubscribers+" is subscribing to topics: "+Arrays.toString(topics));
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).subscribe(topics, inboundPortURI);
	 }
	 public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
			this.logMessage("Subscriber"+Subscriber.nbsubscribers+" is subscribing to topic "+topic+"with Filter");
	        ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).subscribe(topic,filter, inboundPortURI);
	  }
	 public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception{
		 this.logMessage("Subscriber"+Subscriber.nbsubscribers+" is modifying the filter of the topic "+topic);
		 ((PubSubManagementPlugin)this.getPlugin(SUBSCRIBER_MANAGEMENT_PLUGIN_URI)).subscribe(topic, newFilter, inboundPortURI);
	  }
	 public void unsubscribe(String topic, String inboundPortUri) throws Exception {
		 this.logMessage("Subscriber"+Subscriber.nbsubscribers+" is unsubscribing to topic "+topic);
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
	
	 public SubscriberReceptionPlugin getReceptionPlugin()
	 {
		 return receptionPlugin;
	 }
	 
	 public int getSubscriberId()
	 {
		 return this.subscriberId;
	 }
}
