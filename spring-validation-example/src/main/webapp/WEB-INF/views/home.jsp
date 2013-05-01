<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Spring MVC Validation Examples by Thiranjith</title>
	<link href="<c:url value="/resources/css/main.css" />" rel="stylesheet"  type="text/css" />	
</head>
<body>
	<div id="main">
		<p>You can find out more about this example code at <a href="http://www.thiranjith.com" target="_blank">http://www.thiranjith.com</a>.</p>
		<ul>
			<li>Example #1: <a href="<c:url value="/simpleValidationExample"/>">Bean validation using in-built annotations</a></li>
			<li>Example #2: <a href="<c:url value="/simpleValidationWithCustomMessagesExample"/>">Bean validation using in-built annotations and custom messages</a></li>
			<li>Example #3: <a href="<c:url value="/validatorExample"/>">Bean validation using Validator interface</a></li>
			<li>Example #4: <a href="<c:url value="/annotatedValidationExample"/>">Bean validation using custom annotations</a></li>
			<li>Example #5: <a href="<c:url value="/conditionalValidationExample"/>">Conditional validation with JSR 303</a> (using <code>groups</code>)</li>
		</ul>
	</div>
</body>
</html>