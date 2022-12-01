package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;

/**
 * Repolarda genel olması istenen alanlar ve metodlar
 * <p>
 * Kullanan Sınıflar : AbsRepoJdbi,AbsRepoJdbiPure,AbsFkbRepoJdbi
 */
public class RepoGeneralJdbi {

	protected String connProfile;
	protected Jdbi jdbi;
	protected String databaseName;

	// Getter and Setter

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

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

}
