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
    <header>
        <H1>Guten Tag!</H1>
        <p>Последнее обновление страницы в: <span>${currentDate}</span></p>
        <h3>Путь: ${currentPath}</h3>
    </header>

    <a href="${url}?path=${parentPath}" class="back_btn">Назад</a>

    <table>
        <tr>
            <td>Название</td>
            <td>Размер</td>
            <td>Дата</td>
        </tr>
        <c:forEach items="${files}" var="file">
            <tr>
                <td><a target="_blank" href="${downloadUrl}?path=${file.getPath()}" download>${file.getName()}</a></td>
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