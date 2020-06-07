package gestMessages.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gestMessages.interfaces.ReceptionImplementationI;
import messages.MessageI;

public class ReceptionConnector extends AbstractConnector implements ReceptionImplementationI {
	
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((ReceptionImplementationI)this.offering).acceptMessage(m);
		
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		((ReceptionImplementationI)this.offering).acceptMessages(ms);
		
	}



}
