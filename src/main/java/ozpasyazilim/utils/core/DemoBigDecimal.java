package ozpasyazilim.utils.core;

import java.math.BigDecimal;

public class DemoBigDecimal {

    public static void main(String[] args) {

	BigDecimal a1 = BigDecimal.valueOf(10.002012d);
	BigDecimal a2 = BigDecimal.valueOf(10.0020122543d);

	BigDecimal fark = a1.subtract(a2).abs();
	BigDecimal fark2 = fark.setScale(4, BigDecimal.ROUND_HALF_UP);

	System.out.println("Fark :" + fark.toString());
	System.out.println("Fark2 :" + fark2.toString());
	System.out.println("Compare :" + a1.compareTo(a2));
	System.out.println("Fark Compare :" + fark.compareTo(BigDecimal.valueOf(0.01d)));
	System.out.println("Fark2 Compare :" + fark2.compareTo(BigDecimal.valueOf(0.01d)));

    }

}
