package com.taojiang.memory;

public interface Memorys {
	public void initMemory(); // 初始化内存
	public int allocateMemory(int size,String proName);// 分配内存,返回可分配区域的起始地址
//	public void insertZone(int start,int size,boolean state,String proName);// 插入分区
	public void insertZone(int size,boolean state,String proName);// 插入分区
	public void recycleMemory(String proName);// 根据进程名称回收其内存
	public void merge(int p); // 合并相邻分区
	public void print();
}
