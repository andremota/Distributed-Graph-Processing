package pt.isel.ps1314v.g11.common.config;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;

public interface ModuleJobRunner {
	public abstract boolean run(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException, CmdLineException;

	public abstract JobBean createJobBean();
}
