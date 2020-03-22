package gestMessages;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import gestMessages.components.Broker;
import gestMessages.components.Publisher;
import gestMessages.components.Subscriber;
import gestMessages.connectors.ManagementConnector;
import gestMessages.connectors.PublicationConnector;
import gestMessages.connectors.ReceptionConnector;
import gestMessages.ports.PublicationOutboundPort;

public class CVM extends AbstractCVM {
	
	public static final String	BROKER_COMPONENT_URI = "my-URI-broker" ;
///*<<<<<<< HEAD
	/** URI of the consumer component (convenience).						*/
	//protected static final String	PUBLISHER_COMPONENT_URI = "my-URI-publisher" ;
	/** URI of the provider component (convenience)**/
	//protected static final String	SUBSCRIBER_COMPONENT_URI = "my-URI-subscriber" ;
	/** URI of the provider outbound port (simplifies the connection).	*/
	protected static final String	PublicationOutboundPortURI = "publication-outboundport" ;
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	PublicationInboundPortURI = "publication-inboundport" ;
	/** URI of the consumer inbound port (simplifies the connection).		*/
	//protected static final String	ReceptionOutboundPortURI = "reception-outboundport"; 
	/** URI of the consumer inbound port (simplifies the connection).		*/
	//protected static final String	ReceptionInboundPortURI = "reception-inboundport";

	protected static final String	ManagementOutboundPortURIpub = "management-outboundportpub";
	protected static final String	ManagementOutboundPortURIsub = "management-outboundportsub"; 
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	ManagementInboundPortURI = "management-inboundport";
	
	public CVM() throws Exception {
		super();
	}
	protected String	brokerURI ;
	protected String	publisherURI_01 ;
	protected String	subscriberURI_01 ;

	//protected static final String	PublicationOutboundPortURI = "publication-outboundport" ;
	protected static final String	ReceptiontOutboundPortURI = "reception-outboundport";
	protected static final String	ManagementOutboundPortURI = "management-outboundport";

	
	//protected String brokerURI;
	protected String publisher_programming_URI ;
	protected String subscriber_software_developper_URI ;
	
	
	
	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;
		//AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
		//AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
		//AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;
		
		/**broker component**/
		//TOEDIT
		this.brokerURI =
			AbstractComponent.createComponent(Broker.class.getCanonicalName(),
					new Object[]{BROKER_COMPONENT_URI,PublicationInboundPortURI,ManagementInboundPortURI}) ;
	
		assert	this.isDeployedComponent(this.brokerURI) ;
		
		this.toggleTracing(this.brokerURI) ;
		System.out.println("broker cree");
//<<<<<<< HEAD
		// create the publisher component
		
		this.publisherURI_01 =ComponentFactory.createPublisher(PublicationOutboundPortURI, ManagementOutboundPortURIpub);
		
		this.subscriberURI_01 =ComponentFactory.createSubscriber(ManagementOutboundPortURIsub);				
	
		
		super.deploy();	
		assert	this.deploymentDone();
		}



	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#finalise()
	 */
	@Override
	public void				finalise() throws Exception
	{
		super.finalise();
	}
	

	/**
>>>>>>> 9b4051e49f151c3cb65de3dd92a20aad8a903d57
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

		//super.shutdown();
	}

	public static void		main(String[] args)
	{
		try {
			// Create an instance of the defined component virtual machine.
			CVM a = new CVM() ;
			// Execute the application.
			a.startStandardLifeCycle(2500L) ;
			// Give some time to see the traces (convenience).
			Thread.sleep(10000L) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
