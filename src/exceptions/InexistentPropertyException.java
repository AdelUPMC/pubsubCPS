package exceptions;

public class InexistentPropertyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public InexistentPropertyException(String name){
		super("la propri�t� "+name+" n'existe pas");
	  }  

}
