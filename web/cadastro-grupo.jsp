<%-- 
    Document   : cadastro-grupo
    Created on : 18/08/2018, 18:03:35
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row justify-content-md-center">
    <div class="col-8">
        <div class="card ">
            <div class="card-header">
                <strong>Grupo de permissões</strong>
            </div>
            <div class="card-body card-block ">
                <div class="row justify-content-md-center">
                    <div class="col-8 form-group">
                        <input type="text" class="form-control" id="nomeGrupo" placeholder="Nome do grupo">
                    </div>
                    <div class="col-8">
                        <div class="card">
                            <div class="card-header">
                                <strong>Permissões liberadas</strong>
                            </div>
                            <div class="card-body">
                                <ul class="list-group">
                                    <c:forEach items="#{permissoes}" var="permissao" varStatus="loop">
                                        <li class="list-group-item">
                                            ${permissao.nomePermissao} 
                                            <label class="switch switch-3d switch-primary mr-3 float-right">
                                                <input type="checkbox" id="permissaoLiberada${loop.index}" class="switch-input" checked="true">
                                                <span class="switch-label"></span>
                                                <span class="switch-handle"></span>
                                            </label>
                                        </li>
                                    </c:forEach>

                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-8">
                        <button class="btn btn-primary btn-md btn-block" onclick="cadastrarPermissao()">Enviar</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>