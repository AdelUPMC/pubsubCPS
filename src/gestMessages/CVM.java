package gestMessages;

import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {
	
	public static final String	BROKER_COMPONENT_URI = "my-URI-broker" ;
	public static final long TIME_PROGRAMME = 50000L;
	/** URI of the provider outbound port (simplifies the connection).	*/
	protected static final String	PublicationOutboundPortURI = "publication-outboundport" ;
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	PublicationInboundPortURI = "publication-inboundport" ;
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	ManagementOutboundPortURIpub = "management-outboundportpub";
	protected static final String	ManagementOutboundPortURIsub = "management-outboundportsub"; 
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	ManagementInboundPortURI = "management-inboundport";
	

	
	protected String	brokerURI ;
	protected String	publisherURI_01 ;
	protected String	subscriberURI_01 ;

	//protected static final String	PublicationOutboundPortURI = "publication-outboundport" ;
	protected static final String	ReceptiontOutboundPortURI = "reception-outboundport";
	protected static final String	ManagementOutboundPortURI = "management-outboundport";

	
	//protected String brokerURI;
	protected String publisher_programming_URI ;
	protected String subscriber_software_developper_URI ;
	
	public CVM() throws Exception {
		super();
	}
	
	
	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;
		//AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PUBLIHSING) ;
		//AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING) ;
		//AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;
	
		
		//TestScenario.execute(this, TestScenario.TestCompletTopSub);
		TestScenario.execute(this, TestScenario.SCENARIO_BASIC1);
		
		
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
			Thread.sleep(TIME_PROGRAMME) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
