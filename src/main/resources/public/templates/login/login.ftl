<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Login">

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
                          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

                      </div>
                  </div>
              </form>

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