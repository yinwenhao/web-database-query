package com.dhf.tools.sqlParse;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * @author longyaokun
 *
 */
public class SecretSelectItemVisitor implements SelectItemVisitor, SecretColumnIdentifier {

	private boolean isSecret;
	private Set<String> secretColumnSet;

	@Override
	public void visit(SelectExpressionItem selectExpressionItem) {
		Expression expression = selectExpressionItem.getExpression();
		processExpression(expression);

	}

	private void processExpression(Expression expression) {
		if (expression instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) expression;
			SelectBody selectBody = subSelect.getSelectBody();
			if (selectBody instanceof PlainSelect) {
				List<SelectItem> selectItems = ((PlainSelect) selectBody).getSelectItems();
				for (SelectItem item : selectItems) {
					item.accept(this);
					if (isSecret) {
						break;
					}
				}
			}
		} else if (expression instanceof BinaryExpression) {
			BinaryExpression binaryExpression = (BinaryExpression) expression;
			processExpression(binaryExpression.getLeftExpression());
			if (isSecret) {
				return;
			}
			processExpression(binaryExpression.getRightExpression());
		} else if (expression instanceof Function) {
			Function function = (Function) expression;
			List<Expression> expressions = function.getParameters().getExpressions();
			for (Expression exp : expressions) {
				processExpression(exp);
				if (isSecret) {
					break;
				}
			}

		} else if (expression instanceof Column) {
			Column column = (Column) expression;
			String columnName = column.getColumnName().replaceAll("`", "").toLowerCase();
			isSecret = isSecretColumn(columnName);
		}
	}

	private boolean isSecretColumn(String columnName) {
		if (CollectionUtils.isEmpty(secretColumnSet)) {
			return false;
		}
		return secretColumnSet.contains(columnName);
	}

	@Override
	public void visit(AllTableColumns allTableColumns) {
		

	}

	@Override
	public void visit(AllColumns allColumns) {
		

	}

	public boolean isSecret() {
		return isSecret;
	}

	@Override
	public void setSecretColumnSet(Set<String> secretColumnSet) {
		this.secretColumnSet = secretColumnSet;

	}
}
