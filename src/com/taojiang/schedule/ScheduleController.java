package com.taojiang.schedule;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import com.taojiang.cpu.CPUS;
import com.taojiang.queue.Process;
import com.taojiang.queue.Queue;
import com.taojiang.tableclass.TProcess;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ScheduleController extends Thread implements Initializable{
	public static final String image_url_1 = "image/cpu_spare.png";
	public static final String image_url_2 = "image/cpu_busy.png";
	// 定义是否可以添加进程的信号量
	static boolean signal = true;
	// 定义一些调度方法的字符串常量
	private static final String FCFS = "FCFS";
	private static final String PRIORITY = "Priority";
	private static final String RR = "RR";
	private static final String DYNAMIC_PRIORITY = "Dynamic Priority";
	// 保存当前使用的调度方法，默认为FCFS
	static String scheduleMethod = FCFS;
	// 定义两个数组用于初始化TableColumn
	private String[] COLUMN_NAME = {"进程名","优先级","运行时间",
			"主存大小","起始位置","下一个进程名字"};
	private String[] COLUMN_CELL_VALUE = {"proName","proPriority","proRuntime",
			"proStoreSize","proStoreStart","proNextName"};
	// 声明四个TableView的Observablist
	private static ObservableList<TProcess> poolObList;
	private static ObservableList<TProcess> readyObList;
	private static ObservableList<TProcess> suspandObList;
	private static ObservableList<TProcess> waitObList;
	// 声明四个队列
	private static Queue poolQueue;
	private static Queue readyQueue;
	private static Queue suspandQueue;
	private static Queue waitQueue;
	// 声明两个CPU，每个CPU为两道
	static CPUS cpus;
	// 映射FXML里边的控件
	@FXML private TextField tf_proName;
	@FXML private TextField tf_proRuntime;
	@FXML private TextField tf_proPriority;
	@FXML private TextField tf_proMemNeeded;
	@FXML private Button bt_addProcess;
	@FXML private ComboBox<String> cb_selectMethod;
	@FXML private Button bt_startSchedule;
	@FXML private TableView<TProcess> table_pool;
	@FXML private TableView<TProcess> table_ready;
	@FXML private TableView<TProcess> table_suspand;
	@FXML private TableView<TProcess> table_wait;
	@FXML public ImageView iv_cpu1;
	@FXML public ImageView iv_cpu2;
	@FXML public ProgressBar pb_cpu1_1;
	@FXML public ProgressBar pb_cpu1_2;
	@FXML public ProgressBar pb_cpu2_1;
	@FXML public ProgressBar pb_cpu2_2;
	@FXML public Label lb_cpu1_1;
	@FXML public Label lb_cpu1_2;
	@FXML public Label lb_cpu2_1;
	@FXML public Label lb_cpu2_2;
/**************************************以下函数区****************************************/
	// ScheduleController初始化的函数，自动调用
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 实例化队列
		poolQueue = new Queue("后备队列");
		readyQueue = new Queue("就绪队列");
		suspandQueue = new Queue("挂起队列");
		waitQueue = new Queue("等待队列");
		// 实例化CPU
		cpus = new CPUS(2,this);
		// 实例化四个TableView的Observablist
		poolObList = FXCollections.observableArrayList();
		readyObList = FXCollections.observableArrayList();
		suspandObList = FXCollections.observableArrayList();
		waitObList = FXCollections.observableArrayList();
		initTableColumn(table_pool);
		initTableColumn(table_ready);
		initTableColumn(table_suspand);
		initTableColumn(table_wait);
		table_pool.setItems(poolObList);
		table_ready.setItems(readyObList);
		table_suspand.setItems(suspandObList);
		table_wait.setItems(waitObList);
		// 为ComboBox添加item和点击事件
		cb_selectMethod.getItems().addAll("Priority","FCFS","RR","Dynamic Priority");
		cb_selectMethod.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue == oldValue)
					return;
				switch (newValue) {
				case "FCFS":
					scheduleMethod = FCFS;
					break;
				case "Priority":
					scheduleMethod = PRIORITY;
					break;
				case "RR":
					scheduleMethod = RR;
					break;
				case "Dynamic Priority":
					scheduleMethod = DYNAMIC_PRIORITY;
					break;
				default:
					break;
				}
				System.out.println(newValue);
			}
		});

		iv_cpu1.setImage(new Image(image_url_1));
		iv_cpu2.setImage(new Image(image_url_1));
		this.start();
	}
	// 为TableView添加TableColumn
	public void initTableColumn(TableView<TProcess> table){
		for(int i = 0;i < 6;i++){
			TableColumn<TProcess, String> column= new TableColumn<TProcess, String>(COLUMN_NAME[i]);
			column.setCellValueFactory(new PropertyValueFactory<>(COLUMN_CELL_VALUE[i]));
			table.getColumns().add(column);
		}
	}
	// add process button response event
	public void addProButtonAction(){
		signal = false;
		String str1 = tf_proName.getText().toString();
		String str2 = tf_proRuntime.getText().toString();
		String str3 = tf_proPriority.getText().toString();
		String str4 = tf_proMemNeeded.getText().toString();
		if(str1.equals("")||str2.equals("")||str3.equals("")||str4.equals("")){
			System.out.println("system$: " + "添加失败，填写完整进程信息");
			return;
		}
		TProcess pro = new TProcess(str1,str3,str2,str4,"0","");
		if(readyQueue.length() < 8){
			// 就绪队列未满，直接添加到就绪队列
			addProcess(pro,readyQueue,readyObList);
		}else{
			addProcess(pro,poolQueue,poolObList);
		}
//		System.out.println("readyQueue信息如下：");
//		readyQueue.print();
//		System.out.println("readyObList信息如下:");
//		for (int i = 0; i < readyObList.size(); i++) {
//			System.out.println(readyObList.get(i).getProName());
//		}
		tf_proName.clear();
		tf_proRuntime.clear();
		tf_proPriority.clear();
		tf_proMemNeeded.clear();
		signal = true;
	}
	// 向队列添加进程,并显示在界面上
	private void addProcess(TProcess pro, Queue queue, ObservableList<TProcess> list) {
		queue.insertProcess(pro);
		list.add(pro);
	}
	// 从队列移除进程，并从界面上移除
	private Process removeProcess(Queue queue,ObservableList<TProcess> list,int index){
		list.remove(index);
		Process process = queue.remove(index);
//		System.out.println("readyQueue信息如下：");
//		readyQueue.print();
//		System.out.println("readyObList信息如下:");
//		for (int i = 0; i < readyObList.size(); i++) {
//			System.out.println(readyObList.get(i).getProName());
//		}
		return process;
	}
	// start schedule button response event
	public void startScheduleButtonClicked(){
//		this.start();
	}

	// 调度算法
	public synchronized void run() {
		while(true){
			// 将后备队列进程调度到就绪队列
			Thread.currentThread().notify();
			while(readyQueue.length() < 8 && poolQueue.length() > 0){
				Process process;
				process = removeProcess(poolQueue, poolObList, 0);
				if(process != null){
					TProcess tProcess = new TProcess(process.getProName(),
							""+process.getProPriority(),
							""+process.getProRuntime(),
							""+process.getProStoreSize(),
							""+process.getProStoreStart(),
							process.getProPointed()!=null?
									process.getProPointed().getProName():"");
					addProcess(tProcess, readyQueue, readyObList);
				}
			}
//			if(readyQueue.length() != 0 ||poolQueue.length() != 0){
//				System.out.println(readyQueue.length()+" "+poolQueue.length()+" "+cpus.isSpare());
//			}
			// 从就绪队列中选择进程，给CPU调度
			while(readyQueue.length() > 0 && cpus.isSpare()){
				// 优先级调度算法
				if(scheduleMethod == PRIORITY){
					// 队列排序
					readyQueue.sort();
					// 将ObservableList排序
					readyObList.sort(compare);
				}
				while(!signal){
					// 表示当前是否可以进行remove操作
					System.out.println("*********************************************");
				}
				// 将排序后的队首进程从就绪队列删除
				Process process = removeProcess(readyQueue, readyObList, 0);
				// 将该进程交由CPU处理
				cpus.work(process);
			}
		}

	}

	Comparator<TProcess> compare = new Comparator<TProcess>() {

		@Override
		public int compare(TProcess o1, TProcess o2) {
			if(Integer.parseInt(o1.getProPriority().getValue())
					>Integer.parseInt(o2.getProPriority().getValue()))
				return 1;
			else if(Integer.parseInt(o1.getProPriority().getValue())
					<Integer.parseInt(o2.getProPriority().getValue()))
				return -1;
			else
				return 0;
		}
	};
}