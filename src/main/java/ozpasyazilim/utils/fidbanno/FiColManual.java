package ozpasyazilim.utils.fidbanno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * FiCol listesi çıkartırken transient hariç alındığında , Transient olsa da  FiColManual alanları da eklenir.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface FiColManual {
}
