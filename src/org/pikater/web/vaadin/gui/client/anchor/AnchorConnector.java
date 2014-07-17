package org.pikater.web.vaadin.gui.client.anchor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import org.pikater.web.vaadin.gui.client.anchor.AnchorClientRpc;
import org.pikater.web.vaadin.gui.client.anchor.AnchorServerRpc;
import org.pikater.web.vaadin.gui.client.anchor.AnchorState;

import com.vaadin.client.communication.RpcProxy;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.communication.StateChangeEvent;

@Connect(org.pikater.web.vaadin.gui.server.components.anchor.Anchor.class)
public class AnchorConnector extends AbstractComponentConnector
{
	private static final long serialVersionUID = 5557617858638230393L;
	
	private final AnchorServerRpc rpc = RpcProxy.create(AnchorServerRpc.class, this);
	private HandlerRegistration clickHandlerRegistration;
	
	public AnchorConnector()
	{
		this.clickHandlerRegistration = null;
		
		registerRpc(AnchorClientRpc.class, new AnchorClientRpc()
		{
			private static final long serialVersionUID = -8432862667383416641L;
		});
	}

	@Override
	protected Anchor createWidget()
	{
		return GWT.create(Anchor.class);
	}

	@Override
	public Anchor getWidget()
	{
		return (Anchor) super.getWidget();
	}

	@Override
	public AnchorState getState()
	{
		return (AnchorState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent)
	{
		super.onStateChanged(stateChangeEvent);
		
		getWidget().setText(getState().text);
		if(getState().forwardClickToServer)
		{
			getWidget().setHref("");
			if(clickHandlerRegistration == null)
			{
				clickHandlerRegistration = getWidget().addClickHandler(new ClickHandler()
				{
					public void onClick(ClickEvent event)
					{
						if(isEnabled())
						{
							rpc.clicked(MouseEventDetailsBuilder.buildMouseEventDetails(
									event.getNativeEvent(),
									getWidget().getElement()
							));
						}
					}
				});
			}
		}
		else
		{
			if(clickHandlerRegistration != null)
			{
				clickHandlerRegistration.removeHandler();
				clickHandlerRegistration = null;
			}
			getWidget().setHref(getState().hrefAttrContent);
		}
	}
}