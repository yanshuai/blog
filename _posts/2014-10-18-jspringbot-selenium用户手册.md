---
title: jspringbot-selenium用户手册
perex: 介绍如何使用selenium-ide录制用户操作网站行为并导出jspringbot-selenium测试用例，然后对导出的用例进行修改/运行，并提供一些典型的用户案例。
layout: post
---

## jspringbot-selenium简介

jspringbot-selenium为QA提供基于关键字（可自定义）驱动的web ui测试能力，支持主流浏览器，包括Internet Explorer，Firefox，Chrome，Safari及Opera。

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

## 预备知识

* selenium-ide使用

* [xpath语法](http://www.w3schools.com/xpath/xpath_syntax.asp)

* [CSS Selector](http://www.w3schools.com/cssref/css_selectors.asp)

## 测试项目

### 初始化

如果尚未初始化测试项目工程，那么首先需要执行命令```mvn archetype:generate -B -DarchetypeGroupId=com.elong.jspringbot -DarchetypeArtifactId=jspringbot-selenium-archetype -DarchetypeVersion=RELEASE -DgroupId=com.elong.jspringbot -Dpackage=com.elong.jspringbot -DartifactId=jspringbot-selenium-<team> -Dversion=<version>```，其中```<team>```设置为所在团队的名称，```<version>```设置为对应被测项目的版本号。

### 项目结构

项目初始化完成以后，会在执行目录下产生jspringbot-selenium-```<team>```文件夹，该文件夹下目录结构为：

```
src/
├── main
│   ├── java
│   │   └── com
│   │       └── elong
│   │           └── jspringbot
│   │               └── keyword
│   │                   └── sample
│   │                       └── SayHello.java
│   └── resources
│       └── desc
│           └── SayHello.txt
└── test
    ├── resources
    │   ├── jspringbot.properties
    │   ├── log4j.properties
    │   └── spring
    │       └── jspringbot-global.xml
    └── robotframework
        ├── acceptance
        │   └── SayHello.txt
        └── selenium-resources.txt
```

用户可以在src/main目录下自定义关键字，具体操作见自定义关键字章节，定义好关键字以后，就可以在测试用例中使用定义好的关键字了。
用户要将测试用例存放在src/test/robotframework/acceptance目录下或者是其子目录下。对于在子目录下创建的测试用例文件，suite名为各层子目录用.连接的目录名，用例名为suite名.文件名的前缀。的比如在bing/search目录下创建的用例文件SayHello.txt，suite名为bing.search，用例名为bing.search.SayHello。
每个测试用例都是单个文件，根据用例间的依赖关系，测试用例可分为原子用例/顺序用例/笛卡尔集用例。

## 录制用例

录制用例采用标准的selenium-ide录制方式来进行操作。

## 导出用例

### 支持格式

* text格式

```robotframework
*** Settings ***
Resource    ../../../selenium-resources.txt

*** Variables ***
${URL}    http://192.168.14.51/user/login/

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

* csv格式 [TODO]

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

* html格式 [TODO]

!["jspringbot-selenium excel格式测试用例"](Mobile用户正常登录功能.png)

* excel格式 [TODO]

样式与html格式一致，不过提供了可编辑能力。

### 选择格式

打开selenium-ide，点击Options->Options->Formats，选择jspringbot，设置Format为想要导出的格式。如果选择的导出格式是text格式，那么需要设置Indent为想要缩进的空格数，支持2 spaces/4 spaces/8 spaces策略，推荐使用默认4个空格策略。对于别的格式，不需要关心缩进设置。注意，如果修改了格式或者缩进策略，需要重新启动firefox。

!["jspringbot导出用例选项"](jspringbot导出用例选项.png)

### 导出

* 如果想要只导出在Selenium IDE Test Case窗口中的单个case作为原子case，那么双击case名选中要导出的case，然后点击文件->Export Test Case As...->jspringbot，并设置导出的用例文件名为用例名＋后缀名。

* 如果想要导出在Selenium IDE Test Case窗口中的由多个case组合而成的顺序case，那么点击文件->Export Test Suite As...->jspringbot，并设置导出的文件名为顺序用例名＋后缀名。

```
| 导出格式 | 后缀名 |
| -------- | ------ |
| text     | .txt   |
| csv      | .csv   |
| html     | .html  |
| excel    | .xlsx  |
```

* 导出用例后，将用例存放在测试项目工程src/test/robotframework/acceptance目录下或子目录下，建议根据业务来创建对应的子目录作为case的suite名。

## 编辑用例

用户可以直接根据上述提到的格式规则手动编写用例，也可以在导出用例以后进行手动修改，还可以通过命令```mvn jspringbot:edison```启动web界面来进行编辑（Pending?），获取得到当前测试工程所有关键字及关键字文档，包含搜素功能(TODO)，当前先参考引用的第三方库[jspringbot search](http://jspringbot.org/search.html)提供的关键字来使用。

## 自定义关键字

jspringbot-selenium框架支持用户自定义关键字，包括两种方式，一种是实现Keyword接口，另外一种是在方法上打@Setup/@Teardown/@Verify标签。

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

定义好SayHello这个java类，实现Keyword接口的execute方法，在SayHello这个类上打标签@Component及@KeywordInfo。在@KeywordInfo这个标签下，需要设置属性name为关键字的名称Say Hello，设置parameters为关键字后跟着的参数名，description为关键字的描述，建议存放在src/main/resources/desc目录下，描述文件名为类名.txt，比如此关键字的描述文件存放在了src/main/resources/desc/SayHello.txt文件中。这样用户就可以在测试用例文件中使用Say Hello关键字了。

* 打标签[TODO]

采用japi框架原先所采用的@Setup/@Teardown/@Verify标签方式。

## 运行用例

* 执行```mvn clean integration-test```将运行所有的测试用例。

* 执行```mvn clean integration-test -Dtests=<test1>```将运行单个测试用例<test1>，<test1>为suite名.case名，case名不包含后缀。比如在src/test/robotframework/acceptance目录下，存在文件bing/sayHello.txt，要运行此sayHello用例，需要执行命令```mvn clean integration-test -Dtests=bing.sayHello```。
* 默认情况下，用例是在firefox中运行的，如果想要在别的浏览器中运行，需要在命令行下加上参数```-Dbrowser=<browser>```，其中```<browser>```为浏览器的名称，包括```ie/firefox/chrome/safari/opera```。

## 查看报告

运行完用例以后，将在标准屏幕上打印各个用例成功失败的信息，并且在输出目录下产生robotframework-reports目录存放执行报告。

* 标准屏幕打印如下：

!["robotframework stdout reports"](robotframework-stdout-reports.png)

* 在robotframework-reports目录下，report.html/log.html为用户需要查看的报告，TEST-acceptance.xml可作为持续集成引擎Jenkins的报告文件。在report.html中，可根据Tags/Type/Suites来过滤case，log.html包含了详细的统计信息及执行日志。

!["robotframework log reports"](robotframework-log-reports.png)

## 案例

1. [艺龙账号登陆测试用例](jspringbot-selenium-艺龙账号登陆测试用例.html)

2. [vdisk文件操作测试用例](jspringbot-selenium-vdisk文件操作测试用例.html)

3. 更多样例见samples工程。
