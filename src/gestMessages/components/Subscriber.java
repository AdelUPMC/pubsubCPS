package gestMessages.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.InvariantException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.ports.ReceptionInboundPort;
import messages.MessageI;

@OfferedInterfaces(offered = {ReceptionCI.class})
public class Subscriber extends AbstractComponent {

	protected String		uriPrefix ;
	protected Subscriber(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}
	
	protected Subscriber(String uri,String reflectionInboundPortURI, int nbThreads, int nbSchedulableThreads)
			throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
		assert	uri != null :
			new PreconditionException("uri can't be null!") ;
		uriPrefix=uri;
		System.out.println(reflectionInboundPortURI);
		ReceptionInboundPort p = new ReceptionInboundPort(reflectionInboundPortURI, this) ;
		System.out.println("constructeur Subscriber");
		// publish the port
		p.localPublishPort(); ;

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
	
	public void			start() throws ComponentStartException
	{
		System.out.println("Subscriber Start !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		this.logMessage("starting subscriber component.") ;
		super.start();
	}
	@Override
	public void			finalise() throws Exception
	{
		this.logMessage("stopping subscriber component.") ;
		this.printExecutionLogOnFile("subscriber") ;
		super.finalise();
	}
	
	
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		System.out.println("Shutdown Sub");
		try {
			PortI[] p = this.findPortsFromInterface(ReceptionCI.class) ;
			p[0].unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
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
