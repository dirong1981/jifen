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

    <!--公共-->
    <link href="${ctx}/admin/h-ui/static/h-ui/css/H-ui.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/admin/h-ui/static/h-ui.admin/css/H-ui.login.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/admin/h-ui/static/h-ui.admin/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/admin/h-ui/lib/Hui-iconfont/1.0.8/iconfont.css" rel="stylesheet" type="text/css"/>


    <!--私有-->
    <link href="${ctx}/admin/css/login.css" rel="stylesheet" type="text/css"/>


    <title>后台登录 - 够力金融-积分商城</title>
</head>
<body>
<input type="hidden" id="TenantId" name="TenantId" value=""/>
<div class="header"></div>
<div class="loginWraper" style="background: url('${ctx}/admin/images/login-background.jpg') no-repeat center;background-size: 100% 100%; ">
    <div id="loginform" class="loginBox">
        <!--
        enctype='application/json'采用json格式来提交表单
        -->
        <form id="login" class="form form-horizontal" method="post" enctype='application/json'>
            <div class="row cl">
                <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60d;</i></label>
                <div class="formControls col-xs-8">
                    <input id="aName" name="aName" type="text" placeholder="账户" value="${aName}" class="input-text size-L">
                </div>
            </div>
            <div class="row cl">
                <label class="form-label col-xs-3"><i class="Hui-iconfont">&#xe60e;</i></label>
                <div class="formControls col-xs-8">
                    <input id="aPassword" name="aPassword" type="password" placeholder="密码" class="input-text size-L">
                </div>
            </div>
            <div class="row cl">
                <div class="formControls col-xs-8 col-xs-offset-3">
                    <input class="input-text size-L" type="text" placeholder="验证码"
                           onblur="if(this.value==''){this.value='验证码:'}"
                           onclick="if(this.value=='验证码:'){this.value='';}" value="验证码:" style="width:150px;" name="code" id="code">
                    <img src="">
                    <a id="kanbuq" href="javascript:;">看不清，换一张</a>
                </div>
            </div>
            <div class="row cl">
                <div class="formControls col-xs-8 col-xs-offset-3">
                    <input name="" type="button" class="btn btn-success radius size-L"
                           value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;" onclick="ajaxSubmitForm()">
                    <input name="" type="reset" class="btn btn-default radius size-L"
                           value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;">
                </div>
            </div>
        </form>
    </div>
</div>
<div class="footer">Copyright 够力金融-积分商城</div>

<script type="text/javascript" src="${ctx}/admin/h-ui/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/static/h-ui/js/H-ui.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/admin/h-ui/lib/layer/2.4/layer.js"></script>

<script>
    function ajaxSubmitForm() {
        var loading = layer.load(0, {
            shade: [0.1,'#fff'] //0.1透明度的白色背景
        });

        var option = {
            url : 'login/',
            type : 'post',
            dataType : 'json',
            //contentType :  "application/json; charset=utf-8",// 必须
            success : function(data) {
                layer.closeAll('loading');

                if(data.errorCode == "600"){
                    layer.alert('用户名和密码不能为空', {icon: 0});
                }
                if(data.errorCode == "200"){
                    location.href = "/admin/index";
                }
                if(data.errorCode == "1000"){
                    layer.alert('用户名不存在，请重试！', {icon: 2});
                    $("#aName").val("");
                    $("#aPassword").val("");
                }
                if(data.errorCode == "1100"){
                    layer.alert('密码错误，请重试！', {icon: 4});
                    $("#aPassword").val("");
                }
                if(data.errorCode == "1200"){
                    layer.alert('登陆失败，请重试！', {icon: 5});
                }
            },
            error: function(data) {
                layer.closeAll('loading');
                layer.alert('网络错误，请重试！', {icon: 3});
            }
        };
        //alert("aaa");
        $("#login").ajaxSubmit(option);
        return false;
        //return false; //最好返回false，因为如果按钮类型是submit,则表单自己又会提交一次;返回false阻止表单再次提交
        //}
    }
</script>
</body>
</html>