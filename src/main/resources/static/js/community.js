function post(post_parentId,post_content,Type) {
    var parentId = post_parentId;//回复问题的id
    var commentContain = post_content;//回复的内容
    var type=Type;              //一级评论还是二级评论
    if (!commentContain) {
        alert("回复内容不能为空");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": parentId,
            "content": commentContain,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();//刷新当前页面
            } else {
                if (response.code == 2003) {
                    //用户未登录，跳转到登录地址，然后返回到index，最后关闭
                    var result = window.confirm("是否登录进行评价?");//显示带有一段消息以及确认按钮和取消按钮的对话框
                    if (result == true) {
                        window.localStorage.setItem("comment_login", true);//存储一个标识，表示从评论传去的登录
                        window.location.href = "https://github.com/login/oauth/authorize?client_id=6015b3d95e62ffc703e4&redirect_uri=http://localhost:8080/callback&scope=user&state=1";
                    }
                }
            }
            console.log(response);
        },
        dataType: "json"
    });
}
function post_parent() {
    var parentId = $("#parentId").val();//回复问题的id
    var content=$("#commentContain").val();//一级评论内容
    post(parentId,content,1);
}
function post_child() {
    var parentId = $("#parentId").val();//回复问题的id
    var content=$("#childComment"+).val();//二级评论内容
    alert(content);
    alert(parentId+","+2);
    post(parentId,content,2);
}
function collapseComments(e) {
    var id = e.getAttribute("data-id");//获取前端遍历后的指定的id(一级评论的id)
    var child_id = $("#child-" + id);//获取是否开启二级评论的标签

    //通过一级评论和问题id来获得二级评论

    $(child_id).toggle();//自动隐藏或者开启
}

function collapseComment(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}