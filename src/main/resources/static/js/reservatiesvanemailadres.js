"use strict";
import{ byId, toon, verberg, verwijderdChildElementVan} from "./util.js";
byId("zoek").onclick = async function(){
    verbergReservatiesEnFouten();
    const emailAdresInput = byId("emailAdres");
    if (!emailAdresInput.checkValidity()) {
        toon("emailAdresFout");
        emailAdresInput.focus();
        return;
    }
    findByEmailAdres(emailAdresInput.value);
}

function verbergReservatiesEnFouten(){
verberg("reservatiesTable")
    verberg("storing");
    verberg("emailAdresFout");
}

async function findByEmailAdres(emailAdres) {
    const response = await fetch(`reservaties?emailAdres=${emailAdres}`);
    if(response.ok){
        const reservaties = await response.json();
        toon("reservatiesTable");
        const reservatiesBody = byId("reservatiesBody");
        verwijderdChildElementVan(reservatiesBody);
        for(const reservatie of reservaties){
            const tr = reservatiesBody.insertRow();
            tr.insertCell().innerText = reservatie.id;
            tr.insertCell().innerText = reservatie.titel;
            tr.insertCell().innerText = reservatie.plaatsen;
            tr.insertCell().innerText = new Date(reservatie.besteld).toLocaleString("nl-BE");
        }
    } else {
        toon ("storing");
    }
}