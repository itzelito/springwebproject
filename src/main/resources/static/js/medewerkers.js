"use strict";
import{byId, toon, verwijderdChildElementVan, verberg} from "./util.js";
byId("zoek").onclick = async function(){
    verbergAlleFouten();
    const stukVoornaamInput = byId("stukVoornaam");
    const stukFamilienaamInput = byId("stukFamilienaam");
    findByStukVoornaamEnStukFamilienaam(stukVoornaamInput.value, stukFamilienaamInput.value);

}

function verbergAlleFouten(){
    verberg("medewerkersTable");
    verberg("storing");

}
async function findByStukVoornaamEnStukFamilienaam(stukVoornaam, stukFamilienaam) {
    const response= await fetch(`medewerkers?stukVoornaam=${stukVoornaam}&stukFamilienaam=${stukFamilienaam}`);
    if(response.ok){
        const medewerkers = await response.json();
        toon("medewerkersTable");
        const medewerkersBody = byId("medewerkersBody");
        verwijderdChildElementVan(medewerkersBody)
        for (const medewerker of medewerkers){
            const tr= medewerkersBody.insertRow();
            tr.insertCell().innerText = medewerker.id;
            tr.insertCell().innerText = medewerker.voornaam;
            tr.insertCell().innerText = medewerker.familienaam;
        }
    } else {
        toon("storing");
    }
}