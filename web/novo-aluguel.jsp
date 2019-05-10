<%-- 
    Document   : novo-aluguel
    Created on : 18/08/2018, 13:13:20
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="row justify-content-md-center">
    <div class="col-11">
        <div class="card">
            <div class="card-header">
                <strong>Novo Aluguel</strong> <!-- <button class="btn btn-primary btn-md float-right" onclick="preencherAluguel()">Preencher</button>-->

            </div>
            <div class="card-body card-block ">
                <div class="row justify-content-between ">

                    <div class="col-6 ">
                        <div class="card ">
                            <div class="card-header">
                                <strong>Locadores</strong>
                                <button class="btn btn-md btn-primary float-right" onclick="procurarPessoa('locadores')">Adicionar</button>
                            </div>
                            <div class="card-body card-block ">
                                <ul class="list-group" id="listalocadores">

                                </ul>
                            </div>
                        </div>

                    </div>
                    <div class="col-6">
                        <div class="card ">
                            <div class="card-header">
                                <strong>Locatários</strong>
                                <button class="btn btn-md btn-primary float-right" onclick="procurarPessoa('locatarios')">Adicionar</button>
                            </div>
                            <div class="card-body card-block " id="listalocatarios">
                                <ul class="list-group" >

                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="col-12">
                        <div class="card ">
                            <div class="card-header">
                                <strong>Imóvel</strong>
                                <button class="btn btn-md btn-primary float-right" onclick="pesquisarImovel()">Procurar</button>
                            </div>
                            <div class="card-body card-block ">
                                <ul class="list-group" id="listaImoveis" >

                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="card ">
                            <div class="card-header">
                                <strong>Pagamentos</strong>

                            </div>
                            <div class="card-body card-block ">
                                <div class="row">
                                    <div class="col-3">
                                        <label class="form-control-label" for="valorAluguel">Valor do aluguel</label>
                                    </div>
                                    <div class="col-3">
                                        <label class="form-control-label" for="diaPagamento">Dia do vencimento </label>
                                    </div>
                                    <div class="col-3">
                                        <label class="form-control-label" for="dataPrimeiroPagamento">Data do primeiro pagamento </label>
                                    </div>
                                    <div class="col-3">
                                        <label class="form-control-label" for="tipoPagamento">Tipo de pagamento </label>
                                    </div>

                                </div>
                                <div class="row">

                                    <div class="input-group col-3">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">R$</div>
                                        </div>

                                        <input type="number" id="valorAluguel" class="form-control"  placeholder="Valor">
                                    </div>

                                    <div class="input-group col-3">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                        </div>
                                        <input type="number" id="diaPagamento" class="form-control"  placeholder="Dia ">
                                    </div>
                                    <div class="input-group col-3">
                                            
                                        <input type="date" id="dataPrimeiroPagamento" class="form-control"  placeholder="Data ">
                                    </div>
                                    <div class="form-control form-check col-3 " >
                                        <div class="radio" >
                                            <label for="radio1" class="form-check-label ">
                                                <input type="radio" id="radio1" name="radios" value="1" class="form-check-input">Antecipado
                                            </label>
                                        </div>
                                        <div class="radio">
                                            <label for="radio2" class="form-check-label ">
                                                <input type="radio" id="radio2" name="radios" value="2" class="form-check-input">Vencido
                                            </label>
                                        </div>

                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="col-12">
                        <div class="card ">
                            <div class="card-header">
                                <strong>Contrato</strong>
                            </div>
                            <div class="card-body card-block ">
                                <div class="row">
                                    <div class="col-4">
                                        <label class="form-control-label" for="dataInicioContrato">Data de início contrato</label>
                                    </div>
                                    <div class="col-4">
                                        <label class="form-control-label" for="dataFimContrato">Data de fim contrato</label>
                                    </div>
                                    <div class="col-4">
                                        <label class="form-control-label" for="garantiaContrato">Garantia do contrato</label>
                                    </div>

                                </div>
                                <div class="row">
                                    <div class="input-group col-4">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                        </div>
                                        <input type="date" id="dataInicioContrato" class="form-control"  >
                                    </div>
                                    <div class="input-group col-4">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                        </div>
                                        <input type="date" id="dataFimContrato" class="form-control"  >
                                    </div>
                                    <div class="input-group col-4">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                        </div>
                                        <input type="text" id="garantiaContrato" class="form-control"  placeholder="Garantia ">
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-12">
                        <div class="row justify-content-md-center">
                            <div class="col-8">
                                <div class="card ">
                                    <div class="card-header">
                                        <strong>Documentos</strong>
                                        <button class="btn btn-md btn-primary float-right" onclick="abrirDocumento()" >Adicionar</button>
                                    </div>
                                    <div class="card-body card-block ">
                                        <ul class="list-group " id="listaDocumentos">

                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                </div>
                <div class="card-footer">
                    <button type="submit" class="btn btn-primary btn-lg btn-block" onclick="cadastrarAluguel()">
                        <i class="fa fa-check"></i> Enviar
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<form id="arquivosForm" action="Upload" method="post" enctype="multipart/form-data">
</form>
<label id="tiposDocumentos" style="visibility: hidden"><c:forEach items="#{tiposDocumentos}" var="doc" >${doc.id},${doc.nome};</c:forEach></label>

<script>
    $("#arquivosForm").submit(function (e) {
        var formData = new FormData(this);
        formData.append("docs", JSON.stringify(arquivos));
        formData.append("qty", documentos.length);
        $.ajax({
            url: `Upload`, //?insideSystem=true&action=uploadDocumento&id=${obj.id}&qtyDocs=${documentosAdicionados}`, // Url to which the request is send
            type: "POST", // Type of request to be send, called as method
            data: formData, // Data sent to server, a set of key/value pairs (i.e. form fields and values)
            contentType: false, // The content type used when sending data to the server.
            cache: false, // To unable request pages to be cached
            processData: false, // To send DOMDocument or non processed data file it is set to false
            success: function (data)   // A function to be called if request succeeds
            {
                console.log(data);
                documentosAdicionados = 0;
            }
        });
        return false;
    });


    var tipoDocs = [];
    var aux = $("#tiposDocumentos").html().split(";");
    for (var i = 0; i < aux.length; i++) {
        var tipo = {
            id: aux[i].split(",")[0],
            nome: aux[i].split(",")[1]
        };
        tipoDocs.push(tipo);
    }
    tipoDocs.pop();
</script>