package ozpasyazilim.utils.core;

import org.junit.jupiter.api.Test;
import ozpasyazilim.utils.mvc.IFiCol;
import ozpasyazilim.utils.datatypes.PersonEntityTest;
import ozpasyazilim.utils.table.OzColType;
import ozpasyazilim.utils.table.FiCol;

import java.util.ArrayList;
import java.util.List;

class TestFiHtmlReport2 {


	@Test
	void reportBasicHtml() {

		PersonEntityTest personTest = new PersonEntityTest("Abdullah","Muaz");

		PersonEntityTest personTest2 = new PersonEntityTest("Ömer","Ali");

		List<PersonEntityTest> listData = new ArrayList<>();

		listData.add(personTest);
		listData.add(personTest2);

		FiCol tableColumn = new FiCol("firstName", "Adı");
		FiCol tableColumn2 = new FiCol("secondName", "Soyadi");

		//tableColumn.getMapStyle().put(OzTableColumn.ColStyle.alignment,"RIGHT");
		tableColumn.setColType(OzColType.Money);

		List<IFiCol> ozTableColumnList = new ArrayList<>();
		ozTableColumnList.add(tableColumn);
		ozTableColumnList.add(tableColumn2);

		String content = new ReportHtml2().reportBasicHtml(listData, ozTableColumnList, null);

		FiFile.saveHtmlContentToFile(content);

	}

	@Test
	void reportTestCron(){





	}



}