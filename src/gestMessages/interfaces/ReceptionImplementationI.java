package gestMessages.interfaces;

import messages.MessageI;

public interface ReceptionImplementationI{
	void acceptMessage(MessageI m)throws Exception;
	void acceptMessages(MessageI[] ms)throws Exception;
}
