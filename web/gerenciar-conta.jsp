<%-- 
    Document   : gerenciar-conta
    Created on : 18/08/2018, 20:09:44
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

                <div class="row justify-content-between">
                    <div class="col-4">
                        <div class="card">
                            <div class="card-header">
                                <strong>Foto de perfil</strong>
                            </div>
                            <div class="card-body">
                                <img class="img-thumbnail " style="width: 150px; height: 150px" src="uploads/${logado.foto}">
                            </div>
                            <div class="card-footer">
                                <button class="btn btn-secondary btn-md btn-block" onclick="abrirFotoPerfil()">Mudar foto</button>
                            </div>
                        </div>
                    </div>
                    <div class="col-8">
                        <div class="form-group">
                            <label class="form-control-label">Usuario</label>
                            <input class="form-control" type="text" placeholder="" value="${logado.usuario}" readonly>
                        </div>
                        <div class=" form-group">
                            <label class="form-control-label">Nome</label>
                            <input class="form-control" type="text" placeholder="" value="${logado.nome}"  readonly>
                        </div>
                        <div class="card">
                            <div class="card-header">
                                <strong>Mudar senha</strong>
                            </div>
                            <div class="card-body">
                                <div class="form-group">
                                    <label class="form-control-label">Senha</label>
                                    <input type="password" id="senhaTroca" class="form-control" placeholder="Nova Senha">
                                </div>
                                <div class="form-group">
                                    <label class="form-control-label">Digite novamente</label>
                                    <input type="password" id="resenhaTroca" class="form-control" placeholder="Repetir senha">
                                </div>
                            </div>
                            <div class="card-footer">
                                <button class="btn btn-secondary btn-md btn-block" onclick="mudarSenha()">Mudar senha</button>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="row justify-content-md-center">






                </div>
            </div>
        </div>
    </div>
</div>
<form id="arquivosForm" action="Upload" method="post" enctype="multipart/form-data">
</form>
<label id="tiposDocumentos" style="visibility: hidden"></label>

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