package ozpasyazilim.utils.annotations;

// metodu kullanan metodlar olduğunu gösterir
public @interface OzActive {

	// kontrol tarihini gösterir
	String date() default "";
	
}
