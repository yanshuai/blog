---
title: Jira Post Function
perex: Jira Post Function Sample
layout: post
---

```java
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
CustomField customField = customFieldManager.getCustomFieldObjectByName("<customFieldName>");
MutableIssue mutableIssue = issue.getIssueObject();
def customFieldValue = mutableIssue.getCustomFieldValue(customField);
def str = customFieldValue.toString();
def reporter = mutableIssue.getReporter();
def assignee = mutableIssue.getAssignee();
mutableIssue.setAssignee(assignee);
issueManager.updateIssue(reporter, mutableIssue, EventDispatchOption.ISSUE_UPDATED, false);
```
