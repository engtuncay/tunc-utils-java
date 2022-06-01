package ozpasyazilim.utils.core;

public class FiHtmlReportConfig {

	Boolean boSummaryEnabled;
	Integer lnDecimalScale;

	public Boolean getBoSummaryEnabled() {
		return boSummaryEnabled;
	}

	public void setBoSummaryEnabled(Boolean boSummaryEnabled) {
		this.boSummaryEnabled = boSummaryEnabled;
	}

	public Integer getLnDecimalScale() {
		if(lnDecimalScale ==null) return 2;
		return lnDecimalScale;
	}

	public void setLnDecimalScale(Integer lnDecimalScale) {
		this.lnDecimalScale = lnDecimalScale;
	}
}
