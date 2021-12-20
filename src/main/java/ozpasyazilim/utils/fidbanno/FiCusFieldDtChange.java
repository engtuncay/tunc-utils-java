package ozpasyazilim.utils.fidbanno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * DtChange alanını ifade eder
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FiCusFieldDtChange {

}
