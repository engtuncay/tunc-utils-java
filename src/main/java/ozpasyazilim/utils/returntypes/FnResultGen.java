package ozpasyazilim.utils.returntypes;

public class FnResultGen {

    String resultcode;
    Boolean bresult;
    String smessage;
    Object object;
    String sType;
    FnResultStatus resultStatus = FnResultStatus.NULL;
    Boolean hatavar=false;
    Boolean islembasarili=false;
    Boolean islemyapildi=false;

    public FnResultGen(Boolean bpResult, String sMessage) {
	this.bresult = bpResult;
	this.smessage = sMessage;
    }

    public FnResultGen() {
    }

    public FnResultGen buildResult(Boolean bresult){
        this.setBResult(bresult);
        return this;
    }

    public FnResultGen buildMessage(String smessage){
        this.setMessage(smessage);
        return this;
    }

    public FnResultGen(String string) {
	this.smessage = string;
    }

    public FnResultGen(boolean bpResult) {
	this.bresult = bpResult;
    }

    public FnResultGen(String string, String type) {
	this.smessage = string;
	this.sType = type;
    }

    public String getSMessage() {
	return smessage;
    }

    public void setMessage(String messagedetail) {
	this.smessage = messagedetail;
    }

    public void appendMessage(String messagetoAppend) {
	if (smessage == null) {
	    smessage = "";
	}

	this.smessage += messagetoAppend;
    }

    public void appendlnMessage(String messagetoAppend) {
	if (smessage == null) {
	    smessage = "";
	}

	this.smessage += messagetoAppend + "\n";
    }


    public Boolean getBResult() {
	return bresult;
    }

    public void setBResult(Boolean boolResult) {
	this.bresult = boolResult;
    }

    public String getResultcode() {
	return resultcode;
    }

    public void setResultcode(String resultcode) {
	this.resultcode = resultcode;
    }

    public Object getObject() {
	return object;
    }

    public void setObject(Object object) {
	this.object = object;
    }

    public String getsType() {
	return sType;
    }

    public void setsType(String sType) {
	this.sType = sType;
    }

    public FnResultStatus getResultStatus() {
	return resultStatus;
    }

    public void setResultStatus(FnResultStatus resultStatus) {
	this.resultStatus = resultStatus;
    }

    public Boolean getHatavar() {
	return hatavar;
    }

    public void setHatavar(Boolean hatavar) {
	this.hatavar = hatavar;
	this.islemyapildi=true;
    }

    public Boolean getIslembasarili() {
	return islembasarili;
    }

    public void setIslembasarili(Boolean islembasarili) {
	this.islembasarili = islembasarili;
	this.islemyapildi=true;
    }

    public void finalizeStatus() {

	if(!islemyapildi) this.resultStatus= FnResultStatus.NULL;
	if(islembasarili && !hatavar) this.resultStatus = FnResultStatus.SUCCESS;
	if(islembasarili && hatavar) this.resultStatus = FnResultStatus.PARTIALSUCCESS;
	if(!islembasarili && hatavar) this.resultStatus = FnResultStatus.FAIL;

    }

    public FnResultGen wrapFinalizeStatus() {
	finalizeStatus();	
	return this;
    }


}
