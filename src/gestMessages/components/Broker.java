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
import gestMessages.interfaces.ManagementImplementationI;
import gestMessages.interfaces.PublicationsImplementationI;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.interfaces.SubscriptionImplementationI;
import gestMessages.plugins.BrokerManagementPlugin;
import gestMessages.plugins.BrokerPublicationPlugin;
import gestMessages.ports.ReceptionOutboundPort;
import messages.MessageFilterI;
import messages.MessageI;

public class Broker extends AbstractComponent implements ManagementImplementationI,PublicationsImplementationI, SubscriptionImplementationI{

	/**
	 *
	 * Map<String,ArrayList<String, MessageFilterI>> abonnement: 
	 * 	-String: topic
	 * 	-ArrayList<String, MessageFilterI>: liste des uris des abonnes au topic et les Filtre associe
	 * 
	 * Map<String,ReceptionOutboundPort> subsobp: 
	 * 	-String: uri de l'inbound port du  subscriber
	 * 	-ReceptionOutboundPort: outboundport  du subscriber pour la reception	
	 * 
	 * Map<ReceptionOutboundPort,ArrayList<MessageI>> published: 
	 * 	-ReceptionOutboundPort: ReceptionOutboundPort du subscriber
	 * 	-MessageI: message qui doit a été publié et qui doit être transmit au subscriber			
	 *
	 * Map<String, ArrayList<MessageI>> stack: Liste de paquets de messages 
	 * 	-String: Uri de l'inbound port du  subscriber
	 * 	-ArrayList<MessageI>: message qui doit a été publié et qui doit être transmit au subscriber			
	 *
	 * Map<String, boolean> hasToUpdate: 
	 * 	-String: Uri de l'inbound port du  subscriber
	 * 	-boolean: Si il n'a pas été mise a jour lors du dernier cycle			
	 *
	 * ArrayList<String> toSend: liste des uri des abonne qui doivent recevoir leur message 
	 * 	
			
	 */
	protected String uriPrefix;
	protected final String acceptURI="handler-accept";
	protected final String publishMessageURI = "message-URI";
	protected final String publishPaquetURI = "paquet-URI";
	private int 							nbSubscriber;
	private final int 						BUFFER_MESSAGE = 10;
	private final long 						BUFFER_TIME = 1000;
	private final boolean					MODE_DEBUG = false;

	private Map<String,ArrayList<Couple>> abonnements;
	private Map<String,ReceptionOutboundPort> subsobp;
	private ArrayList<String> allTopics;
	
	private Map<String, ArrayList<MessageI>> stack;
	private Map<String, Boolean> hasToUpdate;
	private ArrayList<String> toSend;
	
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final ReadWriteLock stackLock = new ReentrantReadWriteLock();
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
		this.abonnements = new HashMap<>();
		this.stack = new HashMap<>();
		this.subsobp = new HashMap<>();	
		this.hasToUpdate = new HashMap<>();	
		this.allTopics = new ArrayList<>();
		this.toSend = new ArrayList<>();
		
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
	
