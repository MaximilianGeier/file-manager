<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>File manager</title>
    <style>
       <%@include file='styles/styles.css' %>
    </style>
</head>
<body>
    <p>Guten Tag!</p>
    <p>${currentDate}</p>
    <h1>${currentUrl}</h1>

    <a href="${url}/${parentPath}">Назад</a>

    <table>
        <tr>
            <td>Название</td>
            <td>Размер</td>
            <td>Дата</td>
        </tr>
        <c:forEach items="${files}" var="file">
            <tr>
                <td><a target="_blank" href="${url}file:///${file.getPath()}" download>${file.getName()}</a></td>
                <td>${file.getSize()}</td>
                <td>${file.getCreationTime()}</td>
            </tr>
        </c:forEach>
        <c:forEach items="${folders}" var="folder">
            <tr>
                <td><a href="${currentUrl}/<c:out value="${folder.getName()}"/>"><c:out value="${folder.getName()}"/></a></td>
                <td>${folder.getSize()}</td>
                <td>${folder.getCreationTime()}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>