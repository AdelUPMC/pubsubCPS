package gestMessages.interfaces;

import messages.MessageI;

public interface PublicationsImplementationI {
	void publish(MessageI m,String topic) throws Exception;
	void publish(MessageI m,String[] topics) throws Exception;
	void publish(MessageI[] ms,String topic) throws Exception;
	void publish(MessageI[] ms,String[] topics) throws Exception;

	
}
