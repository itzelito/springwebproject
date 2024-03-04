"use strict";
import {toon, byId, setText, verberg} from "./util.js";
byId("zoek").onclick = async function(){
    verbergFilmFouten();
    const zoekIdInput = byId("zoekId");
    if(! zoekIdInput.checkValidity()){
        toon("zoekIdFout");
        zoekIdInput.focus();
        return;
    }
    findById(zoekIdInput.value);
}

function verbergFilmFouten(){
    verberg("film");
    verberg("zoekIdFout");
    verberg("nietGevonden");
    verberg("storing");
}
async function findById(id){
    const response = await fetch(`films/${id}`);
    if(response.ok){
        const film = await response.json();
        toon("film");
        setText("titel", film.titel);
        setText("jaar", film.jaar);
        setText("vrijePlaatsen", film.vrijePlaatsen);
    } else {
        if(response.status === 404){
            toon("nietGevonden");
        } else {
            toon("storing");
        }
    }
}
