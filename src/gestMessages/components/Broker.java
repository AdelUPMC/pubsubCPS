package gestMessages.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import gestMessages.connectors.ManagementConnector;
import gestMessages.connectors.ReceptionConnector;
import gestMessages.interfaces.ManagementCI;
import gestMessages.interfaces.PublicationCI;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.ports.ManagementInboundPort;
import gestMessages.ports.PublicationInboundPort;
import gestMessages.ports.ReceptionOutboundPort;
import messages.MessageFilterI;
import messages.MessageI;


@RequiredInterfaces(required = {ReceptionCI.class})
@OfferedInterfaces(offered = {PublicationCI.class,ManagementCI.class})
public class Broker extends AbstractComponent implements ManagementCI,PublicationCI{

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
	 * 	-String: uri du subscriber
	 * 	-ReceptionOutboundPort: outboundport  du subscriber pour la reception	
	 * 
	 * Map<ReceptionOutboundPort,ArrayList<MessageI>> published: 
	 * 	-ReceptionOutboundPort: ReceptionOutboundPort du subscriber
	 * 	-MessageI: message qui doit a été publié et qui doit être transmit au subscriber			
	 * 
			
	 */
	protected String uriPrefix;
	//protected String managementInboundPortURI;
	protected final String acceptURI="handler-accept";
	protected final String publishMessageURI = "message-URI";
	private int 							nbSubscriber;
	
