package ozpasyazilim.utils.core;

import ozpasyazilim.utils.returntypes.Fdr;

@FunctionalInterface
public interface RunnableFdr {

    public abstract void run(Fdr fdr);

}