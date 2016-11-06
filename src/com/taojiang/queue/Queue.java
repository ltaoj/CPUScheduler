package com.taojiang.queue;

import com.taojiang.tableclass.TProcess;

public class Queue implements Queues{

	public Process first;           // 队首,指向队列root进程
	public Process pointer;         // 指针，可以指向任意进程
	String queueType;               // 队列名称，比如"就绪队列"
	int length;
	// 队列构造函数
	public Queue(String queueType){
		first = new Process();
		pointer = null;
		this.queueType = queueType;
		length = 0;
	}

	public void insertProcess(TProcess tProcess){
		insertProcess(tProcess.getProName().getValue(),
				tProcess.getProRuntime().getValue(),
				tProcess.getProPriority().getValue(),
				tProcess.getProStoreSize().getValue());
	}

	@Override
	public void insertProcess(String pid,String runtime,String priority,String storeSize) {

		Process newProcess;

		Process currentProcess;
		newProcess = new Process(pid,Integer.parseInt(runtime),
				Integer.parseInt(priority),Integer.parseInt(storeSize));

		if(length == 0){// 第一个节点为空，链表无元素

			first = newProcess;// 新节点复制给第一个节点
			first.setProPointed(new Process());// 设置新的尾部
			length++;

		}else {

			currentProcess = first;

			while(!currentProcess.getProPointed().getProName().equals("last")){// 移动到队尾

				currentProcess = currentProcess.getProPointed();
			}

			currentProcess.setProPointed(newProcess);
			currentProcess.getProPointed().setProPointed(new Process());
			length++;
		}
	}

	// 按优先级排序队列,升序
	@Override
	public void sort() {
		//　单向链表比较适合选择排序
		String proName;
		int proRuntime;
		int proPriority;
		int proState;

		Process current;
		Process next;

		for(current = first; !current.getProName().equals("last");current = current.getProPointed()){
			proName = current.getProName();
			proRuntime = current.getProRuntime();
			proPriority = current.getProPriority();
			proState = current.getProState();
			pointer = current;
			for(next = current; !next.getProName().equals("last"); next = next.getProPointed()){
				if(proPriority > next.getProPriority()){
					proName = next.getProName();
					proRuntime = next.getProRuntime();
					proPriority = next.getProPriority();
					proState = next	.getProState();
					pointer = next;

				}
			}
			pointer.setProName(current.getProName());
			pointer.setProRuntime(current.getProRuntime());
			pointer.setProPriority(current.getProPriority());
			pointer.setProState(current.getProState());

			current.setProName(proName);
			current.setProRuntime(proRuntime);
			current.setProPriority(proPriority);
			current.setProState(proState);

		}
	}
	// 判断队列是否为空
	@Override
	public boolean isEmpty() {

		return length() == 0;
	}

	// 当进程结束时，从就绪队列中移除
	// 或当进程状态变为等待时，需要从就绪队列中移除
	// 返回被删除的元素,可能返回null
	@Override
	public Process remove() {
		// TODO Auto-generated method stub
		Process process;
		process = first;
		// 队列为空
		if(process.getProName().equals("last"))
			return null;
		// 队列root节点后移，删除第一个进程
		first = first.getProPointed();
		length--;
		return process;
	}

	@Override
	public int length() {

		return length;
	}

	@Override
	public Process remove(int index) {
		// TODO Auto-generated method stub
		if(index < 0 || index >= length){
			throw new ArrayIndexOutOfBoundsException(index);
		}
		Process process = null;
		Process current;
		int target = 0;
		for(current = first; !current.getProName().equals("last");current = current.getProPointed()){
			if(index == 0){
				first = first.getProPointed();
				break;
			}
			if(target == index-1){
				process = current.getProPointed();
				current.setProPointed(current.getProPointed().getProPointed());
				length--;
				return process;
			}
			target++;

		}
		length--;
		return current;
	}
	@Override
	public void print() {

		Process current;

		System.out.println(queueType + "队列如下");

		if(first != null){

			current = (Process) first.clone();

		}else {

			System.out.println("当前无进程");
			return;
		}

		while(current != null){
			System.out.println(current.toString());
			current = current.getProPointed();
		}
		System.out.println("队列进程数 = " + length());
	}
}
