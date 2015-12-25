<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html id="guestbook"> 
<head>
<meta charset="UTF-8">
<title>방명록</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css">
<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="resources/css/pre-defined.css">
<link rel="stylesheet" type="text/css" href="resources/css/guestbook.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
	<h1 id="title"><a href="./guestbooks">내 방명록</a></h1>
	<button id="toggleNewGuestbookView" class="button-success button-small pure-button">새 방명록</button>
	<div id="newGuestbookView">
		<form method="post" action="./guestbooks">
				<input class="inp-body" type="text" name="body"/>
				<div class='footer'>
					<label>메일 : </label><input class="inp-email" type="text" name="email" placeholder="myemail@email.com"/> 
					<label>비밀번호 : </label><input class="inp-password" type="password" name="password"/> 
					<button class="btn-submit button-success button-small pure-button" type="submit">등록</button>
					<button class="btn-cancel button-basic button-small pure-button" type="button">취소</button>
				</div>
		</form>
	</div>

	<div id="guestbookList">
		<c:forEach var="gb" items="${guestbooks}">
			<div class="post" post_id="${gb.id}">
				<span class="body" title="${gb.body}">${gb.body}</span>
				<span class="by">by</span>
				<span class="email"><a href="mailto:${gb.email}">${gb.email}</a></span>
				<span class="created-date">작성 : <fmt:formatDate value="${gb.createdDate}" pattern="yyyy-MM-dd hh:mm:ss"/></span>
				<span class="modified-date">수정 : <fmt:formatDate value="${gb.modifiedDate}" pattern="yyyy-MM-dd hh:mm:ss"/></span>
				<button class="btn-modify button-xsmall"><i class="fa fa-pencil"></i></button>
			</div>
		</c:forEach>
	</div>
</body> 

<div id="modifyGuestbookView">
	<form method="post" action="./guestbooks/-1">
			<input class="inp-body" type="text" name="body"/>
			<div class="footer">
				<input class="inp-id" type="text" name="id"/><!-- for checking auth -->
				<input class="inp-email" type="text" name="email"/><!-- for checking auth -->
				<label>비밀번호 : </label><input class="inp-password" type="password" name="password"/> <!-- for checking auth -->
				<button class="btn-submit button-success button-small pure-button" type="submit">수정</button>
				<button class="btn-cancel button-basic button-small pure-button" type="button">취소</button>
			</div>
	</form>
</div>

<script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/sha256.js"></script>
<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="resources/js/component.js"></script>
<script src="resources/js/guestbook.js"></script>
</html>