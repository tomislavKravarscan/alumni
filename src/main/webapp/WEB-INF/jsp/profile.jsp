<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>

<head>
    <meta charset="utf-8">
    <title>Alumni - Profil</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <script src="../../scripts/jQuery/jquery-3.2.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.2.13/semantic.min.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.2.13/semantic.min.css">
</head>

<body>

    <%@ include file="../partials/header.jsp" %>

    <div class="ui container">
        <div class="ui segment">
            <div class="ui right rail">
                <div class="ui vertical huge menu">
                    <div class="item">
                        <form action="/users/${user.userId}/edit" method="get">
                            <button type="submit" class="ui green button">Uredi podatke</button>
                        </form>
                    </div>
                    <div class="item">
                        <button onclick="deleteUser('${user.userId}')" class="ui red button">Obriši račun</button>
                    </div>
                    <sec:authorize access="hasRole('ADMINISTRATOR')">
                        <c:if test="${!user.roles[0].equals('ROLE_KORISNIK')}">
                            <div class="item">
                                <button onclick="changeRole('ROLE_KORISNIK', '${user.userId}')" class="ui secondary button">Pretvori u korisnika</button>
                            </div>
                        </c:if>
                        <c:if test="${!user.roles[0].equals('ROLE_MODERATOR')}">
                            <div class="item">
                                <button onclick="changeRole('ROLE_MODERATOR', '${user.userId}')" class="ui secondary button">Pretvori u moderatora</button>
                            </div>
                        </c:if>
                        <c:if test="${!user.roles[0].equals('ROLE_ADMINISTRATOR')}">
                            <div class="item">
                                <button onclick="changeRole('ROLE_ADMINISTRATOR', '${user.userId}')" class="ui secondary button">Pretvori u administratora</button>
                            </div>
                        </c:if>
                    </sec:authorize>
                </div>
            </div>
            <table class="ui celled striped table">
                <tr>
                    <th>Ime</th>
                    <td>${user.firstName}</td>
                </tr>
                <tr>
                    <th>Prezime</th>
                    <td>${user.lastName}</td>
                </tr>
                <tr>
                    <th>E-mail</th>
                    <td>${user.email}</td>
                </tr>
                <tr>
                    <th>Adresa</th>
                    <td>${user.address}</td>
                </tr>
                <tr>
                    <th>Broj Mobitela</th>
                    <td>${user.phone}</td>
                </tr>
                <tr>
                    <th>Datum rođenja</th>
                    <td>${user.birthday}</td>
                </tr>
                <tr>
                    <th>Datum diplomiranja</th>
                    <td>${user.graduation}</td>
                </tr>
                <c:if test="${user.subscriptions.size() != 0}">
                    <tr>
                        <th>Pretplate</th>
                        <td>
                            <c:forEach var="category" items="${user.subscriptions}">
                                <span class="ui default label">${category.name}</span>
                            </c:forEach>
                        </td>
                    </tr>
                </c:if>
                    <tr>
                    <th>Uloga</th>
                    <td>
                        <c:choose>
                            <c:when test="${user.roles[0].equals('ROLE_KORISNIK')}">
                                <p class="ui green label">Korisnik</p>
                            </c:when>
                            <c:when test="${user.roles[0].equals('ROLE_MODERATOR')}">
                                <p class="ui orange label">Moderator</p>
                            </c:when>
                            <c:when test="${user.roles[0].equals('ROLE_ADMINISTRATOR')}">
                                <p class="ui black label">Administrator</p>
                            </c:when>
                            <c:otherwise>
                                <p class="ui red label">Greška</p>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <script src="../../scripts/profile.js"></script>
</body>

</html>