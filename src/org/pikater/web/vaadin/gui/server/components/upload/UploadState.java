package org.pikater.web.vaadin.gui.server.components.upload;

import java.io.Serializable;

import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.UI;

public class UploadState implements Serializable
{
	private static final long serialVersionUID = 7358164834029892739L;
	
	private int currentUploadsCount;
	private PushMode originalPushMode;

	public UploadState()
	{
		this.currentUploadsCount = 0;
		this.originalPushMode = null;
	}
	
	public synchronized void onStreamingStart()
	{
		if(!isUploading())
		{
			onThingsGoingToBeUploaded();
		}
		currentUploadsCount++;
	}
	
	public synchronized void onStreamingEnd()
	{
		currentUploadsCount--;
		if(!isUploading())
		{
			onThingsUploadFinished();
		}
	}
	
	public boolean isUploading()
	{
		return get() > 0;
	}
	
	// -----------------------------------------------
	// PRIVATE FIELDS
	
	private synchronized int get()
	{
		return currentUploadsCount;
	}
	
	private void onThingsGoingToBeUploaded()
	{
		originalPushMode = UI.getCurrent().getPushConfiguration().getPushMode();
		UI.getCurrent().getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
		// alternative: polling (e.g. "UI.setPollInterval(1500);")
	}
	
	private void onThingsUploadFinished()
	{
		UI.getCurrent().getPushConfiguration().setPushMode(originalPushMode);
	}
}