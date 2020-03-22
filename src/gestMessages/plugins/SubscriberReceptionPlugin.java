package gestMessages.plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
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
    protected String pluginURI;


    public SubscriberReceptionPlugin(String receptionInboundPortUri, String pluginURI) {
        this.pluginURI= pluginURI;
         this.receptionInboundPortURI=receptionInboundPortUri;
    }
    @Override
    public void initialise() throws Exception {
        super.initialise();
        this.ribp = new ReceptionInboundPortForPlugin(receptionInboundPortURI,this.getPluginURI(), this.owner) ;
        this.ribp.publishPort() ;
    }
    @Override
    public void			installOn(ComponentI owner) throws Exception
    {
        super.installOn(owner) ;
        assert	owner instanceof ReceptionCI ;
        this.addOfferedInterface(ReceptionCI.class) ;
        this.ribp = new ReceptionInboundPortForPlugin(receptionInboundPortURI,this.getPluginURI(),this.owner) ;
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
