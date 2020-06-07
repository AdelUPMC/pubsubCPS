package gestMessages.plugins;

import java.util.ArrayList;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import gestMessages.interfaces.ReceptionImplementationI;
import gestMessages.ports.ReceptionInboundPortForPlugin;
import messages.MessageI;

public class SubscriberReceptionPlugin extends AbstractPlugin implements ReceptionImplementationI {
	
	
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

    @Override
    public void			installOn(ComponentI owner) throws Exception
    {
    	
        super.installOn(owner) ;
        assert	owner instanceof ReceptionImplementationI ;        
        this.addOfferedInterface(ReceptionImplementationI.class) ;
    }
    
    @Override
    public void initialise() throws Exception
    {
    	super.initialise();
    	this.ribp = new ReceptionInboundPortForPlugin(receptionInboundPortURI,this.getPluginURI(),this.owner) ;
    	this.ribp.publishPort() ;    	
    	
    }
    
    @Override
    public void			uninstall() throws Exception
    {
        this.ribp.unpublishPort();
        this.ribp.destroyPort() ;
        this.removeOfferedInterface(ReceptionImplementationI.class) ;
    }
    
    @Override
    public void acceptMessage(MessageI m) throws Exception {
    	((ReceptionImplementationI)this.owner).acceptMessage(m);
    }

    @Override
    public void acceptMessages(ArrayList<MessageI> ms) throws Exception {
    	((ReceptionImplementationI)this.owner).acceptMessages(ms);
    }

}
