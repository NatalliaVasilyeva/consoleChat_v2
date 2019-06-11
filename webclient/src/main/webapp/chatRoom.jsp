<%--
  Created by IntelliJ IDEA.
  User: Natali
  Date: 09.06.2019
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="${pageContext.request.contextPath}/netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css"
      rel="stylesheet" id="bootstrap-css">
<script src="netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="code.jquery.com/jquery-1.11.1.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat.css">
<!------ Include the above in your HEAD tag ---------->
<c:if test="${not empty welcomeToChat}">
    <div class="alert alert-success">
        <p>${welcomeToChat}</p>
    </div>
</c:if>
<div class="container">
    <div class="row">
        <div class="col-md-5">
            <div class="panel panel-primary">
                <div class="panel-heading" id="accordion">
                    <span class="glyphicon glyphicon-comment"></span> Chat
                    <div class="btn-group pull-right">
                        <a type="button" class="btn btn-default btn-xs" data-toggle="collapse" data-parent="#accordion"
                           href="#collapseOne">
                            <span class="glyphicon glyphicon-chevron-down"></span>
                        </a>
                    </div>
                </div>
                <div class="panel-collapse collapse" id="collapseOne">
                    <div class="panel-body">
                        <ul class="chat">
                            <li class="left clearfix"><span class="chat-img pull-left">
                            <img src="http://placehold.it/50/55C1E7/fff&text=U" alt="User Avatar" class="img-circle"/>
                        </span>
                                <div class="chat-body clearfix">
                                    <div class="header">
                                        <strong class="primary-font">Jack Sparrow</strong>
                                        <small class="pull-right text-muted">
                                            <span class="glyphicon glyphicon-time"></span>12 mins ago
                                        </small>
                                    </div>
                                    <p id="chat_messages">

                                    </p>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="panel-footer">
                        <div class="input-group">
                            <input id="btn-input" type="text" class="form-control input-sm"
                                   placeholder="Type your message here..."/>
                            <span class="input-group-btn">
                            <button class="btn btn-warning btn-sm" id="btn-chat">
                                Send</button>
                        </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
