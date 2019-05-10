<%-- 
    Document   : cadastro-pessoa
    Created on : 16/08/2018, 14:27:35
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row justify-content-md-center">
    <div class="col-11">
        <div class="card">
            <div class="card-header">
                <strong>Novo Cliente</strong> <!--<button class="btn btn-primary btn-md float-right" onclick="preencherPessoa()">Preencher</button>-->
                <small> Cadastro</small>
            </div>
            <div class="card-body card-block">
                <div class="form-group ">
                    <label for="company" class=" form-control-label">Nome</label>
                    <input type="text" id="nomePessoa" placeholder="Nome do cliente" value="${pessoa.nome}" class="form-control">
                </div>
                <div class="form-group ">
                    <label for="vat" class=" form-control-label">Email</label>
                    <input type="text" id="emailPessoa" placeholder="Email do cliente" value="${pessoa.email}" class="form-control">
                </div>
                <div class="row">
                    <div class="form-group col-6">
                        <label for="vat" class=" form-control-label">Telefone</label>
                        <input type="text" id="telefonePessoa" placeholder="(99) 99999-9999" value="${pessoa.telefone}" class="form-control">
                    </div>
                    <div class="form-group col-6">
                        <label for="vat" class=" form-control-label">Data de Nascimento</label>
                        <input type="date" id="dataNascimentoPessoa" placeholder="Data de nascimentos" value="${pessoa.dataNascimentoString}" class="form-control">
                    </div>
                </div>
                <div class="row">
                    <div class="card ">

                        <div class="card-header">
                            <strong>Endereço</strong>
                        </div>
                        <div class="card-body card-block ">
                            <div class="row">
                                <div class="form-group col-8 ">

                                    <input id="ruaEndereco" type="text"  placeholder="Rua"value="${pessoa.endereco.rua}" class="form-control">

                                </div>
                                <div class="form-group col-4">

                                    <input type="text" id="bairroEndereco" placeholder="Bairro"value="${pessoa.endereco.bairro}" class="form-control">

                                </div>
                                <div class="form-group col-3">

                                    <input type="text" id="cepEndereco"  placeholder="CEP" value="${pessoa.endereco.cep}" class="form-control">

                                </div>
                                <div class="form-group col-4">

                                    <input type="text" id="cidadeEndereco"  placeholder="Cidade" value="${pessoa.endereco.cidade}" class="form-control">

                                </div>
                                <div class="form-group col-2">

                                    <input type="text" id="numeroEndereco"  placeholder="Número" value="${pessoa.endereco.numero}" class="form-control">

                                </div>
                                <div class="form-group col-3">

                                    <input type="text" id="complementoEndereco"   placeholder="Complemento" value="${pessoa.endereco.complemento}" class="form-control">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row justify-content-md-center">
                    <div class="col-8">
                        <div class="card ">
                            <div class="card-header">
                                <strong>Documentos</strong>
                                <button class="btn btn-md btn-primary float-right" onclick="abrirDocumento()">Adicionar</button>

                            </div>
                            <div class="card-body card-block ">
                                <ul class="list-group " id="listaDocumentos">
                                   
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="card-footer">
                <c:if test="${pessoa == null}">
                    <button type="submit" class="btn btn-primary btn-lg btn-block" onclick="cadastrarPessoa()">
                        <i class="fa fa-check"></i> Enviar
                    </button>
                     <script>
                        var idPessoa = 0;
                        </script>
                </c:if>
                <c:if test="${pessoa != null}">
                    <script>
                        var idPessoa = ${pessoa.id};
                        </script>
                    <button type="submit" class="btn btn-primary btn-lg btn-block" onclick="editarDadosPessoa()">
                        <i class="fa fa-check"></i> Enviar
                    </button>

                </c:if>
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
 <script>
    <c:forEach items="#{pessoa.documentoPessoaList}" var="doc">

        documentos.push({id:${doc.id},foto: `${doc.foto}`, documento: {id:${doc.documento.id}}});
        addListaOpcoesDoc("${doc.id}", "${doc.foto}", "${doc.foto}");
        $("#selectDocs${doc.id}").val("${doc.documento.id}");
        
    </c:forEach>
        
</script>