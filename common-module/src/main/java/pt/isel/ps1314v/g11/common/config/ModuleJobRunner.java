package pt.isel.ps1314v.g11.common.config;

import java.io.IOException;

/**
 * Base class for algorithms job runners
 *
 */
public interface ModuleJobRunner {
	/**
	 * This method will be called with arguments from the command line and should use them to
	 * start a job.
	 * @param args Arguments from the command line
	 * @return Whether the job finished with success or not
	 */
	public abstract boolean run(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException;

	/**
	 * Should return a {@link JobBean} instance.
	 * @return A JobBean
	 */
	public abstract JobBean createJobBean();
}
