package messages;

import java.io.Serializable;

public interface MessageI extends Serializable {
	String getURI();
	TimeStamp getTimeStamp();
	Properties getProperties();
	Serializable getPayload();
	
}
