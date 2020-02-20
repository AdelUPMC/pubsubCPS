package gestMessages.components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import gestMessages.interfaces.PublicationCI;
import gestMessages.ports.PublicationOutboundPort;
import messages.MessageI;


@RequiredInterfaces(required = {PublicationCI.class})
public class Publisher extends AbstractComponent {
	protected PublicationOutboundPort	portO;
	
	protected Publisher(String uri, String outboundPortURI) throws Exception {
		super(uri, 1, 1) ;
		// TODO Auto-generated constructor stub
		this.portO =	new PublicationOutboundPort(outboundPortURI, this) ;
		// publish the port (an outbound port is always local)
		this.portO.localPublishPort();
		
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
	
	public void			start() throws ComponentStartException
	{
		System.out.println("pub Start ");
		super.start() ;
		this.logMessage("starting publisher component.") ;
		// Schedule the first service method invocation in one second.
		this.scheduleTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						((Publisher)this.getTaskOwner()).testConnection();
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			},
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
		this.portO.unpublishPort();

		// This called at the end to make the component internal
		// state move to the finalised state.
		super.finalise();
	}
	
}
