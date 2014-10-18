---
title: jspringbot-selenium vdisk文件操作测试用例
perex: 介绍“vdisk文件操作测试用例”录制->导出->编写->运行->查看报告整个工作流程
layout: post
---

```robotframework
*** Settings ***
Resource    ../../selenium-resources.txt
Test Setup    deleteAllVisibleCookies

*** Variables ***
${username}    sartisty@gmail.com
${nickname}    闫帅
@{invalid_username}    alex    bob    fred    dave
@{invalid_password}    alex_pass    bob_pass    fred_pass    dave_pass

*** Test Cases ***
正确艺龙账号登录
    [Tags]    https协议登录    正常用例
    open    http://www.elong.com/
    mouseOver    id=user_display_login
    clickAndWait    css=div.new_btnLogin > a
    type    id=UserName    ${username}
    click    css=div#password_tip > input
    type    id=PassWord    ${password}
    clickAndWait    link=登录
    page should not contain    您输入的用户名和密码不匹配，请重新输入！
    page should contain    我的艺龙
    captureEntirePageScreenshot
    [Teardown]    log    使用正确的用户名/密码登录成功

错误艺龙账号登录
    [Tags]    https协议登录    异常用例    数据组合
    open    http://www.elong.com/
    mouseOver    id=user_display_login
    clickAndWait    css=div.new_btnLogin > a
    : FOR    ${i}    IN RANGE    0    3
    \    ${user} =    Get From List    ${invalid_username}    ${i}
    \    ${pass} =    Get From List    ${invalid_password}    ${i}
    \    type    id=UserName    ${user}
    \    ${isVisible} =    Is Element Visible    css=div#password_tip > input
    \    Run Keyword If    ${isVisible}    click    css=div#password_tip > input
    \    type    id=PassWord    ${pass}
    \    clickAndWait    link=登录
    \    page should contain    您输入的用户名和密码不匹配，请重新输入！
    \    captureEntirePageScreenshot
```
