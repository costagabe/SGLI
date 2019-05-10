var documentos = [];
var documentosAdicionados = 0;
var arquivos = [];
var ultimoArquivoAdd = 0;
var succsessPage = "";
var succsessAction = "";
var locadoresImovel = [];
var fotos = [];
var locatarios = [];
var locadores = [];
var idLista = "";
var imovel = null;
var paginaListaPessoas = 1;
var paginaAtual = "";
function emptyVars() {
    documentos = [];
    documentosAdicionados = 0;
    ultimoArquivoAdd = 0;
    arquivos = [];
    locadoresImovel = [];
    succsessPage = "";
    succsessAction = "";
    fotos = [];
    idLista = "";
    imovel = null;
    locatarios = [];
    locadores = [];
    paginaListaPessoas = 1;

}
function logar() {
    var user = $("#user").val();
    var password = $("#password").val();
    $.ajax({
        url: "Login",
        data: {insideSystem: true, action: "logar", user: user, password: password}
    }).done(function (data) {
        console.log(data);
        var dados = JSON.parse(data);
        location.reload();
    });
}
function openPage(page, action = "execute", data = {}) {
    paginaAtual = page;
    var dataa = JSON.stringify(data);
    $.ajax({
        url: "System",
        data: {page: page, action: action, data: dataa}
    }).done(function (data) {
        emptyVars();
        $("#conteudo").html(data);
        $(".page-loader").remove();
    });
}
function changeTitle(url) {
    var aux = url;
    aux = aux.replace("//", "");
    url = "http://localhost:8080/SGL/" + aux;
    window.history.pushState({path: url}, "", url);
}
function sendData(page, action, data, whenDone = showModal) {
    var jsonData = JSON.stringify(data);
    $.ajax({
        url: "System",
        method: "POST",
        data: {page: page, action: action, data: jsonData}
    }).done(function (datta) {
        console.log(datta);
        var dataAux = JSON.parse(datta);
        whenDone(dataAux);
    });
}
function getPessoa() {
    var pessoa = {
        id: idPessoa,
        nome: $("#nomePessoa").val(),
        email: $("#emailPessoa").val(),
        telefone: $("#telefonePessoa").val(),
        dataNascimento: $("#dataNascimentoPessoa").val(),
        qtdDocs: documentosAdicionados,
        endereco: {
            rua: $("#ruaEndereco").val(),
            bairro: $("#bairroEndereco").val(),
            cep: $("#cepEndereco").val(),
            cidade: $("#cidadeEndereco").val(),
            numero: $("#numeroEndereco").val(),
            complemento: $("#complementoEndereco").val()
        },
        documentoPessoaList: documentos
    };
    return pessoa;
}
function abrirArquivo(tipo) {

}
/**Método que deve ser chamado no click de um botão que tenha como intenção upar arquivo pro servidor
 * 
 * @param {String} onChange método que deve ser chamado ao fim da escolha de um arquivo (deve ser string)
 * @returns {id|Number} id do item criado
 */
