package gestMessages.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import gestMessages.connectors.ReceptionConnector;
import gestMessages.interfaces.ManagementCI;
import gestMessages.interfaces.ManagementImplementationI;
import gestMessages.interfaces.PublicationCI;
import gestMessages.interfaces.PublicationsImplementationI;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.plugins.BrokerManagementPlugin;
import gestMessages.plugins.BrokerPublicationPlugin;
import gestMessages.ports.ReceptionOutboundPort;
import messages.MessageFilterI;
import messages.MessageI;

public class Broker extends AbstractComponent implements ManagementImplementationI,PublicationsImplementationI{

	/**
	 * <String,ArrayList<MessageI>> messages: 
	 * 	-String: topic
	 * 	-ArrayList<MessageI>: liste des MessageI associes
	 * 
	 * Map<String,ArrayList<String>> abonnement: 
	 * 	-String: topic
	 * 	-ArrayList<String>: liste des uris des abonnes au topic
	 *
	 * Map<String,MessageFilterI> filters: 
	 * 	-String: inbound port uri
	 * 	-MessageFilterI: MessageFilterI associe	
	 * 
	 * Map<String,ReceptionOutboundPort> subsobp: 
	 * 	-String: uri de l'inbound port du  subscriber
	 * 	-ReceptionOutboundPort: outboundport  du subscriber pour la reception	
	 * 
	 * Map<ReceptionOutboundPort,ArrayList<MessageI>> published: 
	 * 	-ReceptionOutboundPort: ReceptionOutboundPort du subscriber
	 * 	-MessageI: message qui doit a été publié et qui doit être transmit au subscriber			
	 * 
			
	 */
	protected String uriPrefix;
	protected final String acceptURI="handler-accept";
	protected final String publishMessageURI = "message-URI";
	private int 							nbSubscriber;
	
