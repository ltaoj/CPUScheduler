package com.taojiang.memory;

public class Zone {


	public static final boolean STATE_USE = true;
	public static final boolean STATE_SPARE = false;
	private int start;
	private int size;
	private boolean state;
	private String proName;

	public Zone(){}
	public Zone(int start,int size,boolean state,String proName){
		this.start = start;
		this.size = size;
		this.state = state;
		this.proName = proName;
	}

	public Zone(int size, String proName) {
		this.size = size;
		this.proName = proName;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public void print(){
		System.out.println("start=" + start+" size="+size + " state="+state+" proname="+proName);
	}
}
