# ygou
基于Android的二手货交易APP，实现从用户注册到商品发布、出售、订单及评价一系列功能实现。  
系统分为Android前端和后端。前端负责页面展示和跳转，后端负责请求接收和数据存取。  

# 系统架构
[简单架构图] https://github.com/Apathyyi/ygou-client/blob/master/Architecture.png
# Android端介绍
android端使用fragmention + retrofit进行页面布局和请求。  
[fragmention] https://github.com/YoKeyword/Fragmentation  
[retrofit] https://square.github.io/retrofit/  

APP功能包含用户注册与登录，个人资料管理；使用极光消息验证和IM和第三方登录，实现用户注册和买卖双方的交流。   

商品发布和求购功能，用户发布或者求购对应的商品经过后端管理员审核通过便可在APP中浏览。  

商品浏览功，APP实现多分类，细致化的商品浏览机制，同时对商品筛选和和推荐以及搜索进行了不同的实现。

商品购买以及订单流转，商品可添加购物车以及实现第三方支付，并对订单进行分类管理以及各订单状态下的买卖双方的不同操作提示，商品及APP的微信分享功能。

# 后端
后端采用spring boot + mybatis + MySQL，注解开发，有个简单的页面表格进行审核，可自行增加内容  
[后端链接] https://github.com/Apathyyi/ygou-server
