package ozpasyazilim.utils.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class FxBigDecimal extends BigDecimal {

	public FxBigDecimal(char[] in, int offset, int len) {
		super(in, offset, len);
	}

	public FxBigDecimal(char[] in, int offset, int len, MathContext mc) {
		super(in, offset, len, mc);
	}

	public FxBigDecimal(char[] in) {
		super(in);
	}

	public FxBigDecimal(char[] in, MathContext mc) {
		super(in, mc);
	}

	public FxBigDecimal(String val) {
		super(val);
	}

	public FxBigDecimal(String val, MathContext mc) {
		super(val, mc);
	}

	public FxBigDecimal(double val) {
		super(val);
	}

	public FxBigDecimal(double val, MathContext mc) {
		super(val, mc);
	}

	public FxBigDecimal(BigInteger val) {
		super(val);
	}

	public FxBigDecimal(BigInteger val, MathContext mc) {
		super(val, mc);
	}

	public FxBigDecimal(BigInteger unscaledVal, int scale) {
		super(unscaledVal, scale);
	}

	public FxBigDecimal(BigInteger unscaledVal, int scale, MathContext mc) {
		super(unscaledVal, scale, mc);
	}

	public FxBigDecimal(int val) {
		super(val);
	}

	public FxBigDecimal(int val, MathContext mc) {
		super(val, mc);
	}

	public FxBigDecimal(long val) {
		super(val);
	}

	public FxBigDecimal(long val, MathContext mc) {
		super(val, mc);
	}

}
