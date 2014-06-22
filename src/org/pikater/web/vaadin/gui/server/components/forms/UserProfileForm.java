package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.base.FormFieldFactory;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class UserProfileForm extends CustomFormLayout
{
	private static final long serialVersionUID = 1654056604776473039L;

	private JPAUser currentUser;
	
	private final TextField loginField;
	private final PasswordField passwordField; 
	private final TextField emailField;
	
	private final Button btn_changePassword;
	
	public UserProfileForm()
	{
		super("Save changes");
		
		this.currentUser = null;
		
		this.loginField = FormFieldFactory.getLoginField(null, false, true); // not required since completely read-only
		this.passwordField = FormFieldFactory.getGeneralPasswordField("Password:", null, true, true);
		this.emailField = FormFieldFactory.getEmailField(null, true, false);
		
		this.btn_changePassword = new Button("Change password", new ClickListener()
		{
			private static final long serialVersionUID = -8548997603012122979L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				MyDialogs.passwordChangeDialog(currentUser, new MyDialogs.IDialogResultHandler()
				{
					@Override
					public boolean handleResult(Object[] args)
					{
						setValueAndIgnoreReadOnly(passwordField, (String) args[0]);
						return true;
					}
				});
			}
		});
	}
	
	public void enter(JPAUser currentUser)
	{
		this.currentUser = currentUser;
		
		setValueAndIgnoreReadOnly(loginField, currentUser.getLogin());
		setCommitted(loginField);
		setValueAndIgnoreReadOnly(passwordField, currentUser.getPassword());
		setCommitted(passwordField);
		emailField.setValue(currentUser.getEmail());
		setCommitted(emailField);
		
		addField("login", loginField);
		addField("password", passwordField);
		addField("email", emailField);
		
		addCustomButton(btn_changePassword);
	}

	@Override
	public IOnSubmit getSubmitAction()
	{
		return new IOnSubmit()
		{
			@Override
			public boolean onSubmit()
			{
				if(!ServerConfigurationInterface.avoidUsingDBForNow())
				{
					currentUser.setPassword(passwordField.getValue());
					currentUser.setEmail(emailField.getValue());
					DAOs.userDAO.updateEntity(currentUser);
				}
				
				MyNotifications.showSuccess(null, "Changes were successfully saved.");
				return true;
			}
		};
	}
}