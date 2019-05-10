<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="pt-br">
    <%if (request.getSession().getAttribute("logado") == null) {
            response.sendRedirect("Login");
        }%>
    <head>
        <!-- Required meta tags-->
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="au theme template">
        <meta name="author" content="Gabriel Alves">
        <meta name="keywords" content="au theme template">

        <!-- Title Page-->
        <title>Dashboard</title>

        <!-- Fontfaces CSS-->
        <link href="${pageContext.request.contextPath}/css/font-face.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/font-awesome-4.7/css/font-awesome.min.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/font-awesome-5/css/fontawesome-all.min.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/mdi-font/css/material-design-iconic-font.min.css" rel="stylesheet" media="all">

        <!-- Bootstrap CSS-->
        <link href="${pageContext.request.contextPath}/vendor/bootstrap-4.1/bootstrap.min.css" rel="stylesheet" media="all">

        <!-- Vendor CSS-->
        <link href="${pageContext.request.contextPath}/vendor/animsition/animsition.min.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/bootstrap-progressbar/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/wow/animate.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/css-hamburgers/hamburgers.min.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/slick/slick.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/select2/select2.min.css" rel="stylesheet" media="all">
        <link href="${pageContext.request.contextPath}/vendor/perfect-scrollbar/perfect-scrollbar.css" rel="stylesheet" media="all">

        <!-- Main CSS-->
        <link href="${pageContext.request.contextPath}/css/theme.css" rel="stylesheet" media="all">

        <style type="text/css">/* Chart.js */
            @media print {
                body * {
                    visibility: hidden;
                }
                #printable, #printable * {
                    visibility: visible;
                }
                #printable {
                    position: fixed;
                    left: 0;
                    top: 0;
                }
            }
            @-webkit-keyframes chartjs-render-animation{from{opacity:0.99}to{opacity:1}}@keyframes chartjs-render-animation{from{opacity:0.99}to{opacity:1}}.chartjs-render-monitor{-webkit-animation:chartjs-render-animation 0.001s;animation:chartjs-render-animation 0.001s;}</style><style type="text/css">/* Chart.js */
            @-webkit-keyframes chartjs-render-animation{from{opacity:0.99}to{opacity:1}}@keyframes chartjs-render-animation{from{opacity:0.99}to{opacity:1}}.chartjs-render-monitor{-webkit-animation:chartjs-render-animation 0.001s;animation:chartjs-render-animation 0.001s;}</style><style type="text/css">/* Chart.js */
            @-webkit-keyframes chartjs-render-animation{from{opacity:0.99}to{opacity:1}}@keyframes chartjs-render-animation{from{opacity:0.99}to{opacity:1}}.chartjs-render-monitor{-webkit-animation:chartjs-render-animation 0.001s;animation:chartjs-render-animation 0.001s;}</style></head>

    <body class="animsition" style="animation-duration: 900ms; opacity: 1;">
        <div class="page-wrapper">


            <!-- MENU SIDEBAR-->
            <aside class="menu-sidebar d-none d-lg-block">
                <div class="logo">
                    <a href="#">
                        <img src="${pageContext.request.contextPath}/img/logo.png" alt="Cool Admin">
                    </a>
                </div>
                <div class="menu-sidebar__content js-scrollbar1 ps ps--active-y">
                    <nav class="navbar-sidebar">
                        <ul class="list-unstyled navbar__list">

                            <li>
                                <a href="#" class="js-arrow">
                                    <i class="fas fa-user"></i>Pessoa</a>
                                <ul class="list-unstyled navbar__sub-list js-sub-list" style="display: none;">
                                    <li> 
                                        <a href="/Pessoa/Cadastro" target="_blank"  onclick="openPage('Pessoa', 'Cadastro');return false;">
                                            <i class="fas fa-plus"></i>
                                            Cadastrar</a>
                                    </li>
                                    <li>
                                        <a href="#" target="_blank"  onclick="openPage('Pessoa', 'Listar');return false;">
                                            <i class="fa fa-list-ul"></i>
                                            Listar</a>
                                    </li>
                                    <li>
                                </ul>
                            </li>
                            <li>
                                <a href="#" class="js-arrow">
                                    <i class="fas fa-home"></i>Imóvel</a>
                                <ul class="list-unstyled navbar__sub-list js-sub-list" style="display: none;">
                                    <li>
                                        <a href="/Imovel/Cadasatro" target="_blank"  onclick="openPage('Imovel', 'Cadastro');return false;">
                                            <i class="fas fa-plus"></i>
                                            Cadastrar</a>
                                    </li>
                                    <!--   <li>
                                       <a href="#" target="_blank"  onclick="openPage('Imovel', 'Listar');return false;">
                                             <i class="fa fa-list-ul"></i>
                                             Listar</a>
                                     </li>-->

                                    <li>
                                </ul>
                            </li>
                            <li>
                                <a href="#" class="js-arrow">
                                    <i class="fas fa-usd"></i>Aluguel</a>
                                <ul class="list-unstyled navbar__sub-list js-sub-list" style="display: none;">
                                    <li>
                                        <a href="#" target="_blank"  onclick="openPage('Aluguel', 'cadastro');return false;">
                                            <i class="fas fa-plus"></i>
                                            Cadastrar</a>
                                    </li>
                                    <!--<li>
                                        <a href="#" target="_blank"  onclick="openPage('Aluguel', 'Listar');return false;">
                                            <i class="fa fa-list-ul"></i>
                                            Listar</a>
                                    </li>-->

                                    <li>
                                </ul>
                            </li>

                            <li>
                                <a href="#" class="js-arrow">
                                    <i class="fa fa-user"></i>Usuario</a>
                                <ul class="list-unstyled navbar__sub-list js-sub-list" style="display: none;">
                                    <li>
                                        <a href="novo-aluguel.html" target="_blank"   onclick="openPage('Usuario', 'Cadastro');return false;">
                                            <i class="fas fa-plus"></i>
                                            Novo</a>
                                    </li>
                                    <!--   <li>
                                           <a href="#">
                                               <i class="fa fa-list-ul"></i>
                                               Gerenciar</a>
                                       </li>-->
                                    <li>
                                </ul>
                            </li>
                            <li>
                                <a href="#" class="js-arrow">
                                    <i class="fas fa-copy"></i>Documento</a>
                                <ul class="list-unstyled navbar__sub-list js-sub-list" style="display: none;">
                                    <li>
                                        <a href="#" target="_blank"   onclick="openPage('Documento', 'Cadastro');return false;">
                                            <i class="fas fa-plus"></i>
                                            Adicionar Novo Tipo</a>
                                    </li>
                                    <li>
                                        <!--   <a href="#">
                                               <i class="fa fa-list-ul"></i>
                                               Listar</a>
                                       </li>-->
                                    <li>
                                </ul>
                            </li>

                            <li class="has-sub">
                                <a class="js-arrow" href="#">
                                    <i class="fas fa-group"></i>Grupo de permissões</a>
                                <ul class="list-unstyled navbar__sub-list js-sub-list" style="display: none;">
                                    <li>
                                        <a href="#" target="_blank"   onclick="openPage('Grupo', 'Cadastro');return false;">
                                            <i class="fas fa-plus"></i>
                                            Cadastrar</a>
                                    </li>
                                </ul>
                            <li class="has-sub">
                                <a class="js-arrow" href="#">
                                    <i class="fas fa-list-ul"></i>Relatórios</a>
                                <ul class="list-unstyled navbar__sub-list js-sub-list" style="display: none;">
                                    <li>
                                        <a href="#" target="_blank"   onclick="openPage('Relatorios', 'rendimentosLocador');return false;">
                                            <i class="fas fa-plus"></i>
                                            Informe de rendimentos - Locador</a>
                                    </li>
                                    <li>
                                        <a href="#" target="_blank"   onclick="openPage('Relatorios', 'rendimentosLocatario');return false;">
                                            <i class="fas fa-plus"></i>
                                            Informe de rendimentos - Locatário</a>
                                    </li>
                                    <li>
                                        <a href="#" target="_blank"   onclick="openPage('Relatorios', 'z');return false;">
                                            <i class="fas fa-plus"></i>
                                            Observações de aluguel</a>
                                    </li>
                                    <!--   <li>
                                           <a href="#">
                                               <i class="fa fa-list-ul"></i>
                                               Listar</a>
                                       </li>-->

                                </ul>
                            </li> 

                        </ul>
                    </nav>
                    <div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 0px;"></div></div><div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; height: 418px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 364px;"></div></div><div class="ps__rail-x" style="left: 0px; bottom: -61.6px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 61.6px; height: 418px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 53px; height: 364px;"></div></div></div>
            </aside>
            <!-- END MENU SIDEBAR-->

            <!-- PAGE CONTAINER-->
            <div class="page-container">
                <!-- HEADER DESKTOP-->
                <header class="header-desktop">
                    <div class="section__content section__content--p30">
                        <div class="container-fluid">
                            <div class="header-wrap">
                                <form class="form-header" action="#" method="POST" onsubmit="pesquisarItem();return false;">
                                    <input class="au-input au-input--xl" type="text" name="search" id="pesquisaTxt" placeholder="Pesquisar">
                                    <button class="au-btn--submit" type="submit">
                                        <i class="zmdi zmdi-search"></i>
                                    </button>
                                </form>
                                <div class="header-button">

                                    <div class="account-wrap">
                                        <div class="account-item clearfix js-item-menu">
                                            <div class="image">
                                                <img src="uploads/${logado.foto}" alt="${logado.nome}">
                                            </div>
                                            <div class="content">
                                                <a class="js-acc-btn" href="#">${logado.nome}</a>
                                            </div>
                                            <div class="account-dropdown js-dropdown">
                                                <div class="info clearfix">
                                                    <div class="image">
                                                        <a href="#">
                                                            <img src="uploads/${logado.foto}" alt="${logado.nome}">
                                                        </a>
                                                    </div>
                                                    <div class="content">
                                                        <h5 class="name">
                                                            <a href="#">${logado.nome}</a>
                                                        </h5>

                                                    </div>
                                                </div>
                                                <div class="account-dropdown__body">
                                                    <div class="account-dropdown__item">
                                                        <a href="#" target="_blank"  onclick="openPage('Usuario', 'gerenciarConta');return false;">
                                                            <i class="zmdi zmdi-account"></i>Minha conta</a>
                                                    </div>

                                                </div>
                                                <div class="account-dropdown__footer">
                                                    <a href="Logout">
                                                        <i class="zmdi zmdi-power"></i>Logout</a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </header>
                <!-- HEADER DESKTOP-->

                <!-- MAIN CONTENT-->
                <div class="main-content">
                    <div class="section__content section__content--p30" id="printable">
                        <div class="container-fluid" id="conteudo" >

                        </div>
                    </div>
                </div>
                <!-- END MAIN CONTENT-->
                <!-- END PAGE CONTAINER-->
            </div>

        </div>
        <!-- Large modal -->
        <div class="modal fade" id="modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">

                <div class="modal-content" id="modal-lg-body">
                    ...
                </div>
            </div>
        </div>
        <!-- Modal -->
        <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modal-title" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modal-title">Modal title</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body" id="modal-body">
                        ...
                    </div>
                    <div class="modal-footer" id="modal-footer">
                        <button type="button" class="btn btn-secondary"  id="secondary-modal-btn">Close</button>
                        <button type="button" class="btn btn-primary" id="primary-modal-btn">Save changes</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Jquery JS-->
        <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
        <!-- Bootstrap JS-->
        <script src="${pageContext.request.contextPath}/vendor/bootstrap-4.1/popper.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/bootstrap-4.1/bootstrap.min.js"></script>
        <!-- Vendor JS       -->


        <script src="${pageContext.request.contextPath}/vendor/slick/slick.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/wow/wow.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/animsition/animsition.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/bootstrap-progressbar/bootstrap-progressbar.min.js">
        </script>
        <script src="${pageContext.request.contextPath}/vendor/counter-up/jquery.waypoints.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/counter-up/jquery.counterup.min.js">
        </script>
        <script src="${pageContext.request.contextPath}/vendor/circle-progress/circle-progress.min.js"></script> 
        <script src="${pageContext.request.contextPath}/vendor/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/chartjs/Chart.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/select2/select2.min.js"></script>


        <!-- Main JS-->
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
        <script src="${pageContext.request.contextPath}/js/script.js"></script> 



        <!-- end document-->
    </body></html>