package com.dhf.tools.listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener,
		HttpSessionAttributeListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {

	}

	/**
	 * Session销毁时
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		System.out.println("sessionDestroyed");
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		System.out.println("attributeAdded");
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		System.out.println("attributeRemoved");
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		System.out.println("attributeReplaced");
	}

}
