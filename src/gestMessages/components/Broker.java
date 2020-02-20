package gestMessages.components;

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
import gestMessages.interfaces.PublicationCI;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.ports.PublicationInboundPort;
import gestMessages.ports.ReceptionOutboundPort;
import messages.Message;
import messages.MessageFilterI;
import messages.MessageI;
import messages.Properties;

@RequiredInterfaces(required = {ReceptionCI.class})
@OfferedInterfaces(offered = {PublicationCI.class})
public class Broker extends AbstractComponent {

	protected String		uriPrefix ;
	protected ReceptionOutboundPort rop;
	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		//System.out.println("bien parti");
		// TODO Auto-generated constructor stub
	}

	protected Broker(String uri,String reflectionInboundPortURI, int nbThreads, int nbSchedulableThreads,String ReceptionOutboundPortUri)
			throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		System.out.println("constructeur broker");
		
		// Outbound port pour ReceptionCI
		this.rop= new ReceptionOutboundPort(ReceptionOutboundPortUri,this);
		this.rop.localPublishPort();
		
		//Inbound port pour PublicationCI
		PublicationInboundPort p = new PublicationInboundPort(reflectionInboundPortURI, this) ;
		p.localPublishPort();
		
		//ManagementInboundPort pm = new ManagementInboundPort(reflectionInboundPortURI, this) ;
		//pm.localPublishPort();
		uriPrefix=uri;

		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		testConstructeur();

	}
	private void testConstructeur() {
		assert	this.uriPrefix != null :
			new PreconditionException("uri can't be null!") ;
		
	}
	
	
	public void publishService(MessageI m, String topic)throws Exception {
		System.out.println("Broker topic =  " + topic);
	}
	
	public void publishService(MessageI m, String []topics)throws Exception {
	}
	public void publishService(MessageI[] ms, String topic)throws Exception {
	}
	public void publishService(MessageI[] m, String []topics)throws Exception {
	}
	
	public void testAccept()
	{
		try {
			Properties p= new Properties();
			p.putProp("Pays= France");
			//p.put
			System.out.println("Pre accept message");
			this.rop.acceptMessage(new Message(p,new String("allez les bleus!")));
			System.out.println("Post accept message");
			this.logMessage("broker  appelle accept message") ;
		} catch (Exception e) {
			//System.out.println("Presque !!");
			e.printStackTrace();
		}
		
		
	}
	
	public void			start() throws ComponentStartException
	{
		System.out.println("Broker Start ");
		this.logMessage("starting broker component.") ;
		super.start();
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {

							System.out.println("Reussite !");
							((Broker)this.getTaskOwner()).testAccept();
												 ;
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS) ;
	}
	
	
	
	@Override
	public void			finalise() throws Exception
	{
		System.out.println("finalise Broker");
		this.logMessage("stopping broker component.") ;
		this.printExecutionLogOnFile("broker") ;
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		System.out.println("Shutdown Broker");
		try {
			PortI[] p = this.findPortsFromInterface(PublicationCI.class) ;
			p[0].unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * 
	 * TODO
	 * 
	 */
	
	
	public void subscribe(String topic, String inboundPortUri) {
		System.out.println("broker subscribe , topic= "+topic+"inbound= "+inboundPortUri);
		
	}

	public void subscribe(String[] topics, String inboundPortUri) {
		// TODO Auto-generated method stub
		
	}

	public void subscribe(String topic, MessageFilterI newFilter, String inboundPortUri) {
		// TODO Auto-generated method stub
		
	}

	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) {
		// TODO Auto-generated method stub
		
	}

	public void unsubscribe(String topic, String inboundPortUri) {
		// TODO Auto-generated method stub
		
	}

	public void createTopic(String topic) {
		// TODO Auto-generated method stub
		
	}

	public void createTopics(String[] topics) {
		// TODO Auto-generated method stub
		
	}

	public void destroyTopic(String topic) {
		// TODO Auto-generated method stub
		
	}

	public boolean isTopic(String topic) {
		return false;
		
	}

	public String[] getTopics() {
		// TODO Auto-generated method stub
		return null;
	}



}
