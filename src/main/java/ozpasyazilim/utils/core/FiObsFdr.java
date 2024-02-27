package ozpasyazilim.utils.core;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import ozpasyazilim.utils.returntypes.Fdr;

import java.util.ArrayList;
import java.util.List;

public class FiObsFdr {

    ObjectProperty<Fdr> obsFdr;
    List<ChangeListener<Fdr>> listListener;

    public Fdr getObsFdr() {
        return obsFdrProperty().get();
    }

    public ObjectProperty<Fdr> obsFdrProperty() {
        if (obsFdr == null) {
            obsFdr = new SimpleObjectProperty<Fdr>();
        }
        return obsFdr;
    }

    public void setObsFdr(Fdr fdr) {
        obsFdrProperty().set(fdr);
    }

    public void addListenerFi(ChangeListener<Fdr> changeListener) {
        obsFdrProperty().addListener(changeListener);
        getListListenerInit().add(changeListener);
    }

    public List<ChangeListener<Fdr>> getListListenerInit() {
        if (listListener == null) {
            listListener = new ArrayList<>();
        }
        return listListener;
    }

    public void setListListener(List<ChangeListener<Fdr>> listListener) {
        this.listListener = listListener;
    }

    public void removeAllListeners(){

        for (int index = 0; index < getListListenerInit().size(); index++) {
            removeListenerFi(getListListenerInit().get(index));
        }

    }

    public void removeListenerFi(ChangeListener<Fdr> changeListener){
        obsFdrProperty().removeListener(changeListener);
        getListListenerInit().remove(changeListener);
    }

}
