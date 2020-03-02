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
import gestMessages.interfaces.ManagementCI;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.ports.ManagementOutboundPort;
import gestMessages.ports.ReceptionInboundPort;
import messages.MessageI;
@RequiredInterfaces(required = {ManagementCI.class})
@OfferedInterfaces(offered = {ReceptionCI.class})
public class Subscriber extends AbstractComponent {

	protected String		uriPrefix ;
	protected ManagementOutboundPort	portm;
	protected Subscriber(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}
	
	protected Subscriber(String uri,int nbThreads, int nbSchedulableThreads,String receptionInboundPortURI,String managementoutboundPortURI)
			throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
		assert	uri != null :
			new PreconditionException("uri can't be null!") ;
		uriPrefix=uri;
		System.out.println(receptionInboundPortURI);
		ReceptionInboundPort p = new ReceptionInboundPort(receptionInboundPortURI, this) ;
		p.localPublishPort(); ;
		
		this.portm= new ManagementOutboundPort(managementoutboundPortURI,this);
		this.portm.localPublishPort();
		//System.out.println("constructeur Subscriber");
		// publish the port
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		this.tracer.setTitle("Subscriber") ;
		this.tracer.setRelativePosition(1, 1) ;
	}
	
	public void acceptMessageService(MessageI m) throws Exception {
		System.out.println("accept message : URI="+m.getURI());
		if (m.getPayload() instanceof String)
			System.out.println("contenu du message: "+((String)m.getPayload()));
	}
	public void acceptMessagesService(MessageI[] ms) throws Exception {
		System.out.println("accept messages :");
		for(int i=0;i<ms.length;i++) {
			System.out.println("message "+i+" : uri="+ms[i].getURI());
		}
	}
	
	public void testManagement()
	{
		try {	
			System.out.println("Je vais vraiment crash!!!");
			this.portm.createTopic("CPS subscriber");
			this.logMessage("Topic created by a publisher");
		} catch (Exception e) {
			//System.out.println("Presque !!");
			e.printStackTrace();
				}
		
	}
	
	public void			start() throws ComponentStartException
	{
		System.out.println("Subscriber Start !!!!!!!!!!!!!!!");
		super.start();
		this.logMessage("starting subscriber component.") ;
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							System.out.println("Start subscriber!");

							//((Subscriber)this.getTaskOwner()).testManagement();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}
				},
				1000, TimeUnit.MILLISECONDS);

	}
	
	@Override
	public void			finalise() throws Exception
	{
		this.logMessage("stopping subscriber component.") ;
		this.printExecutionLogOnFile("subscriber") ;
		this.portm.doDisconnection();
		//this.portm.unpublishPort();
		super.finalise();
	}
	
	
	
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		System.out.println("Shutdown Sub");
		
		try {
			this.portm.unpublishPort();
			this.portm.destroyPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	/*	try {
			PortI[] p = this.findPortsFromInterface(ReceptionCI.class) ;
			p[0].unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
	
		*/
	}
	
	protected static void	checkInvariant(Subscriber s)
	{
		assert	s.uriPrefix != null :
					new InvariantException("The URI prefix is null!") ;
		assert	s.isOfferedInterface(ReceptionCI.class) :
					new InvariantException("The URI component should "
							+ "offer the interface URIProviderI!") ;
	}
	

}
