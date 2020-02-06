package messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * @author 3415266
 *
 */
public class TimeStamp {
	long time;
	String timestamper;
	
	
	
	public TimeStamp() throws UnknownHostException {
		super();
		time=0;
		timestamper=InetAddress.getLocalHost().getHostName();
	}

	public TimeStamp(long time, String timestamper) {
		super();
		this.time = time;
		this.timestamper = timestamper;
	}

	boolean isInitialised() {
		return time!=0;
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
