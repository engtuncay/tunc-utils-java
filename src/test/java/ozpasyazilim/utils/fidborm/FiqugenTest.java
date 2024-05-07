package ozpasyazilim.utils.fidborm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ozpasyazilim.utils.core.FiCollection;

import java.util.List;

class FiqugenTest {

	@Test
	void getQueryFields() {

		final String string = "select cari_kod as cha_kod,cari_unvan1,cari_vdaire_no,cari_Ana_cari_kodu \n"
				+ "from CARI_HESAPLAR \n"
				+ "where cari_vdaire_no\n"
				+ "IN ( select distinct ch2.cari_vdaire_no FROM CARI_HESAPLAR ch2 WHERE ch2.cari_kod <> ch2.cari_Ana_cari_kodu )\n"
				+ "ORDER BY cari_Ana_cari_kodu";

		List<FiField> queryFields = Fiqugen.getQueryFields(string);

//		for (FiField queryField : queryFields) {
//			System.out.println(queryField.getName());
//		}

		if (FiCollection.isEmpty(queryFields)) {
			Assertions.fail();
		}

	}
}