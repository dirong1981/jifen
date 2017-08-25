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


    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/lib/Hui-iconfont/1.0.8/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/admin/h-ui/static/h-ui.admin/css/style.css"/>

    <link rel="stylesheet" type="text/css" href="${ctx}/admin/css/public.css"/>

    <style type="text/css">
        input[type=text],.select-box{
            width:200px;
        }



    </style>
    <title>够力金融-积分商城-后台管理</title>
</head>
<body>
<article class="cl pd-20">
    <form method="post" class="form form-horizontal" id="form-add-class" enctype='multipart/form-data'>


        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3 "><span class="c-red">*</span>父级分类：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span class="select-box">
                    <select class="select" size="1" name="cParentId" id="cParentId">
                        <option value="0" selected>一级分类</option>
                    </select>
				</span>
            </div>
        </div>

        <!--分类名称-->

        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>分类名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" placeholder="分类名称" id="cName" name="cName">
            </div>
        </div>

        <!--排序-->
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>排序：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" placeholder="排序" id="cSort" name="cSort">
            </div>
        </div>


        <!--  图片-->
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3">图片：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span class="btn-upload form-group">
                    <input class="input-text upload-url" type="text" name="uploadfile" id="uploadfile" readonly nullmsg="请添加附件！" style="width:200px">
                    <a href="javascript:void();" class="btn btn-primary radius upload-btn">
                        <i class="Hui-iconfont">&#xe642;</i>
                        浏览文件
                    </a>
                    <input type="file" multiple name="file-2" class="input-file">
				</span>
            </div>
        </div>



        <!--状态-->
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>状态：</label>
            <div class="formControls col-xs-8 col-sm-9 skin-minimal">
                <div class="radio-box">
                    <input name="cState" type="radio" id="cState1" value="1" checked>
                    <label for="cState1">启用</label>
                </div>
                <div class="radio-box">
                    <input type="radio" id="cState0" name="cState" value="0">
                    <label for="cState0">禁用</label>
                </div>
            </div>
        </div>

        <input type="hidden" name="cLogo" value="aa.jpg">
        <input type="hidden" name="cCreator" value="${sessionScope.session_admin_id}">


        <div class="row cl">
            <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
                <input class="btn btn-primary radius" onclick="add_class_ajax()" type="button" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
            </div>
        </div>
    </form>
</article>

<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/static/h-ui/js/H-ui.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/static/h-ui.admin/js/H-ui.admin.page.js"></script>
<!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/jquery.validation/1.14.0/validate-methods.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/jquery.validation/1.14.0/messages_zh.js"></script>
<script type="text/javascript">


    //获取class列表并更新option
    $(function(){

        $.ajax({
            type:"get",
            contentType :  "application/json; charset=utf-8",// 必须
            //data: JSON.stringify({'name':"王五","password":"1234"}),//转换为json对象
            url:"/admin/item1/parentClassAjax",
            dataType: "json",//必须
            async:  false,
            success:function(data){
                if(data.errorCode == '200'){
                    $.each(data.item.Category, function(index, content)
                    {
                        jQuery('#cParentId').append('<option value="' + content.cId + '">' + content.cName + '</option>');
                    });
                    $("#cParentId").val('${sessionScope.parent_id}');
                }
            }
        });
    });


    //添加分类ajax请求
    function add_class_ajax() {

        var option = {
            url : '/admin/item1/add/',
            type : 'post',
            dataType : 'json',
            //contentType :  "application/json; charset=utf-8",// 必须
            success : function(data) {


                if(data.errorCode == "600"){
                    layer.alert('添加的内容不能为空', {icon: 0});
                }
                if(data.errorCode == "200"){
                    parent.layer.msg('添加成功！', {icon: 1});
                    setTimeout(closePage,1000);


                }
                if(data.errorCode == "500"){
                    layer.alert('添加失败，请重试！', {icon: 5});
                }
            },
            error: function(data) {

                layer.alert('网络错误，请重试！', {icon: 3});
            }
        };

        $("#form-add-class").ajaxSubmit(option);
        return false;
    }


    function closePage() {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    }
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>