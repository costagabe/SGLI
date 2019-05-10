<%-- 
    Document   : relatorio-rendimentos-locador
    Created on : 26/08/2018, 11:32:22
    Author     : Gabriel Alves
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row justify-content-md-center">
    <div class="col-10">
        <div class="row justify-content-md-center">
            <div class="col-10">
                <div class="card">
                    <div class="card-header">
                        <strong>${str}</strong>
                        <button class="btn btn-md btn-primary float-right" onclick="procurarPessoa('locadoresImovel')">Pesquisar</button>
                    </div>
                    <div class="card-body card-block ">
                        <ul class="list-group " id="listalocadoresImovel">

                        </ul>
                        <div class="form-group">
                            <label class="form-control-label">Início</label>
                            <input type="date" class="form-control" id="dataInicioRelatorio">
                            <label class="form-control-label">Fim</label>
                            <input type="date" class="form-control" id="dataFimRelatorio">
                        </div>
                    </div>
                    <div class="card-footer">
                        <button class="btn btn-primary btn-lg btn-block" onclick="gerarRelatorioRendimentoLocador('${tipo}')">Gerar</button>
                    </div>
                </div>
            </div>
            


        </div>
    </div>

   
</div>