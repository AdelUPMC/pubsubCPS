package messages;

import java.io.Serializable;


public class Message implements MessageI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8860020725994791760L;
	private static final String BASE_URI = "Message-URI-";
	/**
	 * 
	 */


	private String uri;
	private TimeStamp t;
	private Properties p;
	private Serializable content;
	private static int idMessage = 0;
	
	public Message(TimeStamp t,Properties p,Serializable content ) {
		synchronized(this)
		{
			idMessage ++;
			this.uri= BASE_URI + idMessage;	
		}		
		this.t=t;
		this.p=p;
		this.content=content;
		
	}
	
	public Message(Serializable content)
	{
		synchronized(this)
		{
			idMessage ++;
			this.uri= BASE_URI + idMessage;	
		}		
		this.content=content;
		this.t = null; // new TimeStamp TODO
		this.p = new Properties();
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

	@Override
	public String toString() {
		return "Message [uri=" + uri + "  " + this.content + "]";
	}
}
