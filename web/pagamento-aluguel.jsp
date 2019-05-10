<%-- 
    Document   : pagamento-aluguel
    Created on : 23/08/2018, 09:55:44
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row justify-content-md-center">
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <strong>Pagamentos de aluguel</strong>
                <button class="btn btn-primary btn-lg float-right" onclick="adicionarPagamentoAluguel(${aluguel.id})">Adicionar </button>
            </div>
            <div class="card-body">
                <div class="table-responsive table--no-card m-b-30">
                    <table class="table table-borderless table-striped table-earning">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Valor</th>
                                <th>Data de efetuação</th>
                                <th class="text-right">Data de repasse</th>
                                <th class="text-right">Recibo</th>
                                <th>Opções</th>

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="#{aluguel.pagamentoAluguelList}" var="pagamento">
                                <tr>
                                    <td>${pagamento.id}</td>
                                    <td>${pagamento.valorStr}</td>
                                    <td>${pagamento.dataEfetuacaoString}</td>
                                    <td>${pagamento.dataRepasseString}</td>
                                    <c:if test="${pagamento.recibo == ''}">
                                        <td>Sem recibo</td>
                                    </c:if>
                                    <c:if test="${pagamento.recibo != ''}">
                                        <td><a target="_blank" href="uploads/${pagamento.recibo}">${pagamento.recibo}</a></td>
                                    </c:if>
                                    <td>
                                        <div class="table-data-feature float-left">
                                            <button class="item" data-toggle="tooltip" data-placement="top" title="Deletar pessoa" onclick="confirmarExcluirPagamento(${pagamento.id})" data-original-title="Deletar">
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
    <script>

        function enviarRecibo(obj) {
            var formData = new FormData(obj);
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
        }
    </script>