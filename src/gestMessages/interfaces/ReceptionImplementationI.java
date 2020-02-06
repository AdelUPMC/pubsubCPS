package gestMessages.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;
import messages.MessageI;

public interface ReceptionImplementationI extends OfferedI,RequiredI {
	void acceptMessage(MessageI m);
	void acceptMessages(MessageI[] ms);
}
