package ozpasyazilim.utils.fipdf;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import ozpasyazilim.utils.core.FiCollection;
import ozpasyazilim.utils.returntypes.Fdr;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FiPdf {

    public static void main(String[] args) {
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        try {
            pdfMergerUtility.addSource(new File("dosya1.pdf"));
            pdfMergerUtility.addSource(new File("dosya2.pdf"));
            pdfMergerUtility.setDestinationFileName("birleştirilmiş_dosya.pdf");
            pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            //System.out.println("PDF dosyaları başarıyla birleştirildi.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Fdr combinePdf(String txBaseDir,List<File> fileList){

        Fdr fdrMain = new Fdr();

        if(FiCollection.isEmpty(fileList))return fdrMain.buiBoResult(false);

        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();

        try {
            for (File file : fileList) {
                pdfMergerUtility.addSource(file); //new File("dosya1.pdf")
            }

            //pdfMergerUtility.addSource(new File("dosya2.pdf"));
            String yeniFileName = txBaseDir + "\\toplu" + fileList.get(0).getName();

            pdfMergerUtility.setDestinationFileName(yeniFileName);
            pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            fdrMain.setFdrTxValue(yeniFileName);
            //System.out.println("PDF dosyaları başarıyla birleştirildi.");
        } catch (IOException e) {
            //e.printStackTrace();

            fdrMain.buiBoResult(false,e);
        }

        return fdrMain;
    }
}