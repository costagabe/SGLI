<%-- 
    Document   : cadastro-documento
    Created on : 19/08/2018, 08:26:43
    Author     : Gabriel Alves
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row justify-content-md-center">
    <div class="col-8">
        <div class="card">
            <div class="card-header">
                <strong>Cadastro novo tipo de documento</strong>
            </div>
            <div class="card-body">
                <div class="row justify-content-md-center">
                    <div class="col-10 form-group">
                        <input class="form-control" type="text" placeholder="Nome do documento" id="nomeDocumento">
                    </div>
                    <div class="col-10 form-group">
                        <select class="form-control" id="tipoDocumento">
                            <option value="">Tipo de documento</option>
                            <option value="1">Documento de Pessoa</option>
                            <option value="2">Documento de Imóvel</option>
                            <option value="3">Documento de Aluguel</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="card-footer">
                <div class="row justify-content-md-center">
                    <div class="col-10">
                        <button class="btn btn-primary btn-lg btn-block" onclick="cadastrarDocumento()">Enviar</button>
                    </div>
                </div>
                
            </div>
        </div>
    </div>
</div>