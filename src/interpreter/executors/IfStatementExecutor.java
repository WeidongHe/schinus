package interpreter.executors;

import static objectmodel.predefined.PredefinedConstant.NONE;

import intermediate.ICodeNode;
import objectmodel.dictionary.Dictionary;

import java.util.ArrayList;

/**
 * <h1>IfStatementExecutor</h1>
 * <p>
 * <p>If statement executor.</p>
 */
public class IfStatementExecutor extends StatementExecutor {
  public IfStatementExecutor() {
  }

  /**
   * @param iCodeNode   if statement code node
   * @param environment environment that used to execute if statement
   * @return the result of execute if statement
   */
  public Object execute(ICodeNode iCodeNode, Dictionary environment) {
    ExpressionStatementExecutor expressionStatementExecutor = new ExpressionStatementExecutor();
    StatementExecutor statementExecutor = new StatementExecutor();
    ArrayList<ICodeNode> branches = iCodeNode.getChildren();
    Object executeResult = NONE;

    // execute all branches of if statement
    for (ICodeNode branch : branches) {
      ArrayList<ICodeNode> children = branch.getChildren();
      ICodeNode conditionExpression = children.get(0);
      ICodeNode bodyNode = children.get(1);

      // execute condition expression of branches
      try {
        Object conditionResult = expressionStatementExecutor.execute(conditionExpression, environment);
        boolean result = checkConditionResult(conditionResult);
        // execute one statement of branches or no statement
        if (result) {
          executeResult = statementExecutor.execute(bodyNode, environment);
          break;
        }
      } catch(Exception e) {
        System.out.println(e);
      }
    }
    return executeResult;
  }

  /**
   * Check the result of condition expression of all branches.
   *
   * @param conditionResult result of condition result.
   * @return if the condition result if true or false
   */
  private boolean checkConditionResult(Object conditionResult) {
    if (conditionResult == null)
      return false; // TODO may be modify
    else if (conditionResult instanceof Boolean) {
      return (Boolean) conditionResult;
    } else if (conditionResult instanceof Integer) {
      return ((Integer) conditionResult) == 0 ? false : true;
    } else if (conditionResult instanceof Float) {
      return ((Float) conditionResult) == 0.0 ? false : true;
    } else {
      return true;
    }
  }
}