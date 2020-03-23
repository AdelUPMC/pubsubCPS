package gestMessages;

import java.util.ArrayList;

import fr.sorbonne_u.components.AbstractComponent;
import gestMessages.components.Broker;



public class TestScenario {
	public static final String	SCENARIO_BASIC1 = "scenario-basic-1";
	public static final String	SCENARIO_BASIC2 = "scenario-basic-2";
	public static final String	TestCompletTopSub = "TestCompletTopSub-1";

	public static TestScenario myself = null;
	
	private CVM cvm;
	private String brokerURI;
	private ArrayList<String> subscribersURI;
	private ArrayList<String> publishersURI;
	
	private TestScenario(CVM cvm, String scenario) throws Exception
	{
		this.cvm = cvm;
		this.subscribersURI = new ArrayList<>();
		this.publishersURI = new ArrayList<>();
		
		this.brokerURI =
				AbstractComponent.createComponent(Broker.class.getCanonicalName(),
						new Object[]{CVM.BROKER_COMPONENT_URI,CVM.PublicationInboundPortURI,CVM.ManagementInboundPortURI}) ;
		cvm.toggleTracing(this.brokerURI);
		switch (scenario) {
		case SCENARIO_BASIC1:
			execute_scenario_basic1();
			break;
		case SCENARIO_BASIC2:
			execute_scenario_basic2();
			break;
		case TestCompletTopSub:
			execute_testCompletTopSub();
		default:
			execute_scenario_basic1();
			break;
		}
	}
	
	public static synchronized void execute(CVM cvm, String scenario) throws Exception
	{
		if (myself == null)
		{		
			myself = new TestScenario(cvm, scenario);
		}
	}
	
	private void execute_scenario_basic1() throws Exception
	{
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, SCENARIO_BASIC1));
		cvm.toggleTracing(this.subscribersURI.get(0));
		this.publishersURI.add(ComponentFactory.createPublisher(CVM.PublicationOutboundPortURI, CVM.ManagementOutboundPortURIpub, SCENARIO_BASIC1));
		cvm.toggleTracing(this.publishersURI.get(0));
	}

	private void execute_scenario_basic2()
	{
		
	}
	
	private void execute_testCompletTopSub() throws Exception
	{
		this.subscribersURI.add(ComponentFactory.createSubscriber(CVM.ManagementOutboundPortURIsub, TestCompletTopSub));
		cvm.toggleTracing(this.subscribersURI.get(0));
	}
	
	public String getBrokerURI() {
		return brokerURI;
	}

	public ArrayList<String> getSubscribersURI() {
		return subscribersURI;
	}

	public ArrayList<String> getPublishersURI() {
		return publishersURI;
	}
}
