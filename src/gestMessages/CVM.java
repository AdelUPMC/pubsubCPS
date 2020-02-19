package gestMessages;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import gestMessages.components.Broker;
import gestMessages.components.Publisher;
import gestMessages.connectors.PublicationConnector;

public class CVM extends AbstractCVM {
	/** URI of the provider component (convenience)**/
	protected static final String	BROKER_COMPONENT_URI = "my-URI-broker" ;
	/** URI of the consumer component (convenience).						*/
	protected static final String	PUBLISHER_COMPONENT_URI = "my-URI-publisher" ;
	/** URI of the provider outbound port (simplifies the connection).	*/
	protected static final String	PublicationOutboundPortURI = "publication-outboundport" ;
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	PublicationInboundPortURI = "publication-inboundport" ;

	public CVM() throws Exception {
		// TODO Auto-generated constructor stub
	}
	protected String	brokerURI ;
	protected String	publisherURI ;
	
	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;

		// create the broker component
		
		System.out.println("Debut !!!");
		
		this.brokerURI =
			AbstractComponent.createComponent(Broker.class.getCanonicalName(),
					new Object[]{BROKER_COMPONENT_URI,PublicationInboundPortURI,1,0}) ;
		System.out.println("!!!!");
		assert	this.isDeployedComponent(this.brokerURI) ;
		System.out.println("!!!!");

		// make it trace its operations; comment and uncomment the line to see
		// the difference
		this.toggleTracing(this.brokerURI) ;
		this.toggleLogging(this.brokerURI) ;

		System.out.println("broker cree");
		
		// create the publisher component
		this.publisherURI =
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[]{PUBLISHER_COMPONENT_URI, PublicationOutboundPortURI}) ;
		assert	this.isDeployedComponent(this.publisherURI) ;
		// make it trace its operations; comment and uncomment the line to see
		// the difference
		this.toggleTracing(this.publisherURI) ;
		this.toggleLogging(this.publisherURI) ;
		System.out.println("publisher cree");
		// --------------------------------------------------------------------
		// Connection phase
		// --------------------------------------------------------------------
	
		// do the connection
		this.doPortConnection(
				this.publisherURI,
				PublicationOutboundPortURI,
				PublicationInboundPortURI,
				PublicationConnector.class.getCanonicalName()) ;
		//System.out.println("connection debut");
		super.deploy();
		//System.out.println("deploy");
		assert	this.deploymentDone() ;
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#finalise()
	 */
	@Override
	public void				finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.
		this.doPortDisconnection(
				this.publisherURI,
				PublicationOutboundPortURI) ;

		super.finalise();
	}

	/**
	 * disconnect the components and then call the base shutdown method.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#shutdown()
	 */
	@Override
	public void				shutdown() throws Exception
	{
		assert	this.allFinalised() ;
		// any disconnection not done yet can be performed here

		super.shutdown();
	}

	public static void		main(String[] args)
	{
		try {
			// Create an instance of the defined component virtual machine.
			CVM a = new CVM() ;
			// Execute the application.
			a.startStandardLifeCycle(20000L) ;
			// Give some time to see the traces (convenience).
			Thread.sleep(5000L) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
