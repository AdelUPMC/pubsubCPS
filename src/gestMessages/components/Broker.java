package gestMessages.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.InvariantException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import gestMessages.interfaces.PublicationCI;
import gestMessages.ports.PublicationInboundPort;
import messages.MessageI;
@OfferedInterfaces(offered = {PublicationCI.class})
public class Broker extends AbstractComponent {

	protected String		uriPrefix ;

	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		System.out.println("bien parti");
		// TODO Auto-generated constructor stub
	}

	protected Broker(String uri,String reflectionInboundPortURI, int nbThreads, int nbSchedulableThreads)
			throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
		assert	uri != null :
			new PreconditionException("uri can't be null!") ;
		uriPrefix=uri;
		System.out.println(reflectionInboundPortURI);
		PublicationInboundPort p = new PublicationInboundPort(reflectionInboundPortURI, this) ;
		System.out.println("constructeur broker");
		// publish the port
		p.localPublishPort(); ;

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
	
	
	public void			start() throws ComponentStartException
	{
		System.out.println("Broker Start ");
		this.logMessage("starting broker component.") ;
		super.start();

		System.out.println("Broker Start End");
	}
	
	@Override
	public void			finalise() throws Exception
	{
		this.logMessage("stopping provider component.") ;
		this.printExecutionLogOnFile("provider") ;
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
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

}
