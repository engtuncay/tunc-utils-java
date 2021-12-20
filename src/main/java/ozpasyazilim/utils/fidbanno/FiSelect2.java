package ozpasyazilim.utils.fidbanno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Select2 sorgusu için seçilecek alanlar
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FiSelect2 {

}
