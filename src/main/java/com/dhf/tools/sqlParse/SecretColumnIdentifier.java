/**
 * 
 */
package com.dhf.tools.sqlParse;

import java.util.Set;

import net.sf.jsqlparser.statement.select.SelectItemVisitor;

/**
 * @author longyaokun
 *
 */
public interface SecretColumnIdentifier extends SelectItemVisitor{

	public boolean isSecret();
	
	public void setSecretColumnSet(Set<String> secretColumnSet);
}
