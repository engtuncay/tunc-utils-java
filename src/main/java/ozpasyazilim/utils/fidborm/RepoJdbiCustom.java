package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;

public class RepoJdbiCustom extends AbsRepoGenJdbi {

	public RepoJdbiCustom(Jdbi jdbi, Class clazz) {
		super(jdbi, clazz);
	}

}
