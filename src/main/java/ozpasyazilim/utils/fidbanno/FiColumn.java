package ozpasyazilim.utils.fidbanno;

import ozpasyazilim.utils.gui.fxcomponents.FxTextField;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * For Integer types , precision = 1 -> tinyint type
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface FiColumn {

	String name() default "";

	String label() default "";

	boolean isNullable() default true;

	boolean isUnique() default false;

	/**
	 * varchar yerine nvarchar kullanılır
	 */
	boolean isUnicodeSupport() default false;

	String defaultValue() default "";

	/**
	 * String alanlar için alanların karekter sayısı
	 */
	int length() default 0;

	/**
	 * Precision 1 : tinyint olduğunu gösterir Integer alanlar için
	 * @return
	 */
	int precision() default 0;

	int scale() default 0;

	String date() default "";

	FiSqlDateTypes dateType() default FiSqlDateTypes.shortdate;

	String colDefinitionExtra() default "";

	String colCustomTypeDefinition() default "";

	String colFieldType() default "";

	boolean defaultUpdateField() default false;

	String typeName() default "";

	FiCollation collation() default FiCollation.Default;

	boolean boExcludeFromAutoColList() default false;

	// True olursa alan FiMapParama eklenirken başına sonuna % işareti ekler
	boolean boFilterLike() default false;

	//enum SqlDateTypes {datetime, shortdate}
}
