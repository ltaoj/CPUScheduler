package com.taojiang.cpu;

import com.taojiang.queue.Process;
import com.taojiang.schedule.ScheduleController;

public class CPUS {

	public final int CPU_STATE_SPARE = 0;  // cpu空闲
	public final int CPU_STATE_WORK = 1;   // cpu工作

	public int cpuCount;                          // cpu数目
	public CPU[] cpus;                            // 保存每个CPU
	private ScheduleController scheduleController;

	public CPUS(int cpuCount,ScheduleController scheduleController){

		this.cpuCount = cpuCount;

		cpus = new CPU[cpuCount];

		// 初始化各个cpu
		for(int i = 0;i < cpus.length;i++){

			cpus[i] = new CPU("cpu"+(i+1),scheduleController);

		}
		this.scheduleController = scheduleController;
	}

	public void setCPUCount(int cpuCount){
		this.cpuCount = cpuCount;

		cpus = new CPU[cpuCount];

		// 初始化各个cpu
		for(int i = 0;i < cpus.length;i++){

			cpus[i] = new CPU("cpu"+(i+1),scheduleController);

		}
	}
	// 打印各个cpu的信息
	public void print(){

		for(int i = 0;i < cpus.length;i++){

			System.out.println(cpus[i].toString());

		}
	}
	// 返回cpu的个数
	public int count(){

		return cpuCount;
	}
	// 判断是否有空闲的cpu
	public boolean isSpare(){

		for(int i = 0;i < count();i++){

			if(cpus[i].state == CPU_STATE_SPARE)

				return true;
		}

		return false;
	}

	public void work(Process process) {
		// TODO Auto-generated method stub
		for(int i = 0;i < count();i++){

			if(cpus[i].state == CPU_STATE_SPARE){
				cpus[i].setProcess(process);
				break;

			}
		}
	}
}