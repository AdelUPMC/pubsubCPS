package gestMessages.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import gestMessages.interfaces.ReceptionCI;
import messages.MessageI;

public class ReceptionConnector extends AbstractConnector implements ReceptionCI {
	
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((ReceptionCI)this.offering).acceptMessage(m);
		
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		((ReceptionCI)this.offering).acceptMessages(ms);
		
	}



}
