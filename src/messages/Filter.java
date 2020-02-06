package messages;

public class Filter implements MessageFilterI {

	boolean expression;
	public Filter(boolean expression) {
		// TODO Auto-generated constructor stub
		this.expression=expression;
	}
	@Override
	public boolean filter(MessageI m) {
		// TODO Auto-generated method stub
		return false;
	}

	
	

}
