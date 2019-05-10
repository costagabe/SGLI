<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">

    <head>
        <!-- Required meta tags-->
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="au theme template">
        <meta name="author" content="Hau Nguyen">
        <meta name="keywords" content="au theme template">

        <!-- Title Page-->
        <title>Login</title>

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

    </head>

    <body class="animsition">
        <div class="page-wrapper">
            <div class="page-content--bge5">
                <div class="container">
                    <div class="login-wrap">
                        <div class="login-content">
                            <div class="login-logo">
                                <a href="#">
                                    <img src="${pageContext.request.contextPath}/img/logo.png" alt="CoolAdmin">
                                </a>
                            </div>
                            <div class="login-form">
                                <form action="Login" method="POST">
                                    <div class="form-group">
                                        <label>Usuario</label>
                                        <input class="au-input au-input--full" type="text" id="user" name="user" placeholder="Usuario">
                                    </div>
                                    <div class="form-group">
                                        <label>Senha</label>
                                        <input class="au-input au-input--full" type="password" name="password" id="password" placeholder="Senha">
                                    </div>
                                    <div class="login-checkbox">
                                        <label>
                                            <input type="checkbox" name="remember">Lembrar-me
                                        </label>
                                        <label>
                                            <a href="#">Esqueci minha senha</a>
                                        </label>
                                    </div>
                                    <button type="submit" class="au-btn au-btn--block au-btn--green m-b-20" onclick="logar()" >Entrar</button>
                                    </form>


                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <!-- Modal -->
        <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="tituloModal">Modal title</h5>

                    </div>
                    <div class="modal-body" id="corpoModal">
                        ...
                    </div>
                    <div class="modal-footer" id="footerModal">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>

                    </div>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/vendor/jquery-3.2.1.min.js"></script>

        <!-- Jquery JS-->

        <!-- Bootstrap JS-->
        <script src="${pageContext.request.contextPath}/vendor/bootstrap-4.1/popper.min.js"></script>
        <script src="${pageContext.request.contextPath}/vendor/bootstrap-4.1/bootstrap.min.js"></script>
        <!-- Vendor JS       -->
        <script src="${pageContext.request.contextPath}/vendor/slick/slick.min.js">
        </script>
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
        <script src="${pageContext.request.contextPath}/vendor/select2/select2.min.js">
        </script>

        <!-- Main JS-->
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
       <!--  <script src="${pageContext.request.contextPath}/js/script.js"></script>


       <script type="text/javascript">
            var modal = null;
            if (`${modal}` !== ``) {
                modal = JSON.parse('${modal}');
                $("#tituloModal").html(modal.title);
                $("#corpoModal").html(modal.body);
                $("#footerModal").append(modal.footer);
                $('#modal').modal();


            }

            $(document).on("keypress", "form", function (event) {
                return event.keyCode != 13;
            });
        </script> -->

    </body>

</html>
<!-- end document-->