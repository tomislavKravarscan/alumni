<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>

<head>
    <meta charset="utf-8">
    <title>Alumni - Unos nove datoteke</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <script src="../../scripts/jQuery/jquery-3.2.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.2.13/semantic.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.2.13/semantic.min.css">
</head>

<body>

    <%@ include file="../partials/header.jsp" %>

    <div class="ui container">
        <h1>Nova datoteka</h1>
        <div class="ui info message">
            <i class="close icon"></i>
            <div class="header">
                Napomena
            </div>
                Polja označena sa zvjezdicom(*) su obavezna
        </div>
        <form method="post" enctype="multipart/form-data" action="/files/newFile" class="ui form ">
            <div class="field">
                <label>Naslov*</label>
                <input type="text" name="title" value="${fuform.title}">
            </div>

            <div class="field">
                <label>Opis*</label>
                <textarea type="textarea" name="description" value="${fuform.description}"></textarea>
            </div>

            <label>Datoteka*</label>
            <input type="file" name="file">
           
            <button class="ui button" type="submit">Unesi</button>
            <a class="ui button" href="/files">Odustani</a>
        </form>
        <spring:hasBindErrors name="fuform">
            <div class="ui error message">
                <div class="header">
                    Imate neke pogreške
                </div>
                <ul class="list">
                    <c:forEach var="error" items="${errors.allErrors}">
                        <li>
                            <spring:message message="${error}" />
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </spring:hasBindErrors>
    </div>

    <script src="../../scripts/file.js"></script>
    <script type="text/javascript" src="../../scripts/includes/global.js"></script>

</body>

</html>