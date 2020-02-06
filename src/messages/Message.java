package messages;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

public class Message implements MessageI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String id;
	TimeStamp t;
	Properties p;
	Serializable content;
	
	public Message(Properties p, Serializable content) throws UnknownHostException {
		String timestamper=InetAddress.getLocalHost().getHostName();
		long time=Instant.now().getEpochSecond();
		t= new TimeStamp(time,timestamper);
		id=java.util.UUID.randomUUID().toString();
		this.p=p;
		this.content=content;
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeStamp getTimeStamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getPayload() {
		// TODO Auto-generated method stub
		return null;
	}

}
