<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring MVC Asynchronous support example</title>
</head>
<body>
	<h1>Async Example</h1>
	<p><span>Usage instructions:</span>
		<ol>
			<li>Login using any user name (e.g. UserA)</li>
			<li>Open another browser to the same page, and login using a different user (e.g. UserB). The first user (UserA) will get a notification about the new user logging into the system.</li>
			<li>Type a message to send to other users logged in. Both UserA and UserB will get the message on their screens</li>
			<li>Logout to exit the application. E.g. if UserA logout, then UserB will get a notification.</li>
		</ol>
	</p>
	
	<c:url var="userLoginUrl" value="/async/login" />
	<form id="userLoginForm" action="${userLoginUrl}" data-bind="visible: activePollingXhr() == null">
		<p>
			<label for="user">User: </label>
			<input id="user" name="user" type="text" data-bind="value: userName"/>
			<button id="start" type="submit" data-bind="click: loginUser">Join Chat</button>
		</p>
	</form>
	
	<div data-bind="visible: activePollingXhr() != null">
		<p>List of messages:</p>
		<textarea rows="15" cols="60" readonly="readonly" data-bind="text: chatContent"></textarea>
	</div>
	
	<c:url var="messagePostUrl" value="/async/chat" />
	<form id="postMessageForm" action="${messagePostUrl}" data-bind="visible: activePollingXhr() != null">
		<p>
			<input hidden="true" id="user" name="user" type="text" data-bind="value: userName"/>
			<input id="message" name="message" type="text" data-bind="value: message" />
			<button id="post" type="submit" data-bind="click: postMessage">Post</button>
		</p>
	</form>
	
	<c:url var="userLogoutUrl" value="/async/logout" />
	<form id="userLogoutForm" action="${userLogoutUrl}" data-bind="visible: activePollingXhr() != null">
		<p>
			<input hidden="true" id="user" name="user" type="text" data-bind="value: userName"/>
			<button id="post" type="submit" data-bind="click: logoutUser">Leave Chat</button>
		</p>
	</form>
</body>
<script type="text/javascript"  src="<c:url value="/resources/js/vendor/jquery-1.8.1.min.js" />"></script>
<script type="text/javascript"  src="<c:url value="/resources/js/vendor/knockout-2.2.1.js" />"></script>
<script type="text/javascript"  src="<c:url value="/resources/js/vendor/moment.min.js" />"></script>
<script type="text/javascript">
$(document).ready(function() {
	function MessageViewModel() {
		var that = this;
		
		that.userName = ko.observable('');
		that.chatContent = ko.observable('');
		that.message = ko.observable('');
		that.activePollingXhr = ko.observable(null);
		
		var keepPolling = false;

		/**
		 * Invoked when the user login to the system by clicking on the 'Join Chat' button. Sends an AJAX request to
		 * login the user. If the user registration is a success, enable long polling.
		 */
		that.loginUser = function() {
			if (that.userName().trim() != '') {
				// send the request to server
				var form = $("#userLoginForm");
				$.ajax({url : form.attr("action"), 
					data: form.serialize(),
					type : "POST", 
					success : function(isSuccess) {
						if (isSuccess) {
							// enable long polling
							keepPolling = true;
							pollForMessages();
						}
					},
					error : function(xhr) {
						console.error("status: " + xhr.status + ", text: " + xhr.statusText);
					}
				});
			}
		}
		
		/**
		 * Invoked when the user logout of the system by clicking on the 'Leave Chat' button
		 */
		that.logoutUser = function() {
			// send the request to server
			var form = $("#userLogoutForm");
			$.ajax({url : form.attr("action"), 
				data: form.serialize(),
				type : "POST", 
				success : function(isSuccess) {
					if (isSuccess) {
						resetUI();
						this.userName('');
					}
				},
				error : function(xhr) {
					console.error("status: " + xhr.status + ", text: " + xhr.statusText);
				}
			});
		}

		/**
		 * Perform long polling to receive real-time data back from the server. Typically, the request can be terminated every 1-5 mins (depends 
		 * on the browser, proxy server etc.), but a new request will be sent using the 'complete' argument whenever this happens.
		 */
		function pollForMessages() {
			if (!keepPolling) {
				return;
			}
			var form = $("#postMessageForm");
			that.activePollingXhr(
				$.ajax({url : form.attr("action") + "?user=" + that.userName(), 
					type : "GET", 
					cache: false,
					success : function(messages) {
						for ( var i = 0; i < messages.length; i++) {
							var date = moment(messages[i].timeStamp);
							var formattedMsg = "";
							if (messages[i].messageType == 'LOGIN_MESSAGE') {
								formattedMsg = "[" + date.format("YYYY-MMM-DD HH:mm:ss a") + "] user \'" + messages[i].user + "\' logged in";
							} else if (messages[i].messageType == 'LOGOUT_MESSAGE') {
								formattedMsg = "[" + date.format("YYYY-MMM-DD HH:mm:ss a") + "] user \'" + messages[i].user + "\' logged out...";
							} else {
								formattedMsg = "[" + date.format("YYYY-MMM-DD HH:mm:ss a") + "] " + messages[i].user + " > " + messages[i].message;
							}
							that.chatContent(that.chatContent() + formattedMsg + "\n");
						}
					},
					error : function(xhr) {
						console.error("status: " + xhr.status + ", text: " + xhr.statusText);
						if (xhr.statusText != "abort" && xhr.status != 503) {
							resetUI();
							console.error("Unable to retrieve chat messages. Chat ended.");
						}
					},
					complete : pollForMessages
				})
			);
			$('#postMessageForm #message').focus();
		}
		
		that.postMessage = function() {
			if (that.message().trim() != '') {
				var form = $("#postMessageForm");
				$.ajax({
					url : form.attr("action"), 
					type : "POST",
				 	data : form.serialize(),
					error : function(xhr) {
						console.error("Error posting chat message: status=" + xhr.status + ", statusText=" + xhr.statusText);
					}
				});
				that.message('');
			}
		}
		
		function resetUI() {
			keepPolling = false;
			that.activePollingXhr(null);
			that.message('');
			that.chatContent('');
		}
	}
	
	//Activate knockout.js
	ko.applyBindings(new MessageViewModel());
});
</script>
</html>