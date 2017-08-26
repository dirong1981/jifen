<!--_meta 作为公共模版分离出去-->
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <!--公共-->
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/lib/Hui-iconfont/1.0.8/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/css/style.css"/>

    <title>够力金融-积分商城-后台管理</title>
</head>
<body>
<header class="navbar-wrapper">
    <div class="navbar navbar-fixed-top">
        <div class="container-fluid cl"><a class="logo navbar-logo f-l mr-10 hidden-xs" href="/aboutHui.shtml">够力金融-积分商城</a>
            <a aria-hidden="false" class="nav-toggle Hui-iconfont visible-xs" href="javascript:;">&#xe667;</a>
            <nav id="Hui-userbar" class="nav navbar-nav navbar-userbar hidden-xs">
                <ul class="cl">
                    <li>超级管理员</li>
                    <li class="dropDown dropDown_hover"><a href="#" class="dropDown_A">${sessionScope.session_admin}<i class="Hui-iconfont">&#xe6d5;</i></a>
                        <ul class="dropDown-menu menu radius box-shadow">
                            <li><a href="javascript:;" onClick="myselfinfo()">个人信息</a></li>
                            <li><a href="#">切换账户</a></li>
                            <li><a href="/admin/logout">退出</a></li>
                        </ul>
                    </li>
                    <li id="Hui-skin" class="dropDown right dropDown_hover"><a href="javascript:;" class="dropDown_A"
                                                                               title="换肤"><i class="Hui-iconfont"
                                                                                             style="font-size:18px">&#xe62a;</i></a>
                        <ul class="dropDown-menu menu radius box-shadow">
                            <li><a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a></li>
                            <li><a href="javascript:;" data-val="blue" title="蓝色">蓝色</a></li>
                            <li><a href="javascript:;" data-val="green" title="绿色">绿色</a></li>
                            <li><a href="javascript:;" data-val="red" title="红色">红色</a></li>
                            <li><a href="javascript:;" data-val="yellow" title="黄色">黄色</a></li>
                            <li><a href="javascript:;" data-val="orange" title="橙色">橙色</a></li>
                        </ul>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</header>
<aside class="Hui-aside">
    <div class="menu_dropdown bk_2">
        <dl >
            <dt><i class="Hui-iconfont">&#xe616;</i> 商品类别管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="item1" target="right-main" title="商品类别">商品类别</a></li>
                </ul>
            </dd>
        </dl>

        <dl >
            <dt><i class="Hui-iconfont">&#xe616;</i> 商品管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="item2/page1"target="right-main" title="资讯管理">商品列表</a></li>
                </ul>
            </dd>
        </dl>

        <dl >
            <dt><i class="Hui-iconfont">&#xe616;</i> 商户管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="article-list.html"target="right-main" title="资讯管理">商户列表</a></li>
                    <li><a href="article-list.html"target="right-main" title="资讯管理">添加商户</a></li>
                </ul>
            </dd>
        </dl>

        <dl >
            <dt><i class="Hui-iconfont">&#xe616;</i> 页面模块管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="item4/page1.jsp"target="right-main" title="页面模块列表">页面模块列表</a></li>
                    <li><a href="item4/page2.jsp"target="right-main" title="添加模块活动">添加模块活动</a></li>

                </ul>
            </dd>
        </dl>

        <dl >
            <dt><i class="Hui-iconfont">&#xe616;</i> 订单管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="item5/page1.jsp"target="right-main" title="线上订单">线上订单</a></li>
                    <li><a href="item5/page2.jsp"target="right-main" title="线下订单">线下订单</a></li>
                    <li><a href="item5/page3.jsp"target="right-main" title="积分转赠">积分转赠</a></li>
                </ul>
            </dd>
        </dl>


        <dl >
            <dt><i class="Hui-iconfont">&#xe616;</i> 用户管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a href="item6/page1.jsp"target="right-main" title="用户管理">用户管理</a></li>
                    <li><a href="javascript:;"  onclick="handle_order('修改密码','change-pwd.jsp','600','300')" target="right-main" title="资讯管理">修改密码</a></li>
                </ul>
            </dd>
        </dl>







    </div>
</aside>
<div style="height:800px;padding-left: 200px">
    <iframe name="right-main" id="rightMain" src="item1" frameborder="0" border="0" marginwidth="0"
            width="100%" height="100%"
            marginheight="0" scrolling="yes" seamless>
    </iframe>
</div>
<!--公共-->
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/static/h-ui/js/H-ui.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/static/h-ui.admin/js/H-ui.admin.page.js"></script>

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript">

    /*处理订单*/
    function handle_order(title, url, w, h) {
        layer_show(title, url, w, h);
    }

</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>