package ozpasyazilim.utils.security;

import java.util.Set;

public class SecureIdentity {

	String moduleCode;

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public Boolean checkSecurity(Set<String> setUserRights){

		if(setUserRights==null || getModuleCode()==null) return false;

		if(setUserRights.contains(getModuleCode())){
			return true;
		}

		return false;
	}


}
