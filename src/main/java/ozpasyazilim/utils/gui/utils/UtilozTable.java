package ozpasyazilim.utils.gui.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import mark.utils.swing.table.ObjectTablemodel;
import mark.utils.swing.table.ObjectTablemodel2;
import ozpasyazilim.utils.gui.components.CustomTablemodelList4;
import ozpasyazilim.utils.gui.components.OjxTable;

import javax.swing.*;

public class UtilozTable {

	public static Integer convertModelIndex(OjxTable tblKrediKartislemListesi, Integer tableindex) {

		return tblKrediKartislemListesi.convertRowIndexToModel(tableindex);

	}

	public static <E> void setExecuteFunctionForElementsSeen(OjxTable oztable, ObjectTablemodel2<E> objectTableModel, Consumer<E> consumeFunc) {

		Integer size = oztable.getRowCount();

		for (int i = 0; i < size; i++) {

			Integer modelindex = oztable.convertRowIndexToModel(i);

			E entity = objectTableModel.getRow(modelindex);

			consumeFunc.accept(entity);

		}


	}

	public static <E> List<E> getTableSeenElements(JTable table, ObjectTablemodel2<E> objectTableModel) {

		Integer size = table.getRowCount();
		List<E> listResult = new ArrayList();

		for (int i = 0; i < size; i++) {

			Integer modelindex = table.convertRowIndexToModel(i);

			E entity = objectTableModel.getRow(modelindex);

			listResult.add(entity);

		}

		return listResult;
	}

	public static <E> List<E> getTableSeenElements(JTable table, ObjectTablemodel<E> objectTableModel) {

		Integer size = table.getRowCount();
		List<E> listResult = new ArrayList();

		for (int i = 0; i < size; i++) {

			Integer modelindex = table.convertRowIndexToModel(i);

			E entity = objectTableModel.getRow(modelindex);

			listResult.add(entity);

		}

		return listResult;
	}

	public static<E> List<E> getTableSeenElements(JTable table, CustomTablemodelList4<E> customTablemodelList4) {

		Integer size = table.getRowCount();
		List<E> listResult = new ArrayList();

		for (int i = 0; i < size; i++) {

			Integer modelindex = table.convertRowIndexToModel(i);

			E entity = customTablemodelList4.getListdata().get(modelindex);

			listResult.add(entity);

		}

		return listResult;


	}
}
