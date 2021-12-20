package ozpasyazilim.utils.file;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ozpasyazilim.utils.log.Loghelper;

public class PropMap extends HashMap<String, String> implements Map<String, String>, Cloneable, Serializable {

	private File file;

	@Override
	public String put(String key, String value) {
		return super.put(key, value);
	}

	// dışarda instance açılması engellendi
	private PropMap() {

	}

	public static PropMap getInstance(File file) {

		Stream<String> stream = null;

		try {
			stream = Files.lines(Paths.get(file.getPath()));
		} catch (IOException exception) {
			// FIXME genel logger olmalı
			Loghelper.get(OzFile.class).error("Dosya Okuma hatası :" + file.getPath()); // UtilModel.exceptiontostring(exception)
		}

		List<String> listContent = stream.collect(Collectors.toList());

		PropMap propMap = new PropMap();
		propMap.setFile(file);

		for (Iterator iterator = listContent.iterator(); iterator.hasNext();) {
			String rowprop = (String) iterator.next();

			// comment rows
			if (rowprop.matches("^#")) continue;

			// Regular expression çevir
			String[] arrRow = rowprop.split("=", 2);

			if (arrRow.length == 2) {
				propMap.put(arrRow[0], arrRow[1]);
			}
		}

		return propMap;

	}

	public File getFile() {
		return file;
	}

	private void setFile(File file) {
		this.file = file;
	}

}