	/**
	 * execute() 
	 * 
	 * creer et envoi 2 thread appelant la fonction FinalSend et PeriodSend
	 */
	@Override
	public void execute() throws Exception{
		super.execute();
		this.createNewExecutorService(publishMessageURI, 5,true);
		this.createNewExecutorService(publishPaquetURI, 2,true);
		this.runTask(publishPaquetURI, (ignore) -> { 	// ignore : @Type ComponentI 
	        try {
	        	FinalSend();
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
		this.runTask(publishPaquetURI, (ignore) -> { 	// ignore : @Type ComponentI 
	        try {
	        	PeriodSend();
	        } catch (Exception e) {	
	            e.printStackTrace();
	        }
	    });
		logMessage("[Execute] done");
	}
	/**
	 * Des que la liste ToSend a des elements
	 * envois tous les paquet de message au subsriber de toSend
	 * @throws Exception
	 */
	private void FinalSend() throws Exception
	{
		while(true)
		{
			if (toSend.isEmpty())
				synchronized (toSend) {
					toSend.wait();					
				}
			if (MODE_DEBUG) System.out.println("[Broker:FinalSend] je me reveille ");
			stackLock.writeLock().lock();
			while(!toSend.isEmpty())
			{
				String sub = toSend.remove(0);
				ReceptionOutboundPort ri = subsobp.get(sub);
				if (stack.get(sub).size() == 1)
					ri.acceptMessage(stack.get(sub).get(0));
				else
					ri.acceptMessages(stack.get(sub));
				stack.put(sub, new ArrayList<MessageI>());
				hasToUpdate.put(sub, false);
			}
			stackLock.writeLock().unlock();
		}
	}
	/**
	 * actualise en boucle tout les cycle de temp et ajoute les 
	 * paquet des message non nul et dont l'abonne n'a pas deja recu ces message lors du precedent cycle
	 * @throws Exception
	 */
	private void PeriodSend() throws Exception
	{
		boolean hasTonotify;
		while (true)
		{
			synchronized (hasToUpdate) {
				hasToUpdate.wait(BUFFER_TIME);
			}
			hasTonotify = false;
			stackLock.readLock().lock();
			for(Map.Entry<String, Boolean> entry: hasToUpdate.entrySet())
			{
				if (entry.getValue() && stack.get(entry.getKey()) != null && !stack.get(entry.getKey()).isEmpty())
				{
					hasTonotify = true;
					toSend.add(entry.getKey());
				}
				else
				{
					entry.setValue(true);
				}
			}
			stackLock.readLock().unlock();
			if (hasTonotify)
				synchronized (toSend) {
					if (MODE_DEBUG) System.out.println("Ca marche");
					toSend.notify();
				}
		}
	}
	
	/**
	 *Utilise jusqu'a 5 threads en parallele
	 * Soccupe de filtrer les message et les ajoute sur la liste stack des paquets 
	 *  
	 */
	public  void  sendpublished(MessageI m,String topic)throws Exception {		
		ArrayList<Couple> uris = abonnements.get(topic);		
		ArrayList<MessageI> messages;
		lock.readLock().lock();
		
		if (uris != null)
		{
			if (MODE_DEBUG) System.out.println("[Broker:sendpublished] il y'a " + uris.size() + " aboonnes");
			for(Couple abonne_uri: uris)
			{
				ReceptionOutboundPort ri = subsobp.get(abonne_uri.getUri());
				if (ri != null)
				{
					logMessage("[sendpublished] try to send \"" +m.getPayload()  + "\" to " + abonne_uri.getUri());		
					if (!abonne_uri.hasFiltre() || (abonne_uri.hasFiltre() && abonne_uri.getFiltre().filter(m)))
					{
						//ri.acceptMessage(m);
						stackLock.writeLock().lock();
						messages = stack.get(abonne_uri.getUri());
						if (messages == null)
						{
							messages = new ArrayList<>();
							stack.put(abonne_uri.getUri(), messages);
						}
						messages.add(m);
						stackLock.writeLock().unlock();
						if (messages.size() > BUFFER_MESSAGE)
						{
							toSend.add(abonne_uri.getUri());
							synchronized (toSend) {
								if (MODE_DEBUG) System.out.println("============Notify");
								toSend.notify();							
							}
						}		
					}
				}
				else
				{
					if (MODE_DEBUG) System.out.println("[sendPublished] PROBLEME un abooner n'est pas connecter");
				}
			}
		}
		if (uris == null || uris.isEmpty())
			{
			logMessage("[sendpulished] aucun abonner au topic :" + topic);
			if (MODE_DEBUG) System.out.println("[sendpulished] aucun abonner au topic :" + topic);
			}
		else
			System.out.println("[Broker:sendpublished] fin " + uris.size() + " for "+ topic);
		lock.readLock().unlock();
	}
	
	/*PublicationCI*/
	public void publish(MessageI m, String topic)throws Exception {
			logMessage("[publish] try to publish : " + m.getPayload());
			System.out.println("[broker:publish] publish:" + m.getPayload());
			//lock.writeLock().lock();
		try {
			this.runTask(publishMessageURI, (ignore) -> { 	// ignore : @Type ComponentI 
		        try {
		        	
		        	sendpublished(m, topic);
		        } catch (Exception e) {	
		            e.printStackTrace();
		        }
		    });
		}finally {
		//	lock.writeLock().unlock();
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
	
	/**ManagementCI
	 *  abonnements ecriture
	 *  allTopic ecriture
	 */
	public  void createTopic(String topic) throws Exception{
		lock.writeLock().lock();
		if(!this.abonnements.containsKey(topic)) {
			this.allTopics.add(topic);
			this.abonnements.put(topic, new ArrayList<Couple>());
			logMessage("[createTopic] " + topic);
		}
		else {
			logMessage("[createTopic] " + topic + " already exists");
		}
		lock.writeLock().unlock();
	}
	
	public void createTopics(String[] topics)throws Exception{
		for(String t : topics) {
			createTopic(t);
		}
	}
	
	/**
	 *  abonnements ecriture
	 * 	allTopic ecriture
	 */
	public  void destroyTopic(String topic)throws Exception{
		lock.writeLock().lock();
		if (this.abonnements.get(topic) != null)
		{
			this.allTopics.remove(topic);
			this.abonnements.get(topic).clear();
			this.abonnements.remove(topic);
			logMessage("[destroyTopic] Broker destroyed topic " + topic);
			System.out.println("[Broker:destroyTopic] Broker destroyed topic :" + topic);	
		}
		else
			logMessage("[destroyTopic] topic nonexistent");
		lock.writeLock().unlock();
	}
	
	/**
	 *  abonnements lecture(non-Javadoc)
	 * 
	 */
	public  boolean isTopic(String topic)throws Exception{
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
	/**
	 * abonement ecriture
	 * subobp ecriture
	 * 
	 */
	public  void subscribe(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception{
		ArrayList<Couple> subsriber;
		logMessage("[subscribe] " + inboundPortUri + " to " + topic + "");
		
		if(!subsobp.containsKey(inboundPortUri))
		{
			synchronized (subsobp)
			{				
				nbSubscriber++;
				String uriSub = inboundPortUri + nbSubscriber;
				ReceptionOutboundPort ropTmp =  new ReceptionOutboundPort(uriSub,this);
				ropTmp.publishPort();
				subsobp.put(inboundPortUri, ropTmp);
				hasToUpdate.put(inboundPortUri, false);
				this.doPortConnection(uriSub, inboundPortUri, ReceptionConnector.class.getCanonicalName());
				System.out.println("[Broker:subscribe] " + inboundPortUri + " doport connection done");
			}
		}
		
		if(!abonnements.containsKey(topic))
		{
			System.out.println("[Broker:subscribe]Ce topic n'existe pas: " + topic);
			createTopic(topic);
		}
		subsriber = abonnements.get(topic);		
		lock.readLock().lock();
		for (Couple couple : subsriber) {
			if (couple.getUri().equals(inboundPortUri))
			{
				logMessage("[subscribe] " + inboundPortUri + "est deja aboné au topic " + topic );
				if (MODE_DEBUG) System.out.println("[Broker:subscribe]Vous etes deja abonné");		
				lock.readLock().unlock();
				return;
			}
		}
		lock.readLock().unlock();
		lock.writeLock().lock(); 
		subsriber.add(new Couple(inboundPortUri, newFilter));
		logMessage("[subscribe] " + inboundPortUri + " to " + topic + " done");
		lock.writeLock().unlock();
		
	}
	
	
	/**
	 * 
	 */
	public  void modifyFilter(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception{
		ArrayList<Couple> subTopic;
		lock.readLock().lock();
		subTopic = abonnements.get(topic);
		if (subTopic == null)
		{
			logMessage("[Broker:modifyFilter] personne n'est abonné a ce topic " +  topic);
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
		lock.readLock().unlock();
			logMessage("[modifyFilter] " + inboundPortUri + " n'est abonné a ce topic: " + topic);
			
	}
	
	/**
	 * abonnement ecriture
	 */
	public  void unsubscribe(String topic,String inboundPortUri)throws Exception{
		ArrayList<Couple> subTopic;

		
		subTopic = abonnements.get(topic);
		if (subTopic == null)
		{
			logMessage("[unsubscribe] Inutile car personne n'est abonné a ce topic");
			return;
		}
		lock.writeLock().lock();
		for (int i = 0; i < subTopic.size(); i++) {
			if (subTopic.get(i).getUri().equals(inboundPortUri))
			{
				subTopic.remove(i);
				logMessage("[unsubscribe] " + inboundPortUri + " a bien ete desaboner au topic " + topic);
				return;
			}
		}
		lock.writeLock().unlock();
		logMessage("[unsubscribe] Inutile car " + inboundPortUri + " n'est abonné a ce topic");
	}
	
	/**
	 *  lectureAllTopic
	 */
	public  String[] getTopics()throws Exception{
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