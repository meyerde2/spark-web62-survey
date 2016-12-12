<#import "/masterTemplate.ftl" as layout />
<@layout.masterTemplate title="Index">
    <h1>${msg.get("INDEX_HEADING")}</h1>
    <h2>${msg.get("INDEX_HEADING_INFO")}</h2>


 <div>
    <p>
    Dieses Softwareprojekt ist im Rahmen des Moduls WEB61 konzipiert wurden und während der Bearbeitung des Moduls WEB62 praktisch umgesetzt wurden.
    </p>
    <p>
        ...
    </p>
 </div>

 <h2>${msg.get("INDEX_HEADING_GOAL")}</h2>
 <div>

    <p>
  Ein Bestandteil der Zielsetzung ist es, dass die Online-Umfrage eine variable inhaltli-che Gestaltung ermöglicht,
   die über einen geschützten Administrationsbereich umgesetzt wird. Darüber hinaus gilt es,
   wesentliche statistische Methoden zur Verfü-gung zu stellen, die eine grundlegende Auswertung der Umfrage ermöglichen.
   Insgesamt soll die Online-Umfrage nach dem Prinzip „Eine Frage – ein Bildschirm“ gestaltet werden,
    da dieses Verfahren nachweislich eine bessere Datengewinnung gewährleistet.
     Dieses Prinzip ist jedoch ausschließlich für die digitale Umfrageform geeignet,
  weil in einer schriftlichen Befragung ein solches Verfahren sowohl ökono-misch als auch ökologisch nicht tragfähig ist.
    </p>

 </div>

 <h2>${msg.get("INDEX_HEADING_PROJECTINFO")}</h2>
 <div>
    <p>
        Der Quelltext der Webapplikation ist unter <a href="https://github.com/meyerde2/spark-web62-survey" target="onBlank_">Github</a> veröffentlicht.
    </p>

 </div>



</@layout.masterTemplate>
