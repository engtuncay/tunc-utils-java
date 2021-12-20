package ozpasyazilim.utils.gui.components;

public class Columndef {
	
	String columnname;
	Integer columnsize;
	
	public Columndef() {
		
	}
	
	public Columndef(String colname, Integer colsize) {
		this.columnname=colname;
		this.columnsize=colsize;
	}
	
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	public Integer getColumnsize() {
		return columnsize;
	}
	public void setColumnsize(Integer columnsize) {
		this.columnsize = columnsize;
	}
	
	

}
