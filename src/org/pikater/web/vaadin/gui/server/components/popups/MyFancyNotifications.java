package org.pikater.web.vaadin.gui.server.components.popups;

import org.vaadin.alump.fancylayouts.FancyNotifications;
import org.vaadin.alump.fancylayouts.gwt.client.shared.FancyNotificationsState.Position;

public class MyFancyNotifications extends FancyNotifications
{
	private static final long serialVersionUID = 3423385555186912646L;

	public MyFancyNotifications()
	{
		super();
		
		setCloseTimeout(5000); // 5 seconds
		setClickClose(true); // click closes notifications
		// notifications.setDefaultIcon(new ThemeResource("images/vaadin.png"));
		setPosition(Position.TOP_RIGHT);
		addListener(new FancyNotifications.NotificationsListener()
		{
			@Override
	        public void notificationClicked(Object id)
	        {
	        }
	    });
	}
}
