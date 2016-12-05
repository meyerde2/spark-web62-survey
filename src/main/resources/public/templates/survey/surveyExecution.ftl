<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Index">

	<div class="col-md-12">

		<h1>${msg.get("SURVEY_HEADING_EXECUTION")}</h1>

		<div class="panel panel-default">

			<div class="panel-body">

                <h2 style="word-break: break-all">Umfragenbezeichnung: ${currentSurvey.getSurveyTitle()}</h2>


                <form id="surveyExecution" name="surveyExecution" method="post" action="/survey/executionQuestion/">

                    <input type="hidden" name="surveyId" id="surveyId" value="${currentSurvey.getSurveyId()}">
                    <input type="hidden" name="elementId" id="elementId" value="${elementId}">
                    <input type="hidden" name="elementType" id="elementType" value="${elementType}">

                    <#if currentSurveyText?has_content>
                            <#if currentSurveyText.getPicture()??>
                                <div class="pictureFrame">
                                    <img src="/pictures/${currentSurveyText.getPicture()}" width="350" class="img-thumbnail"></img>
                                </div>
                            </#if>
                            <div class="textFrame">
                                <p>${currentSurveyText.getText()}</p>
                            </div>
							
                    </#if>

                    <#if currentSurveyPersonalData?has_content>

						<p>${currentSurveyPersonalData.getElementTitle()}</p>
						<#if currentSurveyPersonalData.isFirstname() == true>
							<div class="form-group">
							<label for="firstname">${msg.get("SURVEY_FIRSTNAME")}</label>
							<input type="text" id="firstname" name="firstname"  class="form-control" placeholder="${msg.get('SURVEY_FIRSTNAME')}" value="" required>
							</div>
						</#if>
						<#if currentSurveyPersonalData.isLastname() == true>
							<div class="form-group">
							<label for="lastname">${msg.get("SURVEY_LASTNAME")}</label>
							<input type="text" id="lastname" name="lastname"  class="form-control" placeholder="${msg.get('SURVEY_LASTNAME')}" value="" required>
							</div>
						</#if>
						<#if currentSurveyPersonalData.isAge() == true>
							<div class="form-group">
							<label for="age">${msg.get("SURVEY_AGE")}</label>
							<input type="text" id="age" name="age"  class="form-control" placeholder="${msg.get('SURVEY_AGE')}" value="" required>
							</div>
						</#if>	
						<#if currentSurveyPersonalData.isGender() == true>
						
							<div class="form-group">
							<label for="gender">${msg.get("SURVEY_GENDER")}</label>						
							<select name="gender" id="gender" class="form-control">
							  <option value="1" selected="selected">Männlich</option>
							  <option value="2">Weiblich</option>
							</select>
							</div>
						</#if>	
						<#if currentSurveyPersonalData.isLocation() == true>
							<div class="form-group">
							<label for="location">${msg.get("SURVEY_LOCATION")}</label>
							<input type="text" id="location" name="location"  class="form-control" placeholder="${msg.get('SURVEY_LOCATION')}" value="" required>
							</div>
						</#if>							
                    </#if>
					
					
					
					<#if currentSurveyClosedQuestion?has_content>

						<p>${currentSurveyClosedQuestion.getElementTitle()}</p>
						
						
						<#if currentSurveyClosedQuestion.getPicture()??>
							<div class="pictureFrame">
								<img src="/pictures/${currentSurveyClosedQuestion.getPicture()}" width="350" class="img-thumbnail"></img>
							</div>
						</#if>						
						
						
						<#if currentSurveyClosedQuestion.getSituation()?has_content>
							<div class="textFrame">
								<p>${currentSurveyClosedQuestion.getSituation()}</p>
							</div>
						</#if>
						<#if currentSurveyClosedQuestion.getQuestiontext()?has_content>
							<div class="questionText">
								<h3>${currentSurveyClosedQuestion.getQuestiontext()}</h3>
							</div>
						</#if>
						
							
                    
						<#if currentSurveyClosedQuestion.isMultipleSelection() == true>
							<div class="answers">
								<#if currentSurveyClosedQuestion.getAnswer1()?has_content>
									<div class="answer">
										<input type="checkbox" id="answer1" name="answer1" value="true" >
										<label for="answer1">${currentSurveyClosedQuestion.getAnswer1()}</label>		
									</div>
								</#if>	
								<#if currentSurveyClosedQuestion.getAnswer2()?has_content>
									<div class="answer">
										<input type="checkbox" id="answer2" name="answer2" value="true" >
										<label for="answer2">${currentSurveyClosedQuestion.getAnswer2()}</label>		
									</div>
								</#if>		
								<#if currentSurveyClosedQuestion.getAnswer3()?has_content>
									<div class="answer">
										<input type="checkbox" id="answer3" name="answer3" value="true" >
										<label for="answer3">${currentSurveyClosedQuestion.getAnswer3()}</label>		
									</div>
								</#if>	
								<#if currentSurveyClosedQuestion.getAnswer4()?has_content>
									<div class="answer">
										<input type="checkbox" id="answer4" name="answer4" value="true" >
										<label for="answer4">${currentSurveyClosedQuestion.getAnswer4()}</label>		
									</div>
								</#if>	
								<#if currentSurveyClosedQuestion.getAnswer5()?has_content>
									<div class="answer">
										<input type="checkbox" id="answer5" name="answer5" value="true" >
										<label for="answer5">${currentSurveyClosedQuestion.getAnswer5()}</label>		
									</div>
								</#if>	
								<#if currentSurveyClosedQuestion.getAnswer6()?has_content>
									<div class="answer">
										<input type="checkbox" id="answer6" name="answer6" value="true" >
										<label for="answer6">${currentSurveyClosedQuestion.getAnswer6()}</label>		
									</div>
								</#if>	
											
							</div>
							
						<#else>
							<div class="answers">
								<#if currentSurveyClosedQuestion.getAnswer1()?has_content>
									<div class="answer">
										<input type="radio" id="answer1" name="answer" value="1" required>
										<label for="answer1">${currentSurveyClosedQuestion.getAnswer1()}</label>		
									</div>
								</#if>	
								<#if currentSurveyClosedQuestion.getAnswer2()?has_content>
									<div class="answer">
										<input type="radio" id="answer2" name="answer" value="2" >
										<label for="answer2">${currentSurveyClosedQuestion.getAnswer2()}</label>		
									</div>
								</#if>		
								<#if currentSurveyClosedQuestion.getAnswer3()?has_content>
									<div class="answer">
										<input type="radio" id="answer3" name="answer" value="3" >
										<label for="answer3">${currentSurveyClosedQuestion.getAnswer3()}</label>		
									</div>
								</#if>	
								<#if currentSurveyClosedQuestion.getAnswer4()?has_content>
									<div class="answer">
										<input type="radio" id="answer4" name="answer" value="4" >
										<label for="answer4">${currentSurveyClosedQuestion.getAnswer4()}</label>		
									</div>
								</#if>	
								<#if currentSurveyClosedQuestion.getAnswer5()?has_content>
									<div class="answer">
										<input type="radio" id="answer5" name="answer" value="5" >
										<label for="answer5">${currentSurveyClosedQuestion.getAnswer5()}</label>		
									</div>
								</#if>	
								<#if currentSurveyClosedQuestion.getAnswer6()?has_content>
									<div class="answer">
										<input type="radio" id="answer6" name="answer" value="6" >
										<label for="answer6">${currentSurveyClosedQuestion.getAnswer6()}</label>		
									</div>
								</#if>	
											
							</div>
						</#if>	
						
						<#if currentSurveyClosedQuestion.isOptionalTextfield()?has_content>
							<div class="form-group">
								<label for="optionalTextfield">${msg.get("SURVEY_TEXTFIELD_OPTIONAL")}</label>
								<input type="text" id="optionalTextfield" name="optionalTextfield"  class="form-control" placeholder="${msg.get('SURVEY_TEXTFIELD_OPTIONAL')}" value="">
							</div>
						</#if>
						
					</#if>





					<#if currentSurveyOpenQuestion?has_content>

						<p>${currentSurveyOpenQuestion.getElementTitle()}</p>
						
						
						<#if currentSurveyOpenQuestion.getPicture()??>
							<div class="pictureFrame">
								<img src="/pictures/${currentSurveyOpenQuestion.getPicture()}" width="350" class="img-thumbnail"></img>
							</div>
						</#if>						
						
						
						<#if currentSurveyOpenQuestion.getSituation()?has_content>
							<div class="textFrame">
								<p>${currentSurveyOpenQuestion.getSituation()}</p>
							</div>
						</#if>
						<#if currentSurveyOpenQuestion.getQuestiontext()?has_content>
							<div class="questionText">
								<h3>${currentSurveyOpenQuestion.getQuestiontext()}</h3>
							</div>
						</#if>

						<div class="form-group">
							<label for="textfield">${msg.get("SURVEY_TEXTELEMENT")}</label>
							<input type="text" id="text" name="textfield"  class="form-control" placeholder="${msg.get('SURVEY_TEXTELEMENT')}" value="">
						</div>

						
					</#if>

					<#if currentSurveyScoreTable?has_content>

						<p>${currentSurveyScoreTable.getElementTitle()}</p>
						
						
						<#if currentSurveyScoreTable.getPicture()??>
							<div class="pictureFrame">
								<img src="/pictures/${currentSurveyScoreTable.getPicture()}" width="350" class="img-thumbnail"></img>
							</div>
						</#if>						
						
						
						<#if currentSurveyScoreTable.getSituation()?has_content>
							<div class="textFrame">
								<p>${currentSurveyScoreTable.getSituation()}</p>
							</div>
						</#if>
						<#if currentSurveyScoreTable.getQuestiontext()?has_content>
							<div class="questionText">
								<h3>${currentSurveyScoreTable.getQuestiontext()}</h3>
							</div>
						</#if>


							<div class="criterions">
								<table class="table">
								<tr>
									<th data-type="html" data-sort-use="text" data-breakpoints="xs">Kriterium:</th>
									<th data-type="html" data-sort-use="text" data-breakpoints="xs">gar nicht zufrieden</th>
									<th data-type="html" data-sort-use="text" data-breakpoints="xs">unzufrieden</th>
									<th data-type="html" data-sort-use="text" data-breakpoints="xs">eher unzufrieden</th>
									<th data-type="html" data-sort-use="text" data-breakpoints="xs">eher zufrieden</th>
									<th data-type="html" data-sort-use="text" data-breakpoints="xs">sehr zufrieden</th>
									<th data-type="html" data-sort-use="text" data-breakpoints="xs" class="noEvaluation">nicht beurteilt</th>
								</tr>
								
									<#if currentSurveyScoreTable.getCriterion1()?has_content>							
										<tr data-expanded="true">
											<td><label for="criterion1">${currentSurveyScoreTable.getCriterion1()}</label></td>
											<td><input type="radio" id="criterion1" name="criterion1" value="5" ></td>
											<td><input type="radio" id="criterion1" name="criterion1" value="4" ></td>
											<td><input type="radio" id="criterion1" name="criterion1" value="3" ></td>
											<td><input type="radio" id="criterion1" name="criterion1" value="2" ></td>
											<td><input type="radio" id="criterion1" name="criterion1" value="1" ></td>
											<td class="noEvaluation"><input type="radio" id="criterion1" name="criterion1" value="0" required></td>
										</tr>
									</#if>	
									<#if currentSurveyScoreTable.getCriterion2()?has_content>
										<tr data-expanded="true">
											<td><label for="criterion2">${currentSurveyScoreTable.getCriterion2()}</label></td>
											<td><input type="radio" id="criterion2" name="criterion2" value="5" ></td>
											<td><input type="radio" id="criterion2" name="criterion2" value="4" ></td>
											<td><input type="radio" id="criterion2" name="criterion2" value="3" ></td>
											<td><input type="radio" id="criterion2" name="criterion2" value="2" ></td>
											<td><input type="radio" id="criterion2" name="criterion2" value="1" ></td>
											<td class="noEvaluation"><input type="radio" id="criterion2" name="criterion2" value="0" required></td>
										</tr>
									</#if>		
									<#if currentSurveyScoreTable.getCriterion3()?has_content>
										<tr data-expanded="true">
											<td><label for="criterion3">${currentSurveyScoreTable.getCriterion3()}</label></td>
											<td><input type="radio" id="criterion3" name="criterion3" value="5" ></td>
											<td><input type="radio" id="criterion3" name="criterion3" value="4" ></td>
											<td><input type="radio" id="criterion3" name="criterion3" value="3" ></td>
											<td><input type="radio" id="criterion3" name="criterion3" value="2" ></td>
											<td><input type="radio" id="criterion3" name="criterion3" value="1" ></td>
											<td class="noEvaluation"><input type="radio" id="criterion3" name="criterion3" value="0" required></td>
										</tr>
									</#if>	
									<#if currentSurveyScoreTable.getCriterion4()?has_content>
										<tr data-expanded="true">
											<td><label for="criterion4">${currentSurveyScoreTable.getCriterion4()}</label></td>
											<td><input type="radio" id="criterion4" name="criterion4" value="5" ></td>
											<td><input type="radio" id="criterion4" name="criterion4" value="4" ></td>
											<td><input type="radio" id="criterion4" name="criterion4" value="3" ></td>
											<td><input type="radio" id="criterion4" name="criterion4" value="2" ></td>
											<td><input type="radio" id="criterion4" name="criterion4" value="1" ></td>
											<td class="noEvaluation"><input type="radio" id="criterion4" name="criterion4" value="0" required></td>
										</tr>
									</#if>	
									<#if currentSurveyScoreTable.getCriterion5()?has_content>
										<tr data-expanded="true">
											<td><label for="criterion5">${currentSurveyScoreTable.getCriterion5()}</label></td>
											<td><input type="radio" id="criterion5" name="criterion5" value="5" ></td>
											<td><input type="radio" id="criterion5" name="criterion5" value="4" ></td>
											<td><input type="radio" id="criterion5" name="criterion5" value="3" ></td>
											<td><input type="radio" id="criterion5" name="criterion5" value="2" ></td>
											<td><input type="radio" id="criterion5" name="criterion5" value="1" ></td>
											<td class="noEvaluation"><input type="radio" id="criterion5" name="criterion5" value="0" required></td>
										</tr>
									</#if>	
									<#if currentSurveyScoreTable.getCriterion6()?has_content>
										<tr data-expanded="true">
											<td><label for="criterion6">${currentSurveyScoreTable.getCriterion6()}</label></td>
											<td><input type="radio" id="criterion6" name="criterion6" value="5" ></td>
											<td><input type="radio" id="criterion6" name="criterion6" value="4" ></td>
											<td><input type="radio" id="criterion6" name="criterion6" value="3" ></td>
											<td><input type="radio" id="criterion6" name="criterion6" value="2" ></td>
											<td><input type="radio" id="criterion6" name="criterion6" value="1" ></td>
											<td class="noEvaluation"><input type="radio" id="criterion6" name="criterion6" value="0" required></td>
										</tr>
									</#if>	
								</table>		
							</div>
					
					</#if>						
					
					
                    <div class="form-group">
                          <input type="submit" class="btn btn-primary" id="nextQuestion" name="nextQuestion" value="fortsetzen">
                      </div>
                  </form>
			</div>

        </div>
    </div>

		
	<script>
	
	jQuery(function($){
		$('.table').footable();
	});
	</script>

</@layout.masterTemplate>