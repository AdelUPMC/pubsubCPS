package gestMessages.components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.pipeline.connectors.ManagementConnector;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gestMessages.connectors.PublicationConnector;
import gestMessages.interfaces.ManagementCI;
import gestMessages.interfaces.PublicationCI;
import gestMessages.ports.ManagementOutboundPort;
import gestMessages.ports.PublicationOutboundPort;
import messages.Message;
import messages.MessageI;


@RequiredInterfaces(required = {PublicationCI.class,ManagementCI.class})
public class Publisher extends AbstractComponent {
	protected PublicationOutboundPort	publicationObp;
	protected ManagementOutboundPort	managementObp;
	protected final String publishURI="handler-publish";

	
	protected Publisher(String uri,String publicationObpURI,String managementObpURI) throws Exception {
		super(uri, 0, 1) ;
		
		
		// TODO Auto-generated constructor stub
		this.publicationObp =	new PublicationOutboundPort(publicationObpURI, this) ;
		// publish the port (an outbound port is always local)
		this.publicationObp.publishPort();
		this.managementObp= new ManagementOutboundPort(managementObpURI,this);
		this.managementObp.publishPort();
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir"));
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home"));
		}
		this.tracer.setTitle("Publisher");
		this.tracer.setRelativePosition(1, 1) ;
		this.publicationObp.doConnection(uri, PublicationConnector.class.getCanonicalName());
		this.managementObp.doConnection(uri, ManagementConnector.class.getCanonicalName());
		System.out.println(publicationObpURI + "    " + PublicationConnector.class.getCanonicalName());
	
		//this.doPortConnection(uri , publicationObpURI, PublicationConnector.class.getCanonicalName());
		//this.doPortConnection(uri, managementObpURI, ManagementConnector.class.getCanonicalName());
	}
	
	public void execute() throws Exception{
		this.createNewExecutorService(publishURI,2,true);
		handleRequestAsync(publishURI,new AbstractComponent.AbstractService<Void>() {
			@Override
			public Void call() throws Exception {
				((Publisher)this.getServiceOwner()).testpublish();
				return null;
			}
		});
	}
	public void testConnection()
	{
		try {	
			this.publicationObp.publish((MessageI)null, "Toute mes felicitations");
			this.logMessage("publisher published a new message ") ;
		} catch (Exception e) {
			//System.out.println("Presque !!");
			e.printStackTrace();
				}
		
	}
	
	public void testManagement()
	{
		try {	
			this.managementObp.createTopic("CPS publisher");
			this.logMessage("Topic created by a publisher");
		} catch (Exception e) {
			//System.out.println("Presque !!");
			e.printStackTrace();
				}
		
	}
	public void testpublish()
	{
		try {
			this.publicationObp.publish(new Message(null,null,null,"je tente la publication"), "UPMC");
			this.logMessage("publisher published a new message ") ;
		} catch (Exception e) {
			//System.out.println("Presque !!");
			e.printStackTrace();
				}
		
	}
	public void			start() throws ComponentStartException
	{
		System.out.println("pub Start ");
		super.start() ;
		this.logMessage("starting publisher component.") ;

	}
	/*
	@Override
	public void			finalise() throws Exception
	{
		this.logMessage("stopping publisher component.") ;
		this.printExecutionLogOnFile("publisher");
		// This is the place where to clean up resources, such as
		// disconnecting and unpublishing ports that will be destroyed
		// when shutting down.
		// In static architectures like in this example, ports can also
		// be disconnected by the finalise method of the component
		// virtual machine.
		this.publicationObp.doDisconnection();
		this.managementObp.doDisconnection();
		// This called at the end to make the component internal
		// state move to the finalised state.
		super.finalise();
	}*/
	
	/*
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		System.out.println("Shutdown Pub");
		
		try {
			this.publicationObp.unpublishPort();
			this.publicationObp.destroyPort();
			this.managementObp.unpublishPort();
			this.managementObp.destroyPort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.shutdown();
		/*
		try {
			PortI[] p = this.findPortsFromInterface(ReceptionCI.class) ;
			p[0].unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		*/
		
	//}
	
}
