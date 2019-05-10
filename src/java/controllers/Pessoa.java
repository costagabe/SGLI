package controllers;

import beans.DocumentoPessoa;
import beans.LocatarioAluguel;
import beans.LocatarioImovel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import daos.DocumentoJpaController;
import daos.DocumentoPessoaJpaController;
import daos.EnderecoJpaController;
import daos.LocatarioAluguelJpaController;
import daos.LocatarioImovelJpaController;
import daos.PessoaJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Copier;
import utils.Modal;

/**
 *
 * @author Gabriel Alves
 */
public class Pessoa implements Controller {

    private boolean flagCadastro = false;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "";
    }

    public List<beans.Pessoa> getListaPessoas(int pagina, EntityManagerFactory emf) throws InstantiationException, IllegalAccessException {
        PessoaJpaController pessoaDao = new PessoaJpaController(emf);
        int conta = pagina * 10 - 10;
        ArrayList<beans.Pessoa> ret = new ArrayList();
        pessoaDao.findPessoaEntities(10, conta).forEach(o ->{beans.Pessoa nova = o.clone();ret.add(nova);});
        return ret;
    }

    public String getPessoas(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();

        int pagina = gson.fromJson(req.getParameter("data"), JsonObject.class).get("pagina").getAsInt();

        List<beans.Pessoa> pessoas = getListaPessoas(pagina, emf);

        req.setAttribute("json", gson.toJson(pessoas));
        return "Json";
    }

    public String excluirPessoa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();

        int id = gson.fromJson(req.getParameter("data"), JsonObject.class).get("id").getAsInt();
        PessoaJpaController pessoaDao = new PessoaJpaController(emf);
        beans.Pessoa p = pessoaDao.findPessoa(id);
        DocumentoPessoaJpaController dpDao = new DocumentoPessoaJpaController(emf);
        LocatarioAluguelJpaController laDao = new LocatarioAluguelJpaController(emf);
        LocatarioImovelJpaController liDao = new LocatarioImovelJpaController(emf);
        for (DocumentoPessoa dp : p.getDocumentoPessoaList()) {
            dpDao.destroy(dp.getId());
        }
        for (LocatarioAluguel la : p.getLocatarioAluguelList()) {
            laDao.destroy(la.getId());
        }
        for (LocatarioImovel li : p.getLocatarioImovelList()) {
            liDao.destroy(li.getId());
        }
        new PessoaJpaController(emf).destroy(id);
        Modal m = new Modal("Sucesso", "Pessoa deleletada com sucesso!");
        req.setAttribute("json", m.getJson());
        return "Json";
    }

    public String showDocs(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.Pessoa p = gson.fromJson(req.getParameter("data"), beans.Pessoa.class);
        p = new PessoaJpaController(emf).findPessoa(p.getId());
        req.setAttribute("docs", p.getDocumentoPessoaList());
        req.setAttribute("excluir", "confirmarExcluirDocumentoPessoa");
        return "image-viwer.jsp";
    }
     public String pesquisar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
         Gson gson = new Gson();
         String pesquisa = gson.fromJson(req.getParameter("data"), JsonObject.class).get("pesquisa").getAsString();
         List<beans.Pessoa> pessoas = new ArrayList();
         List<beans.Pessoa> inDb = new PessoaJpaController(emf).findAllByNome(pesquisa);
         for(beans.Pessoa p : inDb){
             pessoas.add(p.clone());
         }
         req.setAttribute("json", gson.toJson(pessoas));
         return "Json";
     }

    public String editar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        DocumentoJpaController docDao = new DocumentoJpaController(emf);
        req.setAttribute("tiposDocumentos", docDao.findDocumentoEntities(1));
        beans.Pessoa p = gson.fromJson(req.getParameter("data"), beans.Pessoa.class);
        p = new PessoaJpaController(emf).findPessoa(p.getId());
        req.setAttribute("pessoa", (p));
        return "cadastro-pessoa.jsp";
    }

    public String editarPessoa(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Pessoa p = new Gson().fromJson(req.getParameter("data"), beans.Pessoa.class);

        PessoaJpaController pessoaDao = new PessoaJpaController(emf);
        beans.Pessoa inDB = pessoaDao.findPessoa(p.getId());
        p.setLocatarioAluguelList(inDB.getLocatarioAluguelList());
        p.setLocatarioImovelList(inDB.getLocatarioImovelList());
        List<DocumentoPessoa> documentos = p.getDocumentoPessoaList();
        DocumentoPessoaJpaController documentoDao = new DocumentoPessoaJpaController(emf);

        for (DocumentoPessoa dp : documentos) {
            if (dp.getId() != null) {
                DocumentoPessoa dpInDb = documentoDao.findDocumentoPessoa(dp.getId());
                dpInDb.setDocumento(dp.getDocumento());
                documentoDao.edit(dpInDb);
                dp = dpInDb;
            } else {
                dp.setPessoa(p);
                documentoDao.create(dp);
            }
        }

        p.getEndereco().setId(inDB.getEndereco().getId());
        p.getEndereco().setImovelList(inDB.getEndereco().getImovelList());
        p.getEndereco().setPessoaList(inDB.getEndereco().getPessoaList());
        new EnderecoJpaController(emf).edit(p.getEndereco());
        pessoaDao.edit(p);
        Modal m = new Modal("Sucesso", "Edição concluida com sucesso!");
        JsonObject obj = new JsonObject();
        obj.addProperty("code", "1");
        obj.add("modal", new Gson().toJsonTree(m));

        req.setAttribute("json", obj);
        return "Json";
    }

    public String excluirDocumento(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.DocumentoPessoa dp = gson.fromJson(req.getParameter("data"), beans.DocumentoPessoa.class);
        new DocumentoPessoaJpaController(emf).destroy(dp.getId());
        Modal m = new Modal("Sucesso", "Exclusão feita com sucesso!");
        req.setAttribute("json", gson.toJson(m));
        return "Json";
    }

    public String Listar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        int pagina = 1;

        List<beans.Pessoa> pessoas = getListaPessoas(1, emf);
        int paginas = (int) (new PessoaJpaController(emf).getPessoaCount() / 10) + 1;
        req.setAttribute("paginas", paginas);
        req.setAttribute("listaPessoas", gson.toJson(pessoas));
        return "listar-pessoas.jsp";
    }

    public String Cadastro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        DocumentoJpaController docDao = new DocumentoJpaController(emf);
        req.setAttribute("tiposDocumentos", docDao.findDocumentoEntities(1));
        return "cadastro-pessoa.jsp";
    }

    public String getClienteByNome(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        String nome = new Gson().fromJson(req.getParameter("data"), JsonObject.class).get("nome").toString();
        JsonArray obj = new JsonArray();
        PessoaJpaController pessoaDao = new PessoaJpaController(emf);
        List<beans.Pessoa> encontrados = pessoaDao.findAllByNome(nome);
        if (!encontrados.isEmpty()) {
            for (beans.Pessoa p : encontrados) {
                JsonObject pessoa = new JsonObject();
                pessoa.addProperty("id", p.getId());
                pessoa.addProperty("nome", p.getNome());
                obj.add(pessoa);
            }
        } else {
            JsonObject nulo = new JsonObject();
            nulo.addProperty("id", 0);
            nulo.addProperty("nome", "Nenhum cliente com esse nome foi encontrado");
            obj.add(nulo);
        }
        req.setAttribute("json", obj);
        return "Json";

    }

    public String confirmarCadastro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        flagCadastro = true;
        return cadastrar(req, res, emf);
    }
    public String listarImoveis(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Pessoa p = new Gson().fromJson(req.getParameter("data"), beans.Pessoa.class);
        p = new PessoaJpaController(emf).findPessoa(p.getId());
        List<beans.Imovel> imoveis = new ArrayList<>();
        
        for(LocatarioImovel li : p.getLocatarioImovelList()){
            beans.Imovel novo = li.getImovel().clone();
            novo.setEndereco(li.getImovel().getEndereco().clone());
            imoveis.add(novo);
        }
        
        req.setAttribute("json", new Gson().toJson(imoveis));
        return "Json";
        
    }
    public String listarAlugueis(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.Pessoa p = gson.fromJson(req.getParameter("data"), beans.Pessoa.class);
        p = new PessoaJpaController(emf).findPessoa(p.getId());
        List<beans.Aluguel> alugueis = new ArrayList<>();
        for(LocatarioAluguel la : p.getLocatarioAluguelList()){
            alugueis.add(la.getAluguel().clone());
        }
        for(beans.LocatarioImovel i : p.getLocatarioImovelList()){
            for(beans.Aluguel a: i.getImovel().getAluguelList()){
                alugueis.add(a.clone());
            }
        }
        req.setAttribute("json", gson.toJson(alugueis));
        return "Json";
    }
    public String cadastrar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        System.out.println(req.getParameter("data"));
        beans.Pessoa p = gson.fromJson(req.getParameter("data"), beans.Pessoa.class);
        List<DocumentoPessoa> documentos = p.getDocumentoPessoaList();
        p.setDocumentoPessoaList(new ArrayList<>());

        PessoaJpaController pessoaDao = new PessoaJpaController(emf);
        JsonObject obj = new JsonObject();
        Modal m = null;
        if (pessoaDao.findByNome(p.getNome()) == null || flagCadastro) {
            if (validaDocumentos(documentos)) {
                new EnderecoJpaController(emf).create(p.getEndereco());
                new PessoaJpaController(emf).create(p);
                DocumentoPessoaJpaController dpDao = new DocumentoPessoaJpaController(emf);

                for (DocumentoPessoa dp : documentos) {
                    dp.setPessoa(p);
                    dpDao.create(dp);
                }

                m = new Modal("Sucesso", "Cadastro realizado com sucesso!");
                m.primaryBtn.action = " $('#modal').modal('toggle')";
                obj.addProperty("code", 1);
                obj.addProperty("id", p.getId());
            } else {
                m = new Modal("Erro", "Algum dos documentos não tem o tipo selecionado <span style='color:red'>O cadastro não foi realizado </span>");
                obj.addProperty("code", "2");
            }
        } else {
            m = new Modal("Atenção", "Já existe uma pessoa no sistema com esse nome. Deseja continuar? <br> <span style='color:red'>Obs: O cadastro não foi realizado, só será realizado, caso confirme essa mensagem </span>");
            m.secondaryBtn.action = " $('#modal').modal('toggle')";
            m.primaryBtn.text = "Confirmar";
            m.primaryBtn.action = "confirmarCadastro()";

            obj.addProperty("code", "2");

        }
        obj.add("modal", new Gson().toJsonTree(m, Modal.class));
        req.setAttribute("json", obj);
        return "Json";
    }

    public String uploadDocumento(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        //req.setAttribute("json", "UP DOCS");
        return "Upload";
    }

    private boolean validaDocumentos(List<DocumentoPessoa> documentos) {
        for (DocumentoPessoa dp : documentos) {
            if (dp.getDocumento().getId() == 0) {
                return false;
            }
        }
        return true;
    }

    private beans.Pessoa[] serializePessoas(List<beans.Pessoa> findPessoaEntities) throws InstantiationException, IllegalAccessException {
        beans.Pessoa[] ret = new beans.Pessoa[findPessoaEntities.size()];
        int count = 0;
        for (beans.Pessoa p : findPessoaEntities) {
            beans.Pessoa nova = new beans.Pessoa();
            nova.setDataNascimento(p.getDataNascimento());
            nova.setDocumentoPessoaList(Copier.copyDocumentoPessoaList(p.getDocumentoPessoaList()));
            nova.setEmail(p.getEmail());
            nova.setEndereco(p.getEndereco());
            nova.setId(p.getId());
            nova.setLocatarioAluguelList((Copier.copyLocatarioAluguelList(p.getLocatarioAluguelList())));
            nova.setLocatarioImovelList((Copier.copyImovelList(p.getLocatarioImovelList())));
            nova.setNome(p.getNome());
            nova.setTelefone(p.getTelefone());
            ret[count] = (nova);
            count++;
        }

        return ret;
    }

    

}
