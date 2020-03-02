package gestMessages.components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gestMessages.interfaces.ManagementCI;
import gestMessages.interfaces.PublicationCI;
import gestMessages.ports.ManagementOutboundPort;
import gestMessages.ports.PublicationOutboundPort;
import messages.Message;
import messages.MessageI;


@RequiredInterfaces(required = {PublicationCI.class,ManagementCI.class})
public class Publisher extends AbstractComponent {
	protected PublicationOutboundPort	portO;
	protected ManagementOutboundPort	portm;

	
	protected Publisher(String uri, int nbThreads, int nbSchedulableThreads, String publicationoutboundPortURI,String managementoutboundPortURI) throws Exception {
		super(uri, nbThreads, nbSchedulableThreads) ;
		// TODO Auto-generated constructor stub
		this.portO =	new PublicationOutboundPort(publicationoutboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.portO.localPublishPort();
		this.portm= new ManagementOutboundPort(managementoutboundPortURI,this);
		this.portm.localPublishPort();
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		this.tracer.setTitle("Publisher") ;
		this.tracer.setRelativePosition(1, 1) ;
	
	}
	
	public void testConnection()
	{
		try {	
			this.portO.publish((MessageI)null, "Toute mes felicitations");
			this.logMessage("publisher published a new message ") ;
		} catch (Exception e) {
			//System.out.println("Presque !!");
			e.printStackTrace();
				}
		
	}
	
	public void testManagement()
	{
		try {	
			this.portm.createTopic("CPS publisher");
			this.logMessage("Topic created by a publisher");
		} catch (Exception e) {
			//System.out.println("Presque !!");
			e.printStackTrace();
				}
		
	}
	public void testpublish()
	{
		try {
			this.portO.publish(new Message(null,null,null,"je tente la publication"), "UPMC");
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
		
		// Schedule the first service method invocation in one second.
		/*
		this.scheduleTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						testEnvoiMsg
						System.out.println("end publisher");//((Publisher)this.getTaskOwner()).testConnection();
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			},
			1000, TimeUnit.MILLISECONDS);
		}
		 */		
		
		this.scheduleTask(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							((Publisher)this.getTaskOwner()).testpublish();
						} catch (Exception e) {
							throw new RuntimeException(e) ;
						}
					}				},
				1000, TimeUnit.MILLISECONDS);
		System.out.println("Pub  Start End ");
	}
	
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
		this.portO.doDisconnection();
		this.portm.doDisconnection();
		// This called at the end to make the component internal
		// state move to the finalised state.
		super.finalise();
	}
	
	
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		System.out.println("Shutdown Pub");
		
		try {
			this.portO.unpublishPort();
			this.portO.destroyPort();
			this.portm.unpublishPort();
			this.portm.destroyPort();
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
		
	}
	
}
