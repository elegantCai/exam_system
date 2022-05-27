
var username = null;
var userpassword = null;
var ruserpassword = null;
var usermail = null;
var addBtn = null;



$(function(){

    username = $("#username");
    userpassword = $("#userpassword");
    ruserpassword = $("#ruserpassword");
    usermail = $("#usermail");



    //初始化的时候，要把所有的提示信息变为：* 以提示必填项，更灵活，不要写在页面上
    username.next().html("*");
    userpassword.next().html("*");
    ruserpassword.next().html("*");
    usermail.next().html("*");

    /*
     * 用户名验证
     * 失焦\获焦
     * jquery的方法传递
     */
    /*
     * 验证
     * 失焦\获焦
     * jquery的方法传递
     */
    usermail.bind("blur",function(){
        //ajax后台验证--userName是否已存在
        //user.do?method=ucexist&userName=**
        $.ajax({
            type:"GET",//请求类型
            url:"stureg/studentmail",//请求的url
            data:{"studentmail":usermail.val()},//请求参数
            dataType:"json",//ajax接口（请求url）返回的数据类型
            success:function(data){//data：返回数据（json对象）
                if(data.result == "exist"){//账号已存在，错误提示
                    validateTip(usermail.next(),{"color":"red"},imgNo+ " 该用户账号已存在",false);
                }else if(data.result == "null"){//账号可用，正确提示
                    validateTip(usermail.next(),{"color":"green"},imgYes+"请输入账号",true);
                }else{//账号可用，正确提示
                    var patrn=/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
                    if(usermail.val().match(patrn)){
                        validateTip(usermail.next(),{"color":"green"},imgYes+" 该账号可以使用",true);
                    }else{
                        validateTip(usermail.next(),{"color":"red"},imgNo + " 您输入邮箱格式不正确",false);
                    }
                }
            },
            error:function(data){//当访问时候，404，500 等非200的错误状态码
                validateTip(usermail.next(),{"color":"red"},imgNo+" 验证账号是否存在错误",false);
            }
        });


    });

    usermail.bind("focus",function(){
        validateTip(usermail.next(),{"color":"#666666"},"* 请输入邮箱",false);
    }).bind("blur",function(){
        var patrn=/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
        if(usermail.val().match(patrn)){
            validateTip(usermail.next(),{"color":"green"},imgYes,true);
        }else{
            validateTip(usermail.next(),{"color":"red"},imgNo + " 您输入邮箱格式不正确",false);
        }
    });

    username.bind("focus",function(){
        validateTip(username.next(),{"color":"#666666"},"* 用户名长度必须是大于1小于10的字符",false);
    }).bind("blur",function(){
        if(username.val() != null && username.val().length > 1
            && username.val().length < 10){
            validateTip(username.next(),{"color":"green"},imgYes,true);
        }else{
            validateTip(username.next(),{"color":"red"},imgNo+" 用户名输入的不符合规范，请重新输入",false);
        }

    });

    userpassword.bind("focus",function(){
        validateTip(userpassword.next(),{"color":"#666666"},"* 密码长度必须是大于6小于20",false);
    }).bind("blur",function(){
        if(userpassword.val() != null && userpassword.val().length > 6
            && userpassword.val().length < 20 ){
            validateTip(userpassword.next(),{"color":"green"},imgYes,true);
        }else{
            validateTip(userpassword.next(),{"color":"red"},imgNo + " 密码输入不符合规范，请重新输入",false);
        }
    });

    ruserpassword.bind("focus",function(){
        validateTip(ruserpassword.next(),{"color":"#666666"},"* 请输入与上面一只的密码",false);
    }).bind("blur",function(){
        if(ruserpassword.val() != null && ruserpassword.val().length > 6
            && ruserpassword.val().length < 20 && userpassword.val() == ruserpassword.val()){
            validateTip(ruserpassword.next(),{"color":"green"},imgYes,true);
        }else{
            validateTip(ruserpassword.next(),{"color":"red"},imgNo + " 两次密码输入不一致，请重新输入",false);
        }
    });






    addBtn.bind("click",function(){
        if(username.attr("validateStatus") != "true"){
            username.blur();
        }else if(userpassword.attr("validateStatus") != "true"){
            userpassword.blur();
        }else if(ruserpassword.attr("validateStatus") != "true"){
            ruserpassword.blur();
        }else if(usermail.attr("validateStatus") != "true"){
            usermail.blur();
        }else{
            if(confirm("是否确认提交数据")){
                $("#userForm").submit();
            }
        }
    });

});