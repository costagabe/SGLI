<%-- 
    Document   : documentos-pessoa
    Created on : 20/08/2018, 10:19:40
    Author     : Gabriel Alves
--%>

<%@page import="com.google.gson.Gson"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row">
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <strong>Imagens</strong>
            </div>
            <div class="card-body">
                <div class="row">
                    <c:forEach items="#{docs}" var="doc">
                        <div class="col-4" id="cardDoc${doc.id}">
                            <div class="card">
                                <div class="card-header">
                                    <strong>${doc.foto}</strong>
                                </div>
                                <div class="card-body">

                                    <div class="row justify-content-md-center">
                                        <img class="img-thumbnail" style="width: 150px; height: 150px;" src="uploads/${doc.foto}">
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <button class="btn btn-lg btn-danger btn-block" onclick="confirmarExcluirDocumentoPessoa(${doc.id})">Excluir</button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

    </div>
</div>
