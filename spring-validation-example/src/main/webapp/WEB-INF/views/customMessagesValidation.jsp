<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Simple Spring Bean validation example: With Custom error messages</title>
	<link href="<c:url value="/resources/css/main.css" />" rel="stylesheet"  type="text/css" />	
</head>
<body>
	<div id="main">
		<form:form id="form" method="post" modelAttribute="customMessageFormBean">
			<div id="formContent">
				<div class="header">
			  		<h2>Form</h2>
			  		<c:if test="${not empty message}">
						<div id="message" class="success">${message}</div>	
			  		</c:if>
			  		<s:bind path="*">
			  			<c:if test="${status.error}">
					  		<div id="message" class="error">Form has errors</div>
			  			</c:if>
			  		</s:bind>
				</div>
				<fieldset>
			  		<legend>Personal Info</legend>
			  		<form:label path="firstName">First Name <form:errors path="firstName" cssClass="error" /></form:label>
			  		<form:input path="firstName" />
			  		
			  		<form:label path="lastName">Last Name <form:errors path="lastName" cssClass="error" /></form:label>
			  		<form:input path="lastName" />
	
			  		<form:label path="age">Age <form:errors path="age" cssClass="error" /></form:label>
			  		<form:input path="age" />
	
			  		<form:label path="dateOfBirth">
			  			Date of Birth (in form yyyy-mm-dd) <form:errors path="dateOfBirth" cssClass="error" />
			 		</form:label>
			  		<form:input path="dateOfBirth" />
	
					<form:label path="comments">Comments (40 letters max) <form:errors path="comments" cssClass="error" /></form:label>
			  		<form:input path="comments" />
			  	</fieldset>
			  	<p><button type="submit">Submit (with validations)</button></p>
			  </div>
		</form:form>
	</div>
</body>
</html>