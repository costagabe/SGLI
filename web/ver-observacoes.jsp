<%-- 
    Document   : ver-observacoes
    Created on : 26/08/2018, 10:34:22
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row justify-content-md-center">
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <strong>Observacoes de aluguel</strong>
                <button class="btn btn-primary btn-lg float-right" onclick="adicionarObservacaoAluguel(${aluguel.id})">Adicionar </button>
            </div>
            <div class="card-body">
                <div class="table-responsive table--no-card m-b-30">
                    <table class="table table-borderless table-striped table-earning">
                        <thead>
                            <tr>
                                <th>Data</th>
                                <th>Observacao</th>
                                <th>Opções</th>

                            </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="#{aluguel.observacaoAluguelList}" var="obs">
                                <tr>
                                    <td>${obs.dataStr}</td>
                                    <td>${obs.observacao}</td>
             
                                    <td>
                                        <div class="table-data-feature float-left">
                                            <button class="item" data-toggle="tooltip" data-placement="top" title="Deletar pessoa" onclick="confirmarExcluirObservacao(${obs.id})" data-original-title="Deletar">
                                                <i class="zmdi zmdi-delete"></i>
                                            </button>

                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>