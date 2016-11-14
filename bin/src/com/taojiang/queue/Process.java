package com.taojiang.queue;
public class Process implements Cloneable{
	// 进程的状态
	public static final int STATE_READY = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_WAITING = 2;
	public static final int STATE_TERMINAL = 3;
	public static final int STATE_SUSPAND = 4;

	// 定义PCB的结构
	private String m_proName;
	private int m_proRuntime;
	private int m_proPriority;
	private int m_proState;
	private int m_proStoreSize;
	private int m_proStoreStart;
	private Process m_proPointed;

	public Process(){
		m_proName = "last";
	}
	// 进程构造方法
	public Process(String m_proName, int m_proRuntime, int m_proPriority,
			int m_proStoreSize) {
		this.m_proName = m_proName;
		this.m_proRuntime = m_proRuntime;
		this.m_proPriority = m_proPriority;
		this.m_proStoreSize = m_proStoreSize;
		// 初始化为逻辑地址
		this.m_proStoreStart = 0;
		this.m_proState = 0;
		this.m_proPointed = null;
	}
	// 得到进程名称/PID
	public String getProName(){
		return m_proName;
	}
	// 设置进程名称
	public void setProName(String m_proName){
		this.m_proName = m_proName;
	}
	// 设置进程运行时间
	public void setProRuntime(int m_proRuntime){
		this.m_proRuntime = m_proRuntime;
	}
	// 得到进程运行时间
	public int getProRuntime(){
		return m_proRuntime;
	}
	// 设置进程优先级
	public void setProPriority(int m_proPriority){
		this.m_proPriority = m_proPriority;
	}
	// 得到进程优先级
	public int getProPriority(){
		return m_proPriority;
	}
	// 设置进程状态
	public void setProState(int m_proState){
		this.m_proState = m_proState;
	}
	// 得到所需主存大小
	public int getProStoreSize() {
		return m_proStoreSize;
	}
	// 设置所需主存大小
	public void setProStoreSize(int m_proStoreSize) {
		this.m_proStoreSize = m_proStoreSize;
	}
	// 得到主存开始位置
	public int getProStoreStart() {
		return m_proStoreStart;
	}
	// 设置主存开始位置
	public void setProStoreStart(int m_proStoreStart) {
		this.m_proStoreStart = m_proStoreStart;
	}
	// 得到进程状态
	public int getProState(){
		return m_proState;
	}
	// 设置队列中进程所指的进程
	public void setProPointed(Process m_proPointed){
		this.m_proPointed = m_proPointed;
	}
	// 得到进程指针所指的进程
	public Process getProPointed(){
		return m_proPointed;
	}

	// 输出当前进程信息
	public String toString(){
		String proInfo = "";
		proInfo += "进程名称/UID = "+getProName() + ", 剩余运行时间  = " + getProRuntime();
		proInfo += ", 优先级 = " + getProPriority() + ", 状态 = " + formatState(getProState());
		proInfo += ", 下一个进程 ";

		if(getProPointed() == null)
			proInfo += "无下一个进程";
		else
			proInfo += getProPointed().getProName();

		return proInfo;
	}
	// 将int值输出对应的字符串
	public static String formatState(int proState) {
		// TODO Auto-generated method stub
		String state = "";
		switch (proState) {
		case STATE_READY:
			state = "就绪";
			break;
		case STATE_RUNNING:
			state = "运行";
			break;
		case STATE_WAITING:
			state = "等待";
			break;
		case STATE_SUSPAND:
			state = "挂起";
			break;
		case STATE_TERMINAL:
			state = "终止";
			break;
		default:
			break;
		}
		return state;
	}

	// 当队列只有一个进程时，并不会拷贝而只是引用
	// 当队列有至少两个进程时，最后一个进程并不会拷贝，而是引用
	public Object clone(){

		Process process = null;

		// 如果队列中只有一个元素的情况,直接引用
		if(this.getProPointed() == null){

			process = this;

			return process;


		}
		// 拷贝基本数据类型
		try {

			process = (Process) super.clone();

		} catch (CloneNotSupportedException  e) {

			e.printStackTrace();

		}

		// 根据需要拷贝Process指针
		if(this.getProPointed() != null && this.getProPointed().getProPointed() ==null)

			;    // 不深层拷贝

		else{

			if(this.getProPointed()!=null)// 此判断条件表示,当前递归拷贝到队列倒数第二个元素时，Process指针不执行拷贝

				process.m_proPointed = (Process) this.getProPointed().clone();

		}
		return process;

	}
}