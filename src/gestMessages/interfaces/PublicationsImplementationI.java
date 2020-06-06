package gestMessages.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;
import messages.MessageI;

public interface PublicationsImplementationI extends OfferedI, RequiredI {
	void publish(MessageI m,String topic) throws Exception;
	void publish(MessageI m,String[] topics) throws Exception;
	void publish(MessageI[] ms,String topic) throws Exception;
	void publish(MessageI[] ms,String[] topics) throws Exception;

	
}
