---
title: jspringbot-selenium 百度阿拉丁搜索测试用例
perex: 介绍纯手动编写百度阿拉丁搜索的jspringbot-selenium测试用例
layout: post
---

### 简介

本文用于介绍采用纯手动方式，不录制，也不写xpath/css来编写百度阿拉丁搜索的jspringbot-selenium测试用例的具体步骤。

### 初始化项目

执行命令```mvn archetype:generate -B -DinteractiveMode=false -DarchetypeGroupId=com.elong.jspringbot -DarchetypeArtifactId=jspringbot-selenium-archetype -DarchetypeVersion=RELEASE -DgroupId=com.elong.jspringbot -Dpackage=com.elong.jspringbot -DartifactId=jspringbot-selenium-aladdin -Dversion=<version>```，其中```<version>```设置为aladdin的版本号。

### 创建suite

到```src/test/robotframework/acceptance```目录下，创建目录```baidu/阿拉丁```，```baidu.阿拉丁```即为测试用例的suite。

### 定义关键字

在```baidu/阿拉丁```目录下创建```resources.txt```，并在此文件中定义关键字，关键字的详细描述记录在```[Documentation]```中，所需的参数记录在```[Arguments]```中，关键字必须的返回值记录在```[Return]```中。

```robotframework
*** Settings ***
Resource    ../../../selenium-resources.txt

*** Keywords ***
找到页面中最邻近的文本输入框    [Arguments]    ${locator}
    [Documentation]    找到离指定${locator}最近的文本输入框
    ${locator} =    Get Locator Nearest To Identify Locator    ${locator}    //input[@type='text'] | //input[not(@type)]
    [Return]    ${locator}

找到页面中最邻近的按钮    [Arguments]    ${locator}    ${text}
    [Documentation]    找到离指定${locator}最近的按钮，其中按钮的value为${text}
    ${node} =    Join Strings To Single String    //*[@value='    ${text}    ']
    ${locator} =    Get Locator Nearest To Identify Locator    ${locator}    ${node}
    [Return]    ${locator}

找到指定文本对应的页面元素定位符    [Arguments]    ${text}
    [Documentation]    返回${text}对应的页面元素的定位符
    ${locator} =    GetSingleLocatorByText    ${text}
    [Return]    ${locator}
```

### 搜索股价测试用例编写

在```baidu/阿拉丁```目录下创建文件```股价搜索.txt```，在此文件中增加“百度搜索百度股价”测试用例，在```[Documentation]```区域添加case的详细描述，在```[Tags]```区域添加该case的标签。

```robotframework
*** Settings ***
Resource    ./resources.txt

*** Test Case ***
百度搜索百度股价
    [Documentation]    测试百度阿拉丁搜索百度股价功能，测试步骤：
    ...                1. 打开百度网页
    ...                2. 找到设置链接的页面元素定位符${设置链接}
    ...                3. 找到页面中最邻近${设置链接}的按钮${百度一下按钮}
    ...                4. 找到${百度一下按钮}最临近的文本输入框${百度输入框}
    ...                5. 在${百度输入框}中输入检索信息百度股价
    ...                6. 点击${百度一下按钮}发起检索
    [Tags]    search    aladdin    股价
    open    http://www.baidu.com/
    ${设置链接} =    找到指定文本对应的页面元素定位符    设置
    ${百度一下按钮} =    找到页面中最邻近的按钮    ${设置链接}    百度一下
    ${百度输入框} =    找到页面中最邻近的文本输入框    ${百度一下按钮}
    type    ${百度输入框}    百度股价
    click    ${百度一下按钮}
```

### 搜索股价测试用例运行

在jspringbot-selenium-aladdin的顶级目录下，执行命令```mvn clean integration-test -Dtests=baidu.阿拉丁.股价搜索```，即可执行“股价搜索”测试用例，执行完成以后，将生成执行报告。

### 查看报告

!["jspringbot selenium 百度阿拉丁搜索测试用例报告"](jspringbot-selenium-百度阿拉丁搜索测试用例报告.png)
