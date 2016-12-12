<#macro masterTemplate title="">
    <html>
    <head>
        <title>${msg.get("COMMON_TITLE")}</title>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />


        <link rel="stylesheet" href="/style/css/footable.bootstrap.css">
        <link rel="stylesheet" href="/style/css/bootstrap.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>


        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.bundle.js"></script>

        <script src="/style/js/bootstrap.js"></script>

        <script src="/style/js/footable.js"></script>



        <link rel="stylesheet" href="/style/css/style.css">

        <link rel="icon" href="/img/favicon.png">
        <meta name="viewport" content="width=device-width, initial-scale=1">

    </head>
    <body>
    <header>
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>

                    <a class="navbar-brand" href="${WebPath.getINDEX()}">WEB62</a>
                </div>

                <#if currentUser??>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">

                        <li <#if currentPage?? && currentPage == "index"> class="active"</#if>><a href="${WebPath.getINDEX()}"><span class="glyphicon glyphicon-home"></span>  ${msg.get("COMMON_NAV_INDEX")}</a></li>
                        <li <#if currentPage?? && currentPage == "survey"> class="active"</#if>><a href="${WebPath.getSURVEY()}" ><span class="glyphicon glyphicon-play"></span>  ${msg.get("COMMON_NAV_SURVEY")}</a></li>

                        <li class="dropdown nav navbar-right  <#if currentPage?? && currentPage == "usercontrol" || currentPage == "surveycontrol"> active</#if>">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-cog"></span>  Administrationsbereich<span class="caret"></span></a>

                        <ul class="dropdown-menu">
                            <li <#if currentPage?? && currentPage == "usercontrol"> class="active"</#if> <#if currentRole?? && currentRole == 2> class="not-activeAnchor" </#if> ><a href="${WebPath.getUSERCONTROL()}"><span class="glyphicon glyphicon-user"></span>  ${msg.get("COMMON_NAV_USERCONTROL")}</a></li>

                            <li role="separator" class="divider"></li>

                            <li class="dropdown-header"><span class="glyphicon glyphicon-user"></span> Account</li>

                            <#if currentUser?has_content>
                            <li class="logoutLi">
                                <form method="post" action="${WebPath.getLOGOUT()}" name="logout" id="logout">
                                    <a id="logout" href="#" class="navLogout" onclick="document.getElementById('logout').submit();"> <span class="glyphicon glyphicon-log-out"></span>  ${msg.get("COMMON_NAV_LOGOUT")}  (${currentUser})</a>
                                </form>
                            </li>

                            <#else>
                                <li><a href="${WebPath.getLOGIN()}">${msg.get("COMMON_NAV_LOGIN")}</a></li>
                            </#if>
                        </ul>

                        </li>
                    </ul>

                </div><!--/.nav-collapse -->
                </#if>

            </div>
        </nav>

    </header>

    <main>
        <div id="container" class="container" role="main">
            <div class="row">
                <div class="col-md-6-col-md-push-4">
                    <#nested />
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
            <div>
                ${msg.get("COMMON_FOOTER_TEXT")}
            </div>

    </footer>

       </body>
    </html>
</#macro>