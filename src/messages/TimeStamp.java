package messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

/**
 * 
 *
 */
public class TimeStamp {
	long time;
	String timestamper;
	
	public TimeStamp() throws UnknownHostException{
		this.time=Instant.now().getEpochSecond();
		this.timestamper = InetAddress.getLocalHost().getHostAddress();
	}

	public TimeStamp(long time, String timestamper) {
		this.time = time;
		this.timestamper = timestamper;
	}

	boolean isInitialised() {
		return time!=0  && timestamper != null;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getTimestamper() {
		return timestamper;
	}

	public void setTimestamper(String timestamper) {
		this.timestamper = timestamper;
	}
	
}
