package com.taojiang.memory;

// 采用数组实现内存的连接
public class Memory implements Memorys{
	public static final int ZONE_SIZE = 20;

	int maxSize = 300;
	public Zone zones[];
	int current = 0;

	@Override
	public void initMemory(){
		zones = new Zone[ZONE_SIZE + 1];
		zones[0] = new Zone(0, 20, Zone.STATE_USE, "system");
		zones[1] = new Zone();
		zones[1].setStart(20);
		zones[1].setSize(maxSize-20);
	}

	@Override
	public int allocateMemory(int size, String proName) {
		int start = -1;
		for(int i = 1;i < zones.length;i++){
			if(zones[i] != null && zones[i].isState() == false && zones[i].getSize() >= size){
				start = zones[i].getStart();
				break;
			}
		}
		return start;
	}
	@Override
	public void insertZone(int size, boolean state, String proName) {
		// TODO Auto-generated method stub
		int start = allocateMemory(size,proName);
		for(int i = 1;i < zones.length;i++){
			if(zones[i].getStart() == start){
				int j = zones.length - 1;
				while(zones[j]==null)j--;
				j++;
				for(;j>i;j--){
					if(j-i!=1){
						zones[j] = new Zone();
						zones[j].setStart(zones[j-1].getStart());
						zones[j].setSize(zones[j-1].getSize());
						zones[j].setState(zones[j-1].isState());
						zones[j].setProName(zones[j-1].getProName());
					}else{
						zones[j] = new Zone();
						zones[j].setStart(zones[j-1].getStart() + size);
						zones[j].setSize(zones[j-1].getSize() - size);
						zones[j].setState(zones[j-1].isState());
						zones[j].setProName(zones[j-1].getProName());
					}
				}
				zones[i].setStart(start);
				zones[i].setSize(size);
				zones[i].setState(state);
				zones[i].setProName(proName);
				break;
			}
		}
	}
	@Override
	public void recycleMemory(String proName) {
		// TODO Auto-generated method stub
		int address = getAddress(proName);
//		int size
		if(address!=-1){
			for(int i = 1;i < zones.length;i++){
				if(zones[i].getStart() == address){
					// 回收内存
					zones[i].setState(Zone.STATE_SPARE);
					zones[i].setProName(null);
					// 合并相邻内存,向前合并
					merge(i);// 合并左侧
					if(zones[i].isState() == Zone.STATE_SPARE)merge(i);// 合并右侧
					break;
				}
			}
		}
	}
	@Override
	public void merge(int p) {
		if(zones[p-1].isState() == Zone.STATE_SPARE){
			// 合并内存
			zones[p-1].setSize(zones[p-1].getSize() + zones[p].getSize());
			int i = p;
			while(zones[i+1]!=null){
				zones[i].setStart(zones[i+1].getStart());
				zones[i].setSize(zones[i+1].getSize());
				zones[i].setState(zones[i+1].isState());
				zones[i].setProName(zones[i+1].getProName());
				i++;
			}
			zones[i] = null;
		}
	}
	private int getAddress(String proName){
		int start = -1;
		for(int i = 1;i < zones.length;i++){
			if(zones[i].isState() == Zone.STATE_USE && zones[i].getProName().equals(proName)){
				start = zones[i].getStart();
				break;
			}
		}
		return start;
	}
	@Override
	public void print() {
		// TODO Auto-generated method stub
		int i = 0;
		while(zones[i++]!=null){
			zones[i-1].print();
		}
	}

	public static void main(String[] args){
		Memory m = new Memory();
		m.initMemory();
		m.insertZone(35, Zone.STATE_SPARE, "p1");
		m.insertZone(40, Zone.STATE_USE, "p2");
		m.insertZone(30, Zone.STATE_USE, "p3");
		m.insertZone(10, Zone.STATE_USE, "p4");
		m.recycleMemory("p2");
		m.recycleMemory("p4");
		m.print();
	}
}
