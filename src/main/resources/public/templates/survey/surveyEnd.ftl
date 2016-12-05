<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Index">

	<div class="col-md-12">

		<h1>${msg.get("SURVEY_HEADING_EXECUTION")}</h1>

		<div class="panel panel-default">

			<div class="panel-body">

                 <#if draft?? && draft?c =="true">
                    <span class="label label-warning">Umfrage nicht freigeschaltet</span>
                 <#else>
                      <#if multipleSurveyExecution?? && multipleSurveyExecution?c =="true">
                        <a href="/survey/execution/${currentSurvey.getSurveyId()}/" class="btn btn-primary">Erneut an der Umfrage teilnehmen?</a>
                      <#else>
                         <span class="label label-warning">keine Durchführung mehr möglich</span>
                      </#if>
                 </#if>
			</div>

        </div>
    </div>


</@layout.masterTemplate>