<!--_meta 作为公共模版分离出去-->
<!DOCTYPE HTML><%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ include file="/WEB-INF/views/common/taglibs.jsp"%><html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/lib/Hui-iconfont/1.0.8/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/css/public.css"/>
    <title>够力金融-积分商城-后台管理</title>

    <style type="text/css">
        #DataTables_Table_0_filter, #DataTables_Table_0_length {
            display: none;
        }
    </style>
</head>
<body>


<nav class="breadcrumb">
    <i class="Hui-iconfont">&#xe67f;</i>
    首页
    <span class="c-gray en">&gt;</span>
    用户中心
    <span class="c-gray en">&gt;</span>
    会员列表
    <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px"
       href="javascript:location.replace(location.href);" title="刷新">
        <i class="Hui-iconfont">&#xe68f;</i>
    </a>
</nav>
<div class="Hui-article">
    <article class="cl pd-20">
        <div class="row cl search-box">

            <div class="col-xs-2" style="padding-left: 0">
                <span class="select-box">
                    <select class="select sort-select" size="1" name="city">
                        <option value="0" selected>上架</option>
                        <option value="1">下架</option>
                    </select>
				</span>
            </div>
            <div class="col-xs-4">
                <input class="input-text search-input" type="text"/>
                <input class="btn btn-success radius search-btn" type="button" value="搜索"/>
            </div>
        </div>
        <div class="cl pd-5 bg-1 bk-gray mt-20">
            <span class="l">
                <a href="javascript:;" onclick="picture_add('添加商品','page1-add')"
                   class="btn btn-primary radius">
                    <i class="Hui-iconfont">&#xe600;</i>
                    添加商品
                </a>
            </span>
            <span class="r">共有数据：<strong>88</strong> 条</span></div>
        <div class="mt-20">
            <table class="table table-border table-bordered table-hover table-bg table-sort">
                <thead>
                <tr class="text-c">
                    <th width="5%">ID</th>
                    <th width="15%">图片</th>
                    <th width="30%">商品名称</th>
                    <th width="10%">价格</th>
                    <th width="10%">状态</th>
                    <th width="">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr class="text-c">
                    <td>1</td>
                    <td>
                        <img class="public-items-imgaes" src="${ctx}/admin/images/test-images/7declare1497949733.jpg"/>
                    </td>
                    <td>小米6 128gb陶瓷</td>
                    <td>4500分</td>
                    <td class="td-status">
                        <span class="label label-success radius">上架</span>
                    </td>
                    <td class="td-manage">
                        <a class="ml-5 btn btn-secondary radius" style="text-decoration:none"  onClick="change_password('审核','change-password.html','10001','600','270')" href="javascript:;" title="审核">
                            <i class="Hui-iconfont">&#xe63f;</i>审核
                        </a>
                        <a class="ml-5 btn btn-success radius" title="编辑" href="javascript:;" onclick="picture_add('添加商品','page1-add.jsp')" style="text-decoration:none">
                            <i class="Hui-iconfont">&#xe6df;</i>编辑
                        </a>

                        <a  class="ml-5 btn btn-danger radius"  title="删除" href="javascript:;" onclick="member_del(this,'1')" class="ml-5" style="text-decoration:none">
                            <i class="Hui-iconfont">&#xe6e2;</i>删除
                        </a>
                    </td>
                </tr>



                <tr class="text-c">
                    <td>2</td>
                    <td>
                        <img class="public-items-imgaes" src="${ctx}/admin/images/test-images/7declare1497949733.jpg"/>
                    </td>
                    <td>小米note plus 128gb陶瓷</td>
                    <td>4500分</td>
                    <td class="td-status">
                        <span class="label label-error radius">下架</span>
                    </td>
                    <td class="td-manage">
                        <a class="ml-5 btn btn-secondary radius" style="text-decoration:none"  onClick="change_password('审核','change-password.html','10001','600','270')" href="javascript:;" title="审核">
                            <i class="Hui-iconfont">&#xe63f;</i>审核
                        </a>
                        <a class="ml-5 btn btn-success radius" title="编辑" href="javascript:;" onclick="picture_add('添加商品','page1-add.jsp')" style="text-decoration:none">
                            <i class="Hui-iconfont">&#xe6df;</i>编辑
                        </a>

                        <a  class="ml-5 btn btn-danger radius"  title="删除" href="javascript:;" onclick="member_del(this,'1')" class="ml-5" style="text-decoration:none">
                            <i class="Hui-iconfont">&#xe6e2;</i>删除
                        </a>
                    </td>
                </tr>


                </tbody>
            </table>
        </div>
    </article>
</div>

<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/static/h-ui/js/H-ui.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/static/h-ui.admin/js/H-ui.admin.page.js"></script>
<!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
    $(function () {
        $('.table-sort').dataTable({
            "bStateSave": true,//状态保存
            "aoColumnDefs": [
                //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
                {"orderable": false, "aTargets": [0, 1, 2, 3, 4, 5]}// 制定列不参与排序
            ]
        });
    });
    function picture_add(title,url){
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*用户-查看*/
    function member_show(title, url, id, w, h) {
        layer_show(title, url, w, h);
    }
    /*用户-停用*/
    function member_stop(obj, id) {
        layer.confirm('确认要停用吗？', function (index) {
            $(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_start(this,id)" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe6e1;</i></a>');
            $(obj).parents("tr").find(".td-status").html('<span class="label label-defaunt radius">已停用</span>');
            $(obj).remove();
            layer.msg('已停用!', {icon: 5, time: 1000});
        });
    }

    /*用户-启用*/
    function member_start(obj, id) {
        layer.confirm('确认要启用吗？', function (index) {
            $(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_stop(this,id)" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>');
            $(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已启用</span>');
            $(obj).remove();
            layer.msg('已启用!', {icon: 6, time: 1000});
        });
    }
    /*用户-编辑*/
    function member_edit(title, url, id, w, h) {
        layer_show(title, url, w, h);
    }
    /*密码-修改*/
    function change_password(title, url, id, w, h) {
        layer_show(title, url, w, h);
    }
    /*用户-删除*/
    function member_del(obj, id) {
        layer.confirm('确认要删除吗？', function (index) {
            $(obj).parents("tr").remove();
            layer.msg('已删除!', {icon: 1, time: 1000});
        });
    }
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>