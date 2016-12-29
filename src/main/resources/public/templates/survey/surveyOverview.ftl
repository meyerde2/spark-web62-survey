<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Index">
	
	<div class="col-md-12">
		
		<h1>${msg.get("SURVEY_HEADING")}</h1>

		<div class="panel panel-default">

			<div class="panel-body">
		  
				<div class="">
				
				  <table class="table table-striped">
					<thead>
					  <tr>
						<th>ID</th>
						<th>Titel</th>
						<th data-type="html" data-sort-use="text" data-breakpoints="xs">Status</th>
						<th data-type="html" data-sort-use="text" data-breakpoints="xs">Aktionen</th>
					  </tr>
					</thead>
					<tbody>
					<#if surveyList?has_content>
						<#list surveyList>

							<#items as element>
							  <tr data-expanded="false">
								<td>${element.getSurveyId()}</td>
								<td>${element.getSurveyTitle()}</td>
								<td>
									<#if element.isPublished()?c =="true">
										<span class="label label-success">veröffentlicht</span>
									<#else>
										<span class="label label-warning">Entwurf</span>
									</#if>
								</td>
								<td>
									<a href="/surveycreation/${element.getSurveyId()}/" class="btn btn-info"><span class="glyphicon glyphicon-pencil"></span></a>
									<a href="/evaluation/${element.getSurveyId()}/" class="btn btn-success"><span class="glyphicon glyphicon-stats"></span></a>
									<a href="#" data-toggle="modal" data-target="#confirm${element.getSurveyId()}" class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span></a>
								</td>
							  </tr>
							
							 <!-- Modal Confirm-->
								<div id="confirm${element.getSurveyId()}" class="modal fade" role="dialog">
									
									<div class="modal-dialog">

										<!-- Modal content-->
										<div class="modal-content">
									
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal">&times;</button>
												<h2 class="modal-title">Umfrage löschen</h2>
											</div>
									  
									  
											<div class="modal-body">

												<p>Wollen Sie diese Umfrage wirklich löschen?</p>

											</div>
											
											<div class="modal-footer">
											<div class="form-group">

											  <a href="/deletesurvey/${element.getSurveyId()}/" class="btn btn-danger">${msg.get("COMMON_DELETE")}</a>
											  <button type="button" class="btn btn-default" data-dismiss="modal">${msg.get("COMMON_CANCEL")}</button>

											</div>
											</div>

										</div>
									</div>
								</div>
								</#items>
						<#else>

						  <p>no </p>
						  
						</#list>
					</#if>
					</tbody>
				  </table>
				</div>
			</div>

            <a href="#" id="newSurvey" data-toggle="modal" data-target="#createSurvey" class="btn btn-primary">Neue Umfrage erstellen?</a>
        </div>
    </div>

	
	 <!-- Modal Closed-Question-Element-->
    <div id="createSurvey" class="modal fade" role="dialog">
		
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
		
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h2 class="modal-title">Neue Umfrage erstellen</h2>
				</div>
		  
		  
				<div class="modal-body">

					<div class="panel panel-default">

						<form action="/createNewSurvey/" method="post" name="createNewSurveyForm">
							<div class="panel-body">

								<div class="modalResult">
								</div>

								<div class="">		
									
									<label for="surveyTitle">${msg.get("SURVEY_TITLE_QUESTION")}</label>								
									<input type="text" name="surveyTitle" id="surveyTitle" class="form-control"  placeholder="${msg.get('SURVEY_TITLE_QUESTION')}" required/>
									
								</div>


								<div class="">				
									<input type="checkbox" name="sessionId" id="sessionId" class=""  value="true" />
									<label for="sessionId">${msg.get("SURVEY_SESSIONID")}</label>
								</div>

								<div class="">				
									<input type="checkbox" name="ipAddress" id="ipAddress" class=""  value="true" />
									<label for="ipAddress">${msg.get("SURVEY_IPADDRESS")}</label>
								</div>


								<div class="modal-footer">
								<div class="form-group">

								  <input type="submit" class="btn btn-primary" id="buttonSave" name="buttonSave" value="${msg.get("SURVEY_ADD")}">
								  <button type="button" class="btn btn-default" data-dismiss="modal">${msg.get("COMMON_CANCEL")}</button>

								</div>
								</div>

							</div>
						</form>
				´		
					</div>
				</div>

			</div>
		</div>
	</div>
	
	
	
	<script>
	
	jQuery(function($){
		$('.table').footable();
	});
	</script>
	
	
</@layout.masterTemplate>
