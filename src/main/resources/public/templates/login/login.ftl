<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Login">


<div class="col-md-8">

    <h1>${msg.get("INDEX_HEADING")}</h1>
    <p>
    Eine Online-Umfrage stellt im Allgemeinen einen digitalen Fragebogen dar, der im Webbrowser beantwortet wird und prinzipiell zwei Strukturtypen enthält,
    wozu offene und geschlossene Fragen zählen. Bei offenen Fragen haben die Befragten die Möglichkeit sich frei zu äußern,
    sodass der Befragte beliebige Antworten mit eigenen Worten abgeben kann. Geschlossene Fragen stellen das Gegenteil dar und geben sämtliche Antwortmöglichkeiten vor,
    wodurch folglich keine freie Antwortwahl für den Befragten existiert.
    </p>
    <p>
       Bei der Konstruktion eines Fragenbogens gilt es darauf zu achten,
       dass sowohl die inhaltliche als auch die optische Gestaltung des Fragenbogens qualitativ hochwertig ist,
       damit der Fragebogen als Instrument zur Datenerhebung ernsthaft geeignet ist.
       Insbesondere die Reihenfolge der Fragen muss zwingend bei der Erstellung eines Fragebogens beachtet und durchdacht werden.
       Dabei ist es sinnvoll mit einfachen Fragestellungen die Umfrage zu beginnen und sukzessive zu komplexeren Zusammenhängen hinüberzuleiten.
       Dies ist darauf zurückzuführen, um den Befragten nicht zu schnell durch zu schwierige Fragen zu überfordern und zugleich zu demotivieren.
    </p>
    <div>
      <blockquote>
        <p>Genau dies ist mittels WebSurvey62 effizient möglich! Überzeugen Sie sich selbst und melden Sie sich an.</p>
        <small><cite title="">Ihr WebSurvey62-Team</cite></small>
      </blockquote>
    </div>
    <p class="indexAdvertisement"></p>
    <img src="/img/surveyCreation.png" class="img-thumbnail"></img>

</div>

<div class="col-md-4">

  <h1>${msg.get("LOGIN_HEADING")}</h1>


  <form id="loginForm" method="post">
      <#if authenticationFailed?? && authenticationFailed?c == "true">
          <p class="btn-lg btn-danger bad notification">${msg.get("LOGIN_AUTH_FAILED")}</p>
      <#elseif authenticationSucceeded?? && authenticationSucceeded?c =="true">
          <p class="btn-lg btn-success good notification">${msg.get("LOGIN_AUTH_SUCCEEDED")} ${currentUser}</p>
      <#elseif loggedOut?? && loggedOut?c == "true">
          <p class="btn-lg btn-warning notification">${msg.get("LOGIN_LOGGED_OUT")}</p>
      </#if>

      <p>${msg.get("LOGIN_INSTRUCTIONS")}</p>



      <div class="form-group">
            <label for="username">${msg.get("LOGIN_LABEL_USERNAME")}</label>
            <div class="input-group">
                <span class="input-group-addon">
                <i class="glyphicon glyphicon-user"></i>
                </span>
                <input type="text" name="username"  class="form-control" placeholder="${msg.get("LOGIN_LABEL_USERNAME")}" value="" required>
            </div>
      </div>

      <div class="form-group">
          <label for="password">${msg.get("LOGIN_LABEL_PASSWORD")}</label>
          <div class="input-group">
                <span class="input-group-addon">
                <i class="glyphicon glyphicon-lock"></i>
                </span>
                <input type="password" name="password" class="form-control" placeholder="${msg.get("LOGIN_LABEL_PASSWORD")}" value="" required>
          </div>
          <#if loginRedirect??>
              <input type="hidden" name="loginRedirect" value="${loginRedirect}">
          </#if>
      </div>
      <div class="form-group">
        <button type="submit" class="btn btn-primary btn-md">
            <span class="glyphicon glyphicon-log-in"></span> ${msg.get("LOGIN_BUTTON_LOGIN")}
        </button>
      </div>
        <div class="form-group">
              <a href="${WebPath.getNEWUSER()}" data-toggle="modal" data-target="#loginNewUser">${msg.get("LOGIN_NEW_USER")}</a>
        </div>
  </form>


    <!-- Modal -->
    <div id="loginNewUser" class="modal fade" role="dialog">
      <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h2 class="modal-title">${msg.get("LOGIN_NEW_USER")}</h2>
          </div>
          <div class="modal-body">

              <form id="userForm" method="post">

                  <div class="modalResult">
                  </div>

                  <div class="form-group">
                      <label for="username">${msg.get("USER_USERNAME")}</label>
                      <input type="text" id="username" name="username"  class="form-control" placeholder="${msg.get("USER_USERNAME")}" value="" required>
                  </div>

                  <div class="form-group">
                      <label for="password">${msg.get("USER_PASSWORD")}</label>
                      <input type="password" id="password" name="password" class="form-control" placeholder="${msg.get("USER_PASSWORD")}" value="" required>
                  </div>

                  <div class="form-group">
                      <label for="password">${msg.get("USER_PASSWORD_CONFIRM")}</label>
                      <input type="password" id="passwordConfirmed" name="passwordConfirmed" class="form-control" placeholder="${msg.get("USER_PASSWORD_CONFIRM")}" value="" required>
                  </div>

                  <div class="modal-footer">
                      <div class="form-group">

                          <input type="button" class="btn btn-primary" id="buttonSave" name="buttonSave" value="${msg.get("USER_CREATE")}">
                          <button type="button" class="btn btn-default" data-dismiss="modal">${msg.get("COMMON_CANCEL")}</button>

                      </div>
                  </div>
              </form>

          </div>
        </div>

      </div>
    </div>
</div>
<script>
$(document).ready(function(){
    $("#buttonSave").click(function(){

        var request = $.ajax({
          url: "/createNewUserLogin/",
          type: "POST",
          data: {username : $('#username').val(), password : $('#password').val(), passwordConfirmed: $('#passwordConfirmed').val()},
          dataType: "html"
        });

        request.done(function(msg) {

            if("success" == msg){
                $(".modalResult").html('<p class="btn-lg btn-success good notification">Benutzeraccount erfolgreich erstellt. Sie können sich jetzt anmelden.</p>');
            }

            if("pwFailed" == msg){
                $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Passwörter ungleich oder nicht gültig.</p>');
            }

            if("usernameAlreadyExists" == msg){
                $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Benutzername existiert bereits.</p>');
            }

        });

        request.fail(function(jqXHR, textStatus) {
          alert( "Request failed: " + textStatus );
        });

    });
});
</script>


</@layout.masterTemplate>