package ozpasyazilim.utils.gui.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionsUtil {

    public static <T> List<T> filter(List<T> coll, Filter<T> filter) {
	List<T> result = new ArrayList<T>();
	for (T t : coll)
	    if (filter.accept(t))
		result.add(t);
	return result;
    }

    public static <T> int firstIndexOf(List<T> coll, Filter<T> filter) {
	int i = coll.size();
	for (int j = 0; j < i; j++)
	    if (filter.accept(coll.get(j)))
		return j;
	return -1;
    }

    public static <T> int lastIndexOf(List<T> coll, Filter<T> filter) {
	int i = coll.size();
	int idx = -1;
	for (int j = 0; j < i; j++)
	    if (filter.accept(coll.get(j)))
		idx = j;
	return idx;
    }

    public static <T> int firstIndexOf(T[] obj, Filter<T> filter) {
	int i = obj.length;
	for (int j = 0; j < i; j++)
	    if (filter.accept(obj[j]))
		return j;
	return -1;
    }

    public static <T> int lastIndexOf(T[] obj, Filter<T> filter) {
	int i = obj.length;
	int idx = -1;
	for (int j = 0; j < i; j++)
	    if (filter.accept(obj[j]))
		idx = j;
	return idx;
    }

    public static <T> Integer[] allMatchIndex(List<T> list, Filter<T> filter) {
	Integer[] result = new Integer[list.size()];
	int currentIdx = 0;
	for (int i = 0; i < list.size(); i++)
	    if (filter.accept(list.get(i)))
		result[currentIdx++] = i;
	return (Integer[]) trim(result);
    }

    public static Object[] trim(Object[] obj) {
	int nullIndex = -1;
	for (int i = 0; i < obj.length; i++)
	    if (obj[i] == null)
		nullIndex = i;
	Object[] objs = new Object[++nullIndex];
	for (int i = 0; i < nullIndex; i++)
	    objs[i] = obj[i];
	return objs;
    }

}
