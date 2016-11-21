/**
 * 
 */
package com.dhf.tools.action;

import org.apache.struts2.convention.annotation.Action;

import com.dhf.tools.common.BaseAction;

/**
 * @author when_how
 * 
 */

public class TestAction extends BaseAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Action("/test")
	public String test() {
		setVelocityLocation("/test.vm");
		return VELOCITY;
	}
	
	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE);
		long aaa = 2147483648L;
		int b = (int) aaa;
		System.out.println(b);
		long c = b;
		System.out.println(c);
	}

}
