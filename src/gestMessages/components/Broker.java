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
public class Broker extends AbstractComponent implements ManagementCI,PublicationCI {

	/**
	 * <String,ArrayList<MessageI>> messages: 
	 * 	-String: topic
	 * 	-ArrayList<MessageI>: liste des MessageI associés
	 * 
	 * Map<String,ArrayList<String>> abonnement: 
	 * 	-String: topic
	 * 	-ArrayList<String>: liste des uris des abonnés au topic
	 *
	 * Map<String,MessageFilterI> filters: 
	 * 	-String: inbound port uri
	 * 	-MessageFilterI: MessageFilterI associé	
	 * 
	 * Map<String,ReceptionOutboundPort> subsport: 
	 * 	-String: uri du subscriber
	 * 	-ReceptionOutboundPort: outboundport du subscriber pour la réception				
	 * 
			
	 */
	protected String uriPrefix;
	//protected String managementInboundPortURI;
	protected final String acceptExecutor="handler-accept";
	
	private Map<String,ArrayList<MessageI>> messages;
	private Map<String,ArrayList<String>> abonnement;
	private Map<String, MessageFilterI> filters;
	private Map<String,ReceptionOutboundPort> subsobp;
	protected ReceptionOutboundPort rop;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	
	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}
	/*
	protected Broker(String uri,int nbThreads, int nbSchedulableThreads,String PublicationInboundPortURI,String ManagementInboundPortURI,String ReceptionOutboundPortUri)
			throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		System.out.println("constructeur broker");
		
		// Outbound port pour ReceptionCI
		this.rop= new ReceptionOutboundPort(ReceptionOutboundPortUri,this);
		this.rop.localPublishPort();
		
		//Inbound port pour PublicationCI
		PublicationInboundPort p = new PublicationInboundPort(PublicationInboundPortURI, this) ;
		p.localPublishPort();
		ManagementInboundPort pm= new ManagementInboundPort(ManagementInboundPortURI,this);
		pm.localPublishPort();
		
		uriPrefix=uri;

		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		testConstructeur();
		//ajout seance TME4
		messages= new HashMap<>();
		abonnement= new HashMap<>();
		topics= new ArrayList<>();

	}*/
	protected Broker(String uri,String PublicationInboundPortURI,String ManagementInboundPortURI)throws Exception {
		
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

	}
	@Override
	public void execute() throws Exception{
		this.createNewExecutorService(acceptExecutor,2,true);
		handleRequestAsync(acceptExecutor,new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((Broker)this.getServiceOwner()).routineBroker();
				return null;
			}
		});
	}
	
	private void sendMessages(String topic) throws Exception
	{
		ArrayList<MessageI> lm;
		ArrayList<String> subsribers;
		if(messages.containsKey(topic)) {
			lm = messages.get(topic);
			if (!abonnement.containsKey(topic) ||abonnement.get(topic).isEmpty() )
			{
				System.out.println("personne d'abonner");
				return;
			}
			subsribers = abonnement.get(topic);
			for (String   subscriber: subsribers) {
				while (!lm.isEmpty())
				{
					rop.acceptMessage(lm.get(0));
					lm.remove(0);
				}
			}
		}
	}
	/*PublicationCI*/
	public void publish(MessageI m, String topic)throws Exception {
		ArrayList<MessageI> lm;
		if(!messages.containsKey(topic)) {
			lm=new ArrayList<MessageI>();
			lm.add(m);
			messages.put(topic,lm);
		}else
		{
			lm = messages.get(topic);
			lm.add(m);
		}
		System.out.println("Broker topic =  " + topic);
		sendMessages(topic);
		
	}
	
	public void publish(MessageI m, String []topics)throws Exception {
		
	}
	public void publish(MessageI[] ms, String topic)throws Exception {
	}
	public void publish(MessageI[] m, String []topics)throws Exception {
	}
	/*ManagementCI*/
	public void createTopic(String topic) throws Exception{
		
		if(this.abonnement.get(topic)==null) {
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
	
	public void routineBroker() throws Exception {
        HashMap<String,Set<MessageI>> sended;
        boolean abonne=false;
        while(true){
            lock.readLock().lock();
            try{
                sended= new HashMap<>();
                String topic;
                for(Map.Entry<String, ArrayList<MessageI>> entry : messages.entrySet()){
                    topic = entry.getKey();
                    if(!abonnement.containsKey(topic)){
                        abonne=false;
                        break;
                    }else {
                        abonne=true;
                    }
                    if(!sended.containsKey(topic)) sended.put(topic,new HashSet<>());
                    for(MessageI msg : entry.getValue()){
                        //Thread.sleep(50);
                        for(String uriSub : abonnement.get(topic)){
                            MessageFilterI filter ;
                            if((filter=filters.get(uriSub))!=null){
                                if(filter.filter(msg)){
                                    subsobp.get(uriSub).acceptMessage(msg);
                                }
                            }else{
                                subsobp.get(uriSub).acceptMessage(msg);
                            }
                            sended.get(topic).add(msg);
                        }
                    }
                }
            }finally {
                lock.readLock().unlock();
            }
            if(!abonne)continue;
            lock.writeLock().lock();
            try{
                for(Map.Entry<String, Set<MessageI>> entry : sended.entrySet()){
                    messages.get(entry.getKey()).removeAll(entry.getValue());
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
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
	



