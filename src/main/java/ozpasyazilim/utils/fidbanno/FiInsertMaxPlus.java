package ozpasyazilim.utils.fidbanno;

import ozpasyazilim.utils.annotations.FiExpiremental;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@FiExpiremental
@Target({FIELD})
@Retention(RUNTIME)
public @interface FiInsertMaxPlus {

}
