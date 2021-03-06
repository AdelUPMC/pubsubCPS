package gestMessages.components;


import java.util.Arrays;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gestMessages.TestScenario;
import gestMessages.plugins.PubSubManagementPlugin;
import gestMessages.plugins.PublisherPublicationPlugin;
import messages.MessageFilterI;
import messages.MessageI;
import test.PublisherScenario;



public class Publisher extends AbstractComponent {
	protected String PUBLISHER_PUBLICATION_PLUGIN_URI = "publisher_publication_URI-" ;
    protected String PUBLISHER_MANAGEMENT_PLUGIN_URI = "publisher_management_URI-" ;
    private PubSubManagementPlugin managementPlugin;
    private PublisherPublicationPlugin publicationPlugin;
    private static int nbpublishers = 0;
    
    private String scenario;
    private final int publisherId;

    protected Publisher() throws Exception {
    	this(null);
    }
    protected Publisher(String scenario) throws Exception {
    	//1 thread, 0 schedulable thread
        this(1, 0, scenario);
    }
	protected Publisher(int nbThreads,int nbSchedulableThreads, String scenario) throws Exception {
		//1 thread, 0 schedulable thread
		super(nbThreads, nbSchedulableThreads) ;
		synchronized(this) {
			nbpublishers++;
			publisherId = nbpublishers;
			this.PUBLISHER_PUBLICATION_PLUGIN_URI = this.PUBLISHER_PUBLICATION_PLUGIN_URI + nbpublishers;
	        this.PUBLISHER_MANAGEMENT_PLUGIN_URI = this.PUBLISHER_MANAGEMENT_PLUGIN_URI + nbpublishers;
		}
		this.scenario = scenario;
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir"));
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home"));
		}
		this.tracer.setTitle("Publisher"+nbpublishers);
		this.tracer.setRelativePosition(nbpublishers, 2) ;
	}

	public void			start() throws ComponentStartException
	{
		super.start() ;
		this.logMessage("starting component publisher "+Publisher.nbpublishers);

	}
	public void execute() throws Exception{
		//create plugings
		this.publicationPlugin=new PublisherPublicationPlugin() ;
		this.managementPlugin=new PubSubManagementPlugin();
		
		//install them
		publicationPlugin.setPluginURI(this.PUBLISHER_PUBLICATION_PLUGIN_URI);
		managementPlugin.setPluginURI(this.PUBLISHER_MANAGEMENT_PLUGIN_URI);
		this.installPlugin(publicationPlugin);
		this.installPlugin(managementPlugin);
		if (scenario == null)
		{
			return;
		}
		switch (scenario)
		{
		case TestScenario.SCENARIO_BASIC1:
			PublisherScenario.testBasic1(this);
			break;
		case TestScenario.SCENARIO_BASIC2:	
			PublisherScenario.testBasic2(this);
			break;
		case TestScenario.SCENARIO_NORMAL1:	
			PublisherScenario.testNormal1(this);
			break;
		case TestScenario.SCENARIO_NORMAL:	
			PublisherScenario.testNormal(this);
			break;
		case TestScenario.SCENARIO_BIGGER:	
			PublisherScenario.testBigger(this);
			break;
		case TestScenario.SCENARIO_INSANE:	
			PublisherScenario.testInsane(this);
			break;
		case TestScenario.TestCompletPublisher:
			PublisherScenario.testCompletPublisher(this);
			break;
		default:
			PublisherScenario.testBasic1(this);
			break;
		}
	}
	
	@Override
	 public void			finalise() throws Exception{
	        this.logMessage("finalising component publisher "+Publisher.nbpublishers) ;
	        super.finalise();
	 }
	@Override
	 public void			shutdown()  throws ComponentShutdownException{
	        this.logMessage("shutdown : component publisher "+Publisher.nbpublishers) ;
	    //    super.shutdown();
	 }
	
	@Override
	 public void			shutdownNow()  throws ComponentShutdownException{
	        this.logMessage("shutdownNow : component publisher "+Publisher.nbpublishers) ;
	        super.shutdownNow();
	 }
	 
	//PublicationCI
	 public void publish(MessageI m, String topic) throws Exception {
		 this.logMessage("Publisher"+Publisher.nbpublishers+" is publishing message:"+m.getPayload()+ "=> topic="+ topic);
		 ((PublisherPublicationPlugin)this.getPlugin(PUBLISHER_PUBLICATION_PLUGIN_URI)).publish(m,topic);
	  }
	 
	 public void publish(MessageI m, String[] topics) throws Exception {
		 this.logMessage("Publisher"+Publisher.nbpublishers+" is publishing message:"+m.getPayload()+ "=> "+ topics.length + "topics");
		 ((PublisherPublicationPlugin)this.getPlugin(PUBLISHER_PUBLICATION_PLUGIN_URI)).publish(m,topics);
	  }
	 
	 public void publish(MessageI[] ms, String topic) throws Exception {
		 String listeMessages="[";
		 for(MessageI m: ms) {
			 listeMessages+=m.getPayload();
		 }
		 listeMessages+="]";
		 this.logMessage("Publisher"+Publisher.nbpublishers+" is publishing messages:"+listeMessages+"=> topic="+topic);
		 ((PublisherPublicationPlugin)this.getPlugin(PUBLISHER_PUBLICATION_PLUGIN_URI)).publish(ms,topic);
	 }
	 
	 public void publish(MessageI[] ms, String[] topics) throws Exception {
		 String listeMessages="[";
		 for(MessageI m: ms) {
			 listeMessages+=m.getPayload();
		 }
		 listeMessages+="]";
		 this.logMessage("Publisher"+Publisher.nbpublishers+" is publishing messages:"+listeMessages+ "=> topics="+ Arrays.toString(topics));
		 ((PublisherPublicationPlugin)this.getPlugin(PUBLISHER_PUBLICATION_PLUGIN_URI)).publish(ms,topics);
	 }
	 
	 
	 //ManagementCI
	 public void subscribe(String topic, String inboundPortURI)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).subscribe(topic, inboundPortURI);
	  }

	 public void subscribe(String[] topics, String inboundPortURI)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).subscribe(topics, inboundPortURI);
	 }
	 public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception{
	        ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).subscribe(topic,filter, inboundPortURI);
	  }
	 public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception{
	        ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).subscribe(topic, newFilter, inboundPortURI);
	  }
	 public void unsubscribe(String topic, String inboundPortUri) throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).unsubscribe(topic, inboundPortUri);
	    }
	 public void createTopic(String topic)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).createTopic(topic);
	  }
	 public void createTopics(String[] topic)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).createTopics(topic);
	  }
	 
	 public void destroyTopic(String topic)throws Exception {
	        ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).destroyTopic(topic);
	  }
	 public boolean isTopic(String topic) throws Exception{
	        return  ((PubSubManagementPlugin)this.getPlugin(PUBLISHER_MANAGEMENT_PLUGIN_URI)).isTopic(topic);
	  }
	 
	 public String[] getTopics() throws Exception{
	        return ((PubSubManagementPlugin)this.getPlugin(this.PUBLISHER_MANAGEMENT_PLUGIN_URI)).getTopics();
	  }
	 
	 public String getPublicationPortURI() throws Exception{
	        return ((PubSubManagementPlugin)this.getPlugin(this.PUBLISHER_MANAGEMENT_PLUGIN_URI)).getPublicationPortURI();
	  }
	 
	 
	 public int getPublisherId()
	 {
		 return this.publisherId;
	 }
	
}
