package messages;

import java.io.Serializable;


public class Message implements MessageI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8860020725994791760L;
	/**
	 * 
	 */


	private String uri;
	private TimeStamp t;
	private Properties p;
	private Serializable content;
	
	public Message(String uri, TimeStamp t,Properties p,Serializable content ) {
		this.uri=uri;
		this.t=t;
		this.p=p;
		this.content=content;
		
	}
	
	//for tests
	public Message(Serializable content ) {
		this.content=content;
	}

	@Override
	public String getURI() {
		return uri;
	}

	@Override
	public TimeStamp getTimeStamp() {
		return t;
	}

	@Override
	public Properties getProperties() {
		return p;
	}

	@Override
	public Serializable getPayload() {
		return content;
	}

	

}