function adicionarArquivo(onChange) {
    ultimoArquivoAdd++;
    var arquivo = {id: ultimoArquivoAdd, nome: ""};
    arquivos.push(arquivo);
    $("#qtyDocs").attr('value', arquivos.length);
    $("#arquivosForm").append();
    $("#arquivosForm").append(`<input type="file" form="arquivosForm" onchange="${onChange}(this,${ultimoArquivoAdd})" name="documento${ultimoArquivoAdd}" id="arquivoInput${ultimoArquivoAdd}" style="visibility: hidden">`);
    $("#arquivosForm").append(`<input type="hidden" form="arquivosForm" name="documentoLabel${ultimoArquivoAdd}" value="documento${ultimoArquivoAdd}" id="arquivoLabel${ultimoArquivoAdd}">`);
    $("#arquivoInput" + ultimoArquivoAdd).click();
    return ultimoArquivoAdd; //id dos itens do documento
}
function abrirDocumento() { //botao de adicionar clicado
    adicionarArquivo("adicionarNovoDocumentoLista");
}
function abrirFoto() {
    adicionarArquivo("adicionarNovaFotoLista");
}
function abrirRecibo() {

}
function abrirFotoPerfil() {
    adicionarArquivo("mudarFotoPerfil");

}
function mudarSenha() {
    var senha = $("#senhaTroca").val();
    var resenha = $("#resenhaTroca").val();

    if (senha === resenha) {
        sendData("Usuario", "mudarSenha", {senha: senha}, showModal);
    } else {
        showSimpleModal("Erro", "as senhas não conferem!");
    }
}
function preencherTabela() {
    $("#corpoTabela").html(" ");
    var keys = Object.keys(tabela[0]);
    var linha = "<tr>"


    for (var i = 0; i < keys.length; i++) {
        linha += `<th>${keys[i]}</th>`;
    }
    linha += "</tr>";
    $("#cabecalhoTabela").html(linha);
    for (var i = 0; i < tabela.length; i++) {
        addLinhaTabela(tabela[i]);
    }
}
function addLinhaTabela(linha) {
    var keys = Object.keys(linha);
    var linhaHtml = "<tr class='tr-shadow'>";

    for (var j = 0; j < keys.length; j++) {
        linhaHtml += `<td>${linha[keys[j]]}</td>`;

    }

    linhaHtml += `</tr><tr class="spacer"></tr>`;

    $("#corpoTabela").append(linhaHtml);
}
function excluirPessoaCerteza(id) {

    sendData("Pessoa", "excluirPessoa", {id: id}, function (obj) {
        showModal(obj);
        abrirPaginaListaPessoas(paginaListaPessoas)
    });
}
function excluirPessoa(id) {
    var modal = {
        title: "Exclusão de Pessoa",
        body: "Deseja realmente excluir esta pessoa do sistema? Ao excluí-la, todos os dados relacionados a ela (Documentos, alugueis, pagamentos, etc) também serão apagados.",
        primaryBtn: {
            text: "Sim",
            action: `excluirPessoaCerteza(${id})`
        },
        secondaryBtn: {
            text: "Não",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);

}
function abrirPaginaListaPessoas(pagina) {
    sendData("Pessoa", "getPessoas", {pagina: pagina}, function (data) {
        pessoas = data;
        tabela = [];
        preencherTabelaPessoas();
        for (var i = 1; i <= qtdPaginas; i++) {
            $("#navPage" + i).attr("class", "page-item");
        }
        $("#navPage" + pagina).attr("class", "page-item active");

    });
    paginaListaPessoas = pagina;
}
function verPagamentosAluguel(id) {
    openModalPage("Aluguel", "verPagamentos", {id: id});
}
function verObservacoesAluguel(id) {
    openModalPage("Aluguel", "verObservacoes", {id: id});
}
function verDocumentosPessoa(id) {
    openModalPage("Pessoa", "showDocs", pessoas[findItemListaById(id, pessoas)]);
}
function verDocumentosAluguel(id) {
    openModalPage("Aluguel", "showDocs", alugueis[findItemListaById(id, alugueis)]);
}
function verDocumentosImovel(id) {
    openModalPage("Imovel", "showDocs", imoveis[findItemListaById(id, imoveis)]);
}
function verFotosImovel(id) {
    openModalPage("Imovel", "showPhotos", imoveis[findItemListaById(id, imoveis)]);
}
function excluirDocumentoPessoa(id) {
    sendData("Pessoa", "excluirDocumento", {id: id}, showModal);
    $("#cardDoc" + id).remove();
    $("#modal").modal("toggle");
    abrirPaginaListaPessoas(paginaListaPessoas);
}
function excluirFotoImovel(id) {
    sendData("Imovel", "excluirFoto", {id: id}, showModal);
    $("#cardDoc" + id).remove();
}
function excluirObservacao(id) {
    sendData("Aluguel", "excluirObservacao", {id: id}, showModal);
}
function excluirDocumentoImovel(id) {
    sendData("Imovel", "excluirDocumento", {id: id}, showModal);
    $("#cardDoc" + id).remove();
    //$("#modal").modal("toggle");
}
function openModalPage(page, action, data) {
    $.ajax({
        url: "System",
        data: {action: action, page: page, data: JSON.stringify(data)},
        method: "POST"
    }).done(function (data) {
        $("#modal-lg-body").html(data);
        $("#modal-lg").modal();
    });
}
function findItemListaById(id, lista) {
    for (var i = 0; i < lista.length; i++) {
        if (lista[i].id === id) {
            return i;
        }
    }
    return -1;
}
function editarPessoa(id) {
    var pessoa = pessoas[findItemListaById(id, pessoas)];
    openPage("Pessoa", "editar", pessoa);
}
function editarImovel(id) {
    var imovel = imoveis[findItemListaById(id, imoveis)];
    openPage("Imovel", "editar", imovel);
}
function mudarFotoPerfil(obj, id) {
    var nomeArquivo = $(obj).val().split("\\")[2];
    var d = Date.now();
    var nomeFoto = d + "." + nomeArquivo.split(".")[nomeArquivo.split(".").length - 1];
    findArquivoById(id).nome = nomeFoto;
    sendData("Usuario", "mudarFoto", {foto: nomeFoto});
    $("#arquivosForm").submit();
    //arquivos=[];
}
function adicionarNovaFotoLista(obj, id) {
    var nomeArquivo = $(obj).val().split("\\")[2];
    var d = Date.now();
    var nomeFoto = d + "." + nomeArquivo.split(".")[nomeArquivo.split(".").length - 1];
    findArquivoById(id).nome = nomeFoto;
    var foto = {
        foto: nomeFoto
    };
    fotos.push(foto);
    var itemLista = `<li class="list-group-item" id="itemFoto${id}">
                                        
                                                ${nomeArquivo} 
                                          
                                                <a class="float-right" href="#" target="_blank" onclick="excluirFotoLista(${id}); return false"><i class="fas fa-2x fa-trash"></i></a> 
                                            </div>
                                        </div>
                                    </li>`;
    $("#listaFotos").append(itemLista);
}
function excluirFotoLista(id) {
    var pos = findPosArquivoById(id);
    console.log(pos);
    $("#itemFoto" + id).remove();
    fotos.splice(pos, 1);
    arquivos.splice(pos, 1);
    excluirArquivo(id);
}
function excluirArquivo(id) {
    $("#arquivoInput" + id).remove();
    $("#arquivoLabel" + id).remove()
}
function findPosArquivoById(id) {
    for (var i = 0; i < arquivos.length; i++) {
        if (arquivos[i].id == id) {
            return i;
        }
    }
}
function findArquivoById(id) {
    for (var i = 0; i < arquivos.length; i++) {
        if (arquivos[i].id == id) {
            return arquivos[i];
        }
    }
}
function adicionarNovoDocumentoLista(obj, id) {
    var nomeArquivo = $(obj).val().split("\\")[2];
    var d = Date.now();
    var nomeFoto = d + "." + nomeArquivo.split(".")[nomeArquivo.split(".").length - 1];
    findArquivoById(id).nome = nomeFoto;
    var doc = {
        foto: nomeFoto,
        documento: {id: 0}
    };
    documentos.push(doc);
    addListaOpcoesDoc(id, nomeArquivo, nomeFoto);
}
function addListaOpcoesDoc(id, nomeArquivo, nomeFoto) {
    var itemLista = `<li class="list-group-item" id="item${id}">
                                        <div class="row">
                                            <div class="col-3">
                                                ${nomeArquivo} 
                                            </div>
                                            <div class="col-7">
                                                <select id="selectDocs${id}" onchange="mudarTipoDocumento('${nomeFoto}',this)" class="form-control">
                                                   
                                                </select>
                                            </div>
                                            <div class="col-2">
                                                <a class="float-right" href="#" target="_blank" onclick="excluirItemLista(${id},'${nomeFoto}'); return false"><i class="fas fa-2x fa-trash"></i></a> 
                                            </div>
                                        </div>
                                    </li>`;
    $("#listaDocumentos").append(itemLista);

    var options = "<option>Selecione</option>";
    for (var i = 0; i < tipoDocs.length; i++) {
        options += `<option value="${tipoDocs[i].id}">${tipoDocs[i].nome}</option>`;
    }
    $(`#selectDocs${id}`).html(options);
}
function findDocByName(name) {
    for (var i = 0; i < documentos.length; i++) {
        if (documentos[i].foto === name) {
            return i;
        }
    }
}
function mudarTipoDocumento(nome, obj) {

    documentos[findDocByName(nome)].documento.id = $(obj).val();

}
function preencherPessoa() {
    $("#nomePessoa").val("Nome Teste" + Date.now());
    $("#emailPessoa").val("Email Teste");
    $("#telefonePessoa").val("Telefone Teste");
    $("#dataNascimentoPessoa").val("1111-11-11");
    $("#ruaEndereco").val("Rua Teste");
    $("#bairroEndereco").val("Bairro Teste");
    $("#cepEndereco").val("Endereço Teste");
    $("#cidadeEndereco").val("Cidade Teste");
    $("#numeroEndereco").val("2255");
    $("#complementoEndereco").val("Complemente Teste");
}
function excluirItemLista(id, foto) {

    $("#item" + id).remove();
    $("#documentosInput" + id).remove();
    $("#documentoLabel" + id).remove();
    documentos.splice(findDocByName(foto), 1);
    documentosAdicionados--;
    $("#qtyDocs").attr('value', documentosAdicionados);
    arquivos.splice(findArquivoById(id), 1);
    excluirArquivo(id);
    //$("#jsonDocs").attr("value", JSON.stringify(documentos))
}
function cadastrarPessoa() {
    var pessoa = getPessoa();
    if (validaPessoa(pessoa)) {
        sendData("Pessoa", "cadastrar", pessoa, enviarArquivos);
    }
    succsessPage = "Pessoa";
    succsessAction = "Cadastro";
}
function validaPessoa(p) {
    if (p.nome === "") {
        showSimpleModal("Erro", "Pessoa sem nome");
        return false;
    }
    if (p.email === "" || !p.email.includes("@") || !p.email.includes(".") || p.email.includes(" ")) {
        showSimpleModal("Erro", "Email inválido");
        return false;
    }
    return true;
}
function editarDadosPessoa() {
    var pessoa = getPessoa();
    if (validaPessoa(pessoa)) {
        sendData("Pessoa", "editarPessoa", pessoa, enviarArquivos);
    }
    succsessPage = "Pessoa";
    succsessAction = "Cadastro";
}
function editarDadosImovel() {
    var imovel = getImovel();
    sendData("Imovel", "editarImovel", imovel, enviarArquivos);
    succsessPage = "Imovel";
    succsessAction = "Cadastro";
}
function confirmarCadastro() {
    var pessoa = getPessoa();
    $("#modal").modal("toggle");
    sendData("Pessoa", "confirmarCadastro", pessoa, enviarArquivos);
    succsessPage = "Imovel";
    succsessAction = "Cadastro";

}
function enviarArquivos(obj) {
    if (obj.code == 1 && arquivos.length > 0) {
        $("#arquivosForm").submit();
    }
    if (obj.code == 1) {
        succsess();
    }
    showModal(obj.modal);
}
function succsess() {
    console.log("sucesson " + succsessPage);
    setTimeout('openPage(succsessPage, succsessAction);emptyVars();', 500);

}
function cadastrarDocumento() {
    var nomeDocumento = $("#nomeDocumento").val();
    var tipoDocumento = $("#tipoDocumento").val();
    if (nomeDocumento !== "" && tipoDocumento !== "") {
        var documento = {
            nome: nomeDocumento,
            tipo: tipoDocumento
        };
        sendData("Documento", "cadastrar", documento, showModal);
    } else {
        showSimpleModal("Atenção", "Você não selecionou o tipo do documento ou o nome está em branco");
    }
}
function confirmarExcluirObservacao(id) {
    var modal = {
        title: "Exclusão de Observação",
        body: "Deseja realmente excluir esta observação?",
        primaryBtn: {
            text: "Sim",
            action: `excluirObservacao(${id})`
        },
        secondaryBtn: {
            text: "Não",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);
}
function confirmarExcluirFotoImovel(id) {
    var modal = {
        title: "Exclusão de foto",
        body: "Deseja realmente excluir esta foto?",
        primaryBtn: {
            text: "Sim",
            action: `excluirFotoImovel(${id})`
        },
        secondaryBtn: {
            text: "Não",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);
}

function confirmarExcluirDocumentoImovel(id) {
    var modal = {
        title: "Exclusão de documento",
        body: "Deseja realmente excluir este documento?",
        primaryBtn: {
            text: "Sim",
            action: `excluirDocumentoImovel(${id})`
        },
        secondaryBtn: {
            text: "Não",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);
}
function confirmarExcluirDocumentoPessoa(id) {
    var modal = {
        title: "Exclusão de documento",
        body: "Deseja realmente excluir este documento desta pessoa?",
        primaryBtn: {
            text: "Sim",
            action: `excluirDocumentoPessoa(${id})`
        },
        secondaryBtn: {
            text: "Não",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);
}
function showModal(data) {
    $("#modal-title").html(data.title);
    $("#modal-body").html(data.body);
    $("#primary-modal-btn").html(data.primaryBtn.text);
    $("#secondary-modal-btn").html(data.secondaryBtn.text);
    $("#primary-modal-btn").attr("onclick", data.primaryBtn.action);
    $("#secondary-modal-btn").attr("onclick", data.secondaryBtn.action);

    $('#modal').modal();

}
/**
 * 
 * @param {type} idL tem que ser o mesmo nome do vetor que vai gerenciar
 * @returns {undefined}
 */
function procurarPessoa(idL) {
    abrirModalPesquisa('procurarCliente', "Pesquisar Pessoa");
    idLista = idL;
}
function pesquisarImovel() {
    if (locadores.length > 0) {
        sendData("Imovel", "pesquisar", locadores[0], procurarImovel);
    } else {
        showSimpleModal("Erro", "Nenhum locador selecionado");
    }
}
function procurarImovel(obj) {

    if (obj.length === 1) {
        var aux = obj[0];
        var linha = `<li class="list-group-item">Rua: ${aux.endereco.rua} Bairro: ${aux.endereco.bairro} Número: ${aux.endereco.numero} Cidade: ${aux.endereco.cidade} Complemento: ${aux.endereco.complemento}</li>`;
        $("#listaImoveis").html(linha);
        imovel = aux;
    } else if (obj.length === 0) {
        if (locadores.length === 1) {
            showSimpleModal("Atenção!", "Nenhum Imóvel cadastrado para o cliente!");
        } else if (locadores.length > 1) {
            showSimpleModal("Atenção!", "Um dos clientes não possui o imóvel cadastrado em seu nome.");
        }
    } else {
        var itens = [];
        for (var i = 0; i < obj.length; i++) {
            var aux = obj[i];
            itens.push({id: obj[i].id, str: `Rua: ${aux.endereco.rua} Bairro: ${aux.endereco.bairro} Número: ${aux.endereco.numero} Cidade: ${aux.endereco.cidade} Complemento: ${aux.endereco.complemento}`});
        }
        abrirModalSelecao('escolherImovelAluguel', itens);
    }
}
function escolherImovelAluguel(id) {
    $("#modal").modal("toggle");
    var linha = `<li class="list-group-item">${imovel.enderecoo}</li>`;
    $("#listaImoveis").html(linha);
}
function showSimpleModal(title, body) {
    $("#modal-title").html(title);
    $("#modal-body").html(body);
    $("#primary-modal-btn").html("Ok");
    $("#secondary-modal-btn").html("Cancelar");
    $("#primary-modal-btn").attr("onclick", `$("#modal").modal('toggle');`);
    $("#secondary-modal-btn").attr("onclick", `$("#modal").modal('toggle');`);

    $('#modal').modal();
}
/**
 * 
 * @param {type} onClick função que vai ser chamada no click do item selecionado
 * @param {type} itens // vetor com objetos do tipo {id,str} pra ser processado
 * @returns {undefined}
 */
function abrirModalSelecao(onClick, itens) {
    var formConsulta = `<ul class="list-group" >`;
    for (var i = 0; i < itens.length; i++) {
        var aux = itens[i].str;
        formConsulta += `<li class="list-group-item"><a href="#" target="_blank" onclick="imovel={id:${itens[i].id},enderecoo:'${aux}' };${onClick}(${itens[i].id}); return false;">${aux}</a></li>`;
    }
    formConsulta += `</ul>`;
    $("#modal-title").html("Encontrar cliente");
    $("#modal-body").html(formConsulta);
    $("#primary-modal-btn").html("Selecionar");
    $("#secondary-modal-btn").html("Cancelar");
    $("#primary-modal-btn").attr("onclick", `$("#modal").modal('toggle');`);
    $("#secondary-modal-btn").attr("onclick", `$("#modal").modal('toggle');`);
    $("#modal").modal();
}
function abrirModalPesquisa(onClick, placeholder) {
    var formConsulta = ` <div class="form-group">
                            <div class="row">
                                <div class="col-10">
                                    <input type="text" id="txtCliente" class="form-control" placeholder="${placeholder}"> 
                                </div>
                                <div class="col-2">
                                    <button class="btn btn-primary btn-lg form-control" onclick="${onClick}()"><i class="fas fa fa-search"></i> </button>
                                </div>
                            </div>
                        </div>
    <span id="listaPesquisa" class="text-center"></span>`;
    $("#modal-title").html("Encontrar cliente");
    $("#modal-body").html(formConsulta);
    $("#primary-modal-btn").html("Selecionar");
    $("#secondary-modal-btn").html("Cancelar");
    $("#primary-modal-btn").attr("onclick", "");
    $("#secondary-modal-btn").attr("onclick", `$("#modal").modal('toggle');`);
    $("#modal").modal();
}
function procurarCliente() {
    var nomeCliente = {nome: $("#txtCliente").val()};
    sendData("Pessoa", "getClienteByNome", nomeCliente, fillClientList);
}
function fillClientList(obj) {
    var conteudo = "";
    for (var i = 0; i < obj.length; i++) {
        conteudo += (`<a href="#" target="_blank" onclick="selecionarCliente(${obj[i].id},'${obj[i].nome}',${idLista} ); return false">${obj[i].nome}</a><br>`);
    }
    $("#listaPesquisa").html(conteudo);
}
function selecionarCliente(id, nome, lista) {
    var locador = {id: id, nome: nome};
    var itemLista = `<li class="list-group-item text-center" id="lista${idLista}${id}">
                        ${nome}
                        <a class="float-right" href="#" target="_blank" onclick="excluirClienteLista(${id},${idLista},'${idLista}'); return false"><i class="fas fa-2x fa-trash"></i></a>
                    </li>`;
    $("#lista" + idLista).append(itemLista);
    $("#modal").modal("toggle");
    lista.push(locador);
}
function excluirClienteLista(id, lista, str) {
    //essa pog tá aqui porque, ao excluir um cliente da lista, é necessário clicar no botão de procura imóvel de novo, pode acontecer de ter selecionado um cliente antes, escolher o imovel e depois apagar o cliente e selecionar outro.  
    if (lista === locadores) {
        imovel = {}
        $("#listaImoveis").html("");
    }
    for (var i = 0; i < lista.length; i++) {
        if (lista[i].id == id) {
            lista.splice(i, 1);
            $("#lista" + str + id).remove();
        }
    }
}
function getImovel() {
    var li = [];
    for (var i = 0; i < locadoresImovel.length; i++) {
        var id = 0;
        if (locadoresImovel[i].idLI !== undefined) {
            li.push({id: locadoresImovel[i].idLI, locatario: locadoresImovel[i]});
        } else {
            li.push({locatario: locadoresImovel[i]});
        }
    }
    var imovel = {
        id: idImovel,
        endereco: {
            rua: $("#ruaEndereco").val(),
            bairro: $("#bairroEndereco").val(),
            cep: $("#cepEndereco").val(),
            cidade: $("#cidadeEndereco").val(),
            numero: $("#numeroEndereco").val(),
            complemento: $("#complementoEndereco").val()
        },
        fotoImovelList: fotos,
        documentoImovelList: documentos,
        locatarioImovelList: li

    };
    return imovel;
}
function cadastrarImovel() {
    var dados = getImovel();
    succsessPage = "Imovel";
    succsessAction = "Cadastro";
    sendData("Imovel", "cadastrar", dados, enviarArquivos);

}
function getAluguel() {
    imovel.locatarioImovelList = locadores;
    var locatarioAluguelList = [];

    for (var i = 0; i < locatarios.length; i++) {
        locatarioAluguelList.push({locatario: locatarios[i]});
    }

    var aluguel = {
        documentoAluguelList: documentos,
        locatarioAluguelList: locatarioAluguelList,
        imovel: imovel,
        dataInicioContrato: $("#dataInicioContrato").val(),
        dataFimContrato: $("#dataFimContrato").val(),
        dataPrimeiroPagamento: $("#dataPrimeiroPagamento").val(),
        diaPagamento: $("#diaPagamento").val(),
        garantia: $("#garantiaContrato").val(),
        tipoPagamento: $(document.getElementsByName("radios")).val(),
        valor: $("#valorAluguel").val()
    };
    return aluguel;
}
function cadastrarAluguel() {
    if (validaFormularioAluguel()) {
        var aluguel = getAluguel();
        succsessAction = "cadastro";
        succsessPage = "Aluguel";
        sendData("Aluguel", "cadastrar", aluguel, enviarArquivos);
    }
}
function validaFormularioAluguel() {
    var aviso = "";
    var retorno = false;
    if (imovel == null) {
        aviso = "Nenhum imóvel selecionado";
    }
    if (locadores.length == 0) {
        aviso = "Nenhum locador selecionado";
    }
    if (locatarios == 0) {
        aviso = "Nenhum locatário selecionado";
    }
    var aluguel = getAluguel();
    if (aluguel.dataFimContrato == "") {
        aviso = "Data do fim do contrato não preenchida";
    } else if (aluguel.dataInicioContrato == "") {
        aviso = "Data do inicio do contrato não preenchida";
    } else if (aluguel.dataPrimeiroPagamento == "") {
        aviso = "Data do primeiro dia de pagamento do contrato não preenchida";
    } else if (aluguel.diaPagamento == "") {
        aviso = "Dia do pagamento não preenchido";
    } else if (aluguel.garantia == "") {
        aviso = "Garantia do contrato não preenchida";
    } else if (aluguel.tipoPagamento == "") {
        aviso = "Tipo de pagamento não preenchido";
    } else if (aluguel.valor == "") {
        aviso = "Valor do aluguel não preenchido";
    } else if (!validaDocumentos()) {
        aviso = "Algum dos documentos não tem o tipo selecionado";
    } else {
        retorno = true;
    }
    if (!retorno) {
        showSimpleModal("Aviso", aviso);
    }
    return retorno;
}
function validaDocumentos() {
    for (var i = 0; i < documentos.length; i++) {
        if (documentos[i].documento.id === 0) {
            return false;
        }
    }
    return true;
}
function preencherAluguel() {
    $("#dataInicioContrato").val("1111-11-11");
    $("#dataFimContrato").val("1111-11-11");
    $("#dataPrimeiroPagamento").val("1234-11-11");
    $("#diaPagamento").val("5");
    $("#garantiaContrato").val("Garantia Teste");
    $(document.getElementsByName("radios")).val("1");
    $("#valorAluguel").val("1599");
}
function cadastrarPermissao() {
    var i = 0;
    var grupo = {
        grupo: $("#nomeGrupo").val(),
        permissoes: []
    };
    while ($("#permissaoLiberada" + i).val() !== undefined) {
        var checkBox = document.getElementById("permissaoLiberada" + i);
        if (checkBox.checked) {
            grupo.permissoes.push(i);
        }
        i++;
    }
    sendData("Grupo", "cadastrar", grupo, showModal);
}
function cadastrarUsuario() {
    var usuario = {
        nome: $("#nomeUsuario").val(),
        grupo: {id: $("#grupoUsuario").val()},
        usuario: $("#usuarioUsuario").val()
    };
    sendData("Usuario", "cadastrar", usuario, showModal);
}
function get(classe, id, whenDone) {
    $.ajax({
        url: "Json",
        method: "POST",
        data: {classe: classe, id: id}
    }).done(function (data) {
        var dados = JSON.parse(data);
        whenDone(dados);
    });
}
function preencherTabelaImoveis() {

    for (var i = 0; i < imoveis.length; i++) {
        var imovel = imoveis[i];
        var endereco = imovel.endereco.rua + " , " + imovel.endereco.bairro + " - " + imovel.endereco.cidade;
        var locadores = `<button class="btn btn-primary btn-md btn-block" onclick="listarLocadoresImovel(${imovel.id})"><i class="fa fa-search"></i> Ver  </button>`;
        var fotos = `<button class="btn btn-primary btn-md btn-block" onclick="verFotosImovel(${imovel.id})"><i class="fa fa-search"></i> Abrir </button>`;
        var documentos = `<button class="btn btn-primary btn-md btn-block" onclick="verDocumentosImovel(${imovel.id})"><i class="fa fa-search"></i> Abrir </button>`;
        var opcoes = `<div class="table-data-feature float-left">

                            <button class="item" data-toggle="tooltip" data-placement="top" onclick="editarImovel(${imovel.id})" title="Editar imóvel" data-original-title="Editar">
                                <i class="zmdi zmdi-edit"></i>
                            </button>
                            <button class="item" data-toggle="tooltip" data-placement="top" title="Deletar pessoa" onclick="confirmarExcluirImovel(${imovel.id})" data-original-title="Deletar">
                                <i class="zmdi zmdi-delete"></i>
                            </button>

                        </div>`;
        tabela.push({id: imovel.id, endereco: endereco, locadores: locadores, fotos: fotos, documentos: documentos, opcoes: opcoes});
    }
    preencherTabela();
}

function confirmarExcluirPagamento(id) {
    var modal = {
        title: "Exclusão de pagamento",
        body: "Deseja realmente excluir este pagamento?",
        primaryBtn: {
            text: "Sim",
            action: `excluirPagamento(${id})`
        },
        secondaryBtn: {
            text: "Não",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);
}
function confirmarExcluirImovel(id) {
    var modal = {
        title: "Exclusão de Imóvel",
        body: "Deseja realmente excluir este imóvel?",
        primaryBtn: {
            text: "Sim",
            action: `excluirImovel(${id})`
        },
        secondaryBtn: {
            text: "Não",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);
}
function excluirImovel(id) {
    sendData("Imovel", "excluir", {id: id}, function (data) {
        showModal(data);
        openPage("Imovel", "Listar");
    });
}
function excluirPagamento(id) {
    sendData("Aluguel", "excluirPagamento", {id: id}, showModal)
}
function preencherTabelaAlugueis() {
    $("#corpoTabela").html("");
    for (var i = 0; i < alugueis.length; i++) {
        var aluguel = alugueis[i];

        var documentos = `<button class="btn btn-primary btn-md btn-block" onclick="verDocumentosAluguel(${aluguel.id})"><i class="fa fa-search"></i> Ver </button>`;
        var imovel = ` <button class="btn btn-primary btn-md btn-block" onclick="listarImovelAluguel(${aluguel.id})"><i class="fa fa-search"></i> Abrir </button>`;
        var locatarios = `<button class="btn btn-primary btn-md" onclick="openModalPage('Aluguel','listarLocatariosAluguel',{id:${aluguel.id}})"><i class="fa fa-search"></i> Ver </button> <button class="btn btn-primary btn-md " onclick="verLocatariosAluguel(${aluguel.id})"><i class="fa fa-search"></i> Abrir </button>`;
        var locadores = `<button class="btn btn-primary btn-md" onclick="openModalPage('Aluguel','listarLocadoresAluguel',{id:${aluguel.id}})"><i class="fa fa-search"></i> Ver </button> <button class="btn btn-primary btn-md" onclick="verLocadoresAluguel(${aluguel.id})"><i class="fa fa-search"></i> Abrir </button>`
        var pagamentos = `<button class="btn btn-primary btn-md btn-block" onclick="verPagamentosAluguel(${aluguel.id})"><i class="fa fa-search"></i> Abrir </button>`;
        var observacoes = `<button class="btn btn-primary btn-md btn-block" onclick="verObservacoesAluguel(${aluguel.id})"><i class="fa fa-search"></i> Abrir </button>`;
        var opcoes = `<div class="table-data-feature float-left">

                            <button class="item" data-toggle="tooltip" data-placement="top" onclick="editarPessoa(${aluguel.id})" title="Editar pessoa" data-original-title="Editar">
                                <i class="zmdi zmdi-edit"></i>
                            </button>
                            <button class="item" data-toggle="tooltip" data-placement="top" title="Deletar pessoa" onclick="" data-original-title="Deletar">
                                <i class="zmdi zmdi-delete"></i>
                            </button>

                        </div>`;
        tabela.push({id: aluguel.id, documentos: documentos, imovel: imovel, locatarios: locatarios, locadores, pagamentos: pagamentos, observacoes: observacoes});
    }
    preencherTabela();

}


function preencherTabelaPessoas() {
    $("#corpoTabela").html("");
    for (var i = 0; i < pessoas.length; i++) {
        var pessoa = pessoas[i];

        var documentos = `<button class="btn btn-primary btn-md btn-block" onclick="verDocumentosPessoa(${pessoa.id})"><i class="fa fa-search"></i> Ver </button>`;
        var imoveis = ` <button class="btn btn-primary btn-md btn-block" onclick="listarImoveisLocador(${pessoa.id})"><i class="fa fa-search"></i> Abrir </button>`;
        var alugueis = ` <button class="btn btn-primary btn-md btn-block" onclick="listarAlugueisPessoa(${pessoa.id})"><i class="fa fa-search"></i> Abrir </button>`;
        var opcoes = `<div class="table-data-feature float-left">

                            <button class="item" data-toggle="tooltip" data-placement="top" onclick="editarPessoa(${pessoa.id})" title="Editar pessoa" data-original-title="Editar">
                                <i class="zmdi zmdi-edit"></i>
                            </button>
                            <button class="item" data-toggle="tooltip" data-placement="top" title="Deletar pessoa" onclick="excluirPessoa(${pessoa.id})" data-original-title="Deletar">
                                <i class="zmdi zmdi-delete"></i>
                            </button>

                        </div>`;
        tabela.push({id: pessoa.id, nome: pessoa.nome, email: pessoa.email, documentos: documentos, imoveis: imoveis, alugueis: alugueis, opcoes: opcoes});
    }
    preencherTabela();

}
function listarDadosById(id, classe, action, array, func) {
    emptyVars();
    tabela = [];

    while (array.length !== 0) {
        array.pop();
    }
    sendData(classe, action, {id: id}, function (data) {
        console.log("---");
        console.log(data);
        console.log("---");
        array.push(...data);
        func();
    });
}
function listarImoveisLocador(id) {
    imoveis = [];
    listarDadosById(id, "Pessoa", "listarImoveis", imoveis, preencherTabelaImoveis);
}
function listarAlugueisPessoa(id) {
    alugueis = [];
    listarDadosById(id, "Pessoa", "listarAlugueis", alugueis, preencherTabelaAlugueis);
}
function listarImovelAluguel(id) {
    imoveis = [];
    listarDadosById(id, "Aluguel", "listarImovel", imoveis, preencherTabelaImoveis);
}
function listarLocadoresImovel(id) {
    pessoas = [];
    listarDadosById(id, "Imovel", "listarLocadores", pessoas, preencherTabelaPessoas);
}
function verLocatariosAluguel(id) {
    pessoas = [];
    listarDadosById(id, "Aluguel", "listarLocatarios", pessoas, preencherTabelaPessoas);
}
function verLocadoresAluguel(id) {
    pessoas = [];
    listarDadosById(id, "Aluguel", "listarLocadores", pessoas, preencherTabelaPessoas);
}
function pesquisarItem() {
    var pesquisa = $("#pesquisaTxt").val();
    var fpreencher;
    tabela = [];
    switch (paginaAtual) {
        case "Pessoa":
            fpreencher = function (data) {
                pessoas = data;
                preencherTabelaPessoas();
            };
            sendData(paginaAtual, "pesquisar", {pesquisa: pesquisa}, fpreencher);
            break;
    }


}
function adicionarObservacaoAluguel(id) {
    var modal = {
        title: "Cadastro de pagamento",
        body: `<div class="form-group">
                   <label class="form-control-label">Digite a observação</label>
                   <textarea  class="form-control" id="observacaoAluguel" placeholder="Observação"></textarea>
                </div>
                `,
        primaryBtn: {
            text: "Cadastrar",
            action: `cadastrarObservacaoAluguel(${id})`
        },
        secondaryBtn: {
            text: "Cancelar",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);
}
function cadastrarObservacaoAluguel(id) {
    var obs = $("#observacaoAluguel").val();
    sendData("Aluguel", "cadastrarObservacao", {aluguel: {id: id}, observacao: obs}, showModal);
}
function adicionarPagamentoAluguel(id) {
    var modal = {
        title: "Cadastro de pagamento",
        body: `<div class="form-group">
                   <label class="form-control-label">Valor do pagamento</label>
                   <input type="number" class="form-control" id="valorPagamento" placeholder="Valor">
                </div>
                <div class="form-group">
                    <label class="form-control-label">Data da efetuação</label>
                   <input type="date" class="form-control" id="dataEfetuacaoPagamento">
                </div>
                <div class="form-group">
                    <label class="form-control-label">Data de repasse</label>
                   <input type="date" class="form-control" id="dataRepassePagamento" >
                </div>
                <div class="form-group">
                    <label class="form-control-label">Recibo</label>
                    <form id="arquivosForm"  action="Upload" method="post" enctype="multipart/form-data">
                        <input type="file" form="arquivosForm" name="documento1" id="arquivoInput1" >
                        <input type="hidden" form="arquivosForm" name="documentoLabel1" value="documento1" id="arquivoLabel1">
                    </form>
                    
                </div>  
                <script>$("#arquivosForm").submit(function (e) {
            var formData = new FormData(this);
            formData.append("docs", JSON.stringify(arquivos));
            formData.append("qty", documentos.length);
            $.ajax({
                url: 'Upload', 
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
        });</script>
                `,
        primaryBtn: {
            text: "Cadastrar",
            action: `cadastrarPagamentoAluguel(${id})`
        },
        secondaryBtn: {
            text: "Cancelar",
            action: `$("#modal").modal("toggle")`
        }
    };
    showModal(modal);
}

function cadastrarPagamentoAluguel(id) {
    var valorPagamento = $("#valorPagamento").val();
    var dataEfetuacaoPagamento = $("#dataEfetuacaoPagamento").val();
    var dataRepassePagamento = $("#dataRepassePagamento").val();
    var recibo = "";
    if ($("#arquivoInput1").val().split("\\")[2] !== undefined) {
        var extencao = $("#arquivoInput1").val().split("\\")[2].split(".")[1];
        var recibo = "recibo" + id + Date.now() + "." + extencao;
        arquivos[0] = {id: 1, nome: recibo};
    }
    var pagamento = {valor: valorPagamento, dataEfetuacao: dataEfetuacaoPagamento, dataRepasse: dataRepassePagamento, recibo: recibo, aluguel: {id: id}};
    sendData("Aluguel", "cadastrarPagamento", pagamento, showModal);
    $("#arquivosForm").submit();
}

function gerarRelatorioRendimentoLocador(tipo) {
    var locador = locadoresImovel[0];
    if (locador === undefined) {
        showSimpleModal("Erro", "Nenhum locador selecionado");
    } else {
        var dataInicio = $("#dataInicioRelatorio").val();
        var dataFim = $("#dataFimRelatorio").val();
        if (dataInicio === "") {
            dataInicio = "1111-11-11";
        }
        if (dataFim === "") {
            dataFim = "2050-11-11";
        }
        console.log(dataInicio);
        openPage("Relatorios", tipo, {locador: locador, dataInicio: dataInicio, dataFim: dataFim});
    }
}

function imprimirRelatorio() {
    var conteudo = document.getElementById('conteudo').innerHTML.replace("(Imprimir)","").replace(`border="0"`,`border="1"` ),
            
            tela_impressao = window.open('about:blank');

    tela_impressao.document.write(conteudo);
    tela_impressao.window.print();
    tela_impressao.window.close();
}