package testmessages;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.InexistentPropertyException;
import messages.Message;
import messages.MessageI;
import messages.Properties;
import messages.TimeStamp;

class TestMessage {
	//String BASE_URI = "Message-URI-";
    private MessageI m1;
    private MessageI m2;
    TimeStamp ts1;
    TimeStamp ts2;
    long time1;
    long time2;
    String timestamper1;
    String timestamper2;
    Properties p1;
    Properties p2;
    

	@BeforeEach
	void setUp() throws Exception {
		time1=Instant.now().getEpochSecond();
    	Thread.sleep(1000);
    	time2=Instant.now().getEpochSecond();
    	
    	timestamper1="user1";
    	timestamper2="user2";
    	ts1=new TimeStamp(time1,timestamper1);
    	ts2=new TimeStamp(time2,timestamper2);
    	
    	p1= new Properties();
    	p2= new Properties();
    	
    	p1.putProp("Object-Oriented language",false);
    	p2.putProp("Object-Oriented language",true);
    	p1.putProp("idLanguage",1);
    	p2.putProp("idLanguage",2);
    	p1.putProp("Language","OCaml");
    	p2.putProp("Language","Java");
    	p1.putProp("Users",0.1);
    	p2.putProp("Users",0.8);
    	p1.putProp("Users",10000);
    	p2.putProp("Users",9000000);
    	
        this.m1 = new Message(ts1,p1,"Hello world from OCaml");
        this.m2=new Message(ts2,p2,"Hello world from Java");
	}
	@Test
    void testgetPayload() {
    	assert(m1.getPayload()=="Hello world from OCaml");
    	assert(m2.getPayload()=="Hello world from Java");
    }
    
    @Test
    void testgetUri() {
    	assertEquals(m1.getURI(),"Message-URI-1");
    	assertEquals(m2.getURI(),"Message-URI-2");
    }
    
    @Test
    void testgetProperties() {
    	try {
			assertEquals(m1.getProperties().getBooleanProp("Object-Oriented language"),false);
			assertEquals(m2.getProperties().getBooleanProp("Object-Oriented language"),true);
	    	assert(m1.getProperties().getIntProp("idLanguage")==1);
	    	assert(m2.getProperties().getIntProp("idLanguage")==2);
	    	assertEquals(m1.getProperties().getStringProp("Language"),"OCaml");
	    	assertEquals(m2.getProperties().getStringProp("Language"),"Java");
	    	assert(m1.getProperties().getDoubleProp("Users")==0.1);
	    	assert(m2.getProperties().getDoubleProp("Users")==0.8);
	     	assert(m1.getProperties().getIntProp("Users")==10000);
	    	assert(m2.getProperties().getIntProp("Users")==9000000);
		} catch (InexistentPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	
    }
    
    @Test
    void testTimeStamp() {
    	assertEquals(m1.getTimeStamp(),ts1);
        assertEquals(m2.getTimeStamp(),ts2);
        assertEquals(m1.getTimeStamp().getTime(),time1);
        assertEquals(m2.getTimeStamp().getTime(),time2);
        assertEquals(m1.getTimeStamp().getTimestamper(),timestamper1);
        assertEquals(m2.getTimeStamp().getTimestamper(),timestamper2);
        
    }

}
