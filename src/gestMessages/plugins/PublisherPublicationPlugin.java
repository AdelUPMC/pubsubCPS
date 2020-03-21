package gestMessages.plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import gestMessages.CVM;
import gestMessages.connectors.PublicationConnector;
import gestMessages.interfaces.PublicationCI;
import gestMessages.ports.PublicationOutboundPort;
import messages.MessageI;

public class PublisherPublicationPlugin extends AbstractPlugin {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected PublicationOutboundPort pobp;

	@Override
	public void installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		this.addRequiredInterface(PublicationCI.class);
		this.pobp = new PublicationOutboundPort(this.owner);
		this.pobp.publishPort();
	}
	
	@Override
	public void initialise() throws Exception
	{
		// Using the reflection approach to get 
		//the URI of the inbound port of the publicaition component
		this.addRequiredInterface(ReflectionI.class);
		ReflectionOutboundPort robp_publisher = new ReflectionOutboundPort(this.owner);
		robp_publisher.publishPort();
		
		this.owner.doPortConnection(
				robp_publisher.getPortURI(), 
				CVM.BROKER_COMPONENT_URI,
				ReflectionConnector.class.getCanonicalName());
		
		String[] publishers_uris = robp_publisher.findPortURIsFromInterface(PublicationCI.class) ;
        //System.out.println("publishers_uris= "+publishers_uris.toString());
		assert	publishers_uris != null && publishers_uris.length == 1 ;

		
		this.owner.doPortDisconnection(robp_publisher.getPortURI()) ;
		robp_publisher.unpublishPort() ;
		robp_publisher.destroyPort() ;
		this.removeRequiredInterface(ReflectionI.class) ;
		
		// connect the outbound port.
		this.owner.doPortConnection(
				this.pobp.getPortURI(),
				publishers_uris[0],
				PublicationConnector.class.getCanonicalName()) ;


		super.initialise();
	}
	@Override
	public void finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.pobp.getPortURI());
	}	
	@Override
	public void uninstall() throws Exception
	{
		this.pobp.unpublishPort();
		this.pobp.destroyPort();
		this.removeRequiredInterface(PublicationCI.class);
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		this.pobp.publish(m, topic);
	}

	public void publish(MessageI m, String[] topics) throws Exception {
		this.pobp.publish(m, topics);
	}

	public void publish(MessageI[] ms, String topic) throws Exception {
		this.pobp.publish(ms, topic);
	}
	
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.pobp.publish(ms, topics);
	}

}
