<%--
  Created by IntelliJ IDEA.
  User: DiRong
  Date: 2017/8/20
  Time: 23:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="/js/jquery/jquery-3.2.1.js"></script>
    <title>Title</title>
</head>
<body>
<a onclick="add()">添加用户</a>

<script>
    function add(){
        var flag = false;

        $.ajax({
            type:"post",
            contentType :  "application/json; charset=utf-8",// 必须
            data: JSON.stringify({'name':"王五","password":"1234"}),//转换为json对象
            url:"add",
            dataType: "json",//必须
            async:  false,
            success:function(result){
                flag = true;
            }
        });
        return flag;
    }
</script>
</body>
</html>
