package org.pikater.web.vaadin.gui.server.webui;

import org.pikater.shared.FieldVerifier;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.vaadin.gui.server.components.linklabel.LinkLabel;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class MyDialogs
{
	// -------------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private static Window createConfirmDialog(UI parentUI, String caption, Component subContent, final OnOkClicked okAction)
	{
		// define underlying components
        Label spacer = new Label("");
        spacer.setSizeFull();
        
        final Button cancel = new Button("Cancel");
        final Button ok = new Button("OK");
        ok.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				if(okAction.handleOkEvent())
				{
					cancel.click();
				}
			}
		});
        ok.setClickShortcut(KeyCode.ENTER, null);

        // create the buttons component
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();
        buttons.setSpacing(true);
        buttons.addComponent(spacer);
        buttons.setExpandRatio(spacer, 1f);
        buttons.addComponent(ok);
        buttons.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);
        buttons.addComponent(cancel);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
        
        // wrap the content component
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setStyleName("pikaterDialogContent");
        content.addComponent(subContent);
        content.addComponent(buttons);

        // create the dialog
        final Window result = new Window(caption, content);
        content.setSizeUndefined();
        setDialogProperties(result);
        result.setSizeUndefined();
        cancel.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				result.close();
			}
		});
        parentUI.addWindow(result);
        parentUI.setFocusedComponent(result);
        
        return result;
	}
	
	private static void setDialogProperties(Window window)
	{
		window.setClosable(true);
		window.setDraggable(false);
		window.setModal(true);
		window.setResizable(false);
		window.setCloseShortcut(KeyCodes.KEY_ESCAPE, null);
	}
	
	// -------------------------------------------------------------------------
	// SPECIFIC DIALOGS
	
	public static Window createSimpleConfirmDialog(UI parentUI, String message, OnOkClicked okAction)
	{
		return createConfirmDialog(parentUI, "Confirm action", new Label(message), okAction);
	}
	
	public static Window createTextPromptDialog(UI parentUI, String caption, String oldValue, final ITextPromptDialogResult newValueHandler) 
	{
		final TextField tf = new TextField("The new value:");
		if(oldValue != null)
		{
			tf.setValue(oldValue);
		}
		return createConfirmDialog(parentUI, caption, tf, new OnOkClicked()
		{
			@Override
			public boolean handleOkEvent()
			{
				return newValueHandler.handleResult(tf.getValue());
			}
		});
	}
	
	public static Window createLoginDialog(final UI parentUI, final ILoginDialogResult resultHandler)
	{
		FormLayout form = new FormLayout();
		final TextField login = new TextField("Login:", "sj");
		final PasswordField password = new PasswordField("Password:", "123");
		form.addComponent(login);
		form.addComponent(password);
		form.addComponent(new LinkLabel("Create account", new ClickListener()
		{
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event)
			{
				createRegisterDialog(parentUI);
			}
		}));
		return createConfirmDialog(parentUI, "Please, authenticate yourself", form, new OnOkClicked()
		{
			@Override
			public boolean handleOkEvent()
			{
				return resultHandler.handleResult(login.getValue(), password.getValue());
			}
		});
	}
	
	public static Window createRegisterDialog(UI parentUI)
	{
		FormLayout form = new FormLayout();
		final TextField login = new TextField("Login:");
		final PasswordField password = new PasswordField("Password:");
		final TextField email = new TextField("Email:");
		form.addComponent(login);
		form.addComponent(password);
		form.addComponent(email);
		return createConfirmDialog(parentUI, "Create a new account", form, new OnOkClicked()
		{
			@Override
			public boolean handleOkEvent()
			{
				if(FieldVerifier.isValidEmail(email.getValue()))
				{
					DAOs.userDAO.storeEntity(new JPAUser(
							login.getValue(),
							password.getValue(),
							email.getValue(),
							DAOs.roleDAO.getAdminRole()
					));
					return true;
				}
				else
				{
					Notification.show("Email is not valid. It has to be specified, in the form 'a@b.c' where 'a' has "
							+ "64 characters at most and b and c have at most 254 characters altogether.");
					return false;
				}
			}
		});
	}
	
	// -------------------------------------------------------------------------
	// DIALOG RESULTS
	
	public interface OnOkClicked
	{
		/**
		 * Custom action to be called when the user clicks the OK button.
		 * @return true if the dialog is no longer needed and it should close
		 */
		boolean handleOkEvent();
	}
	
	public interface ITextPromptDialogResult
	{
		/**
		 * Custom action to check the user's input.
		 * @return true if the dialog is no longer needed and it should close
		 */
		boolean handleResult(String promptResultString);
	}
	
	public interface ILoginDialogResult
	{
		/**
		 * Custom action to check the user's input.
		 * @return true if the dialog is no longer needed and it should close
		 */
		boolean handleResult(String login, String password);
	}
}
