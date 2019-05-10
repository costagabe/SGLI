<%-- 
    Document   : cadastro-imovel
    Created on : 15/08/2018, 19:22:42
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row justify-content-md-center">
    <div class="col-10">
        <div class="card">
            <div class="card-header">
                <strong>Novo Imóvel</strong>
                <small> Cadastro</small>
            </div>
            <div class="card-body card-block ">
                <div class="col-12 ">

                    <div class="card ">

                        <div class="card-header">
                            <strong>Endereço</strong>
                        </div>
                        <div class="card-body card-block ">
                            <div class="row">
                                <div class="form-group col-8 ">

                                    <input type="text" id="ruaEndereco" value="${imovel.endereco.rua}" placeholder="Rua" class="form-control">

                                </div>
                                <div class="form-group col-4">

                                    <input type="text"id="bairroEndereco" value="${imovel.endereco.bairro}"  placeholder="Bairro" class="form-control">

                                </div>
                                <div class="form-group col-3">

                                    <input type="text"id="cepEndereco" value="${imovel.endereco.cep}"  placeholder="CEP" class="form-control">

                                </div>
                                <div class="form-group col-4">

                                    <input type="text" id="cidadeEndereco" value="${imovel.endereco.cidade}" placeholder="Cidade" class="form-control">

                                </div>
                                <div class="form-group col-2">

                                    <input type="text" id="numeroEndereco"value="${imovel.endereco.numero}"   placeholder="Número" class="form-control">

                                </div>
                                <div class="form-group col-3">

                                    <input type="text" id="complementoEndereco" value="${imovel.endereco.complemento}"  placeholder="Complemento" class="form-control">

                                </div>
                            </div>
                        </div>
                    </div>



                </div>
                <div class="row justify-content-md-center">
                    <div class="col-8">
                        <div class="card">
                            <div class="card-header">
                                <strong>Locadores</strong>
                                <button class="btn btn-md btn-primary float-right" onclick="procurarPessoa('locadoresImovel')">Adicionar</button>
                            </div>
                            <div class="card-body card-block ">
                                <ul class="list-group " id="listalocadoresImovel">
                                    <c:forEach items="#{imovel.locatarioImovelList}" var="li">
                                        <li class="list-group-item text-center" id="listalocadoresImovel${li.locatario.id}">
                                            ${li.locatario.nome}
                                            <a class="float-right" href="#" target="_blank" onclick="excluirClienteLista(${li.locatario.id}, locadoresImovel, 'locadoresImovel'); return false"><i class="fas fa-2x fa-trash"></i></a>
                                        </li>`;
                                    </c:forEach>

                                </ul>
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
                                <ul class="list-group"  id="listaDocumentos">

                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row justify-content-md-center">
                    <div class="col-8">
                        <div class="card ">
                            <div class="card-header">
                                <strong>Fotos</strong>
                                <button class="btn btn-md btn-primary float-right" onclick="abrirFoto()">Adicionar</button>
                            </div>
                            <div class="card-body card-block ">
                                <ul class="list-group" id="listaFotos">
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <div class="card-footer">
                <c:if test="${imovel == null}">
                    <button type="submit" class="btn btn-primary btn-lg btn-block" onclick="cadastrarImovel()">
                        <i class="fa fa-check"></i> Enviar
                    </button>
                    <script>idImovel = 0;</script>
                </c:if>
                <c:if test="${imovel != null}">
                    <button type="submit" class="btn btn-primary btn-lg btn-block" onclick="editarDadosImovel()">
                        <i class="fa fa-check"></i> Enviar
                    </button>
                     <script>idImovel = ${imovel.id};</script>
                </c:if>
            </div>
        </div>
    </div>
</div>
<form id="arquivosForm" action="Upload" method="post" enctype="multipart/form-data">
</form>
<label id="tiposDocumentos" style="visibility: hidden"><c:forEach items="#{tiposDocumentos}" var="doc" >${doc.id},${doc.nome};</c:forEach></label>

    <script>
       
    <c:forEach items="#{imovel.locatarioImovelList}" var="li">
        locadoresImovel.push({id:${li.locatario.id}, nome: `${li.locatario.nome}`,idLI:${li.id}});
    </c:forEach>
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


    <c:forEach items="#{imovel.documentoImovelList}" var="doc">

        documentos.push({id:${doc.id}, foto: `${doc.foto}`, documento: {id:${doc.documento.id}}});
        addListaOpcoesDoc("${doc.id}", "${doc.foto}", "${doc.foto}");
        $("#selectDocs${doc.id}").val("${doc.documento.id}");

    </c:forEach>
    <c:forEach items="#{imovel.fotoImovelList}" var="doc">

        fotos.push({id:${doc.id},foto: `${doc.foto}`});

        $("#listaFotos").append(`<li class="list-group-item" id="itemFoto${doc.id}">${doc.foto}<a class="float-right" href="#" target="_blank" onclick="excluirFotoLista(${doc.id}); return false"><i class="fas fa-2x fa-trash"></i></a></div></div></li>`);

    </c:forEach>
</script>

