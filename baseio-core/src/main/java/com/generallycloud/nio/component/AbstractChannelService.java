package com.generallycloud.nio.component;

import java.nio.channels.SelectableChannel;

import com.generallycloud.nio.configuration.ServerConfiguration;

public abstract class AbstractChannelService implements ChannelService {

	protected BaseContext		context;
	protected SelectableChannel	selectableChannel;

	public BaseContext getContext() {
		return context;
	}

	public void setContext(BaseContext context) {
		this.context = context;
	}

	protected abstract int getSERVER_PORT(ServerConfiguration configuration);

	protected abstract void setChannelService(BaseContext context);

	protected String getSERVER_HOST(ServerConfiguration configuration) {
		return configuration.getSERVER_HOST();
	}

	public SelectableChannel getSelectableChannel() {
		return selectableChannel;
	}

}
