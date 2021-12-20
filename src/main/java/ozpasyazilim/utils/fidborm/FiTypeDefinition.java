

package ozpasyazilim.utils.fidborm;

import javax.persistence.Temporal;

/**
 * Sql den Java Aktarırken ALanların özellikleri
 */
public class FiTypeDefinition {

	String name;

	Integer minLength;
	Integer maxLength;

	Integer minScale;
	Integer maxScale;

	Integer minPrecision;
	Integer maxPrecision;

	Integer defaultLength;
	Integer defaultPrecision;
	Integer defaultScale;

	Temporal temporalType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMinScale() {
		return minScale;
	}

	public void setMinScale(Integer minScale) {
		this.minScale = minScale;
	}

	public Integer getMaxScale() {
		return maxScale;
	}

	public void setMaxScale(Integer maxScale) {
		this.maxScale = maxScale;
	}

	public Integer getMinPrecision() {
		return minPrecision;
	}

	public void setMinPrecision(Integer minPrecision) {
		this.minPrecision = minPrecision;
	}

	public Integer getMaxPrecision() {
		return maxPrecision;
	}

	public void setMaxPrecision(Integer maxPrecision) {
		this.maxPrecision = maxPrecision;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getDefaultLength() {
		return defaultLength;
	}

	public void setDefaultLength(Integer defaultLength) {
		this.defaultLength = defaultLength;
	}

	public Integer getDefaultPrecision() {
		return defaultPrecision;
	}

	public void setDefaultPrecision(Integer defaultPrecision) {
		this.defaultPrecision = defaultPrecision;
	}

	public Integer getDefaultScale() {
		return defaultScale;
	}

	public void setDefaultScale(Integer defaultScale) {
		this.defaultScale = defaultScale;
	}

	public Temporal getTemporalType() {
		return temporalType;
	}

	public void setTemporalType(Temporal temporalType) {
		this.temporalType = temporalType;
	}

}
