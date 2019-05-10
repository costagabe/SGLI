<%-- 
    Document   : relatorio-rendimentos-locador
    Created on : 26/08/2018, 11:51:15
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="row justify-content-md-center" >

    <div class="col-10" >
        <div class="row justify-content-md-center">
            <h3 class="text-center">Relatório de informe de rendimento</h3><label><a href="#"onclick="imprimirRelatorio()">(Imprimir)</a></label><br>
  
        </div>
        <div class="table-responsive table-responsive-data2">
            <table class="table table-data2" border='0'>
                <thead id="cabecalhoTabela">
                <th>Data</th>
                <th>Valor</th>
                <th>Endereço do imóvel</th>
                </thead>
                <tbody id="corpoTabela">
                    <c:forEach  items="#{pagamentos}" var="pagamento">
                        <tr class="tr-shadow">
                            <td>${pagamento.dataEfetuacaoString}</td>
                            <td>${pagamento.valorStr}</td>
                            <td>${pagamento.aluguel.imovel.endereco.enderecoCompleto}</td>
                        </tr>
                        <tr class="spacer"></tr>
                    </c:forEach>
                    <tr>
                        <th>Total:</th>
                        <th>${total}</th>
                        <th> </th>
                    </tr>

                </tbody>
            </table>
        </div>
    </div>
</div>