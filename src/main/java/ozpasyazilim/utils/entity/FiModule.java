package ozpasyazilim.utils.entity;

import ozpasyazilim.utils.core.FiString;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

public class FiModule {
	String txModuleCode;

	/**
	 * module label - menüde kullanılıyor
	 */
	String txModuleLabel;
	String txModuleName;
	String txModuleDesc;
	Class clazz;
	Boolean boDisabled;
	List<Class> listModules;

	/**
	 * IsNonValidate , Dogrulama (Erişim hakkı) gerekmeyen moduller.
	 */
	Boolean boIsNonValidate;

	@Transient
	FiModule upperModule;

	@Column(length = 20)
	String txShortName;

	@Deprecated
	String txModuleDesc2;

	//kullanıcılar modülünde tabloda enable disable için kullanılıyor
	@Transient
	Boolean boEnabled;

	/**
	 * Uuid
	 */
	String txUid;

	public FiModule() {
	}

	public FiModule(String txModuleCode, String txModuleLabel) {
		setTxModuleCode(txModuleCode);
		setTxModuleLabel(txModuleLabel);
		setTxModuleDesc(txModuleLabel);
	}

	public FiModule(String txCode, FiModule fiModuleUpper, String txLabel) {
		setTxModuleCode(txCode);
		setTxModuleLabel(txLabel);
		setUpperModule(fiModuleUpper);
	}

	public FiModule(String txModuleCode, String txModuleLabel, String txModuleDesc, String txModuleName) {
		this.setTxModuleCode(txModuleCode);
		this.setTxModuleLabel(txModuleLabel);
		this.setTxModuleDesc(txModuleDesc);
		this.setTxModuleName(txModuleName);
	}

	public FiModule(String txModuleCode, String txModuleLabel, String txModuleName) {
		this.setTxModuleCode(txModuleCode);
		this.setTxModuleLabel(txModuleLabel);
		this.setTxModuleName(txModuleName);
	}

	public FiModule(String txModuleCode, String txModuleLabel, Class classModule) {
		this.setTxModuleCode(txModuleCode);
		this.setTxModuleLabel(txModuleLabel);
		this.setTxModuleName(classModule.getSimpleName());
		//this.clazz = classModule;
	}

	public FiModule buiTxLabel(String txLabel) {
		setTxModuleLabel(txLabel);
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FiModule) {
			FiModule module = (FiModule) obj;

			// boş veya null ise de false olur
			if (FiString.isEmptyTrim(getTxModuleCode()) || FiString.isEmptyTrim(module.getTxModuleCode())) {
				return false;
			}

			if (getTxModuleCode().equals(module.getTxModuleCode())) {
				return true;
			}
		}

		if (obj instanceof String) {
			if (FiString.isEmptyTrim(getTxModuleCode()) || FiString.isEmptyTrim((String) obj)) {
				return false;
			}

			if (getTxModuleCode().equals(obj)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return txModuleCode;
	}

	public String getTxModuleCode() {
		return txModuleCode;
	}

	public void setTxModuleCode(String txModuleCode) {
		this.txModuleCode = txModuleCode;
	}

	public String getTxModuleLabel() {
		return txModuleLabel;
	}

	public void setTxModuleLabel(String txModuleLabel) {
		this.txModuleLabel = txModuleLabel;
	}

	public String getTxModuleDesc() {
		return txModuleDesc;
	}

	public void setTxModuleDesc(String txModuleDesc) {
		this.txModuleDesc = txModuleDesc;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Boolean getBoIsNonValidate() {
		return boIsNonValidate;
	}

	public void setBoIsNonValidate(Boolean boIsNonValidate) {
		this.boIsNonValidate = boIsNonValidate;
	}

	public FiModule getUpperModule() {
		return upperModule;
	}

	public void setUpperModule(FiModule upperModule) {
		this.upperModule = upperModule;
		setTxModuleName(upperModule.getTxModuleName());
	}

	public String getTxModuleDesc2() {
		return txModuleDesc2;
	}

	public void setTxModuleDesc2(String txModuleDesc2) {
		this.txModuleDesc2 = txModuleDesc2;
	}

	public String getTxShortName() {
		return txShortName;
	}

	public void setTxShortName(String txShortName) {
		this.txShortName = txShortName;
	}

	public Boolean getBoEnabled() {
		return boEnabled;
	}

	public void setBoEnabled(Boolean boEnabled) {
		this.boEnabled = boEnabled;
	}

	public String getTxModuleName() {
		return txModuleName;
	}

	public void setTxModuleName(String txModuleName) {
		this.txModuleName = txModuleName;
	}

	public Boolean getBoDisabled() {
		return boDisabled;
	}

	public void setBoDisabled(Boolean disabledModule) {
		boDisabled = disabledModule;
	}

	public String getTxUid() {
		return txUid;
	}

	public void setTxUid(String txUid) {
		this.txUid = txUid;
	}

	public List<Class> getListModules() {
		return listModules;
	}

	public List<Class> getListModulesInit() {
		if (listModules == null) {
			listModules = new ArrayList<>();
		}
		return listModules;
	}

	public void setListModules(List<Class> listModules) {
		this.listModules = listModules;
	}
}
