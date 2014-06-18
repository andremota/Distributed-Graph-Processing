package pt.isel.ps1314v.g11.common.config;

import java.io.IOException;

public interface ModuleJobRunner {
	public abstract boolean run(String[] args) throws IOException, ClassNotFoundException, InterruptedException;
	public abstract JobBean createJobBean();
}