	private Map<String,ArrayList<MessageI>> messages;
	private Map<String,ArrayList<String>> abonnement;
	private Map<String, MessageFilterI> filters;
	private Map<String,ReceptionOutboundPort> subsobp;
	private Map<ReceptionOutboundPort,ArrayList<MessageI>> published;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	
	
	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}
	
	protected Broker(String uri,String PublicationInboundPortURI, String ManagementInboundPortURI)throws Exception {
		
		super(uri, 0, 1) ;
		assert	uri != null :
			new PreconditionException("error uri null") ;
		assert	PublicationInboundPortURI != null :
			new PreconditionException("error PublicationInboundPortURI null") ;
		assert	ManagementInboundPortURI != null :
			new PreconditionException("error ManagementInboundPortURI null") ;
		this.uriPrefix=uri;
		this.messages= new HashMap<>();
		this.abonnement= new HashMap<>();
		this.filters=new HashMap<>();
		this.subsobp= new HashMap<>();
		this.published= new HashMap<>();
		
		//Inbound port pour PublicationCI
		PublicationInboundPort pip = new PublicationInboundPort(PublicationInboundPortURI, this) ;
		pip.publishPort();
		ManagementInboundPort mip= new ManagementInboundPort(ManagementInboundPortURI,this);
		mip.publishPort();
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;
		Broker.checkInvariant(this);
		
		/*verifications*/
		assert	this.isPortExisting(PublicationInboundPortURI) :
					new PostconditionException("port "+PublicationInboundPortURI+" doesn't exist") ;
		assert	this.isPortExisting(ManagementInboundPortURI) :
			new PostconditionException("port "+ManagementInboundPortURI+" doesn't exist") ;
		
		assert	this.findPortFromURI(PublicationInboundPortURI).
					getImplementedInterface().equals(PublicationCI.class) :
					new PostconditionException("PublicationInboundPort has to implement PublicationCI") ;
		assert	this.findPortFromURI(PublicationInboundPortURI).isPublished() :
					new PostconditionException("PublicationInboundPortURI is not published") ;

		assert	this.findPortFromURI(ManagementInboundPortURI).
					getImplementedInterface().equals(ManagementCI.class) :
					new PostconditionException("ManagementInboundPortURI has to implement ManagementCI") ;
		assert	this.findPortFromURI(ManagementInboundPortURI).isPublished() :
					new PostconditionException("ManagementInboundPortURI is not published") ;
		
	}
	

	@Override
	public void	start() throws ComponentStartException{
		super.start();
	}
	
	@Override
	public void execute() throws Exception{
		super.execute();
		this.createNewExecutorService(publishMessageURI, 5,true);
		/*this.createNewExecutorService(acceptURI,2,true);
		handleRequestAsync(acceptURI,new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).observerPublished();
				return null;
			}
		});*/
	}
	
	public void sendpublished(MessageI m,String topic)throws Exception {
		ArrayList<String> uris=abonnement.get(topic);
		for(String abonne_uri: uris) {
			ReceptionOutboundPort ri=subsobp.get(abonne_uri);
		/*	if(!published.containsKey(ri)) {
				ArrayList<MessageI> pubmessages= new ArrayList<>();
				pubmessages.add(m);
				published.put(ri, pubmessages);
			}
			else {
				ArrayList<MessageI> pubmessages= published.get(ri);
				pubmessages.add(m);
				published.put(ri, pubmessages);
			}
		}
		*/
		if (ri != null)
			{
				System.out.println("try to send  " + m);
				ri.acceptMessage(m);
			}
		else
		{
			System.out.println("Il n'y a aucun abonner");
		}
	}
	}
	public void observerPublished() throws Exception {
		while(true) {
			//Thread.sleep(150L);
			if (!published.isEmpty()) {
				for(ReceptionOutboundPort rop: published.keySet()) {
					ArrayList<MessageI> l = published.get(rop);
					for(MessageI m: l) {
						rop.acceptMessage(m);
						l.remove(m);
					}
					//published.remove(rop);
				}
			}
		}
	}
	/*PublicationCI*/
	public void publish(MessageI m, String topic)throws Exception {
			lock.writeLock().lock();
		try {
			if(!messages.containsKey(topic)) 
				createTopic(topic);
		//	lm = messages.get(topic);
		//	lm.add(m);
			this.runTask(publishMessageURI, (ignore) -> { 	// ignore : @Type ComponentI 
		        try {
		        	System.out.println("debut runTask publish");
		        	sendpublished(m, topic);
		        } catch (Exception e) {
		        	
		            e.printStackTrace();
		        }
		    });
			//System.out.println("Broker topic =  " + topic);
		}finally {
			sendpublished(m,topic);
			lock.writeLock().unlock();
		}
			//sendMessages(topic);}// à débattre s'il faut le faire en exclusion mutuelle ou non
		
		
		
	}
	
	public void publish(MessageI m, String []topics)throws Exception {
		
	}
	public void publish(MessageI[] ms, String topic)throws Exception {
	}
	public void publish(MessageI[] m, String []topics)throws Exception {
	}
	/*ManagementCI*/
	public void createTopic(String topic) throws Exception{

		if(!this.abonnement.containsKey(topic)) {
			this.abonnement.put(topic, new ArrayList<String>());
			System.out.println("Broker created topic :" + topic);
		}
		else {
			System.out.println("topic"+topic+" already exists");
		}
	}
	public void createTopics(String[] topics)throws Exception{
		for(String t : topics) {
			createTopic(t);
		}
	}
	public void destroyTopic(String topic)throws Exception{
		this.abonnement.remove(topic);
		System.out.println("Broker destroyed topic :" + topic);
		
	}
	public boolean isTopic(String topic)throws Exception{
		return this.abonnement.containsKey(topic);
	}
	//TODO SubscriptionImplementation 
	public void subscribe(String topic, String inboundPortUri) throws Exception{
		ArrayList<String> subsriber;
		
		if(!subsobp.containsKey(inboundPortUri)) {
			nbSubscriber++;
			String uriSub = inboundPortUri + nbSubscriber;
			ReceptionOutboundPort ropTmp =  new ReceptionOutboundPort(uriSub,this);
			subsobp.put(inboundPortUri, ropTmp);
			this.doPortConnection(uriSub, inboundPortUri, ReceptionConnector.class.getCanonicalName());
		}
		if(!abonnement.containsKey(topic))
		{
			System.out.println("Ce topic n'existe pas !!!!!!!!!!!!");
			// on peut le creer !
			return ;
		}
	
		subsriber = abonnement.get(topic);		
		if (subsriber.contains(inboundPortUri))
			System.out.println("Vous etes deja abonné");
		else
			subsriber.add(inboundPortUri);
		
	}

	public void subscribe(String[] topics, String inboundPortUri)throws Exception{
		
	}
	public void subscribe(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception{
		
	}
	public void modifyFilter(String topic,MessageFilterI newFilter, String inboundPortUri)throws Exception{
		
	}
	public void unsubscribe(String topic,String inboundPortUri)throws Exception{
		
	}
	public String[] getTopics()throws Exception{
		return null;
	}
	@Override
	public void				finalise() throws Exception
	{
		
		for (String uriReception: subsobp.keySet()) {
			this.doPortDisconnection(uriReception);			
		}
	}

	@Override
	public String getPublicationPortURI() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	public void testAccept()
	{
		try {
			Properties p= new Properties();
			p.putProp("auteur", "Andrzej Sapkowski");
			System.out.println("Pre accept message");
			this.rop.acceptMessage(new Message(null,null,p,new String("allez les bleus!")));
			System.out.println("Post accept message");
			this.logMessage("broker  appelle accept message") ;
		} catch (Exception e) {
			//System.out.println("Presque !!");
			e.printStackTrace();
		}
		*/

		
		
}
	



