package ozpasyazilim.utils.fidbanno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface FiEntity {

	String name() default "";

	String prefix() default "";

	boolean boActive() default true;

}
