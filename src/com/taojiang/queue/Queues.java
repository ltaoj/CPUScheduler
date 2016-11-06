package com.taojiang.queue;
public interface Queues {

	public 	void insertProcess(String pid, String runtime, String priority, String storeSize);
	                                     // 插入新进程到链表合适位置

	public void sort();                  // 按优先级排序

	public boolean isEmpty();            // 判断链表是否为空

	public Process remove();             // 删除

	public Process remove(int index);    // 删除指定下标的进程

//	public Process find(String proName); // 返回指定名称的进程

	public int length();                 // 链表的长度

	void print();

//	public void print();                 // 打印进程信息

//	public void RR(int timeslice);       // 时间片轮转调度

//	public void priority();              // 优先权调度

}
