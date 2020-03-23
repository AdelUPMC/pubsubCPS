package messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Properties implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8289798615516456793L;
	Map<String, Boolean> boolprop;
	Map<String, Byte> byteprop;
	Map<String, Character> charprop;
	Map<String, Double> doubleprop;
	Map<String, Float> floatprop;
	Map<String, Integer> intprop;
	Map<String, Long> longprop;
	Map<String, Short> shortprop;
	Map<String, String> stringprop;

	public Properties() {
		this.boolprop = new HashMap<>();
		this.byteprop = new HashMap<>();
		this.charprop = new HashMap<>();
		this.doubleprop = new HashMap<>();
		this.floatprop = new HashMap<>();
		this.intprop = new HashMap<>();
		this.longprop = new HashMap<>();
		this.shortprop = new HashMap<>();
		this.stringprop = new HashMap<>();

	}
	
	public void putProp(String name, Boolean v) {
		this.boolprop.put(name, v);
	}

	public void putProp(String name, Byte v) {
		this.byteprop.put(name, v);
	}

	public void putProp(String name, Character v) {
		this.charprop.put(name, v);
	}

	public void putProp(String name, Double v) {
		this.doubleprop.put(name, v);
	}

	public void putProp(String name, Float v) {
		this.floatprop.put(name, v);
	}

	public void putProp(String name, Integer v) {
		this.intprop.put(name, v);
	}

	public void putProp(String name, Long v) {
		this.longprop.put(name, v);
	}

	public void putProp(String name, Short v) {
		this.shortprop.put(name, v);
	}

	public void putProp(String name, String v) {
		this.stringprop.put(name, v);
	}

	public Boolean getBooleanProp(String name) {
		return (Boolean) this.boolprop.get(name);
	}

	public Byte getByteProp(String name) {
		return (Byte) this.byteprop.get(name);
	}

	public Character getCharProp(String name) {
		return (Character) this.charprop.get(name);
	}

	public Double getDoubleProp(String name) {
		return (Double) this.doubleprop.get(name);
	}

	public Float getFloatProp(String name) {
		return (Float) this.floatprop.get(name);
	}

	public Integer getIntProp(String name) {
		if (this.intprop.get(name) != null)
			return (Integer) this.intprop.get(name);
		else
			return -1;
	}

	public Long getLongProp(String name) {
		return (Long) this.longprop.get(name);
	}

	public Short getShortProp(String name) {
		return (Short) this.shortprop.get(name);
	}

	public String getStringProp(String name) {
		return (String) this.stringprop.get(name);
	}
	
}
