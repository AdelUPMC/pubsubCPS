package gestMessages;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import gestMessages.components.Broker;
import gestMessages.components.Publisher;
import gestMessages.components.Subscriber;
import gestMessages.connectors.ManagementConnector;
import gestMessages.connectors.PublicationConnector;
import gestMessages.connectors.ReceptionConnector;

public class CVM extends AbstractCVM {
	/** URI of the provider component (convenience)**/
	protected static final String	BROKER_COMPONENT_URI = "my-URI-broker" ;
	/** URI of the consumer component (convenience).						*/
	//protected static final String	PUBLISHER_COMPONENT_URI = "my-URI-publisher" ;
	/** URI of the provider component (convenience)**/
	//protected static final String	SUBSCRIBER_COMPONENT_URI = "my-URI-subscriber" ;
	/** URI of the provider outbound port (simplifies the connection).	*/
	//protected static final String	PublicationOutboundPortURI = "publication-outboundport" ;
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	PublicationInboundPortURI = "publication-inboundport" ;
	/** URI of the consumer inbound port (simplifies the connection).		*/
	//protected static final String	ReceptionOutboundPortURI = "reception-outboundport"; 
	/** URI of the consumer inbound port (simplifies the connection).		*/
	//protected static final String	ReceptionInboundPortURI = "reception-inboundport";

	//protected static final String	ManagementOutboundPortURIpub = "management-outboundportpub";
	//protected static final String	ManagementOutboundPortURIsub = "management-outboundportsub"; 
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	ManagementInboundPortURI = "management-inboundport";
	public CVM() throws Exception {
		// TODO Auto-generated constructor stub
	}
	protected String	brokerURI ;
	protected String	publisherURI ;
	protected String	subscriberURI ;
	
	
	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;

		this.brokerURI =
			AbstractComponent.createComponent(Broker.class.getCanonicalName(),
					new Object[]{BROKER_COMPONENT_URI,PublicationInboundPortURI,ManagementInboundPortURI}) ;
		assert	this.isDeployedComponent(this.brokerURI) ;
		this.toggleTracing(this.brokerURI) ;
		this.toggleLogging(this.brokerURI) ;

		System.out.println("broker cree");
		
		// create the publisher component
		this.publisherURI =ComponentFactory.createPublisher(PublicationInboundPortURI, ManagementInboundPortURI);
		/*this.publisherURI =
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[]{PUBLISHER_COMPONENT_URI,1,1,PublicationOutboundPortURI,ManagementOutboundPortURIpub}) ;
		assert	this.isDeployedComponent(this.publisherURI) ;
		this.toggleTracing(this.publisherURI) ;
		this.toggleLogging(this.publisherURI) ;
		System.out.println("publisher cree");
		
		
		this.subscriberURI =
				AbstractComponent.createComponent(
						Subscriber.class.getCanonicalName(),
						new Object[]{SUBSCRIBER_COMPONENT_URI,ReceptionInboundPortURI,ManagementOutboundPortURIsub});	
			assert	this.isDeployedComponent(this.subscriberURI) ;

			this.toggleTracing(this.subscriberURI) ;
			this.toggleLogging(this.subscriberURI) ;
			System.out.println("subscriber cree");
			*/

		this.subscriberURI =ComponentFactory.createSubscriber(ManagementInboundPortURI);
	
				
		// --------------------------------------------------------------------
		// Connection phase
		// --------------------------------------------------------------------
		
		//connection de l'outboundport du publisher vers l'inbound du broker
		/*this.doPortConnection(
				this.publisherURI,
				PublicationOutboundPortURI,
				PublicationInboundPortURI,
				PublicationConnector.class.getCanonicalName()) ;
		
		//connection de l'outboundport du broker vers l'inbound du subscriber	
		this.doPortConnection(
				this.brokerURI,
				ReceptionOutboundPortURI,
				ReceptionInboundPortURI,
				ReceptionConnector.class.getCanonicalName()) ;
		
		
		this.doPortConnection(
				this.publisherURI,
				ManagementOutboundPortURIpub,
				ManagementInboundPortURI,
				ManagementConnector.class.getCanonicalName()) ;
		
		this.doPortConnection(
				this.subscriberURI,
				ManagementOutboundPortURIsub,
				ManagementInboundPortURI,
				ManagementConnector.class.getCanonicalName()) ;
		*/
		super.deploy();
		assert	this.deploymentDone() ;
		}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#finalise()
	 */
	/*
	@Override
	public void				finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.
		this.doPortDisconnection(
				this.publisherURI,
				PublicationOutboundPortURI) ;
		
		this.doPortDisconnection(
				this.brokerURI,
				ReceptionOutboundPortURI) ;
		
		this.doPortDisconnection(
				this.publisherURI,
				ManagementOutboundPortURIpub) ;

		this.doPortDisconnection(
				this.subscriberURI,
				ManagementOutboundPortURIsub) ;
		
	//	super.finalise();
	}
	*/

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
			a.startStandardLifeCycle(2500L) ;
			// Give some time to see the traces (convenience).
			Thread.sleep(2000L) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
