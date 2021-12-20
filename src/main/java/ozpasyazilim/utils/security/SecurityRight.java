package ozpasyazilim.utils.security;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ozpasyazilim.utils.gui.fxcomponents.IFxSecureNode;

import java.util.HashSet;
import java.util.Set;

public class SecurityRight {

	// güncelleme yapılmayacak ilk oluşturulduğunda olacak , property olmasına gerek yok.
	private Set<String> userRights;
	private String profileCode;

	public SecurityRight() {

	}

	public SecurityRight(Set<String> setUserRights, String workspace) {
		setUserRights(setUserRights);
		setProfileCode(workspace);
	}

	public Set<String> getUserRights() {
		if (userRights == null) {
			userRights= new HashSet<>();
		}
		return userRights;
	}

	private void setUserRights(Set<String> userRights) {
		this.userRights = userRights;
	}

	public String getProfileCode() {
		return profileCode;
	}

	private void setProfileCode(String profileCode) {
		this.profileCode = profileCode;
	}

	public static Boolean checkSecurityRight(Set<String> setUserRights, String moduleCode,IFxSecureNode iFxSecureNode){

		if(setUserRights.contains(moduleCode)){
			iFxSecureNode.setNodeDisabledBySecurity(false);
			return true;
		}

		iFxSecureNode.setNodeDisabledBySecurity(true);
		return false;
	}

	public static Boolean checkSecurityRight(SecurityRight securityRight, String moduleCode, IFxSecureNode iFxSecureNode){

		if(securityRight.getUserRights().contains(moduleCode)){
			iFxSecureNode.setNodeDisabledBySecurity(false);
			return true;
		}

		iFxSecureNode.setNodeDisabledBySecurity(true);
		return false;
	}

	public static Boolean checkSecurityRight(SecurityRight securityRight, IFxSecureNode iFxSecureNode){

		if(securityRight.getUserRights().contains(iFxSecureNode.getModuleCode())){
			iFxSecureNode.setNodeDisabledBySecurity(false);
			return true;
		}

		iFxSecureNode.setNodeDisabledBySecurity(true);
		return false;
	}

	public static Boolean checkSecurityRight(SecurityRight securityRight, String profileCode, String moduleCode,IFxSecureNode iFxSecureNode){

		if(securityRight.getProfileCode().equals(profileCode) && securityRight.getUserRights().contains(moduleCode)){
			iFxSecureNode.setNodeDisabledBySecurity(false);
			return true;
		}
		iFxSecureNode.setNodeDisabledBySecurity(true);
		return false;
	}


}


//	public void makeSecureButton(ObjectProperty<SecurityRight> securityRightProperty, String moduleCode) {
//		setModuleCode(moduleCode);
//		makeSecureButton(securityRightProperty);
//	}
//
//	public void makeSecureButton(ObjectProperty<SecurityRight> securityRightProperty) {
//
//		SecurityRight.checkSecurityRight(securityRightProperty.get(), getModuleCode(), this);
//
//		securityRightProperty.addListener((observable, oldValue, newValue) -> {
//			SecurityRight.checkSecurityRight(newValue, getModuleCode(), this);
//		});
//
////		securityRightProperty.get().userRightsProperty().addListener((observable, oldValue, newValue) -> {
////			SecurityRight.checkSecurityRight(newValue, getModuleCode(), this);
////		});
//
//	}
