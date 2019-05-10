package controllers;

import beans.DocumentoImovel;
import beans.Endereco;
import beans.FotoImovel;
import beans.LocatarioImovel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import daos.AluguelJpaController;
import daos.DocumentoImovelJpaController;
import daos.DocumentoJpaController;
import daos.EnderecoJpaController;
import daos.FotoImovelJpaController;
import daos.ImovelJpaController;
import daos.LocatarioImovelJpaController;
import daos.PessoaJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.Modal;

/**
 *
 * @author Gabriel Alves
 */
public class Imovel implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "";
    }

    public String getImoveis(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        return "Json";
    }

    public String excluirDocumento(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.DocumentoImovel di = gson.fromJson(req.getParameter("data"), beans.DocumentoImovel.class);
        new DocumentoImovelJpaController(emf).destroy(di.getId());
        Modal m = new Modal("Sucesso", "Documento excluído com sucesso!");
        req.setAttribute("json", m.getJson());
        return "Json";
    }

    public String excluirFoto(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.FotoImovel fi = gson.fromJson(req.getParameter("data"), beans.FotoImovel.class);
        new FotoImovelJpaController(emf).destroy(fi.getId());
        Modal m = new Modal("Sucesso", "Foto excluído com sucesso!");
        req.setAttribute("json", m.getJson());
        return "Json";
    }


    public String excluir(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.Imovel i = gson.fromJson(req.getParameter("data"), beans.Imovel.class);
        ImovelJpaController imovelDao = new ImovelJpaController(emf);
        FotoImovelJpaController fotoDao = new FotoImovelJpaController(emf);
        DocumentoImovelJpaController documentoDao = new DocumentoImovelJpaController(emf);
        LocatarioImovelJpaController locadorDao = new LocatarioImovelJpaController(emf);
        AluguelJpaController aluguelDao = new AluguelJpaController(emf);
        i = imovelDao.findImovel(i.getId());

        for (FotoImovel fi : i.getFotoImovelList()) {
            fotoDao.destroy(fi.getId());
        }
        for (LocatarioImovel li : i.getLocatarioImovelList()) {
            locadorDao.destroy(li.getId());
        }
        for (DocumentoImovel di : i.getDocumentoImovelList()) {
            documentoDao.destroy(di.getId());
        }
        for (beans.Aluguel a : i.getAluguelList()) {
            aluguelDao.destroy(a.getId());
        }

        new ImovelJpaController(emf).destroy(i.getId());
        Modal m = new Modal("Sucesso", "Imovel excluído com sucesso!");
        req.setAttribute("json", m.getJson());
        return "Json";
    }

    public String showDocs(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.Imovel p = gson.fromJson(req.getParameter("data"), beans.Imovel.class);
        p = new ImovelJpaController(emf).findImovel(p.getId());
        req.setAttribute("docs", p.getDocumentoImovelList());
        req.setAttribute("excluir", "confirmarExcluirDocumentoImovel");
        return "image-viwer.jsp";
    }

    public String editarImovel(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        ImovelJpaController imovelDao = new ImovelJpaController(emf);
        DocumentoImovelJpaController documentoImovelDao = new DocumentoImovelJpaController(emf);
        FotoImovelJpaController fotoDao = new FotoImovelJpaController(emf);
        LocatarioImovelJpaController locadorDao = new LocatarioImovelJpaController(emf);
        EnderecoJpaController enderecoDao = new EnderecoJpaController(emf);
        beans.Imovel i = gson.fromJson(req.getParameter("data"), beans.Imovel.class);
        beans.Imovel onDb = imovelDao.findImovel(i.getId());

        List<beans.DocumentoImovel> documentos = i.getDocumentoImovelList();
        List<beans.FotoImovel> fotos = i.getFotoImovelList();
        List<beans.LocatarioImovel> locadores = i.getLocatarioImovelList();
        boolean flag = false;

        // apagando e/ou editando os documentos/fotos/locadores
        for (DocumentoImovel di : onDb.getDocumentoImovelList()) {
            int pos = documentos.indexOf(di);
            if (pos == -1) {
                documentoImovelDao.destroy(di.getId());
            } else {
                di.setDocumento(documentos.get(pos).getDocumento());
                documentoImovelDao.edit(di);
            }
        }

        for (FotoImovel fi : onDb.getFotoImovelList()) {
            int pos = fotos.indexOf(fi);
            if (pos == -1) {
                fotoDao.destroy(fi.getId());
            }
        }
        for (LocatarioImovel li : onDb.getLocatarioImovelList()) {
            int pos = locadores.indexOf(li);
            if (pos == -1) {
                locadorDao.destroy(li.getId());
            }
        }

        //adicionando documentos/fotos/locatarios
        for (DocumentoImovel di : documentos) {
            if (di.getId() == null) {
                di.setImovel(onDb);
                documentoImovelDao.create(di);
                flag = true;
            }
        }
        for (FotoImovel fi : fotos) {
            if (fi.getId() == null) {
                fi.setImovel(onDb);
                fotoDao.create(fi);
                flag = true;
            }
        }
        for (LocatarioImovel li : locadores) {
            if (li.getId() == null) {
                li.setImovel(onDb);
                locadorDao.create(li);
            }
        }

        //editando endereço
        i.getEndereco().setId(onDb.getEndereco().getId());
        i.getEndereco().setImovelList(onDb.getEndereco().getImovelList());
        i.getEndereco().setPessoaList(onDb.getEndereco().getPessoaList());
        enderecoDao.edit(i.getEndereco());

        Modal m = new Modal("Sucesso", "Edição realizada com sucesso");
        JsonObject obj = new JsonObject();
        obj.add("modal", gson.toJsonTree(m));
        if (flag) {
            obj.addProperty("code", "1");
        } else {
            obj.addProperty("code", "2");
        }

        req.setAttribute("json", obj);
        return "Json";
    }

    public String editar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        DocumentoJpaController docDao = new DocumentoJpaController(emf);
        req.setAttribute("tiposDocumentos", docDao.findDocumentoEntities(2));
        beans.Imovel i = gson.fromJson(req.getParameter("data"), beans.Imovel.class);
        i = new ImovelJpaController(emf).findImovel(i.getId());
        req.setAttribute("imovel", (i));
        return "cadastro-imovel.jsp";
    }

    public String showPhotos(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        Gson gson = new Gson();
        beans.Imovel p = gson.fromJson(req.getParameter("data"), beans.Imovel.class);
        p = new ImovelJpaController(emf).findImovel(p.getId());
        req.setAttribute("docs", p.getFotoImovelList());
        req.setAttribute("excluir", "confirmarExcluirFotoImovel");
        return "image-viwer.jsp";
    }

    public String Listar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        ImovelJpaController imovelDao = new ImovelJpaController(emf);

        List<beans.Imovel> imoveis = new ArrayList<>();

        imovelDao.findImovelEntities(10, 0).forEach(o -> {
            beans.Imovel n = o.clone();
            n.setEndereco(o.getEndereco().clone());
            imoveis.add(n);
        });
        int conta = (imovelDao.getImovelCount() / 10) + 1;
        req.setAttribute("imoveis", new Gson().toJson(imoveis));
        req.setAttribute("paginas", conta);
        return "listar-imoveis.jsp";
    }

    public String listarLocadores(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Imovel i = new Gson().fromJson(req.getParameter("data"), beans.Imovel.class);

        ImovelJpaController imovelDao = new ImovelJpaController(emf);
        i = imovelDao.findImovel(i.getId());
        List<beans.Pessoa> locadores = new ArrayList<>();

        for (LocatarioImovel li : i.getLocatarioImovelList()) {
            locadores.add(li.getLocatario().clone());
        }
        req.setAttribute("json", new Gson().toJson(locadores));
        return "Json";
    }

    public String cadastrar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Imovel i = new Gson().fromJson(req.getParameter("data"), beans.Imovel.class);
        System.out.println(i.getLocatarioImovelList());
        i.setDataCapitacao(java.util.Calendar.getInstance().getTime());
        JsonObject obj = new JsonObject();

        Modal m = null;
        if (validaDocumentos(i.getDocumentoImovelList())) {
            if (!i.getLocatarioImovelList().isEmpty()) {
                PessoaJpaController pessoaDao = new PessoaJpaController(emf);
                if (validaImoveisPessoa(pessoaDao, i.getLocatarioImovelList(), i.getEndereco())) {
                    List<FotoImovel> fotosImovel = i.getFotoImovelList();
                    List<DocumentoImovel> documentosImovel = i.getDocumentoImovelList();
                    List<LocatarioImovel> locadoresImovel = i.getLocatarioImovelList();
                    //setar como array vazio para n dar conflito no create do DAO
                    i.setDocumentoImovelList(new ArrayList<>());
                    i.setFotoImovelList(new ArrayList<>());
                    i.setLocatarioImovelList(new ArrayList<>());

                    EnderecoJpaController enderecoDao = new EnderecoJpaController(emf);
                    ImovelJpaController imovelDao = new ImovelJpaController(emf);
                    FotoImovelJpaController fotoDao = new FotoImovelJpaController(emf);
                    DocumentoImovelJpaController documentoDao = new DocumentoImovelJpaController(emf);
                    LocatarioImovelJpaController locadorImovelDao = new LocatarioImovelJpaController(emf);

                    enderecoDao.create(i.getEndereco());
                    imovelDao.create(i);

                    for (FotoImovel fi : fotosImovel) {
                        fi.setImovel(i);
                        fotoDao.create(fi);
                    }
                    for (DocumentoImovel di : documentosImovel) {
                        di.setImovel(i);
                        documentoDao.create(di);
                    }
                    for (LocatarioImovel li : locadoresImovel) {
                        li.setImovel(i);
                        //li.setLocatario(new beans.Pessoa(li.getId()));
                        // li.setId(0);
                        locadorImovelDao.create(li);
                    }

                    m = new Modal("Sucesso", "Imóvel cadastrado com sucesso!");
                    obj.addProperty("code", "1");
                } else {
                    m = new Modal("Atenção", "<span style='color:yellow'>Pelo menos 1 dos locadores já possuem um imóvel com essas mesmas características. Deseja Continuar?</span>");
                    obj.addProperty("code", "2");
                }

            } else {
                m = new Modal("Erro", "<span style='color:red'>Nenhum locador selecionado para o imóvel!</span>");
                obj.addProperty("code", "2");
            }
        } else {
            m = new Modal("Erro", "Algum dos documentos selecionados está sem o tipo selecionado. <span style='color:red'>O cadastro não foi realizado </span>");
            obj.addProperty("code", "2");
        }
        obj.add("modal", new Gson().toJsonTree(m, Modal.class));
        req.setAttribute("json", obj);
        return "Json";
    }

    public String Cadastro(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        DocumentoJpaController docDao = new DocumentoJpaController(emf);
        req.setAttribute("tiposDocumentos", docDao.findDocumentoEntities(2));
        return "cadastro-imovel.jsp";
    }

    public boolean validaDocumentos(List<DocumentoImovel> l) {
        for (DocumentoImovel di : l) {
            if (di.getDocumento().getId() == 0) {
                return false;
            }
        }
        return true;
    }

    public String pesquisar(HttpServletRequest req, HttpServletResponse res, EntityManagerFactory emf) throws Exception {
        beans.Pessoa p = new Gson().fromJson(req.getParameter("data"), beans.Pessoa.class);
        LocatarioImovelJpaController liDao = new LocatarioImovelJpaController(emf);
        List<LocatarioImovel> imoveis = liDao.findByPessoa(p.getId());
        JsonArray array = new JsonArray();
        for (LocatarioImovel li : imoveis) {
            JsonObject obj = new JsonObject();
            obj.addProperty("id", li.getImovel().getId());
            beans.Endereco e = new Endereco();
            e.setBairro(li.getImovel().getEndereco().getBairro());
            e.setCep(li.getImovel().getEndereco().getCep());
            e.setNumero(li.getImovel().getEndereco().getNumero());
            e.setRua(li.getImovel().getEndereco().getRua());
            e.setCidade(li.getImovel().getEndereco().getCidade());
            e.setId(li.getImovel().getEndereco().getId());
            e.setComplemento(li.getImovel().getEndereco().getComplemento());
            obj.add("endereco", new Gson().toJsonTree(e, beans.Endereco.class));
            array.add(obj);
        }
        req.setAttribute("json", array);
        return "Json";
    }

    private boolean validaImoveisPessoa(PessoaJpaController pessoaDao, List<LocatarioImovel> locatarioImovelList, beans.Endereco c) {
        for (LocatarioImovel li : locatarioImovelList) {
            beans.Pessoa p = pessoaDao.findPessoa(li.getLocatario().getId());
            for (LocatarioImovel li2 : p.getLocatarioImovelList()) {
                Endereco e = li2.getImovel().getEndereco();
                if (e.getNumero().equals(c.getNumero()) && e.getBairro().equals(c.getBairro())) {
                    return false;
                }
            }
        }
        return true;
    }
}
