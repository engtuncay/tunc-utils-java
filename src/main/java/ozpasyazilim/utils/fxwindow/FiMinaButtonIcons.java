package ozpasyazilim.utils.fxwindow;

import de.jensd.fx.glyphs.icons525.Icons525;
import ozpasyazilim.utils.gui.fxcomponents.*;

public class FiMinaButtonIcons {

    public static FxButton genBtnRefresh() {
        FxButton btn = new FxButton("Yenile", Icons525.REFRESH);
        return btn;
    }

    public static FxButton genBtnReport() {
        return new FxButton("Rapor", getReportIcon());
    }

    // Aynı tür component kullanmak için yapıldı
    public static FxButton genBtnSecim() {
        FxButton btn = new FxButton("Seç", Icons525.SELECT);
        return btn;
    }

    // Aynı tür component kullanmak için yapıldı
    public static FxButton genBtnAdd(FxButton btnAdd) {
        FxButton btn = new FxButton("Ekle", Icons525.ADDTHIS);
        return btn;
    }

    public static FxButton genBtnAdd() {
        FxButton btn = new FxButton("Ekle", Icons525.ADDTHIS);
        return btn;
    }

    public static void configBtnSave(FxButton btn) {
        if (btn == null) return;
        btn.setFxIcon(Icons525.SAVE);
        btn.setText("Kaydet");
    }


    public static FxButton genBtnEdit() {
        FxButton btnTemp = new FxButton("Düzenle", Icons525.EDIT); // Icons525.PENCIL Alternatif
        return btnTemp;
    }

    public static FxButton genBtnDelete() {
        FxButton btnTemp = new FxButton("Sil", Icons525.CIRCLEDELETE);
        return btnTemp;
    }

    public static FxButton genBtnSaveAndClose() {
        FxButton btnTemp = new FxButton("Kaydet ve Kapat", Icons525.MAIL_SEND);
        return btnTemp;
    }

    public static FxButton genBtnSave() {
        FxButton btnTemp = new FxButton("Kaydet", Icons525.SAVE);
        return btnTemp;
    }

    public static FxButton genBtnSaveAndNew() {
        FxButton btnTemp = new FxButton("Kaydet", Icons525.DATABASE);
        return btnTemp;
    }

    public static FxButton genBtnExcel() {
        FxButton btnTemp = new FxButton("Excel Yükle", Icons525.EXCEL);
        return btnTemp;
    }

    // Icons

    public static Icons525 getReportIcon() {
        return Icons525.PLAY_CIRCLE;  //Alternatif SHAREPOINT
    }

    public static Icons525 getHelpIcon() {
        return Icons525.INFO;
    }

    public static Icons525 getEditIcon() {
        return Icons525.EDIT;
    }

    public static Icons525 getExcelReadIcon() {
        return Icons525.EXCEL;
    }

    public static Icons525 getUploadAktarimIcon() {
        return Icons525.CIRCLE_UPLOAD;
    }

    public static Icons525 getAction1Icon() {
        return Icons525.CIRCLE_1;
    }

    public static Icons525 getAction2Icon() {
        return Icons525.CIRCLE_2;
    }

    public static Icons525 getBakiyeGosterIcon() {
        return Icons525.EXCHANGE;
    }

    public static Icons525 getPrintIcon() {
        return Icons525.PRINTER;
    }

    public static Icons525 getExcelIcon() {
        return Icons525.EXCEL;
    }

    public static Icons525 getAnaHesapIcon() {
        return Icons525.CIRCLE;
    }


    public static Icons525 getHareketSayiIcon() {
        return Icons525.DISC;
    }

    public static Icons525 getRutaEkleIcon() {
        return Icons525.ADD_TO_CART;
    }

    public static Icons525 getRuttanCikarIcon() {
        return Icons525.REMOVE;
    }

    public static Icons525 getDeleteIcon() {
        return Icons525.CIRCLEDELETE;
    }

    public static Icons525 getSecilenleriAktarIcon() {
        return Icons525.BUCKET;
    }

    public static Icons525 getKilitGuncelleIcon() {
        return Icons525.LOCK_OPEN;
    }

    public static Icons525 getKilitIcon() {
        return Icons525.LOCK;
    }

    public static Icons525 getOnayIcon() {
        return Icons525.MOON;
    }

    public static Icons525 getTickIcon() {
        return Icons525.TOGGLE_OFF; // düğme basılmış
    }

    public static Icons525 getUnTickIcon() {
        return Icons525.TOGGLE_ON; // düğme basılmamış hali
    }
}
