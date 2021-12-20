package ozpasyazilim.utils.fidbanno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface ColDefinitionExtra {

	/**
	 * Default Value definition , this string is added to the end of ddl query
	 *
	 * @return
	 */
	String value();
}
