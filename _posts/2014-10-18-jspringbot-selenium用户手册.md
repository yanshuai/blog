---
title: jspringbot-selenium用户手册
perex: 介绍如何使用selenium-ide录制用户操作网站行为并导出jspringbot-selenium测试用例，然后对导出的用例进行修改/运行，并提供一些典型的用户案例。
layout: post
---

## jspringbot-selenium简介
jspringbot-selenium为QA提供基于关键字（可自定义）驱动的web ui测试能力，支持主流浏览器，包括Internet Explorer，Firefox，Chrome，Safiri及Opera。

## 预装环境
* firefox + selenium-ide + webdriver
* jspringbot-selenium-formats.js

安装firefox及selenium-ide插件后，点击selenium-ide按钮，选择Options->Options->Formats，然后点击Add按钮，设置Name of the format为jspringbot，设置source content（设置完成后需要重启firefox）为jspringbt-selenium-formats.js的文件内容，其中jspringbot-selenium-formats.js文件内容如下：

```javascript
var options = {
  indent: '    ',
  format: 'text'
};

var configForm =
  '<description>Format</description>' +
  '<menulist id="options_format">' +
    '<menupopup>' +
      '<menuitem label="text" value="text"></menuitem>' +
      '<menuitem label="csv" value="csv"></menuitem>' +
      '<menuitem label="excel" value="xlsx"></menuitem>' +
      '<menuitem label="html" value="html"></menuitem>' +
    '</menupopup>' +
  '</menulist>' +
  '<description>Indent</description>' +
  '<menulist id="options_indent">' +
    '<menupopup>' +
      '<menuitem label="2 spaces" value="  "></menuitem>' +
      '<menuitem label="4 spaces" value="    "></menuitem>' +
      '<menuitem label="8 spaces" value="        "></menuitem>' +
    '</menupopup>' +
  '</menulist>';

function formatCommands(commands, baseURL) {
  if (baseURL.endsWith('/')) {
    baseURL = baseURL.substring(0, baseURL.length - 1);
  }
  var result = '';
  for (var i = 0; i < commands.length; i++) {
    var command = commands[i];
    if (command.type === 'command') {
      var keyword = command.command;
      if (keyword === null) {
      	keyword = command.command;
      }
      var target = command.target;
      if (keyword === 'open') {
        target = baseURL + target;
      }
      result += options.indent + keyword;
      if (target !== null && target !== '') {
        result += options.indent + target;
      }
      if (command.value !== null && command.value !== '') {
        result += options.indent + command.value;
      }
      result += '\n';
    }
  }
  return result;
}

function parse(testCase, source) {
  var doc = source;
  var commands = [];
  while (doc.length > 0) {
    var line = /(.*)(\r\n|[\r\n])?/.exec(doc);
    var array = line[1].split(options.indent);
    if (array.length >= 3) {
      var command = new Command();
      command.command = array[0];
      command.target = array[1];
      command.value = array[2];
      commands.push(command);
    }
    doc = doc.substr(line[0].length);
  }
  testCase.setCommands(commands);
}

function format(testCase, name) {
  var result = '*** Settings ***\n' +
    'Resource' + options.indent +
    '../selenium-resources.txt\n\n' +
    '*** Test Cases ***\n';

  var baseURL = testCase.getBaseURL();
  commands = formatCommands(testCase.commands, baseURL);
  return result + testCase.getTitle() + '\n' + commands + '\n';
}

function formatSuite(testSuite, filename) {
  var formattedSuite = '*** Settings ***\n' +
    'Resource' + options.indent +
    '../selenium-resources.txt\n\n' +
    '*** Test Cases ***\n';

  for (var i = 0; i < testSuite.tests.length; ++i) {
    if (0 !== i) {
      formattedSuite += '\n';
    }
    var testCase = testSuite.tests[i];
    var testName = testCase.getTitle();
    var testCaseContent = testCase.content;
    formattedSuite += testName + '\n';
    formattedSuite += formatCommands(testCaseContent.commands, testCaseContent.getBaseURL());
  }

  return formattedSuite;
}
```

* java
* maven

## 项目初始化
如果尚未初始化测试项目工程，那么首先需要执行命令```shell
mvn archetype:generate -DgroupId=com.elong.jspringbot -DartifactId=jspringbot-selenium-artifact-plugin -Dversion=RELEASE
```

## 录制用例
录制用例采用标准的selenium-ide录制方式来进行操作。

## 导出用例
### 支持格式
* text格式

```robotframework
*** Settings ***
Resource    ../../../selenium-resources.txt

*** Variables ***
${URL}    http://192.168.14.51/user/login

*** Keywords ***
添加Mobile请求Header    [Arguments]    ${DeviceType}    ${ClientType}    ${Version}    ${DeviceId}    ${ChannelId}
    Add HTTP Request Header    DeviceType    ${DeviceType}
    Add HTTP Request Header    ClientType    ${ClientType}
    Add HTTP Request Header    Version    ${Version}
    Add HTTP Request Header    DeviceId    ${DeviceId}
    Add HTTP Request Header    ChannelId    ${ChannelId}

*** Test Cases ***
Mobile用户正常登录功能
    New HTTP Session
    Create HTTP Post Request    ${URL}
    添加Mobile请求Header    0    1    200    TestWeb    ew
    ${req} =    Join Strings To Json String
    ...    {
    ...      "verifyCode": "",
    ...      "loginNo": "49",
    ...      "password": "111111"
    ...    }
    ${encrypt_req} =    AES Encrypt    ${req}    1234567890123456
    Add HTTP Request Parameter    req    ${encrypt_req}
    Invoke HTTP Request
    HTTP Response Status Code Should Be Equal To    200
    HTTP Response Should Be JSON
    ${actual_resp} =    Get HTTP Response
    Set JSON String    ${actual_resp}
    ${cardNo} =    Get JSON Value    $.CardNo
    Should Be Equal As Integers    ${cardNo}    49
```

