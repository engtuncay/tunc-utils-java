package ozpasyazilim.utils.fidbanno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * LnUserId alanını olduğunu ifade eder
 *
 * Kullanım Yerleri : ?
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FiCusFieldUserId {

}
