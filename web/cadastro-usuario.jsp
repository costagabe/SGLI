<%-- 
    Document   : cadastro-usuario
    Created on : 18/08/2018, 17:42:11
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row justify-content-md-center">
    <div class="col-8">

        <div class="card ">
            <div class="card-header">
                <strong>Cadastro de usuário</strong>

            </div>
            <div class="card-body card-block ">
                <div class="row justify-content-md-center">
                    <div class="col-8 form-group">
                        <input class="form-control" placeholder="Usuário" id="usuarioUsuario">
                    </div>
                    <div class="col-8 form-group">
                        <input class="form-control" placeholder="Nome" id="nomeUsuario">
                    </div>
                    <div class="col-8 form-group">
                        <select class="form-control" id="grupoUsuario">
                            <option>Grupo</option>
                            <c:forEach items="#{grupos}" var="grupo">
                                <option value="${grupo.id}"> ${grupo.grupo}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-8">
                        <button class="btn btn-primary btn-md btn-block" onclick="cadastrarUsuario()">Cadastrar</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>