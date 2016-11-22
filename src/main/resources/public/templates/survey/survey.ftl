<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Index">
	
	<div class="col-md-12">
		
		<h1>${msg.get("SURVEY_HEADING")}</h1>

		<div class="panel panel-default">
			
			<form action="/updateSurvey/${currentSurvey.getSurveyId()}/" method="post" name="updateSurveyForm">
			<div class="panel-body">
		  
				<div class="">
					 
					<div class="col-md-4">
						<label for="link">${msg.get("SURVEY_LINK")}</label>
						<a href="http://localhost:4567/surveycreation/${currentSurvey.getSurveyId()}/" id="link" name="link" target="_blank">http://localhost:4567/surveycreation/${currentSurvey.getSurveyId()}/</a>
					</div>
		
				
					<div class="col-md-3">
						<input type="checkbox" name="isPublished" id="isPublished" class=""  value="true" <#if currentSurvey.isPublished()?c =="true">checked</#if>/>
						<label for="isPublished">${msg.get("SURVEY_STATUS")}</label>
						 <#if currentSurvey.isPublished()?c =="true">
						 	<span class="label label-success">Veröffentlicht</span>
						 <#else>
							<span class="label label-warning">Entwurf</span>
						 </#if>

					
					</div>			
					
					<div class="col-md-2">

						<input type="checkbox" name="ipAddress" id="ipAddress" class=""  value="true"  <#if currentSurvey.isIpAddress()?c =="true">checked</#if>/>
						<label for="ipAddress">${msg.get("SURVEY_IPADDRESS")}</label>
					</div>
					<div class="col-md-2">
						<input type="checkbox" name="sessionId" id="sessionId" class=""  value="true"  <#if currentSurvey.isSessionId()?c =="true">checked</#if>/>
						<label for="sessionId">${msg.get("SURVEY_SESSIONID")}</label>
					
					</div>
				</div>
				
				</br>
								
				
			</div>

	

			<div class="panel-body">
				<div class="col-md-12">				
					<label for="surveyTitle">${msg.get("SURVEY_TITLE_QUESTION")}</label>
					<div class="input-group">
						<input type="text" name="surveyTitle" id="surveyTitle" class="form-control"  placeholder="${msg.get('SURVEY_TITLE')}" value="${currentSurvey.getSurveyTitle()}" />
						<span class="input-group-btn">
							<button class="btn btn-primary" type="submit">Speichern</button>
						</span>
					</div>				
				</div>
			</div>
			
			</form>
