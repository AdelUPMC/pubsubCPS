package gestMessages.plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import gestMessages.interfaces.ReceptionCI;
import gestMessages.ports.ReceptionInboundPortForPlugin;
import messages.MessageI;

public class SubscriberReceptionPlugin extends AbstractPlugin implements ReceptionCI {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReceptionInboundPortForPlugin ribp;
	public String receptionInboundPortURI;
    protected String pluginURI2;


    public SubscriberReceptionPlugin(String receptionInboundPortUri, String pluginURI) {
        this.pluginURI2= pluginURI;
         this.receptionInboundPortURI=receptionInboundPortUri;
    }
/*uri != null and owner != null and implementedInterface != null
 pre	pluginURI != null
 pre	OfferedI.class.isAssignableFrom(implementedInterface)
 pre	implementedInterface.isAssignableFrom(this.getClass())
 pre	this.getOwner().isInstalled(pluginURI)
	 */
    @Override
    public void			installOn(ComponentI owner) throws Exception
    {
    	
        super.installOn(owner) ;
        
        assert	owner instanceof ReceptionCI ;        
        this.addOfferedInterface(ReceptionCI.class) ;
        assert	pluginURI2 != null :
			new PreconditionException("pluginURI != null") ;
         System.out.println("crash car "  + receptionInboundPortURI + "   " + this.getPluginURI());
        System.out.println("fin install ON ");
    }
    
    @Override
    public void initialise() throws Exception {
    	super.initialise();
    	this.ribp = new ReceptionInboundPortForPlugin(receptionInboundPortURI,this.getPluginURI(),this.owner) ;
    	System.out.println("debtu");
    	this.ribp.publishPort() ;    	
    }
    
    @Override
    public void			uninstall() throws Exception
    {
        this.ribp.unpublishPort();
        this.ribp.destroyPort() ;
        this.removeOfferedInterface(ReceptionCI.class) ;
    }
    
    @Override
    public void acceptMessage(MessageI m) throws Exception {
    	((ReceptionCI)this.owner).acceptMessage(m);
    }

    @Override
    public void acceptMessages(MessageI[] ms) throws Exception {
    	((ReceptionCI)this.owner).acceptMessages(ms);
    }

}
