<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Index">

	<div class="col-md-12">
		<h1>${msg.get("EVALUATION_HEADING")}</h1>

		<div class="panel panel-default">

			<div class="panel-body">
			
			<#assign elementCounter = 0>
			
				<#if surveyElementList?has_content>
				
					<#list surveyElementList>

						<#items as surveyElement>

							<#if (elementCounter % 2) ==0>
								<!--<div class="row"> -->
							</#if>
							<#if surveyElement.getElementType() == 2>
´
								<#if personalDataEvaluationList?has_content>
									<#assign elementCounter = elementCounter + 1>

									<#list personalDataEvaluationList>

										<#items as personalData>
										
											<div class="col-md-8">
											
												<h2>${surveyElement.getElementTitle()}</h2>
													
												<#if surveyElement.getElementId() == personalData.getElementId()>

													<#if personalData.getAges()?has_content>

													<h3>Alter</h3>
														<table class="table table-striped">
															<tr>
																<td>Minimalwert</td>
																<td>${personalData.getAgeMin()}</td>
															</tr>
															<tr>
																<td>Maximalwert</td>
																<td>${personalData.getAgeMax()}</td>
															</tr>
															<tr>
																<td>Median</td>
																<td>${personalData.getAgeMedian()}</td>
															</tr>	
															<tr>
																<td>Arithmetisches Mittel</td>
																<td>${personalData.getAgeAverage()}</td>
															</tr>	
															<tr>
																<td>Standardabweichung</td>
																<td>${personalData.getStandardDeviation()}</td>
															</tr>													
														</table>

														<#if personalData.getAges()?has_content>
															<canvas id="myChart${personalData.getSurveyId()}u${personalData.getElementId()}"></canvas>
															
															<script>
																$(document).ready(function(){
																	var ctx = document.getElementById("myChart${personalData.getSurveyId()}u${personalData.getElementId()}").getContext('2d');
						
																	var myChartPersonalData = new Chart(ctx, {
																		type: 'line',
																		data: {
																			labels: [
																			
																				<#list personalData.getAges()>
																					<#items as age>

																						${age}, 

																					</#items>
																				</#list>
																						
																			],
																			datasets: [
																				{
																					label: "Alter",
																					fill: false,
																					lineTension: 0.1,
																					backgroundColor: "rgba(75,192,192,0.4)",
																					borderColor: "rgba(75,192,192,1)",
																					borderCapStyle: 'butt',
																					borderDash: [],
																					borderDashOffset: 0.0,
																					borderJoinStyle: 'miter',
																					pointBorderColor: "rgba(75,192,192,1)",
																					pointBackgroundColor: "#fff",
																					pointBorderWidth: 1,
																					pointHoverRadius: 5,
																					pointHoverBackgroundColor: "rgba(75,192,192,1)",
																					pointHoverBorderColor: "rgba(220,220,220,1)",
																					pointHoverBorderWidth: 2,
																					pointRadius: 1,
																					pointHitRadius: 10,
																					data: [
																						<#list personalData.getAges()>
																							<#items as age>

																								${age}, 
																		
																							</#items>
																						</#list>
																					],
																					spanGaps: false,
																				}
																			]
																		},
																		options: {
																			scales: {
																				xAxes: [{
																					display: false
																				}]
																			}
																		}
																																	  
																	});
																  });
															</script>
														</#if>

													</#if>
													
													<#if personalData.getLocationCount()?has_content && personalData.getLocationCount()?size != 1 >
														

															<h3>Wohnorte</h3>
															<canvas id="myChart${personalData.getSurveyId()}u${personalData.getElementId()}LocationCount"></canvas>
															
															<script>
																$(document).ready(function(){
																	var ctx = document.getElementById("myChart${personalData.getSurveyId()}u${personalData.getElementId()}LocationCount").getContext('2d');
						
																	var myChartPersonalData = new Chart(ctx, {
																		type: 'bar',
																		data: {
																			labels: [
																			
																				<#list personalData.getLocationCount()>
																					<#items as locationName>

																						'${locationName.getLocation()}', 

																					</#items>
																				</#list>
																						
																			],
																			
																			datasets: [
																				{
																					label: "Wohnorte",
																					backgroundColor: [
																						"#2ecc71",
																						"#3498db",
																						"#95a5a6",
																						"#9b59b6",
																						"#f1c40f",
																						"#e74c3c",
																						"#c14b3c"
																					  ],
																					data: [
																						<#list personalData.getLocationCount()>
																							<#items as count>

																								${count.getCount()}, 
																		
																							</#items>
																						</#list>
																					]
																				}
																			]
					
																		},
																		options: {
																			scales: {
																				yAxes: [{
																					ticks: {
																					beginAtZero: true
																					}
																				}]
																			}
																		}

																																	  
																	});
																  });
															</script>
														</#if>
														
														<#if personalData.getMaleCounter()?has_content>
														
															<h3>Geschlecht</h3>
															<div class="col-md-6">
															<canvas id="myChart${personalData.getSurveyId()}u${personalData.getElementId()}Gender"></canvas>
															
															<script>
																$(document).ready(function(){
																	var ctx = document.getElementById("myChart${personalData.getSurveyId()}u${personalData.getElementId()}Gender").getContext('2d');
						
																	var myChartPersonalData = new Chart(ctx, {
																		type: 'doughnut',
																		data: {
																		labels: [
																				"Männlich",
																				"Weiblich"
																			],
																			datasets: [{
																				labels: ['Männlich', 'Weiblich'],
																				backgroundColor: [
																						"#3498db",
																						"#FF00BF"
																					],
																				data: [${personalData.getMaleCounter()}, ${personalData.getFemaleCounter()}]
																			}]
																		}
																																	  
																	});
																  });
															</script>
															</div>
														</#if>
												</#if>
											</div>
										</#items>
									</#list>
								</#if>
							</#if>
							
							
							
							<#if surveyElement.getElementType() == 3>
							
			
			
								<#if closedQuestionEvaluationList?has_content>
									<#assign elementCounter = elementCounter + 1>

									<#list closedQuestionEvaluationList>
										
										<#items as element>
											<#if surveyElement.getElementId() == element.getElementId()>
												<div class="col-md-8">
													<h2>${surveyElement.getElementTitle()}</h2>
	
														<div class="col-md-7">
															<canvas id="myChart${element.getSurveyId()}u${element.getElementId()}"></canvas>
														</div>
														<script>
															$(document).ready(function(){
																var ctx = document.getElementById("myChart${element.getSurveyId()}u${element.getElementId()}").getContext('2d');
																var myChart = new Chart(ctx, {
																  type: 'pie',
																  data: {
																	labels: [
																		"${element.getClosedAnswerCounter().getAnswer1()}"
																		<#if element.getClosedAnswerCounter().getAnswer2()?has_content> , "${element.getClosedAnswerCounter().getAnswer2()}"</#if>
																		 <#if element.getClosedAnswerCounter().getAnswer3()?has_content> , "${element.getClosedAnswerCounter().getAnswer3()}"</#if>
																		 <#if element.getClosedAnswerCounter().getAnswer4()?has_content> , "${element.getClosedAnswerCounter().getAnswer4()}"</#if>
																		 <#if element.getClosedAnswerCounter().getAnswer5()?has_content> , "${element.getClosedAnswerCounter().getAnswer5()}"</#if>
																		 <#if element.getClosedAnswerCounter().getAnswer6()?has_content> , "${element.getClosedAnswerCounter().getAnswer6()}"</#if>
																		 <#if element.getClosedAnswerCounter().getAnswerOtherc()?has_content> , "Sonstige"</#if>],
																	datasets: [{
																	  backgroundColor: [
																		"#2ecc71",
																		"#3498db",
																		"#95a5a6",
																		"#9b59b6",
																		"#f1c40f",
																		"#e74c3c",
																		"#c14b3c"
																	  ],
																	  data: [
																	  ${element.getClosedAnswerCounter().getAnswer1c()}
																	  <#if element.getClosedAnswerCounter().getAnswer2()?has_content> , ${element.getClosedAnswerCounter().getAnswer2c()}</#if>
																	  <#if element.getClosedAnswerCounter().getAnswer3()?has_content> , ${element.getClosedAnswerCounter().getAnswer3c()}</#if>
																	  <#if element.getClosedAnswerCounter().getAnswer4()?has_content> , ${element.getClosedAnswerCounter().getAnswer4c()}</#if>
																	  <#if element.getClosedAnswerCounter().getAnswer5()?has_content> , ${element.getClosedAnswerCounter().getAnswer5c()}</#if> 
																	  <#if element.getClosedAnswerCounter().getAnswer6()?has_content> , ${element.getClosedAnswerCounter().getAnswer6c()}</#if>
																	  <#if element.getClosedAnswerCounter().getAnswerOtherc()?has_content> , ${element.getClosedAnswerCounter().getAnswerOtherc()}</#if>]
																	}]
																  }
																});
															});
														</script>

													<#if element.getOptionalTextfield()?has_content>
													<div class="col-md-12">
													<h3>Sonstige Antworten</h3>
														<table class="table table-striped">
															<#list element.getOptionalTextfield()>
																<#items as optionalTextfield>
																	<tr>
																		<td>
																			${optionalTextfield}
																		</td>
																	</tr>
																</#items>
															</#list>
														</table>
													</div>
													</#if>
														
														
												</div>
											</#if>
										</#items>
											
									</#list>
								</#if>
							</#if>
							
							
							
							
							
							
														
							<#if surveyElement.getElementType() == 4>
								
									<#if openQuestionEvaluationList?has_content>
									<#assign elementCounter = elementCounter + 1>

									<#list openQuestionEvaluationList>
										
										<#items as element>
											<#if surveyElement.getElementId() == element.getElementId()>
												<div class="col-md-8">
													<h2>${surveyElement.getElementTitle()}</h2>


													<#if element.getText()?has_content>
													<div class="">
													<h3>Offene Antworten</h3>
														<table class="table table-striped">
														<tbody class="openQuestionEvaluationList">
															<#list element.getText()>
																<#items as text>
																	<tr>
																		<td>
																			${text}
																		</td>
																	</tr>
																</#items>
															</#list>
														</tbody>
														</table>
														<div class="text-center">
														  <ul class="pagination pagination-lg pager openQuestionEvaluationListPager"></ul>
														</div>
													</div>
													</#if>
														
														
												</div>
											</#if>
										</#items>
											
									</#list>
								</#if>
								
							</#if>
							
							
							
							<#if surveyElement.getElementType() == 5>
							
								<#if scoreTableEvaluationList?has_content>
									<#assign elementCounter = elementCounter + 1>
									<div class="col-md-8">
									<#list scoreTableEvaluationList>
										
										<#items as element>
											<#if surveyElement.getElementId() == element.getElementId()>
												<div class="">
													<h2>${surveyElement.getElementTitle()}</h2>

													
													<#if element.getScoreTableAnswerCounter().getCriterion1()?has_content>
													
														<div class="">
															<canvas id="myChart${element.getSurveyId()}u${element.getElementId()}uCriterion1"></canvas>
														</div>
														<script>
															$(document).ready(function(){
																var ctx = document.getElementById("myChart${element.getSurveyId()}u${element.getElementId()}uCriterion1").getContext('2d');
																var myChart = new Chart(ctx, {
																  type: 'bar',
																  data: {
																	labels: ["gar nicht zufrieden", "unzufrieden", "eher unzufrieden", "eher zufrieden", "zufrieden", "nicht beurteilbar"],
																	datasets: [{
																		label: "${element.getScoreTableAnswerCounter().getCriterion1()}",
																		backgroundColor: [
																		"#2ecc71",
																		"#3498db",
																		"#95a5a6",
																		"#9b59b6",
																		"#f1c40f",
																		"#e74c3c"
																	  ],
																	  data: [${element.getScoreTableAnswerCounter().getAnswer1c1()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer1c2()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer1c3()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer1c4()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer1c5()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer1c0()}]
																	}]
																  },
																	options: {
																	     layout: {
																			padding: {
																			  // Any unspecified dimensions are assumed to be 0
																			  left: 10,
																			  bottom: 5
																			}
																		  },
																		scales: {
																			yAxes: [{
																				ticks: {
																				beginAtZero: true
																				}
																			}]
																		}
																	}																  
																});
															});
														</script>
														
													</#if>
													
													<#if element.getScoreTableAnswerCounter().getCriterion2()?has_content>
													
														<div class="">
															<canvas id="myChart${element.getSurveyId()}u${element.getElementId()}uCriterion2"></canvas>
														</div>
														<script>
															$(document).ready(function(){
																var ctx = document.getElementById("myChart${element.getSurveyId()}u${element.getElementId()}uCriterion2").getContext('2d');
																var myChart = new Chart(ctx, {
																  type: 'bar',
																  data: {
																	labels: ["gar nicht zufrieden", "unzufrieden", "eher unzufrieden", "eher zufrieden", "zufrieden", "nicht beurteilbar"],
																	datasets: [{
																		label: "${element.getScoreTableAnswerCounter().getCriterion2()}",
																		backgroundColor: [
																		"#2ecc71",
																		"#3498db",
																		"#95a5a6",
																		"#9b59b6",
																		"#f1c40f",
																		"#e74c3c"
																	  ],
																	  data: [${element.getScoreTableAnswerCounter().getAnswer2c1()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer2c2()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer2c3()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer2c4()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer2c5()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer2c0()}]
																	}]
																  }
																});
															});
														</script>
														
													</#if>
													
													<#if element.getScoreTableAnswerCounter().getCriterion3()?has_content>
													
														<div class="">
															<canvas id="myChart${element.getSurveyId()}u${element.getElementId()}uCriterion3"></canvas>
														</div>
														<script>
															$(document).ready(function(){
																var ctx = document.getElementById("myChart${element.getSurveyId()}u${element.getElementId()}uCriterion3").getContext('2d');
																var myChart = new Chart(ctx, {
																  type: 'bar',
																  data: {
																	labels: ["gar nicht zufrieden", "unzufrieden", "eher unzufrieden", "eher zufrieden", "zufrieden", "nicht beurteilbar"],
																	datasets: [{
																		label: "${element.getScoreTableAnswerCounter().getCriterion3()}",
																	  backgroundColor: [
																		"#2ecc71",
																		"#3498db",
																		"#95a5a6",
																		"#9b59b6",
																		"#f1c40f",
																		"#e74c3c"
																	  ],
																	  data: [${element.getScoreTableAnswerCounter().getAnswer3c1()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer3c2()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer3c3()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer3c4()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer3c5()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer3c0()}]
																	}]
																  }
																});
															});
														</script>
														
													</#if>

													
													<#if element.getScoreTableAnswerCounter().getCriterion4()?has_content>
													
														<div class="">
															<canvas id="myChart${element.getSurveyId()}u${element.getElementId()}uCriterion4"></canvas>
														</div>
														<script>
															$(document).ready(function(){
																var ctx = document.getElementById("myChart${element.getSurveyId()}u${element.getElementId()}uCriterion4").getContext('2d');
																var myChart = new Chart(ctx, {
																  type: 'bar',
																  data: {
																	labels: ["gar nicht zufrieden", "unzufrieden", "eher unzufrieden", "eher zufrieden", "zufrieden", "nicht beurteilbar"],
																	datasets: [{
																		label: "${element.getScoreTableAnswerCounter().getCriterion4()}",
																	  backgroundColor: [
																		"#2ecc71",
																		"#3498db",
																		"#95a5a6",
																		"#9b59b6",
																		"#f1c40f",
																		"#e74c3c"
																	  ],
																	  data: [${element.getScoreTableAnswerCounter().getAnswer4c1()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer4c2()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer4c3()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer4c4()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer4c5()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer4c0()}]
																	}]
																  }
																});
															});
														</script>
														
													</#if>
	
													
													<#if element.getScoreTableAnswerCounter().getCriterion5()?has_content>
													
														<div class="">
															<canvas id="myChart${element.getSurveyId()}u${element.getElementId()}uCriterion5"></canvas>
														</div>
														<script>
															$(document).ready(function(){
																var ctx = document.getElementById("myChart${element.getSurveyId()}u${element.getElementId()}uCriterion5").getContext('2d');
																var myChart = new Chart(ctx, {
																  type: 'bar',
																  data: {
																	labels: ["gar nicht zufrieden", "unzufrieden", "eher unzufrieden", "eher zufrieden", "zufrieden", "nicht beurteilbar"],
																	datasets: [{
																		label: "${element.getScoreTableAnswerCounter().getCriterion5()}",
																		backgroundColor: [
																		"#2ecc71",
																		"#3498db",
																		"#95a5a6",
																		"#9b59b6",
																		"#f1c40f",
																		"#e74c3c"
																	  ],
																	  data: [${element.getScoreTableAnswerCounter().getAnswer5c1()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer5c2()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer5c3()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer5c4()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer5c5()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer5c0()}]
																	}]
																  }
																});
															});
														</script>
														
													</#if>
													
													
													<#if element.getScoreTableAnswerCounter().getCriterion6()?has_content>
													
														<div class="">
															<canvas id="myChart${element.getSurveyId()}u${element.getElementId()}uCriterion6"></canvas>
														</div>
														<script>
															$(document).ready(function(){
																var ctx = document.getElementById("myChart${element.getSurveyId()}u${element.getElementId()}uCriterion6").getContext('2d');
																var myChart = new Chart(ctx, {
																  type: 'bar',
																  data: {
																	labels: ["gar nicht zufrieden", "unzufrieden", "eher unzufrieden", "eher zufrieden", "zufrieden", "nicht beurteilbar"],
																	datasets: [{
																		label: "${element.getScoreTableAnswerCounter().getCriterion6()}",
																		backgroundColor: [
																			"#2ecc71",
																			"#3498db",
																			"#95a5a6",
																			"#9b59b6",
																			"#f1c40f",
																			"#e74c3c"
																	  ],
																	  data: [${element.getScoreTableAnswerCounter().getAnswer6c1()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer6c2()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer6c3()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer6c4()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer6c5()}, 
																	  ${element.getScoreTableAnswerCounter().getAnswer6c0()}]
																	}]
																  }
																});
															});
														</script>
														
													</#if>

													
												</div>
											</#if>
										</#items>
											
									</#list>
									</div>
								</#if>
							</#if>
						
						
						
						
						
						
							<#if elementCounter % 2 == 0>
								<!--</div>-->
							</#if>
						<div class="col-md-12">	
						<hr>	
						</div>
						</#items>


					</#list>
				</#if>
			
			</div>
		</div>
	</div>

	
	<script>
		$(document).ready(function(){
			$('.table').footable();
			$('.openQuestionEvaluationList').pageMe({pagerSelector:'.openQuestionEvaluationListPager',showPrevNext:true,hidePageNumbers:true,perPage:5});

		});
		
		
		$.fn.pageMe = function(opts){
			var $this = this,
				defaults = {
					perPage: 7,
					showPrevNext: false,
					hidePageNumbers: false
				},
				settings = $.extend(defaults, opts);
			
			var listElement = $this;
			var perPage = settings.perPage; 
			var children = listElement.children();
			var pager = $('.pager');
			
			if (typeof settings.childSelector!="undefined") {
				children = listElement.find(settings.childSelector);
			}
			
			if (typeof settings.pagerSelector!="undefined") {
				pager = $(settings.pagerSelector);
			}
			
			var numItems = children.size();
			var numPages = Math.ceil(numItems/perPage);

			pager.data("curr",0);
			
			if (settings.showPrevNext){
				$('<li><a href="#" class="prev_link">«</a></li>').appendTo(pager);
			}
			
			var curr = 0;
			while(numPages > curr && (settings.hidePageNumbers==false)){
				$('<li><a href="#" class="page_link">'+(curr+1)+'</a></li>').appendTo(pager);
				curr++;
			}
			
			if (settings.showPrevNext){
				$('<li><a href="#" class="next_link">»</a></li>').appendTo(pager);
			}
			
			pager.find('.page_link:first').addClass('active');
			pager.find('.prev_link').hide();
			if (numPages<=1) {
				pager.find('.next_link').hide();
			}
			  pager.children().eq(1).addClass("active");
			
			children.hide();
			children.slice(0, perPage).show();
			
			pager.find('li .page_link').click(function(){
				var clickedPage = $(this).html().valueOf()-1;
				goTo(clickedPage,perPage);
				return false;
			});
			pager.find('li .prev_link').click(function(){
				previous();
				return false;
			});
			pager.find('li .next_link').click(function(){
				next();
				return false;
			});
			
			function previous(){
				var goToPage = parseInt(pager.data("curr")) - 1;
				goTo(goToPage);
			}
			 
			function next(){
				goToPage = parseInt(pager.data("curr")) + 1;
				goTo(goToPage);
			}
			
			function goTo(page){
				var startAt = page * perPage,
					endOn = startAt + perPage;
				
				children.css('display','none').slice(startAt, endOn).show();
				
				if (page>=1) {
					pager.find('.prev_link').show();
				}
				else {
					pager.find('.prev_link').hide();
				}
				
				if (page<(numPages-1)) {
					pager.find('.next_link').show();
				}
				else {
					pager.find('.next_link').hide();
				}
				
				pager.data("curr",page);
				pager.children().removeClass("active");
				pager.children().eq(page+1).addClass("active");
			
			}
		};

	</script>
	
</@layout.masterTemplate>