´		</div>
	</div>
	
	
	<div class="col-md-3">
		<h2>${msg.get("SURVEY_HEADING_NEWELEMENTS")}</h2>
		<div class="panel panel-default">
			<div class="panel-body">
		  
				<ul class="nav nav-pills nav-stacked">
				  <li role="presentation"><a href="#" data-toggle="modal" data-target="#textElementModal">${msg.get("SURVEY_TEXT")}</a></li>
				  <li role="presentation"><a href="#" data-toggle="modal" data-target="#personalDataElementModal">${msg.get("SURVEY_PERSONALDATA")}</a></li>
				  <li role="presentation"><a href="#" data-toggle="modal" data-target="#closedQuestionElementModal">${msg.get("SURVEY_CLOSEDQUESTION")}</a></li>
				  <li role="presentation"><a href="#" data-toggle="modal" data-target="#openQuestionElementModal">${msg.get("SURVEY_OPENQUESTION")}</a></li>
				  <li role="presentation"><a href="#" data-toggle="modal" data-target="#scoreTableElementModal">${msg.get("SURVEY_SCORETABLE")}</a></li>
				</ul>
								
				
			</div>
		</div>
	</div>
	
	
	<div class="col-md-9">
	
		<h2>${msg.get("SURVEY_HEADING_OVERVIEW")}</h2>

			
		<div class="panel panel-default">
			<div class="panel-body">
		  
				<div class="table-responsive">
				  <table class="table table-striped">
					<thead>
					  <tr>
						<th>ID</th>
						<th>Titel</th>
						<th>Element</th>
						<th>bearbeiten?</th>
						<th>löschen?</th>
					  </tr>
					</thead>
					<tbody>
					<#if surveyElements?has_content>
						<#list surveyElements>

							<#items as element>
							  <tr>
								<td>${element.getElementId()}</td>
								<td>${element.getElementTitle()}</td>
								<td>${element.getElementType()}</td>
								<td><a href="/survey/${element.getSurveyId()}/element/${element.getElementId()}/"><span class="glyphicon glyphicon-pencil"></span></a></td>
								<td><a href="/survey/${element.getSurveyId()}/element/${element.getElementId()}/"><span class="glyphicon glyphicon-remove"></span></a></td>
							  </tr>
							</#items>

						<#else>

						  <p>no </p>
						  
						</#list>
					</#if>
					</tbody>
				  </table>
				</div>
			</div>
		</div>
	</div>
	


    <!-- Modal Text-Element-->
    <div id="textElementModal" class="modal fade" role="dialog">
      <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h2 class="modal-title">${msg.get("SURVEY_TEXT")}</h2>
          </div>
          <div class="modal-body">

              <form id="textElementForm" method="post">

                  <div class="modalResult">
                  </div>
					
					<div class="form-group">
						<label for="textElement">${msg.get("SURVEY_TEXTELEMENT")}</label>
						<textarea class="form-control" name="textElement" id="textElement" rows="4" cols="50"></textarea>
					</div>
					
                  <div class="form-group">
                      <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file" class="form-control file">
                  </div>


                  <div class="modal-footer">
                      <div class="form-group">

                          <input type="button" class="btn btn-primary" id="buttonSave" name="buttonSave" value="${msg.get("SURVEY_ADD")}">
                          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

                      </div>
                  </div>
              </form>

          </div>
        </div>

      </div>
    </div>
	
	 <!-- Modal PersonalData-Element-->
    <div id="personalDataElementModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_PERSONALDATA")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="personalDataElementForm" method="post">

				  <div class="modalResult">
				  </div>
					
					<div class="">					
						<input type="checkbox" name="firstname" id="firstname" class=""  value="true" />
						<label for="firstname">${msg.get("SURVEY_FIRSTNAME")}</label>
					</div>
					<div class="">				
						<input type="checkbox" name="lastname" id="lastname" class=""  value="true" />
						<label for="lastname">${msg.get("SURVEY_LASTNAME")}</label>
					</div>
					<div class="">				
						<input type="checkbox" name="age" id="age" class=""  value="true" />
						<label for="age">${msg.get("SURVEY_AGE")}</label>
					</div>
					<div class="">				
						<input type="checkbox" name="gender" id="gender" class=""  value="true" />
						<label for="gender">${msg.get("SURVEY_GENDER")}</label>
					</div>
					<div class="">				
						<input type="checkbox" name="location" id="location" class=""  value="true" />
						<label for="location">${msg.get("SURVEY_LOCATION")}</label>
					</div>
					
					<div class="modal-footer">
					  <div class="form-group">

						  <input type="button" class="btn btn-primary" id="buttonSave" name="buttonSave" value="${msg.get("SURVEY_ADD")}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

					  </div>
				  </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>
	
	
	
		
	 <!-- Modal Closed-Question-Element-->
    <div id="closedQuestionElementModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_CLOSEDQUESTION")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="closedQuestionElementForm" method="post">

				  <div class="modalResult">
				  </div>
					
					<div class="form-group col-md-12">
						<label for="textElement">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="textElement" id="textElement" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="question">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="question" name="question"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" required>
					  </div>
					  
					<div class="form-group col-md-6">
					  <label for="answer1">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer1" name="answer1"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="answer2">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer2" name="answer2"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="answer3">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer3" name="answer3"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="answer4">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer4" name="answer4"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="answer5">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer5" name="answer5"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="answer6">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer6" name="answer6"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" required>
					</div>
					
					<div class="col-md-6">				
						<input type="checkbox" name="optionalTextfield" id="optionalTextfield" class=""  value="true" />
						<label for="optionalTextfield">${msg.get("SURVEY_TEXTFIELD_OPTIONAL")}</label>
					</div>
					
					<div class="col-md-6">				
						<input type="checkbox" name="multipleSelection" id="multipleSelection" class=""  value="true" />
						<label for="multipleSelection">${msg.get("SURVEY_MULTIPLE_SELECTION")}</label>
					</div>
					
					<div class="form-group col-md-12">
					  <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file" class="form-control file">
					</div>
					
					<div class="modal-footer">
					  <div class="form-group">
						  <input type="button" class="btn btn-primary" id="buttonSave" name="buttonSave" value="${msg.get('SURVEY_ADD')}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					  </div>
				  </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>
	
		
	 <!-- Modal Closed-Question-Element-->
    <div id="openQuestionElementModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_OPENQUESTION")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="openQuestionElementForm" method="post">

				  <div class="modalResult">
				  </div>
					
					<div class="form-group col-md-12">
						<label for="textElement">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="textElement" id="textElement" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="question">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="question" name="question"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" required>
					  </div>

					<div class="form-group col-md-12">
					  <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file" class="form-control file">
					</div>
					
					<div class="modal-footer">
					  <div class="form-group">
						  <input type="button" class="btn btn-primary" id="buttonSave" name="buttonSave" value="${msg.get('SURVEY_ADD')}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					  </div>
				  </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>

	
	
		
	 <!-- Modal Closed-Question-Element-->
    <div id="scoreTableElementModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_SCORETABLE")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="scoreTableElementForm" method="post">

				  <div class="modalResult">
				  </div>
					
					<div class="form-group col-md-12">
						<label for="textElement">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="textElement" id="textElement" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="question">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="question" name="question"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" required>
					  </div>
					  
					<div class="form-group col-md-6">
					  <label for="criterion1">${msg.get("SURVEY_CRITERION")}</label>
					  <input type="text" id="criterion1" name="criterion1"  class="form-control" placeholder="${msg.get('SURVEY_CRITERION')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="criterion2">${msg.get("SURVEY_CRITERION")}</label>
					  <input type="text" id="criterion2" name="criterion2"  class="form-control" placeholder="${msg.get('SURVEY_CRITERION')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="criterion3">${msg.get("SURVEY_CRITERION")}</label>
					  <input type="text" id="criterion3" name="criterion3"  class="form-control" placeholder="${msg.get('SURVEY_CRITERION_OPTIONAL')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="criterion4">${msg.get("SURVEY_CRITERION")}</label>
					  <input type="text" id="criterion4" name="criterion4"  class="form-control" placeholder="${msg.get('SURVEY_CRITERION_OPTIONAL')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="criterion5">${msg.get("SURVEY_CRITERION")}</label>
					  <input type="text" id="criterion5" name="criterion5"  class="form-control" placeholder="${msg.get('SURVEY_CRITERION_OPTIONAL')}" value="" required>
					</div>
					<div class="form-group col-md-6">
					  <label for="criterion6">${msg.get("SURVEY_CRITERION")}</label>
					  <input type="text" id="criterion6" name="criterion6"  class="form-control" placeholder="${msg.get('SURVEY_CRITERION_OPTIONAL')}" value="" required>
					</div>
					
					
					<div class="form-group col-md-12">
					  <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file" class="form-control file">
					</div>
					
					<div class="form-group col-md-12">
						<label for="criterion3">Bewertungsskala:</label>
						<p>gar nicht zufrieden, unzufrieden, eher unzufrieden, eher zufrieden, zufrieden, nicht beurteilbar</p>
					</div>
					
					<div class="modal-footer">			  					
					  <div class="form-group">
						  <input type="button" class="btn btn-primary" id="buttonSave" name="buttonSave" value="${msg.get('SURVEY_ADD')}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					  </div>
				  </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>
	
</@layout.masterTemplate>
