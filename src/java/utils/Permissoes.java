package utils;

import java.util.ArrayList;
import java.util.List;

public class Permissoes {

    private final List<Permissao> PERMISSOES = new ArrayList<>();
    private static Permissoes instance;

    private final List<Permissao> LIVRES = new ArrayList<>();

    private Permissoes() {
        PERMISSOES.add(new Permissao("Cadastrar clientes", "Pessoa", new String[]{"Cadastro", "cadastrar", "confirmarCadastro", "getClienteByNome", "validaDocumentos"}));
        PERMISSOES.add(new Permissao("Cadastrar imóveis", "Imovel", new String[]{"Cadastro", "cadastrar", "pesquisar", "validaDocumentos"}));
        PERMISSOES.add(new Permissao("Cadastrar aluguéis", "Aluguel", new String[]{"cadastro", "cadastrar"}));
        PERMISSOES.add(new Permissao("Cadastrar usuários", "Usuario", new String[]{"Cadastro", "cadastrar"}));
        PERMISSOES.add(new Permissao("Cadastrar grupos de permissões", "Grupo", new String[]{"Cadastro", "cadastrar"}));
        PERMISSOES.add(new Permissao("Cadastrar novo tipo de documento", "Documento", new String[]{"Cadastro", "cadastrar"}));
        PERMISSOES.add(new Permissao("Visualizar dados de pessoas", "Pessoa", new String[]{"Listar", "getPessoas", "showDocs", "listarImoveis", "pesquisar", "listarAlugueis"}));
        PERMISSOES.add(new Permissao("Editar pessoas", "Pessoa", new String[]{"excluirDocumento", "editar", "editarPessoa"}));
        PERMISSOES.add(new Permissao("Excluir pessoas", "Pessoa", new String[]{"excluirPessoa"}));
        PERMISSOES.add(new Permissao("Visulaizar dados de imóveis", "Imovel", new String[]{"Listar", "getImoveis", "listarLocadores", "showDocs", "showPhotos", "pesquisar"}));
        PERMISSOES.add(new Permissao("Editar dados de imóveis", "Imovel", new String[]{"excluirDocumento", "excluirFoto", "editar", "editarImovel"}));
        PERMISSOES.add(new Permissao("Excluir imóveis", "Imovel", new String[]{"excluir"}));
        PERMISSOES.add(new Permissao("Visualizar dados de alugueis", "Aluguel", new String[]{"Listar", "showDocs", "listarImovel", "listarLocatarios", "listarLocadores", "verPagamentos","listarLocatariosAluguel","listarLocadoresAluguel"}));
        PERMISSOES.add(new Permissao("Cadastrar pagamentos de aluguéis", "Aluguel", new String[]{"cadastrarPagamento"}));
        PERMISSOES.add(new Permissao("Excluir pagamentos de aluguéis", "Aluguel", new String[]{"excluirPagamento"}));
        PERMISSOES.add(new Permissao("Visualizar observações de aluguel", "Aluguel", new String[]{"verObservacoes"}));
        PERMISSOES.add(new Permissao("Cadastrar observações de aluguel", "Aluguel", new String[]{"cadastrarObservacao"}));
        PERMISSOES.add(new Permissao("Excluir observações de aluguel", "Aluguel", new String[]{"excluirObservacao"}));
        PERMISSOES.add(new Permissao("Gerar relatórios", "Relatorios", new String[]{"rendimentosLocador","rendimentosLocatario","gerarRelatorioRendimentosLocador","gerarRelatorioRendimentosLocatario"}));

        LIVRES.add(new Permissao("", "Sistema", new String[]{"execute"}));
        LIVRES.add(new Permissao("", "Usuario", new String[]{"mudarFoto", "gerenciarConta", "mudarSenha"}));
    }

    public List<Permissao> getPermissoes() {
        return PERMISSOES;
    }

    public List<Permissao> getPermissoesLivres() {
        return LIVRES;
    }

    public static Permissoes getInstance() {
        if (instance == null) {
            instance = new Permissoes();
        }
        return instance;
    }
}
