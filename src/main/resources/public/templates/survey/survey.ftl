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
								<td>
								<#if element.getElementTitle()??>
								${element.getElementTitle()}
								</#if>
								</td>
								<td>
									<#if element.getElementType() == 1>Text</#if>
									<#if element.getElementType() == 2>Persönliche Daten</#if>
									<#if element.getElementType() == 3>Geschlossene Frage</#if>
									<#if element.getElementType() == 4>Offene Frage</#if>
									<#if element.getElementType() == 5>Bewertungstabelle</#if>
								</td>
								<td>

									<a class="editElement" href="/survey/${element.getSurveyId()}/element/${element.getElementId()}/elementtype/
										<#if element.getElementType() == 1>1/</#if>
										<#if element.getElementType() == 2>2/</#if>
										<#if element.getElementType() == 3>3/</#if>
										<#if element.getElementType() == 4>4/</#if>
										<#if element.getElementType() == 5>5/</#if>"
										><span class="glyphicon glyphicon-pencil"></span>
									</a>
									
								</td>
								<td><a href="#" data-toggle="modal" data-target="#deleteSurvey${element.getSurveyId()}Element${element.getElementId()}"><span class="glyphicon glyphicon-remove"></span></a></td>
								<td>
								<!-- Modal Confirm-->
								<div id="deleteSurvey${element.getSurveyId()}Element${element.getElementId()}" class="modal fade" role="dialog">
									
									<div class="modal-dialog">

										<!-- Modal content-->
										<div class="modal-content">
									
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal">&times;</button>
												<h2 class="modal-title">${msg.get("SURVEY_HEADING")}</h2>
											</div>
									  									  
											<div class="modal-body">

												<p>Wollen Sie diese Umfrage wirklich löschen?</p>

											</div>
											
											<div class="modal-footer">
											<div class="form-group">

											  <a href="/delete/survey/${element.getSurveyId()}/element/${element.getElementId()}/" class="btn btn-primary">Löschen</a>
											  <button type="button" class="btn btn-default" data-dismiss="modal">Abbrechen</button>

											</div>
											</div>

										</div>
									</div>
								</div>
								
								</td>
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


              <form id="textElementForm" name="textElementForm" method="post" enctype="multipart/form-data">
					<div class="modalResult">
					</div>

                   <input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					
					<div class="form-group">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" required>
					</div>
					
					<div class="form-group">
						<label for="textElement">${msg.get("SURVEY_TEXTELEMENT")}</label>
						<textarea class="form-control" name="textElement" id="textElement" rows="4" cols="50"></textarea>
					</div>
					
                  <div class="form-group">
                      <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file"  accept=".png, .jpg, .jpeg" class="form-control file">
                  </div>


                  <div class="modal-footer">
                      <div class="form-group">
                          <input type="button" class="btn btn-primary" id="saveTextElement" name="saveTextElement" value="${msg.get("SURVEY_ADD")}">
                          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                      </div>
                  </div>
              </form>
          </div>
        </div>
      </div>
    </div>
	
	<!-- Modal Text-Element Update-->
    <div id="textElementModalUpdate" class="modal fade" role="dialog">
      <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <h2 class="modal-title">${msg.get("SURVEY_TEXT")}</h2>
          </div>
          <div class="modal-body">


              <form id="textElementFormUpdate" name="textElementFormUpdate" method="post" enctype="multipart/form-data">
					<div class="modalResult">
					</div>

                   <input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					<input type="hidden" name="elementId" id="elementId" value="0">
					
					<div class="form-group">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" required>
					</div>
					
					<div class="form-group">
						<label for="textElement">${msg.get("SURVEY_TEXTELEMENT")}</label>
						<textarea class="form-control" name="textElement" id="textElement" rows="4" cols="50"></textarea>
					</div>
					
                  <div id="textElementPictureFrame" class="form-group">
        
                  </div>					
					
                  <div class="form-group">
                      <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file"  accept=".png, .jpg, .jpeg" class="form-control file">
                  </div>


                  <div class="modal-footer">
                      <div class="form-group">
                          <input type="button" class="btn btn-primary" id="saveTextElementUpdate" name="saveTextElementUpdate" value="${msg.get("SURVEY_ADD")}">
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

			  <form id="personalDataElementForm" name="personalDataElementForm" method="post">

				  <div class="modalResult">
				  </div>
					<input type="hidden" name="surveyIdP" id="surveyIdP" value="${currentSurvey.getSurveyId()}">
					
					<div class="form-group">
						  <label for="elementTitleP">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitleP" name="elementTitleP"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" required>
					</div>
					<div class="">					
						<input type="checkbox" name="firstnameP" id="firstnameP" class=""  value="true" />
						<label for="firstnameP">${msg.get("SURVEY_FIRSTNAME")}</label>
					</div>
					<div class="">				
						<input type="checkbox" name="lastnameP" id="lastnameP" class=""  value="true" />
						<label for="lastnameP">${msg.get("SURVEY_LASTNAME")}</label>
					</div>
					<div class="">				
						<input type="checkbox" name="ageP" id="ageP" class=""  value="true" />
						<label for="ageP">${msg.get("SURVEY_AGE")}</label>
					</div>
					<div class="">				
						<input type="checkbox" name="genderP" id="genderP" class=""  value="true" />
						<label for="genderP">${msg.get("SURVEY_GENDER")}</label>
					</div>
					<div class="">				
						<input type="checkbox" name="locationP" id="locationP" class=""  value="true" />
						<label for="locationP">${msg.get("SURVEY_LOCATION")}</label>
					</div>
					
					<div class="modal-footer">
					  <div class="form-group">

						  <input type="button" class="btn btn-primary" id="savePersonalDataElement" name="savePersonalDataElement" value="${msg.get("SURVEY_ADD")}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

					  </div>
				  </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>
	
	<!-- Modal PersonalData-Element Update-->
    <div id="personalDataElementModalUpdate" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_PERSONALDATA")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="personalDataElementFormUpdate" name="personalDataElementFormUpdate" method="post" enctype="multipart/form-data">

				  <div class="modalResult">
				  </div>
					<input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					<input type="hidden" name="elementId" id="elementId" value="0">
					
					<div class="form-group">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" required>
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

						  <input type="button" class="btn btn-primary" id="savePersonalDataElementUpdate" name="savePersonalDataElementUpdate" value="${msg.get("SURVEY_ADD")}">
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

			  <form id="closedQuestionElementForm" name="closedQuestionElementForm" method="post" enctype="multipart/form-data">

				  <div class="modalResult">
				  </div>
					<input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					
					<div class="form-group col-md-12">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" >
					</div>					
					<div class="form-group col-md-12">
						<label for="situationC">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="situation" id="situation" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="questiontextC">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="questiontext" name="questiontext"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" >
					  </div>
					  
					<div class="form-group col-md-6">
					  <label for="answer1">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer1" name="answer1"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer2">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer2" name="answer2"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer3">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer3" name="answer3"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer4">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer4" name="answer4"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer5">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer5" name="answer5"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer6">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer6" name="answer6"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" >
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
						  <input type="button" class="btn btn-primary" id="closedQuestionElementbuttonSave" name="closedQuestionElementbuttonSave" value="${msg.get('SURVEY_ADD')}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					  </div>
				  </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>
	
	<!-- Modal Closed-Question-Element Update-->
    <div id="closedQuestionElementModalUpdate" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_CLOSEDQUESTION")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="closedQuestionElementFormUpdate" name="closedQuestionElementFormUpdate" method="post" enctype="multipart/form-data">

				  <div class="modalResult">
				  </div>
					<input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					<input type="hidden" name="elementId" id="elementId" value="0">
					
					<div class="form-group col-md-12">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" >
					</div>					
					<div class="form-group col-md-12">
						<label for="situationC">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="situation" id="situation" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="questiontextC">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="questiontext" name="questiontext"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" >
					  </div>
					  
					<div class="form-group col-md-6">
					  <label for="answer1">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer1" name="answer1"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer2">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer2" name="answer2"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer3">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer3" name="answer3"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer4">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer4" name="answer4"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer5">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer5" name="answer5"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" >
					</div>
					<div class="form-group col-md-6">
					  <label for="answer6">${msg.get("SURVEY_ANSWER")}</label>
					  <input type="text" id="answer6" name="answer6"  class="form-control" placeholder="${msg.get('SURVEY_ANSWER_OPTIONAL')}" value="" >
					</div>
			
					<div class="col-md-6">				
						<input type="checkbox" name="optionalTextfield" id="optionalTextfield" class=""  value="true" />
						<label for="optionalTextfield">${msg.get("SURVEY_TEXTFIELD_OPTIONAL")}</label>
					</div>
					
					<div class="col-md-6">				
						<input type="checkbox" name="multipleSelection" id="multipleSelection" class=""  value="true" />
						<label for="multipleSelection">${msg.get("SURVEY_MULTIPLE_SELECTION")}</label>
					</div>
	
					<div id="closedQuestionElementPictureFrame" class="form-group">
        
					</div>	
					
					<div class="form-group col-md-12">
					  <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file" class="form-control file">
					</div>
					
					<div class="modal-footer">
					  <div class="form-group">
						  <input type="button" class="btn btn-primary" id="closedQuestionElementbuttonUpdate" name="closedQuestionElementbuttonUpdate" value="${msg.get('SURVEY_ADD')}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					  </div>
				  </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>	
	
		
	 <!-- Modal Open-Question-Element-->
    <div id="openQuestionElementModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_OPENQUESTION")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="openQuestionElementForm" name="openQuestionElementForm" method="post" enctype="multipart/form-data">

				  <div class="modalResult">
				  </div>
					
					<input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					
					<div class="form-group col-md-12">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" required>
					</div>				
					<div class="form-group col-md-12">
						<label for="situation">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="situation" id="situation" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="questiontext">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="questiontext" name="questiontext"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" required>
					  </div>

					<div class="form-group col-md-12">
					  <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file" class="form-control file">
					</div>
					
					<div class="modal-footer">
					  <div class="form-group">
						  <input type="button" class="btn btn-primary" id="openQuestionElementFormSave" name="openQuestionElementFormSave" value="${msg.get('SURVEY_ADD')}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					  </div>
				    </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>

			
	 <!-- Modal Open-Question-Element Update-->
    <div id="openQuestionElementModalUpdate" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_OPENQUESTION")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="openQuestionElementFormUpdate" name="openQuestionElementFormUpdate" method="post" enctype="multipart/form-data">

				  <div class="modalResult">
				  </div>
					
					<input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					<input type="hidden" name="elementId" id="elementId" value="0">
					
					<div class="form-group col-md-12">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" required>
					</div>				
					<div class="form-group col-md-12">
						<label for="situation">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="situation" id="situation" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="questiontext">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="questiontext" name="questiontext"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" required>
					  </div>

					<div id="openQuestionElementPictureFrame" class="form-group">
        
					</div>	
					
					<div class="form-group col-md-12">
					  <label for="picture">${msg.get("SURVEY_PICTURE")}</label>
						<input id="picture" name="picture" type="file" class="form-control file">
					</div>
					
					<div class="modal-footer">
					  <div class="form-group">
						  <input type="button" class="btn btn-primary" id="openQuestionElementFormSaveUpdate" name="openQuestionElementFormSaveUpdate" value="${msg.get('SURVEY_ADD')}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					  </div>
				    </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>
	
		
	 <!-- Modal ScoreTable-Element-->
    <div id="scoreTableElementModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_SCORETABLE")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="scoreTableElementForm" name="scoreTableElementForm" method="post" enctype="multipart/form-data">

				  <div class="modalResult">
				  </div>

					<input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					
					<div class="form-group col-md-12">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" required>
					</div>	
					
					<div class="form-group col-md-12">
						<label for="situation">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="situation" id="situation" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="questiontext">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="questiontext" name="questiontext"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" required>
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
						  <input type="button" class="btn btn-primary" id="scoreTableElementFormSave" name="scoreTableElementFormSave" value="${msg.get('SURVEY_ADD')}">
						  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					  </div>
				  </div>
			  </form>

		  </div>
		</div>

      </div>
    </div>

	
	<!-- Modal ScoreTable-Element Update-->
    <div id="scoreTableElementModalUpdate" class="modal fade" role="dialog">
		<div class="modal-dialog">

		<!-- Modal content-->
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h2 class="modal-title">${msg.get("SURVEY_SCORETABLE")}</h2>
		  </div>
		  <div class="modal-body">

			  <form id="scoreTableElementFormUpdate" name="scoreTableElementFormUpdate" method="post" enctype="multipart/form-data">

				  <div class="modalResult">
				  </div>

					<input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
					<input type="hidden" name="elementId" id="elementId" value="0">
					
					
					<div class="form-group col-md-12">
						  <label for="elementTitle">${msg.get("SURVEY_TITLE_ELEMENT")}</label>
						  <input type="text" id="elementTitle" name="elementTitle"  class="form-control" placeholder="${msg.get('SURVEY_TITLE_ELEMENT')}" value="" required>
					</div>	
					
					<div class="form-group col-md-12">
						<label for="situation">${msg.get("SURVEY_SITUATION")}</label>
						<textarea class="form-control" name="situation" id="situation" rows="4" cols="50"></textarea>
					</div>
					
					  <div class="form-group col-md-12">
						  <label for="questiontext">${msg.get("SURVEY_QUESTIONTEXT")}</label>
						  <input type="text" id="questiontext" name="questiontext"  class="form-control" placeholder="${msg.get('SURVEY_QUESTIONTEXT')}" value="" required>
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
					
					<div id="scoreTableElementPictureFrame" class="form-group">
        
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
						  <input type="button" class="btn btn-primary" id="scoreTableElementFormSaveUpdate" name="scoreTableElementFormSaveUpdate" value="${msg.get('SURVEY_ADD')}">
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
        $("#saveTextElement").click(function(){

			var vcfData = new FormData($('#textElementForm')[0]); 
			alert(vcfData);
            var request = $.ajax({
              url: "/textupload/",
              type: "POST",
              data: vcfData,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });

            request.fail(function(jqXHR, textStatus) {
              $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Request failed: ' + textStatus);
            });

        });
		
		$("#saveTextElementUpdate").click(function(){

			var vcfDataUpdate = new FormData($('#textElementFormUpdate')[0]); 
			alert(vcfDataUpdate);
            var request = $.ajax({
              url: "/textuploadUpdate/",
              type: "POST",
              data: vcfDataUpdate,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });

            request.fail(function(jqXHR, textStatus) {
              $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Request failed: ' + textStatus);
            });

        });
		
		
		
		$("#savePersonalDataElement").click(function(){

            var request = $.ajax({
              url: "/personaldata/",
              type: "POST",
              data:  {surveyId : $('#surveyId').val(), elementTitle : $('#elementTitleP').val(), firstname : $('#firstnameP').is(':checked'), lastname : $('#lastnameP').is(':checked'), age : $('#ageP').is(':checked'),
			  gender : $('#genderP').is(':checked'), locationP : $('#locationP').is(':checked')},
              dataType: "html"

            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel angeben und eine Checkbox auswählen.</p>');
                }else{
					location.reload();
				}
            });

            request.fail(function(jqXHR, textStatus) {
              $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Request failed: ' + textStatus);
            });

        });
		
		$("#savePersonalDataElementUpdate").click(function(){


				var vcfPersonalDataUpdate = new FormData($('#personalDataElementFormUpdate')[0]); 
				alert(vcfPersonalDataUpdate);
				var request = $.ajax({
              url: "/personaldataUpdate/",
              type: "POST",
              data: vcfPersonalDataUpdate,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });

            request.fail(function(jqXHR, textStatus) {
              $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Request failed: ' + textStatus);
            });

        });


	     $("#closedQuestionElementbuttonSave").click(function(){

			var vcfDataClosedQuestion = new FormData($('#closedQuestionElementForm')[0]); 
	
            var request = $.ajax({
              url: "/closedquestion/",
              type: "POST",
              data: vcfDataClosedQuestion,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });


        });
		
		$("#closedQuestionElementbuttonUpdate").click(function(){

			var vcfDataClosedQuestionUpdate = new FormData($('#closedQuestionElementFormUpdate')[0]); 
	
            var request = $.ajax({
              url: "/closedquestionUpdate/",
              type: "POST",
              data: vcfDataClosedQuestionUpdate,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });


        });
		
		$("#openQuestionElementFormSave").click(function(){

			var vcfDataOpenQuestion = new FormData($('#openQuestionElementForm')[0]); 
	
            var request = $.ajax({
              url: "/openquestion/",
              type: "POST",
              data: vcfDataOpenQuestion,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });


        });
		
		
		$("#openQuestionElementFormSaveUpdate").click(function(){

			var vcfDataOpenQuestionUpdate = new FormData($('#openQuestionElementFormUpdate')[0]); 
	
            var request = $.ajax({
              url: "/openquestionUpdate/",
              type: "POST",
              data: vcfDataOpenQuestionUpdate,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });


        });

		
		$("#scoreTableElementFormSave").click(function(){

			var vcfDataScoreTable = new FormData($('#scoreTableElementForm')[0]); 
	
            var request = $.ajax({
              url: "/scoretable/",
              type: "POST",
              data: vcfDataScoreTable,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });


        });
		
		$("#scoreTableElementFormSaveUpdate").click(function(){

			var vcfDataScoreTableUpdate = new FormData($('#scoreTableElementFormUpdate')[0]); 
	
            var request = $.ajax({
              url: "/scoretableUpdate/",
              type: "POST",
              data: vcfDataScoreTableUpdate,
              dataType: "html",
			  processData: false,
			  contentType: false
            });

            request.done(function(msg) {
				
                if("false" == msg){
                    $(".modalResult").html('<p class="btn-lg btn-danger bad notification">Sie müssen mindestens einen Titel und einen Text angeben.</p>');
                }else{
					location.reload();
				}
            });


        });
		
		
		$(".editElement").click(function(){
		
			event.preventDefault();
			
			var addressValue = $(this).attr("href");
					
			var request = $.ajax({
				type: "GET",
				url: addressValue
			});
			
			request.done(function(jsonResult) {
			
				alert(jsonResult);
				var obj = jQuery.parseJSON(jsonResult);
				
				var str = addressValue.replace(/[^a-zA-Z 0-9]+/g,'');

				if(str.indexOf('elementtype1') !== -1){

					$('#textElementModalUpdate #surveyId').val(obj.surveyId);
					$('#textElementModalUpdate #elementId').val(obj.elementId);
					$('#textElementModalUpdate #elementTitle').val(obj.elementTitle);
					$('#textElementModalUpdate #textElement').val(obj.text);
					
					$("#textElementPictureFrame").empty();
					$("#textElementPictureFrame").append( "<img src=\"/pictures/" + obj.picture + "\" class=\"img-thumbnail\" alt=\"PICTURE\" width=\"250\"></img>");
					
					$('#textElementModalUpdate').modal('show');
					
				}else if(str.indexOf('elementtype2') !== -1){
					$('#personalDataElementModalUpdate #surveyId').val(obj.surveyId);
					$('#personalDataElementModalUpdate #elementId').val(obj.elementId);
					$('#personalDataElementModalUpdate #elementTitle').val(obj.elementTitle);
					$('#personalDataElementModalUpdate #firstname').attr("checked",obj.firstname);
					$('#personalDataElementModalUpdate #lastname').attr("checked",obj.lastname);
					$('#personalDataElementModalUpdate #age').attr("checked",obj.age);
					$('#personalDataElementModalUpdate #gender').attr("checked",obj.gender);
					$('#personalDataElementModalUpdate #location').attr("checked",obj.location);

					
					$('#personalDataElementModalUpdate').modal('show');
					
				}else if(str.indexOf('elementtype3') !== -1){
				
					$('#closedQuestionElementModalUpdate #surveyId').val(obj.surveyId);					
					$('#closedQuestionElementModalUpdate #elementId').val(obj.elementId);					
					$('#closedQuestionElementModalUpdate #elementTitle').val(obj.elementTitle);
					$('#closedQuestionElementModalUpdate #situation').val(obj.situation);
					$('#closedQuestionElementModalUpdate #questiontext').val(obj.questiontext);
					$('#closedQuestionElementModalUpdate #answer1').val(obj.answer1);
					$('#closedQuestionElementModalUpdate #answer2').val(obj.answer2);	
					$('#closedQuestionElementModalUpdate #answer3').val(obj.answer3);	
					$('#closedQuestionElementModalUpdate #answer4').val(obj.answer4);	
					$('#closedQuestionElementModalUpdate #answer5').val(obj.answer5);	
					$('#closedQuestionElementModalUpdate #answer6').val(obj.answer6);						
					$('#closedQuestionElementModalUpdate #optionalTextfield').attr("checked", obj.optionalTextfield);	
					$('#closedQuestionElementModalUpdate #multipleSelection').attr("checked", obj.multipleSelection);	
					
					$("#closedQuestionElementPictureFrame").empty();
					$("#closedQuestionElementPictureFrame").append( "<img src=\"/pictures/" + obj.picture + "\" class=\"img-thumbnail\" alt=\"PICTURE\" width=\"250\"></img>");
					
					$('#closedQuestionElementModalUpdate').modal('show');
					
				}else if(str.indexOf('elementtype4') !== -1){
				
					$('#openQuestionElementModalUpdate #surveyId').val(obj.surveyId);
					$('#openQuestionElementModalUpdate #elementId').val(obj.elementId);		
					$('#openQuestionElementModalUpdate #elementTitle').val(obj.elementTitle);
					$('#openQuestionElementModalUpdate #situation').val(obj.situation);
					$('#openQuestionElementModalUpdate #questiontext').val(obj.questiontext);
					
					$("#openQuestionElementPictureFrame").empty();
					$("#openQuestionElementPictureFrame").append( "<img src=\"/pictures/" + obj.picture + "\" class=\"img-thumbnail\" alt=\"PICTURE\" width=\"250\"></img>");
					
					$('#openQuestionElementModalUpdate').modal('show');
					
				}else if(str.indexOf('elementtype5') !== -1){
				
					$('#scoreTableElementModalUpdate #surveyId').val(obj.surveyId);
					$('#scoreTableElementModalUpdate #elementId').val(obj.elementId);	
					$('#scoreTableElementModalUpdate #elementTitle').val(obj.elementTitle);
					$('#scoreTableElementModalUpdate #situation').val(obj.situation);				
					$('#scoreTableElementModalUpdate #questiontext').val(obj.questiontext);				
					$('#scoreTableElementModalUpdate #criterion1').val(obj.criterion1);	
					$('#scoreTableElementModalUpdate #criterion2').val(obj.criterion2);	
					$('#scoreTableElementModalUpdate #criterion3').val(obj.criterion3);	
					$('#scoreTableElementModalUpdate #criterion4').val(obj.criterion4);	
					$('#scoreTableElementModalUpdate #criterion5').val(obj.criterion5);	
					$('#scoreTableElementModalUpdate #criterion6').val(obj.criterion6);	
					
					$("#scoreTableElementPictureFrame").empty();
															
					$("#scoreTableElementPictureFrame").append( "<img src=\"/pictures/" + obj.picture + "\" class=\"img-thumbnail\" alt=\"PICTURE\" width=\"250\"></img>");
					
					$('#scoreTableElementModalUpdate').modal('show');
				}
			
			});
		

		});

		
    });
    </script>
</@layout.masterTemplate>
