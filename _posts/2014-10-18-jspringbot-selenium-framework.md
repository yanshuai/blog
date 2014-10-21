---
title: jspringbot selenium framework
perex: jspringbot selenium框架设计
layout: post
---

### 总体设计

### 代码结构

### 部署
* jspringbot-common

```sh
mvn deploy
```

* jspringbot-maven-plugin

```sh
mvn deploy
```

* jspringbot-selenium

```sh
mvn deploy
```

* jspringbot-selenium-artifact

```sh
mvn archetype:create-from-project
cd target/generated-sources/archetype
mvn deploy
```
