package ozpasyazilim.utils.fidborm;

import org.jdbi.v3.core.Jdbi;

public class RepoJdbiCustom extends AbsRepoJdbi {

	public RepoJdbiCustom(Jdbi jdbi, Class clazz) {
		super(jdbi, clazz);
	}

}