	//private Map<String,ArrayList<MessageI>> messages;
	//private Map<String,ArrayList<String>> abonnement;
	private Map<String,ArrayList<Couple>> abonnements;
	//private Map<String, MessageFilterI> filters;
	private ArrayList<String> allTopics;
	private Map<String,ReceptionOutboundPort> subsobp;
	//private Map<ReceptionOutboundPort,ArrayList<MessageI>> published;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	protected String BROKER_PUBLICATION_PLUGIN_URI = "broker_publication_URI-" ;
    protected String BROKER_MANAGEMENT_PLUGIN_URI = "broker_management_URI-" ;
    private BrokerManagementPlugin bmanagementPlugin;
    private BrokerPublicationPlugin bpublicationPlugin;
	private class Couple
	{
		private final String uri;
		private MessageFilterI filtre;
		
		
		public Couple(String uri, MessageFilterI filtre) {
			this.uri = uri;
			this.filtre = filtre;
		}	
		public String getUri() {
			return uri;
		}
		public MessageFilterI getFiltre() {
			return filtre;
		}
		public void setFiltre(MessageFilterI filtre) {
			this.filtre = filtre;
		}
		public boolean hasFiltre()
		{
			return (filtre != null);
		}
	}

	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}
	
	protected Broker(String uri,String PublicationInboundPortURI, String ManagementInboundPortURI)throws Exception {
		
		super(uri, 0, 1) ;
		this.addRequiredInterface(ReceptionCI.class);
		assert	uri != null :
			new PreconditionException("error uri null") ;
		assert	PublicationInboundPortURI != null :
			new PreconditionException("error PublicationInboundPortURI null") ;
		assert	ManagementInboundPortURI != null :
			new PreconditionException("error ManagementInboundPortURI null") ;
		this.uriPrefix=uri;
		this.abonnements= new HashMap<>();
		this.subsobp= new HashMap<>();
	//	this.published= new HashMap<>();
		this.allTopics = new ArrayList<>();
		
		//create plugins
		this.bmanagementPlugin = new BrokerManagementPlugin();
		this.bpublicationPlugin= new BrokerPublicationPlugin();
		
		
		//install them
		bmanagementPlugin.setPluginURI(this.BROKER_MANAGEMENT_PLUGIN_URI);
		this.installPlugin(bmanagementPlugin);
		
		bpublicationPlugin.setPluginURI(this.BROKER_PUBLICATION_PLUGIN_URI);
		this.installPlugin(bpublicationPlugin);
	
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;
	}
	

	@Override
	public void	start() throws ComponentStartException{
		super.start();
		logMessage("[start] done");
	}
	
	@Override
	public void execute() throws Exception{
		super.execute();
		this.createNewExecutorService(publishMessageURI, 5,true);
		logMessage("[Execute] done");
	}
	
	public void sendpublished(MessageI m,String topic)throws Exception {
		ArrayList<Couple> uris = abonnements.get(topic);
		if (uris != null)
		{
			for(Couple abonne_uri: uris)
			{
				ReceptionOutboundPort ri = subsobp.get(abonne_uri.getUri());
				if (ri != null)
				{
					logMessage("[sendpublished] try to send \"" +m.getPayload()  + "\" to " + abonne_uri.getUri());
					if (abonne_uri.hasFiltre() && abonne_uri.getFiltre().filter(m))
						ri.acceptMessage(m);
				}
				else
				{
					System.out.println("[sendPublished] PROBLEME un abooner n'est pas connecter");
				}
		}
		}
		if (uris == null || uris.isEmpty())
			logMessage("[sendpulished] aucun abonner au topic :" + topic);
		System.out.println("[Broker:sendpublished] fin");
	}
	
	/*PublicationCI*/
	public void publish(MessageI m, String topic)throws Exception {
			logMessage("[publish] try to publish : " + m.getPayload());
			System.out.println("broker publish :" + m.getPayload());
			lock.writeLock().lock();
		try {
			this.runTask(publishMessageURI, (ignore) -> { 	// ignore : @Type ComponentI 
		        try {
		        	sendpublished(m, topic);
		        } catch (Exception e) {	
		            e.printStackTrace();
		        }
		    });
		}finally {
			lock.writeLock().unlock();
		}	
	}
	
	public void publish(MessageI m, String []topics)throws Exception {
		for (String string : topics) {
			publish(m, string);
		}
	}
	public void publish(MessageI[] ms, String topic)throws Exception {
		for (MessageI messageI : ms) {
			publish(messageI, topic);
		}
	}
	public void publish(MessageI[] m, String []topics)throws Exception {
		for (String string : topics) {
			publish(m, string);
		}
	}
	/*ManagementCI*/
	public void createTopic(String topic) throws Exception{
		if(!this.abonnements.containsKey(topic)) {
			this.allTopics.add(topic);
			this.abonnements.put(topic, new ArrayList<Couple>());
			logMessage("[createTopic] " + topic);
		}
		else {
			logMessage("[createTopic] " + topic + " already exists");
		}
	}
	
	public void createTopics(String[] topics)throws Exception{
		for(String t : topics) {
			createTopic(t);
		}
	}
	
	public void destroyTopic(String topic)throws Exception{
		if (this.abonnements.get(topic) != null)
		{
			this.allTopics.remove(topic);
			this.abonnements.get(topic).clear();
			this.abonnements.remove(topic);
			logMessage("[destroyTopic] destroy " + topic);
		}
		else
			logMessage("[destroyTopic] topic nonexistent");
		System.out.println("Broker destroyed topic :" + topic);	
	}
	
	public boolean isTopic(String topic)throws Exception{
		return this.abonnements.containsKey(topic);
	}
	
	//TODO SubscriptionImplementation 
	public void subscribe(String topic, String inboundPortUri) throws Exception{
		subscribe(topic, null, inboundPortUri);
	}

	public void subscribe(String[] topics, String inboundPortUri)throws Exception{
		for (String topic : topics) {
			subscribe(topic, null, inboundPortUri);
		}
	}
	
	public void subscribe(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception{
		ArrayList<Couple> subsriber;
		logMessage("[subscribe] " + inboundPortUri + " to " + topic + "");
		
		if(!subsobp.containsKey(inboundPortUri))
		{
			nbSubscriber++;
			String uriSub = inboundPortUri + nbSubscriber;
			ReceptionOutboundPort ropTmp =  new ReceptionOutboundPort(uriSub,this);
			ropTmp.publishPort();
			subsobp.put(inboundPortUri, ropTmp);			
			this.doPortConnection(uriSub, inboundPortUri, ReceptionConnector.class.getCanonicalName());
			System.out.println("[Broker:subscribe] " + inboundPortUri + " doport connection done");
		}
		if(!abonnements.containsKey(topic))
		{
			System.out.println("Ce topic n'existe pas !!");
			createTopic(topic);
		}
		subsriber = abonnements.get(topic);		
		 
		for (Couple couple : subsriber) {
			if (couple.getUri().equals(inboundPortUri))
			{
				logMessage("[subscribe] " + inboundPortUri + "est deja aboné au topic " + topic );
				System.out.println("Vous etes deja abonné");		
				return;
			}
		}
		subsriber.add(new Couple(inboundPortUri, newFilter));
		logMessage("[subscribe] " + inboundPortUri + " to " + topic + " done");
		
		
	}
	public void modifyFilter(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception{
		ArrayList<Couple> subTopic;
		
		subTopic = abonnements.get(topic);
		if (subTopic == null)
		{
			logMessage("[modifyFilter] personne n'est abonné a ce topic");
			return;
		}
		for (Couple couple : subTopic) {
			if (couple.getUri().equals(inboundPortUri))
			{
				couple.setFiltre(newFilter);
				logMessage("[modifyFilter] filtre a bien ete modifier");
				return;
			}
		}
			logMessage("[modifyFilter] " + inboundPortUri + " n'est abonné a ce topic");
			
	}
	
	public void unsubscribe(String topic,String inboundPortUri)throws Exception{
		ArrayList<Couple> subTopic;

		
		subTopic = abonnements.get(topic);
		if (subTopic == null)
		{
			logMessage("[unsubscribe] Inutile car personne n'est abonné a ce topic");
			return;
		}
		for (int i = 0; i < subTopic.size(); i++) {
			if (subTopic.get(i).getUri().equals(inboundPortUri))
			{
				subTopic.remove(i);
				logMessage("[unsubscribe] " + inboundPortUri + " a bien ete desaboner au topic " + topic);
				return;
			}
		}
		logMessage("[unsubscribe] Inutile car " + inboundPortUri + " n'est abonné a ce topic");
	}
	
	public String[] getTopics()throws Exception{
		return (String[])allTopics.toArray(new String[allTopics.size()]);
		}
	
	
	@Override
	public void				finalise() throws Exception
	{
		this.logMessage("finalising broker component.") ;
		 super.finalise();
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		return this.bpublicationPlugin.getPublicationInboundPort().getPortURI();
	}

}
	



