package com.dice.model;

/**
 * Created by IntelliJ IDEA.
 * User: duket
 * Date: 4/6/12
 * Time: 7:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class Search {
	private String t = "";
	private String z = "";
	private String f = "";
	private int d = 5;
	private int o = 0;

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getZ() {
		return z;
	}

	public void setZ(String z) {
		this.z = z;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public int getO() {
		return o;
	}

	public void setO(int o) {
		this.o = o;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("t="); buf.append(getT());
		buf.append('&'); buf.append("z="); buf.append(getZ());
		buf.append('&'); buf.append("d="); buf.append(getD());
		buf.append('&'); buf.append("o="); buf.append(getO());
		return buf.toString();
	}
}
