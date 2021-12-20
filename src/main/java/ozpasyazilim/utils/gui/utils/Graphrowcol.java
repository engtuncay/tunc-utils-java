package ozpasyazilim.utils.gui.utils;

public class Graphrowcol {

	private Integer x = 0;
	private Integer y = 0;
	private Integer currposx;
	private Integer currposy;
	private Integer lastrowx = 0;
	private Integer lastrowy = 0;
	private Integer boxwidth = 0;
	private Integer boxheight = 0;
	private Integer gapy = 0;
	private Integer gapx = 0;

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public void setLocation(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	public void setCurrposy(Integer currposy) {
		this.currposy = currposy;
	}

	public Integer getLastrowx() {
		return lastrowx;
	}

	public void setLastrowx(Integer lastrowx) {
		this.lastrowx = lastrowx;
	}

	public Integer getBoxwidth() {
		return boxwidth;
	}

	public void setBoxwidth(Integer boxwidth) {
		this.boxwidth = boxwidth;
	}

	public Integer getBoxheight() {
		return boxheight;
	}

	public void setBoxheight(Integer boxheight) {
		this.boxheight = boxheight;
	}

	public Integer setGapy() {
		return gapy;
	}

	public void setGapy(Integer starty) {
		this.gapy = starty;
	}

	public Integer getGapx() {
		return gapx;
	}

	public void setGapx(Integer startx) {
		this.gapx = startx;
	}

	public Integer getCurrposx() {
		return lastrowx + (this.x * boxwidth) - boxwidth + gapx;
	}

	public Integer getCurrposy() {
		return lastrowy + (this.y * boxheight) - boxheight + gapy;
	}

	public Integer getLastrowy() {
		return lastrowy;
	}

	public void setLastrowy(Integer lastrowy) {
		this.lastrowy = lastrowy;
	}

}
