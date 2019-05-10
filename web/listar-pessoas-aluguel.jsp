<%-- 
    Document   : listar-pessoas-aluguel
    Created on : 26/08/2018, 13:58:03
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<ul class="list-group">
    <c:forEach items="#{pessoas}" var="pessoa">
        <li class="list-group-item">
            ${pessoa.nome}
        </li>
    </c:forEach>
</ul>