* csv格式

```text
*** Settings ***,,,,,,
Resource,../../selenium-resource,,,,,
,,,,,,
*** Variables ***,,,,,,
${URL},http://192.168.14.51/user/login,,,,,
,,,,,,
***Keywords ***,,,,,,
添加Mobile请求Header,[Arguments],${DeviceType},${ClientType},${Version},${DeviceId},${ChannelId}
,Add HTTP Request Header,DeviceType,${DeviceType},,,
,Add HTTP Request Header,ClientType,${ClientType},,,
,Add HTTP Request Header,Version,${Version},,,
,Add HTTP Request Header,DeviceId,${DeviceId},,,
,Add HTTP Request Header,ChannelId,${ChannelId},,,
,,,,,,
*** Test Case ***,,,,,,
Mobile用户正常登录功能,,,,,,
,New HTTP Session,,,,,
,Create HTTP Post Request,${URL},,,,
,添加Mobile请求Header,0,1,200,TestWeb,ew
,${req} =,Join Strings To Json String,,,,
,…,{,,,,
,…,"  “verifyCode”: “”,",,,,
,…,"  “loginNo”: “49”,",,,,
,…,  “password”: “111111”,,,,
,…,},,,,
,${encrypt_req} =,AES Encrypt,${req},1234567890123460,,
,Add HTTP Request Parameter,req,${encrypt_req},,,
,Invoke HTTP Request,,,,,
,HTTP Response Status Code Should Be Equal To,200,,,,
,HTTP Response Should Be JSON,,,,,
,${actual_resp} =,Get HTTP Response,,,,
,Set JSON String,${actual_resp},,,,
,${cardNo} =,Get JSON Value,$.CardNo,,,
```

* html格式

<img src="Mobile用户正常登录功能.png" alt="jspringbot-selenium excel格式测试用例" />

* excel格式

样式与html格式一致，不过提供了可编辑能力。

### 选择格式

打开selenium-ide，点击Options->Options->Formats，选择jspringbot，设置Format为想要导出的格式。如果选择的导出格式是text格式，那么需要设置Indent为想要缩进的空格数，支持2 spaces/4 spaces/8 spaces策略，推荐使用默认4个空格策略。

<img src="jspringbot导出用例选项.png" alt="jspringbot导出用例选项" />

### 导出
* 如果想要只导出单个case，那么在Selenium IDE的Test Case窗口双击case名选中要导出的case，然后点击文件->Export Test Case As...->jspringbot，并设置导出的用例文件名为用例名＋后缀名。
* 如果想要导出整个suite，那么点击文件->Export Test Suite As...->jspringbot，并设置导出的testsuite文件名为suite name＋后缀名。

| 导出格式 | 后缀名 |
| -------- | ------ |
| text     | .txt   |
| csv      | .csv   |
| html     | .html  |
| excel    | .xlsx  |

## 编写用例

## 自定义关键字
jspringbot-selenium框架支持用户自定义关键字，包括两种方式，一种是实现Keyword接口，另外一种是在方法上打@Setup @Teardown @Verify标签。

* 实现Keyword接口
用户自定义关键字默认需要存放在com.elong.jspringbot.keyword包或者子包下，并实现Keyword接口：

```java
package org.jspringbot;

public interface Keyword {

    public Object execute(Object[] os) throws Exception;
}
```

以sample工程SayHello为例：

```java
package com.elong.jspringbot.keyword.sample;

import org.jspringbot.Keyword;
import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Say Hello",
        parameters = {},
        description = "classpath:desc/SayHello.txt"
)
public class SayHello implements Keyword {

    public Object execute(Object[] params) throws Exception {
        System.out.println("hello, world!");
        return null;
    }
}
```

定义好SayHello这个java类，实现Keyword接口的execute方法，在SayHello这个类上打标签@Component及@KeywordInfo。在@KeywordInfo这个标签下，需要设置属性name为关键字的名称Say Hello，设置parameters为关键字后跟着的参数名，description为关键字的描述，建议存放在src/main/resources/desc目录下，描述文件名为类名.txt，比如此关键字的描述文件存放在了src/main/resources/desc/SayHello.txt文件中。

* 打标签[TODO]

采用japi框架原先所采用的@Setup/@Teardown/@Verify标签方式。

## 运行用例

## 案例
1. [艺龙账号登陆测试用例](jspringbot-selenium-艺龙账号登陆测试用例.html)
2. [vdisk文件操作测试用例](jspringbot-selenium-vdisk文件操作测试用例.html)
