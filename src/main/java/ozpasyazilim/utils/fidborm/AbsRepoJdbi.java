package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;

/**
 * Repolarda genel olması istenen alanlar ve metodlar
 * <p>
 * Kullanan Sınıflar : AbsRepoGenJdbi, AbsRepoJdbiNog (not generic), AbsFkbRepoJdbi
 */
public class AbsRepoJdbi {

	protected String connProfile;
	protected Jdbi jdbi;
	// Optional (bilgi amaçlı)
	protected String databaseName;

	// Getter and Setter
	public Jdbi getJdbi() {
		return jdbi;
	}

	public void setJdbi(Jdbi jdbi) {
		this.jdbi = jdbi;
	}

	public String getConnProfile() {
		return connProfile;
	}

	public void setConnProfile(String connProfile) {
		this.connProfile = connProfile;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}


}
