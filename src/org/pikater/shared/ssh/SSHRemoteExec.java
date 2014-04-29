package org.pikater.shared.ssh;

import java.io.IOException;

import org.pikater.shared.ssh.SSHBatchExecChannel.ISSHBatchChannelNotificationHandler;

import com.jcraft.jsch.JSchException;

public class SSHRemoteExec implements ISSHChannel, ISSHAsyncCommandExec, ISSHSyncCommandExec
{
	private final SSHBatchExecChannel batchExecChannel;
	private final SSHInteractiveExecChannel interactiveExecChannel;
	
	public SSHRemoteExec(SSHSession session, ISSHBatchChannelNotificationHandler outputHandler)
	{
		super();
		
		this.batchExecChannel = new SSHBatchExecChannel(session, outputHandler, true);
		this.interactiveExecChannel = new SSHInteractiveExecChannel(session);
	}
	
	@Override
	public String execSync(String command) throws JSchException, InterruptedException
	{
		return this.interactiveExecChannel.execSync(command);
	}
	
	@Override
	public void execAsync(String command) throws JSchException, IOException
	{
		this.batchExecChannel.execAsync(command);
	}

	@Override
	public void close()
	{
		batchExecChannel.close();
		interactiveExecChannel.close();
	}

	@Override
	public SSHSession getSession()
	{
		return batchExecChannel.getSession();
	}
}