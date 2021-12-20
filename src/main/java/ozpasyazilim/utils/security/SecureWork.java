package ozpasyazilim.utils.security;

import java.util.Set;

public class SecureWork {

	private Runnable runnableFn;
	String moduleCode;
	Set<String> setRights;

	public SecureWork(Runnable secureMethod) {
		this.runnableFn = secureMethod;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public void run(Set<String> userRights){

		if(userRights==null || getModuleCode()==null) return;

		if(userRights.contains(getModuleCode())){
			this.runnableFn.run();
		}
	}
}
