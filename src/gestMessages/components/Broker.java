package gestMessages.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.InvariantException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import gestMessages.interfaces.ManagementCI;
import gestMessages.interfaces.PublicationCI;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.ports.ManagementInboundPort;
import gestMessages.ports.PublicationInboundPort;
import gestMessages.ports.ReceptionOutboundPort;
import messages.Message;
import messages.MessageFilterI;
import messages.MessageI;
import messages.Properties;

@RequiredInterfaces(required = {ReceptionCI.class})
@OfferedInterfaces(offered = {PublicationCI.class,ManagementCI.class})
public class Broker extends AbstractComponent implements ManagementCI,PublicationCI {

	protected String		uriPrefix ;
	protected ReceptionOutboundPort rop;
	protected ArrayList<String> topics;
	protected Map<String,ArrayList<MessageI>> messages;
	protected Map<String,ArrayList<String>> abonnement;
	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		//System.out.println("bien parti");
		// TODO Auto-generated constructor stub
	}

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
		/*ajout seance TME4*/
		messages= new HashMap<>();
		abonnement= new HashMap<>();
		topics= new ArrayList<>();

	}
	private void testConstructeur() {
		assert	this.uriPrefix != null :
			new PreconditionException("uri can't be null!") ;
		
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
		System.out.println("Broker topic created =  " + topic);
		if (topics == null)
			System.out.println("Je vais crash!!");
		this.topics.add(topic);
	}
	public void createTopics(String[] topics)throws Exception{
		for(String t : topics) {
			System.out.println("Broker topic created =  " + t);
			this.topics.add(t);
		}
	}
	public void destroyTopic(String topic)throws Exception{
		this.topics.remove(topic);
		System.out.println("Broker topic destroyed =  " + topic);
		
	}
	public boolean isTopic(String topic)throws Exception{
		return this.topics.contains(topic);
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
		
		
	}
	
	
	public void			start() throws ComponentStartException
	{

		this.logMessage("starting broker component.") ;
		super.start();
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {					
						try {
							System.out.println("Start Broker!");
							Thread.sleep(1000000);
							System.out.println("end Broker!");
							//((Broker)this.getTaskOwner()).testAccept();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;
	}
	
	
	/*
	@Override
	public void			finalise() throws Exception
	{
		System.out.println("finalise Broker");
		this.logMessage("stopping broker component.") ;
		this.printExecutionLogOnFile("broker") ;
		this.rop.doDisconnection();
		super.finalise();
	}
*/
	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		System.out.println("Shutdown Broker");
		try {
			this.rop.unpublishPort();
			this.rop.destroyPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		try {
			PortI[] p = this.findPortsFromInterface(PublicationCI.class) ;
			p[0].unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		*/
		
		super.shutdown();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdownNow()
	 */
	@Override
	public void			shutdownNow() throws ComponentShutdownException
	{
		try {
			PortI[] p = this.findPortsFromInterface(PublicationCI.class) ;
			p[0].unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdownNow();
	}

	/**
	 * check the invariant of the class on an instance.
	 *
	 * @param c	the component to be tested.
	 */
	protected static void	checkInvariant(Broker c)
	{
		assert	c.uriPrefix != null :
					new InvariantException("The URI prefix is null!") ;
		assert	c.isOfferedInterface(PublicationCI.class) :
					new InvariantException("The URI component should "
							+ "offer the interface URIProviderI!") ;
	}


}
