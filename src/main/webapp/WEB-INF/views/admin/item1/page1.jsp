<!--_meta 作为公共模版分离出去-->
<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/taglibs.jsp" %>
<html>
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
    <style type="text/css">
        #DataTables_Table_0_filter, #DataTables_Table_0_length {
            display: none;
        }
    </style>
    <title>够力金融-积分商城-后台管理</title>
</head>
<body>


<nav class="breadcrumb">
    <i class="Hui-iconfont">&#xe67f;</i>
    首页
    <span class="c-gray en">&gt;</span>
    商品类别管理
    <span class="c-gray en">&gt;</span>
    商品类别
    <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px"
       href="javascript:location.replace(location.href);" title="刷新">
        <i class="Hui-iconfont">&#xe68f;</i>
    </a>
</nav>
<div class="Hui-article">
    <article class="cl pd-20">

        <div class="row cl search-box" style="display: none;">
            <input class="input-text search-input" type="text"/>
            <input class="btn btn-success radius search-btn" type="button" value="搜索"/>
        </div>
        <div class="cl pd-5 bg-1 bk-gray mt-20">
            <span class="l">
                <a href="javascript:;" onclick="member_add('添加类别','/admin/item1/add','600','400')"
                   class="btn btn-primary radius">
                    <i class="Hui-iconfont">&#xe600;</i>
                    添加类别
                </a>
            </span>
            <span class="r">共有数据：<strong>88</strong> 条</span>
        </div>
        <div class="mt-20">
            <table class="table table-border table-bordered table-hover table-bg table-sort">
                <thead>
                <tr class="text-c">
                    <th width="5%">ID</th>
                    <th width="15%">类别名称</th>
                    <th width="10%">类别层级</th>
                    <th width="10%">LOGO</th>
                    <th width="10%">排序</th>
                    <th width="15%">状态</th>
                    <th width="">操作</th>
                </tr>
                </thead>
                <tbody id="content">


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

    //    $(function () {
    //        $('.table-sort').dataTable({
    //            "bStateSave": false,//状态保存
    //            "aoColumnDefs": [
    //                //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
    //                {"orderable": false, "aTargets": [0, 1, 2, 3, 4, 5, 6]}// 制定列不参与排序
    //            ]
    //        });
    //    });

    /*用户-添加*/
    function member_add(title, url, w, h) {
        //layer_show(title, url, w, h);

        if (title == null || title == '') {
            title = false;
        }
        ;
        if (url == null || url == '') {
            url = "404.html";
        }
        ;
        if (w == null || w == '') {
            w = 800;
        }
        ;
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        ;
        layer.open({
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false, //不固定
            maxmin: true,
            shade: 0.4,
            title: title,
            content: url,
            end: function () {
                reload();
            }
        });


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

            //发送删除请求ajax
            $.ajax({
                type: "get",
                //contentType: "application/json; charset=utf-8",// 必须
                //data: JSON.stringify({"c_id":""+id+""}),//转换为json对象
                url: "item1/deleteClassAjax/" + id,
                //dataType: "json",//必须
                async: false,
                success: function (data) {
                    if (data.errorCode == '200') {
                        $(obj).parents("tr").remove();
                        layer.msg('已删除!', {icon: 1, time: 1000});
                    }
                    if (data.errorCode == "500") {
                        layer.msg('删除失败，请重试!', {icon: 4, time: 1000});
                    }
                }
            });
        });
    }

    //重新加载页面
    function reload() {
        location.reload();
    }

    //获取父类型ajax请求
    $(function () {
        var html = '';
        $.ajax({
            type: "get",
            contentType: "application/json; charset=utf-8",// 必须
            //data: JSON.stringify({'name':"王五","password":"1234"}),//转换为json对象
            url: "item1/parentClassAjax",
            dataType: "json",//必须
            async: false,
            success: function (data) {
                if (data.errorCode == '200') {
                    $.each(data.item.Category, function (index, content) {
                        html += '<tr class="text-c" id = "parent_' + content.cId + '">\n' +
                            '                    <td>' + content.cId + '</td>\n' +
                            '                    <td><h4>' + content.cName + '</h4></td>\n' +
                            '                    <td>一级类</td>\n' +
                            '                    <td class="">\n' +
                            '                        <img class="public-items-imgaes" src="${ctx}/admin/images/test-images/7declare1497949733.jpg"/>\n' +
                            '                    </td>\n' +
                            '                    <td><input class="input-text public-sort-input" value="' + content.cSort + '" maxlength="4"/></td>\n' +
                            '                    <td class="td-status">\n' +
                            '                        <span class="label label-success radius">已通过</span>\n' +
                            '                    </td>\n' +
                            '                    <td class="td-manage">\n' +
                            '                        <a style="text-decoration:none" onclick="member_add(\'添加用户\',\'/admin/item1/add\',\'600\',\'400\')" href="javascript:;"\n' +
                            '                           title="添加子分类" class="btn btn-secondary radius">\n' +
                            '                            <i class="Hui-iconfont">&#xe610;</i>添加子分类\n' +
                            '                        </a>\n' +
                            '                        <a style="text-decoration:none" onClick="member_stop(this,\'10001\')" href="javascript:;"\n' +
                            '                           title="停用" class="btn btn-success radius">\n' +
                            '                            <i class="Hui-iconfont">&#xe631;</i>通过审核\n' +
                            '                        </a>\n' +
                            '                        <a title="编辑" href="javascript:;"   onclick="member_add(\'添加用户\',\'/admin/item1/add\',\'600\',\'400\')"\n' +
                            '                           class="ml-5 btn btn-warning radius" style="text-decoration:none">\n' +
                            '                            <i class="Hui-iconfont">&#xe6df;</i>修改\n' +
                            '                        </a>\n' +
                            '                        <a title="删除" href="javascript:;" onclick="member_del(this,\'' + content.cId + '\')" class="ml-5 btn btn-danger radius "\n' +
                            '                           style="text-decoration:none">\n' +
                            '                            <i class="Hui-iconfont">&#xe6e2;</i>删除\n' +
                            '                        </a>\n' +
                            '                    </td>\n' +
                            '                </tr>';
                    });
                }
                $('#content').html(html);
            }
        });
    });


    //获取子类型ajax请求


    $(function () {
        var html = '';
        $.ajax({
            type: "get",
            contentType: "application/json; charset=utf-8",// 必须
            //data: JSON.stringify({'name':"王五","password":"1234"}),//转换为json对象
            url: "item1/sonClassAjax",
            dataType: "json",//必须
            async: false,
            success: function (data) {
                if (data.errorCode == '200') {
                    $.each(data.item.sonClass, function (index, content) {
                        html = '<tr class="text-c">\n' +
                            '                    <td>' + content.cId + '</td>\n' +
                            '                    <td><span class="type-symbol">&nbsp;&nbsp; ├ </span>' + content.cName + '</td>\n' +
                            '                    <td>二级类</td>\n' +
                            '                    <td class="">\n' +
                            '                        <img class="public-items-imgaes" src="${ctx}/admin/images/test-images/7declare1497950456.jpg"/>\n' +
                            '                    </td>\n' +
                            '                    <td><input class="input-text public-sort-input" maxlength="4" value="1001"/></td>\n' +
                            '                    <td class="td-status">\n' +
                            '                        <span class="label label-error radius">未通过</span>\n' +
                            '                    </td>\n' +
                            '                    <td class="td-manage">\n' +
                            '                        <a style="visibility: hidden" style="text-decoration:none" onclick="member_add(\'添加用户\',\'/admin/item1/add\',\'600\',\'400\')" href="javascript:;"\n' +
                            '                           title="添加子分类" class="btn btn-secondary radius">\n' +
                            '                            <i class="Hui-iconfont">&#xe610;</i>添加子分类\n' +
                            '                        </a>\n' +
                            '                        <a style="text-decoration:none" onClick="member_stop(this,\'10001\')" href="javascript:;"\n' +
                            '                           title="停用" class="btn btn-success radius">\n' +
                            '                            <i class="Hui-iconfont">&#xe631;</i>通过审核\n' +
                            '                        </a>\n' +
                            '                        <a title="编辑" href="javascript:;"   onclick="member_add(\'添加用户\',\'/admin/item1/add\',\'600\',\'400\')"\n' +
                            '                           class="ml-5 btn btn-warning radius" style="text-decoration:none">\n' +
                            '                            <i class="Hui-iconfont">&#xe6df;</i>修改\n' +
                            '                        </a>\n' +
                            '                        <a title="删除" href="javascript:;" onclick="member_del(this,\'1\')" class="ml-5 btn btn-danger radius "\n' +
                            '                           style="text-decoration:none">\n' +
                            '                            <i class="Hui-iconfont">&#xe6e2;</i>删除\n' +
                            '                        </a>\n' +
                            '                    </td>\n' +
                            '                </tr>';


                        $('#parent_' + content.cParentId).after(html);
                    });
                }
                //$('#content').html(html);
            }
        });
    });
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>