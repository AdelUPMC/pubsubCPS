package gestMessages;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import gestMessages.components.Broker;
import gestMessages.components.Publisher;
import gestMessages.components.Subscriber;

public class CVM extends AbstractCVM {
	
	public static final String	BROKER_COMPONENT_URI = "my-URI-broker" ;
	public static final String	PUBLISHER_COMPONENT_URI = "my-URI-publisher-programming" ;
	public static final String	SUBSCRIBER_COMPONENT_URI = "my-URI-subscriber-software_developer" ;

	protected static final String	PublicationInboundPortURI = "publication-inboundport" ;
	protected static final String	ReceptiontInboundPortURI = "reception-inboundport";
	protected static final String	ManagementInboundPortURI = "management-inboundport";
	
	protected static final String	PublicationOutboundPortURI = "publication-outboundport" ;
	protected static final String	ReceptiontOutboundPortURI = "reception-outboundport";
	protected static final String	ManagementOutboundPortURI = "management-outboundport";

	
	protected String brokerURI;
	protected String publisher_programming_URI ;
	protected String subscriber_software_developper_URI ;
	
	
	public CVM() throws Exception {
		super() ;
	}

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
		
		/** create the publisher component**/
		
		this.publisher_programming_URI=
				AbstractComponent.createComponent(Publisher.class.getCanonicalName(),
						new Object[]{1, 0});
		assert	this.isDeployedComponent(publisher_programming_URI) ;
		this.toggleTracing(this.publisher_programming_URI) ;
		System.out.println("publisher_programming cree");

		/** create the subscriber component**/
		this.subscriber_software_developper_URI =
				AbstractComponent.createComponent(
						Subscriber.class.getCanonicalName(),
						new Object[]{ReceptiontInboundPortURI,2,0}) ;
		assert	this.isDeployedComponent(this.subscriber_software_developper_URI) ;
		this.toggleTracing(this.subscriber_software_developper_URI) ;
		System.out.println("subscriber_software_developper cree");	
		
		super.deploy();
		assert	this.deploymentDone() ;
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
			Thread.sleep(10000L) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
