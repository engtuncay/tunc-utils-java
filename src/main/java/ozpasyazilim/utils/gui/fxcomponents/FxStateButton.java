package ozpasyazilim.utils.gui.fxcomponents;

public class FxStateButton extends FxButton {

	Integer tiState;
	Integer tiStateMod;
	Integer nextState;

	public FxStateButton() {
		setTiStateMod(2);
	}

	public FxStateButton(String text) {
		super(text);
		setTiStateMod(2);
	}

	public FxStateButton(String colFxNodeHeader, Integer countState) {
		super(colFxNodeHeader);
		tiStateMod=countState;
	}

	public Integer getTiState() {
		if(tiState==null) { tiState=0; }
		return tiState;
	}

	public void setTiState(Integer tiState) {
		this.tiState = tiState;

	}

	public Integer toggleTiState(){
		setTiState((getTiState()+1)%tiStateMod);
		setText(getTiState().toString());
		return this.tiState;
	}

	public Integer preToggleNextState(Integer tiOnayDurum) {
		if(tiOnayDurum==null)tiOnayDurum=0;
		setNextState((tiOnayDurum+1)%tiStateMod);
		//setText(getTiState().toString());
		return this.nextState;
	}

	public Integer toggleNextState(){
		setTiState(getNextState());
		//setText(getNextState().toString());
		return this.tiState;
	}

	public Integer toggleTiState(Integer tiPreState) {
		if(tiPreState==null)tiPreState=0;
		setTiState((tiPreState+1)%tiStateMod);
		//setText(getTiState().toString());
		return this.tiState;
	}

	public Integer getTiStateMod() {
		return tiStateMod;
	}

	public void setTiStateMod(Integer tiStateMod) {
		this.tiStateMod = tiStateMod;
	}

	public Integer getNextState() {
		return nextState;
	}

	public void setNextState(Integer nextState) {
		this.nextState = nextState;
	}
}
