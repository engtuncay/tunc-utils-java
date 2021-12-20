package ozpasyazilim.utils.core;

public class FiConsoleConfig {

	private Boolean boShowNull;

	// eşittir işaretini çıktıya koymaz
	private Boolean boEqualSignHide;

	// Alanların tiplerini göstermez
	private Boolean boTypeHide;

	private Boolean boFieldNameHide;



	public Boolean getBoShowNull() {
		return boShowNull;
	}

	public void setBoShowNull(Boolean boShowNull) {
		this.boShowNull = boShowNull;
	}

	public Boolean getBoEqualSignHide() {
		return boEqualSignHide;
	}

	public void setBoEqualSignHide(Boolean boEqualSignHide) {
		this.boEqualSignHide = boEqualSignHide;
	}

	public Boolean getBoTypeHide() {
		return boTypeHide;
	}

	public void setBoTypeHide(Boolean boTypeHide) {
		this.boTypeHide = boTypeHide;
	}

	public Boolean getBoFieldNameHide() {
		return boFieldNameHide;
	}

	public void setBoFieldNameHide(Boolean boFieldNameHide) {
		this.boFieldNameHide = boFieldNameHide;
	}
}
