# todomvp

谷歌的mvp项目的练习[谷歌给出的mvp架构](https://github.com/googlesamples/android-architecture)
### 简介

这个项目是很多衍生版本的基础，是一个实现了mvp设计模式并且没有架构框架的例子。他使用手动注入一个仓库来提供本地和远程数据源。回调处理异步任务。

<img src="https://github.com/googlesamples/android-architecture/wiki/images/mvp.png" alt="Diagram"/>

注意：在一个MVP上下文中“View”已经重载：

  * android.view.View类是“Android View”引用
  * 从MVP中的presenter接受指令的view被才称为“View”
  
### Fragments

使用fragment有两个原因：

  * Activity和Fragment的分离更适合实现MVP：Activity是创建和连接View与presenter的整体控制器
  * table布局或者在屏幕上存在多个View更适合使用Fragment

###关键概念

在这个app中有四个特性：

  * Tasks
  * TaskDetail
  * AddEditTask
  * statistics

每个特性具有：

  * 在view接口和presenter接口中定义约定（原文：A contract defining the view and the presenter）
  * Avtivity负责创建fragment和Presenters
  * Fradment实现view接口
  * presenter实现presenter接口
  
通常，presenter实现业务逻辑并依靠view做安卓界面工作
###依赖

  * 公共的安卓支持库（com.android.support.*）
  * 安卓测试支持库（Espresso，AndroidJUnitRunner...）
  * Mockito
  * Guava(空检测)

##特性

**复杂性，可理解性**

- 是否使用架构框架/库/工具：否
- 概念上的复杂性：低，它是一个单纯的mpv实现

**测试性**

- 单元测试性：高，presenters are unit tested as well as repositories and data sources.（不会翻译）
- 界面测试性：高，injection of fake modules allow for testing with fake data（这句也不会）

**代码指标**

考虑到传统的项目没有使用框架，这个例子引入了额外的类和接口：presenters，a repository，contracts，等等。所以代码的行数和类的数量在mvp中会更多

```
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                            46           1075           1451           3451
XML                             34             97            337            601
-------------------------------------------------------------------------------
SUM:                            80           1172           1788           4052
-------------------------------------------------------------------------------
```
**可维护性**

- 易于修改或添加一个特性：高
- 学习成本：低，特性很容易理解，责任清晰，开发者不需要熟悉额外的依赖
