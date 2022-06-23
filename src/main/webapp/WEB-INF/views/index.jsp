<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:message code="select.option.submit" var="submitValue"/>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="app.title"/></title>
</head>
<body>
<form action="/getOption">
    <h1><spring:message code="select.option.header"/></h1>
    <br><spring:message code="select.option.first"/>
    <br><spring:message code="select.option.second"/>
    <br/><spring:message code="select.option.third"/>
    <br><spring:message code="select.option.four"/>
    <br><spring:message code="select.option.five"/>
    <br>
    <input name="option" type="text">
    <input value=${submitValue} type="submit">
</form>
<div>
    <div class="dropdown">
        <button class="btn btn-danger dropdown-toggle" type="button" id="dropdownMenuButton"
                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><spring:message
                code="app.lang.title"/></button>
        <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
            <a class="dropdown-item" href="?lang=en"><spring:message code="app.lang.english"/></a>
            <a class="dropdown-item" href="?lang=ru"><spring:message code="app.lang.russian"/></a>
        </div>
    </div>
</div>
<br/>
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>