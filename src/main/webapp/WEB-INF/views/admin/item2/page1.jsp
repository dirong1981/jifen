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
        <div class="row cl search-box" style="display: none">

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
                <a href="javascript:;" onclick="product_add('添加商品','page1-add')"
                   class="btn btn-primary radius">
                    <i class="Hui-iconfont">&#xe600;</i>
                    添加商品
                </a>
            </span>
            <span class="r">共有数据：<strong>88</strong> 条</span></div>
        <div class="mt-20">
            <table class="table table-border table-bordered table-hover table-bg table-sort" id="allProduct">
                <thead>
                <tr class="text-c">
                    <th width="5%">序号</th>
                    <th width="5%">商品id</th>
                    <th width="15%">图片</th>
                    <th width="30%">商品名称</th>
                    <th width="10%">价格</th>
                    <th width="10%">状态</th>
                    <th width="">操作</th>
                </tr>
                </thead>

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
        var t = $('#allProduct').DataTable({
            ajax: {
                //指定数据源
                url: "/admin/item2/allProductAjax"
            },
            //每页显示三条数据
//            pageLength: 3,
            processing: true,
            //deferRender: true,
            bStateSave: true,
            columns: [
                {
                    className: "text-c",
                    "data": null //此列不绑定数据源，用来显示序号
                },
                {
                    "data": "pId" //此列不绑定数据源，用来显示序号
                },
                {
                    className: "text-c",
                    "data": "pLogo"
                },
                {
                    className: "text-c",
                    "data": "pName"
                },
                {
                    className: "text-c",
                    "data": "pIntegral"
                },
                {
                    className: "text-c td-status",
                    "data": "pState"
                },
                {
                    className: "text-c td-manage",
                    "data": "pCreator"
                }
            ],
            columnDefs: [
                {
                    "render": function(data, type, row, meta) {
                        //渲染 把数据源中的标题和url组成超链接
                        return '<img class="public-items-imgaes" src="${ctx}/image/product-images/'+data+'"/>';
                    },
                    //指定是第三列
                    "targets": 2

                },
                {
                    "visible": false,
                    "targets": 1

                },
                {
                    "render": function(data, type, row, meta) {
                        //渲染 把数据源中的标题和url组成超链接
                        return data + '分';
                    },
                    //指定是第三列
                    "targets": 4

                },
                {
                    "render": function(data, type, row, meta) {
                        //渲染 把数据源中的标题和url组成超链接
                        var state = '';
                        if(data == 1){
                            state = '<span class="label label-success radius">上架</span>';
                        }else{
                            state = '<span class="label label-error radius">下架</span>';
                        }
                        return state;
                    },
                    //指定是第三列
                    "targets": 5

                },
                {
                    "render": function(data, type, row, meta) {

                        var check = '';
                        if(row.pState == 1){
                            check = '<a class="ml-5 btn btn-secondary radius" style="text-decoration:none"  onClick="product_stop(this,\''+row.pId+'\')" href="javascript:;" title="下架">'+
                                '<i class="Hui-iconfont">&#xe63f;</i>下架'+
                                '</a>';
                        }else{
                            check = '<a class="ml-5 btn btn-secondary-outline radius" style="text-decoration:none"  onClick="product_start(this,\''+row.pId+'\')" href="javascript:;" title="上架">'+
                                '<i class="Hui-iconfont">&#xe63f;</i>上架'+
                                '</a>';
                        }


                        //渲染 把数据源中的标题和url组成超链接
                        return check +
                        '<a class="ml-5 btn btn-success radius" title="编辑" href="javascript:;" onclick="product_edit(\'修改商品信息\',\'/admin/item2/updateProduct/'+row.pId+'\')" style="text-decoration:none">'+
                         '   <i class="Hui-iconfont">&#xe6df;</i>编辑'+
                        '</a>'+
                        '<a  class="ml-5 btn btn-danger radius"  title="删除" href="javascript:;" onclick="product_del(this,\''+row.pId+'\')" class="ml-5" style="text-decoration:none">'+
                         '   <i class="Hui-iconfont">&#xe6e2;</i>删除'+
                        '</a>';
                    },
                    //指定是第三列
                    "targets": 6
                }
            ]
        });


        t.on('order.dt search.dt',
            function() {
                t.column(0, {
                    "search": 'applied',
                    "order": 'applied'
                }).nodes().each(function(cell, i) {
                    cell.innerHTML = i + 1;
                });
            }).draw();


    });



    //打开添加窗口
    function product_add(title,url){
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }




    /*商品下架*/
    function product_stop(obj, id) {
        layer.confirm('确认要下架该商品吗？', function (index) {
            jQuery($(obj).parents("tr").find(".td-manage")).prepend('&nbsp;<a style="text-decoration:none" onClick="product_start(this,\''+id+'\')" href="javascript:;" title="上架" class="btn btn-secondary-outline radius"><i class="Hui-iconfont">&#xe63f;</i>上架</a>');
            $(obj).parents("tr").find(".td-status").html('<span class="label label-defaunt radius">下架</span>');
            $(obj).remove();
            $.ajax({
                type:"get",
                contentType :  "application/json; charset=utf-8",// 必须
                //data: JSON.stringify({'name':"王五","password":"1234"}),//转换为json对象
                url:"/admin/item2/stopProductAjax/"+id,
                dataType: "json",//必须
                async:  false,
                success:function(data){
                    if(data.errorCode == '200'){
                        layer.msg('已下架!', {icon: 5, time: 1000});
                    }
                }
            });

        });
    }


    /*商品上架*/
    function product_start(obj, id) {
        layer.confirm('确认要上架该商品吗？', function (index) {
            jQuery($(obj).parents("tr").find(".td-manage")).prepend('&nbsp;<a style="text-decoration:none" onClick="product_stop(this,\''+id+'\')" href="javascript:;" title="下架" class="btn btn-secondary radius"><i class="Hui-iconfont">&#xe63f;</i>下架</a>');
            $(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">上架</span>');
            $(obj).remove();
            $.ajax({
                type:"get",
                contentType :  "application/json; charset=utf-8",// 必须
                //data: JSON.stringify({'name':"王五","password":"1234"}),//转换为json对象
                url:"/admin/item2/startProductAjax/"+id,
                dataType: "json",//必须
                async:  false,
                success:function(data){
                    if(data.errorCode == '200'){
                        layer.msg('已上架!', {icon: 1, time: 1000});
                    }
                }
            });

        });
    }


    /*用户-编辑*/
    function product_edit(title, url, w, h) {

        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*密码-修改*/
    function change_password(title, url, id, w, h) {
        layer_show(title, url, w, h);
    }
    /*用户-删除*/
    function product_del(obj, id) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type:"get",
                contentType :  "application/json; charset=utf-8",// 必须
                //data: JSON.stringify({'name':"王五","password":"1234"}),//转换为json对象
                url:"/admin/item2/deleteProductAjax/"+id,
                dataType: "json",//必须
                async:  false,
                success:function(data){
                    if(data.errorCode == '200'){
                        $(obj).parents("tr").remove();
                        layer.msg('已删除!', {icon: 1, time: 1000});
                    }
                }
            });
        });
    }
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>