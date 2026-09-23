package ozpasyazilim.utils.fiutils;

public class FiSbUtil {

  public static StringBuilder addBr(StringBuilder sb) {
    sb.append("<br>");
    return sb;
  }

  public static StringBuilder addBr(StringBuilder sb, String str) {
    sb.append("<br>");
    sb.append(str);
    return sb;
  }
}
