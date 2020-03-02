package messages;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Properties implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8289798615516456793L;
	Map<String, Object> prop;
	
	public Properties() {
		this.prop = new HashMap<String, Object>();
	}
	
	public void putProp(String name, Boolean v) {
		this.prop.put(name, v);
	}

	public void putProp(String name, Byte v) {
		this.prop.put(name, v);
	}

	public void putProp(String name, Character v) {
		this.prop.put(name, v);
	}

	public void putProp(String name, Double v) {
		this.prop.put(name, v);
	}

	public void putProp(String name, Float v) {
		this.prop.put(name, v);
	}

	public void putProp(String name, Integer v) {
		this.prop.put(name, v);
	}

	public void putProp(String name, Long v) {
		this.prop.put(name, v);
	}

	public void putProp(String name, Short v) {
		this.prop.put(name, v);
	}

	public void putProp(String name, String v) {
		this.prop.put(name, v);
	}

	public Boolean getBooleanProp(String name) {
		return (Boolean) this.prop.get(name);
	}

	public Byte getByteProp(String name) {
		return (Byte) this.prop.get(name);
	}

	public Character getCharProp(String name) {
		return (Character) this.prop.get(name);
	}

	public Double getDoubleProp(String name) {
		return (Double) this.prop.get(name);
	}

	public Float getFloatProp(String name) {
		return (Float) this.prop.get(name);
	}

	public Integer getIntProp(String name) {
		return (Integer) this.prop.get(name);
	}

	public Long getLongProp(String name) {
		return (Long) this.prop.get(name);
	}

	public Short getShortProp(String name) {
		return (Short) this.prop.get(name);
	}

	public String getStringProp(String name) {
		return (String) this.prop.get(name);
	}
	
}
