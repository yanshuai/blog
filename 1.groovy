import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.util.ImportUtils;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.event.type.EventDispatchOption;

UserManager userManager = ComponentAccessor.getUserManager();
IssueManager issueManager = ComponentAccessor.getIssueManager();
CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
CustomField customField = customFieldManager.getCustomFieldObjectByName("业务线");
MutableIssue mutableIssue = issue.getIssueObject();
def comp = mutableIssue.getCustomFieldValue(customField);
def str = comp.toString();
def reporter = mutableIssue.getReporter();
def assignee = mutableIssue.getAssignee();

switch (str) {
  case "1":
    assignee = userManager.getUserByKey("1");
    break;
  case "2":
    assignee = userManager.getUserByKey("2");
    break;
  default:
    assignee = userManager.getUserByKey("yanshuai");
}

mutableIssue.setAssignee(assignee);
issueManager.updateIssue(reporter, mutableIssue, EventDispatchOption.ISSUE_UPDATED, false);
