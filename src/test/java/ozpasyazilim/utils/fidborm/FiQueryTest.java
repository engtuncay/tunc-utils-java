package ozpasyazilim.utils.fidborm;

import org.junit.jupiter.api.Test;
import ozpasyazilim.utils.core.FiConsole;
import ozpasyazilim.utils.datatypes.FiKeyBean;

import java.util.ArrayList;
import java.util.List;

class FiQueryTest {

	@Test
	void convertListParamToMultiParams() {

		String sql = "--sq191114_1725\n" +
				"select chh.cha_RECno\n" +
				",chh.cha_satici_kodu\n" +
				"--,ISNULL(cpt.cari_per_adi,'') as cari_per_adi\n" +
				",chh.cha_kod,chh.cari_unvan1, chh.cha_meblag\n" +
				",chh.cha_srmrkkodu, chh.cha_evrakno_seri, chh.cha_evrakno_sira\n" +
				",chh.cha_tarihi, chh.cha_belge_tarih, chh.cha_aciklama\n" +
				",chh.cha_belge_no, chh.cha_evrak_tip, chh.cha_tip\n" +
				",chh.cha_cinsi, chh.cha_kasa_hizmet, chh.cha_kasa_hizkod\n" +
				",chh.cari_unvan1_karsi, chh.cha_satir_no\n" +
				"--,CHEvrKisaIsim\n" +
				"---,CHCinsIsim\n" +
				"FROM OZV_CARI_HESAP_HAREKETLER_ENT2 chh\n" +
				"LEFT JOIN OZPASENTEGRE.dbo.EntCariHareketEk ece\n" +
				"ON ece.eceChaRecNo = chh.cha_RECno and ece.eceTxFirmaKod = @eceTxFirmaKod\n" +
				"WHERE 1=1 \n" +
				"AND chh.cha_tarihi >= '20210101'\n" +
				"--!cha_evrak_tip\n" +
				"--AND chh.cha_evrak_tip IN(@cha_evrak_tip)\n" +
				"ORDER BY chh.cha_RECno DESC\n";
//
		FiKeyBean fiKeyBean = new FiKeyBean();

		List<Integer> listEvraklar = new ArrayList<>();
		listEvraklar.add(30);
		listEvraklar.add(31);
//		listEvraklar.add(MetaMikroEvrakTip.CekGirisBordrosu.getCha_evrak_tip());
		fiKeyBean.buiPut("cha_evrak_tip", listEvraklar);
		fiKeyBean.buiPut("eceTxFirmaKod", "ozpas");

		FiQuery fiQuery = new FiQuery(sql, fiKeyBean);
		fiQuery.activateParamsByMapParams();
		fiQuery.convertListParamsToMultiParams();

		System.out.println(fiQuery.getTxQuery());
		FiConsole.printMapFi(fiQuery.getMapParams());
	}
}