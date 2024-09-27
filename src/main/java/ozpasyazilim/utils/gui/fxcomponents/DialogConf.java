package ozpasyazilim.utils.gui.fxcomponents;

import javafx.scene.Node;
import ozpasyazilim.utils.fidbanno.FiTable;

/**
 * Dialog (Modal) Window Config
 */
@FiTable
public class DialogConf {

    String txContent;
    String title;
    Double width;
    Double height;

    /**
     * Modal Pencere : Uygulama Pasif Eder,Cevap Bekler
     * <p>
     * NonModal Pencere : Uygulama Pasif Etmez,Pencereyi arkaya alıp, başka çalışmalar yapmaya izin verir
     */
    Boolean boNonModal;
    Node nodeRelative;
    String cssFileName;

    /**
     * Başarılı işlemlerde sadece pop mesaj görüntüler
     */
    Boolean boShowPopOnlyIfSuccess;

    //Double nmPrefWidth;

    public DialogConf() {
    }

    public DialogConf(String txContent) {
        this.txContent = txContent;
    }

    public static DialogConf factory() {
        return new DialogConf();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DialogConf buildTitle(String title) {
        setTitle(title);
        return this;
    }

    public DialogConf buiContent(String txContent) {
        setTxContent(txContent);
        return this;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public DialogConf buildWidth(double v) {
        setWidth(v);
        return this;
    }

    public String getTxContent() {
        return txContent;
    }

    public void setTxContent(String txContent) {
        this.txContent = txContent;
    }

    public Boolean getBoNonModal() {
        return boNonModal;
    }

    public void setBoNonModal(Boolean boNonModal) {
        this.boNonModal = boNonModal;
    }

    public Node getNodeRelative() {
        return nodeRelative;
    }

    public void setNodeRelative(Node nodeRelative) {
        this.nodeRelative = nodeRelative;
    }

    public String getCssFileName() {
        return cssFileName;
    }

    public void setCssFileName(String cssFileName) {
        this.cssFileName = cssFileName;
    }

    public Boolean getBoShowPopOnlyIfSuccess() {
        return boShowPopOnlyIfSuccess;
    }

    public void setBoShowPopOnlyIfSuccess(Boolean boShowPopOnlyIfSuccess) {
        this.boShowPopOnlyIfSuccess = boShowPopOnlyIfSuccess;
    }
}
