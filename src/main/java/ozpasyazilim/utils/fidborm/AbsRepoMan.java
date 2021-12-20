package ozpasyazilim.utils.fidborm;

public class AbsRepoMan extends AbsRepoJdbi implements IRepoJdbi{

	public AbsRepoMan(Class clazz) {
		setEntityClass(clazz);
	}

	public AbsRepoMan(String connProfile, Class clazz) {
		setEntityClass(clazz);
		setConnProfile(connProfile);
	}
}
