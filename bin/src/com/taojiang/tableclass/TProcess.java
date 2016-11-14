package com.taojiang.tableclass;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TProcess {
	private StringProperty proName;
	private StringProperty proPriority;
	private StringProperty proRuntime;
	private StringProperty proStoreSize;
	private StringProperty proStoreStart;
	private StringProperty proNextName;
	public TProcess(String proName,String proPriority,String proRuntime,
			String proStoreSize,String proStoreStart,String proNextName){

		this.proName = new SimpleStringProperty(proName);
		this.proPriority = new SimpleStringProperty(proPriority);
		this.proRuntime = new SimpleStringProperty(proRuntime);
		this.proStoreSize = new SimpleStringProperty(proStoreSize);
		this.proStoreStart = new SimpleStringProperty(proStoreStart);
		this.proNextName = new SimpleStringProperty(proNextName);

	}


	private String formatState(String proState) {
		// TODO Auto-generated method stub
		String state = "";
		switch (proState) {
		case "0":
			state = "就绪";
			break;
		case "1":
			state = "运行";
			break;
		case "2":
			state = "等待";
			break;
		case "3":
			state = "终止";
			break;
		case "4":
			state = "挂起";
			break;
		default:
			break;
		}
		System.out.println(state);
		return state;
	}


	public StringProperty getProName() {
		return proName;
	}

	public void setProName(StringProperty proName) {
		this.proName = proName;
	}

	public StringProperty getProRuntime() {
		return proRuntime;
	}

	public void setProRuntime(StringProperty proRuntime) {
		this.proRuntime = proRuntime;
	}

	public StringProperty getProPriority() {
		return proPriority;
	}

	public void setProPriority(StringProperty proPriority) {
		this.proPriority = proPriority;
	}

	public StringProperty getProStoreSize() {
		return proStoreSize;
	}

	public void setProStoreSize(StringProperty proStoreSize) {
		this.proStoreSize = proStoreSize;
	}

	public StringProperty getProStoreStart() {
		return proStoreStart;
	}

	public void setProStoreStart(StringProperty proStoreStart) {
		this.proStoreStart = proStoreStart;
	}

	public StringProperty getProNextName() {
		return proNextName;
	}

	public void setProNextName(StringProperty proNextName) {
		this.proNextName = proNextName;
	}
	public StringProperty proNameProperty(){
		if(proName==null)
			proName = new SimpleStringProperty(this,"proName");
		return 	proName;
	}
	public StringProperty proRuntimeProperty(){
		if(proRuntime==null)
			proRuntime = new SimpleStringProperty(this,"proRuntime");
		return 	proRuntime;
	}
	public StringProperty proPriorityProperty(){
		if(proPriority==null)
			proPriority = new SimpleStringProperty(this,"proPriority");
		return 	proPriority;
	}

	public StringProperty proStoreSizeProperty(){
		if(proStoreSize==null)
			proStoreSize = new SimpleStringProperty(this,"proStoreSize");
		return 	proStoreSize;
	}

	public StringProperty proStoreStartProperty(){
		if(proStoreStart==null)
			proStoreStart = new SimpleStringProperty(this,"proStoreStart");
		return 	proStoreStart;
	}

	public StringProperty proNextNameProperty(){
		if(proNextName==null)
			proNextName = new SimpleStringProperty(this,"proNextName");
		return 	proNextName;
	}
}