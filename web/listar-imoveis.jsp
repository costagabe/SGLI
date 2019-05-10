<%-- 
    Document   : listar-pessoas
    Created on : 19/08/2018, 11:04:32
    Author     : Gabriel Alves
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script>
    var imoveis = JSON.parse(`${imoveis}`);

</script>

<div class="row justify-content-md-center">
    <div class="col-12">
        <nav aria-label="...">
            <ul class="pagination">

                </li>
                <c:forEach var="i" begin="1" end="${paginas}">
                    <li class="page-item " id="navPage${i}" onclick="abrirPaginaListaImoveis(${i})"><a class="page-link" href="#">${i}</a></li>
                    </c:forEach>
            </ul>
        </nav>
    </div>
    <div class="table-responsive table-responsive-data2">
        <table class="table table-data2">
            <thead id="cabecalhoTabela"></thead>
            <tbody id="corpoTabela">


            </tbody>
        </table>
    </div>

</div>
<script>
    var tabela = [];
    
    
    preencherTabelaImoveis();
    preencherTabela();
    $("#navPage1").attr("class", "page-item active");
    var qtdPaginas = ${paginas};
</script>