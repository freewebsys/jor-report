###1，项目介绍参考：
http://www.oschina.net/p/jor

简表(JOR)，是一款开源的报表工具，完全java实现，核心代码来自于国内一线报表工具品牌杰表.2008 。

提起开源的报表工具，不能不说说jaspereport，jasperreport作为著名的开源报表工具，在java程序员中一直享有盛名。但由于其设计主要是针对西式的简单报表，设计起复杂的中式报表来，有点力不从心，所以，在国内的项目中能成功应用的例子并不多。JOR，作为能做复杂报表的开源报表工具，可以弥补jasperreport的不足。

简表(JOR) 的License已发布，可免费用于商业项目中。

简表(JOR) 的特点：

√ 完全开源
√ 支持可视化地设计报表
√ 支持复杂的交叉表，如多级行上、列上分组；分组不平衡的交叉表等
√ 支持复杂的跨组运算，如同比、占比、环比、排名等
√ 支持图片，图片源可以来自数据库、本地文件、网络、classpath、内嵌
√ 支持脚本，脚本可以扩展
√ 支持简单列表、主从报表、标签报表、分栏报表、套打报表
√ 支持jsp tag、ajax、web form 集成
√ 支持 HTML,PDF,EXCEL导出
√ 支持统计图
√ 支持所有主流数据库，如 Oracle、DB2、Sql Server 、Mysql等
√ 支持任何的j2ee应用服务器，如Tomcat、Apusic、 WebSphere、WebLogic等
√ 支持Windows、Linux、Unix、Mac os等操作系统
√ JDK1.7 以上版本适用

项目官网：
http://www.jatools.com/jor/


###2，关于工程

这个是从官网拷贝的代码，不修改包结构，不修改log。
将代码拆分成两个部分，一个是jor-report-gui，一个是jor-report-web。
代码已经修改成支持jdk1.7版本。

主要把代码梳理下能跑通，切换成maven工程。


###3，关于版权

项目license参考网站：
http://www.jatools.com/jor/license.htm

项目严格遵循其开源协议。

jor-report-gui 工程main函数在 jatools.designer.App，即可运行设计器。