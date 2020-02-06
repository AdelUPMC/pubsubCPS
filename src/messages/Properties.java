package messages;

public class Properties {
	String name;
	boolean propbool;
	byte popbyte;
	char propchar;
	double propdouble;
	float propfloat;
	int propint;
	long proplong;
	short propshort;
	
	String propstring;
	
	public Properties() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Properties(String name, boolean propbool, byte popbyte, char propchar, double propdouble, float propfloat,
			int propint, long proplong, short propshort, String propstring) {
		super();
		this.name = name;
		this.propbool = propbool;
		this.popbyte = popbyte;
		this.propchar = propchar;
		this.propdouble = propdouble;
		this.propfloat = propfloat;
		this.propint = propint;
		this.proplong = proplong;
		this.propshort = propshort;
		this.propstring = propstring;
	}
	public boolean isPropbool() {
		return propbool;
	}
	public void putProp(boolean propbool) {
		this.propbool = propbool;
	}
	public byte getByteProp() {
		return popbyte;
	}
	public void putProp(byte popbyte) {
		this.popbyte = popbyte;
	}
	public char getCharProp() {
		return propchar;
	}
	public void putProp(char propchar) {
		this.propchar = propchar;
	}
	public double getDoubleProp() {
		return propdouble;
	}
	public void putProp(double propdouble) {
		this.propdouble = propdouble;
	}
	public float getFloatProp() {
		return propfloat;
	}
	public void putProp(float propfloat) {
		this.propfloat = propfloat;
	}
	public int getIntProp() {
		return propint;
	}
	public void putProp(int propint) {
		this.propint = propint;
	}
	public long getLongProp() {
		return proplong;
	}
	public void putProp(long proplong) {
		this.proplong = proplong;
		
	}
	public short getShortProp() {
		return propshort;
	}
	public void putProp(short propshort) {
		this.propshort = propshort;
	}
	public String getStringProp() {
		return propstring;
	}
	public void putProp(String propstring) {
		this.propstring = propstring;
	}
	

	
	
}
