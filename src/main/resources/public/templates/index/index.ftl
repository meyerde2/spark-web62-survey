<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Index">
    <h1>${msg.get("INDEX_HEADING")}</h1>
    <h2>${msg.get("INDEX_HEADING_INFO")}</h2>

 <div>

    <p>
        Zusammenfassend bestehen die wesentlichen Anforderungen an die praktische Realisierung des Online-Umfrage-Tools,
        die durch das Assignment im Modul WEB61 entstanden sind, aus einer Login- bzw. Logout-Funktionalität und einem damit einhergehenden Rollensystem für Umfrageersteller sowie Administratoren.
        Im geschlossenen Bereich der Anwendung muss die Möglichkeit vorhanden sein, zusätzliche Benutzer und Umfragen zu erstellen, zu bearbeiten und zu löschen.
    </p>
        <img src="/img/surveyOverview.png" class="img-thumbnail"></img>
    <p>
        Die Kernfunktionalität des Online-Umfrage-Tools in Form der Umfrageerfassung besteht aus fünf verschiedenen Bestandteilen, wozu das Erstellen, das Bearbeiten und das Löschen von Textelementen,
        persönlichen Daten, geschlossenen Fragen, offenen Fragen und einer Bewertungstabelle zählen. Solange die Browsersitzung aktiv ist,
        kann die Umfrage vom Befragten unterbrochen werden und zu einem späteren Zeitpunkt über den jeweiligen Umfragelink fortgesetzt werden.
        Die Umfrageergebnisse sind in einem Auswertungsbereich einsehbar. Um zu diesem Aus-wertungsbereich und zur Umfrageerfassung bzw. -bearbeitung zu gelangen,
        ist eine vorherige erfolgreiche Anmeldung über die Login-Funktionalität auf der Startseite durchzuführen. Innerhalb der Umfrageeinstelllungen ist auswählbar,
        ob eine Umfrage mehrmals hintereinander von demselben Befragten durchführbar ist. Diesbezüglich existieren Sperrmaßnahmen,
        um nach Session-IDs und/oder IP-Adressen zu filtern. Bei der IP-Adressen-Selektion ist zu bedenken, dass dadurch ggf.
        auch andere potenzielle Befragte innerhalb desselben lokalen Netzwerkes ausgeschlossen werden.
    </p>
    <img src="/img/surveyEditor.png" class="img-thumbnail"></img>

    <p>
        Da die Erstellung und die Durchführung von Umfragen als Kernfunktionalität deklariert ist, liegt der Fokus der Entwicklungsaktivitäten darauf.
        In der Folge werden andere (Teil-) Funktionalitäten als Zusatznutzen eingestuft und sind somit nicht Bestandteil der während dieses Assignments entstandenen Software.
        Daher ist u. a. die in der funktionalen Anforderung (FA) 7 spezifizierte Einzelansicht einer beantworteten Umfrage nicht Teil des Prototyps
        und folglich bisher nicht im Auswertungsbereich der Online-Umfragen ersichtlich (s. Assignment aus WEB61).
    </p>
    <img src="/img/surveyExecution.png" class="img-thumbnail"></img>

 </div>

 <h2>${msg.get("INDEX_HEADING_PROJECTINFO")}</h2>
 <div>
    <p>
        Der Quelltext der Webapplikation ist unter <a href="https://github.com/meyerde2/spark-web62-survey" target="onBlank_">Github</a> veröffentlicht.
    </p>

 </div>



</@layout.masterTemplate>
