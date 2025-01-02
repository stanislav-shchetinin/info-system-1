import {reg} from "./reg.js";
import {auth} from "./auth.js";

const btnReg = document.getElementById('btn-reg');
btnReg.addEventListener('click', reg);

const btnAuth = document.getElementById('btn-auth');
btnAuth.addEventListener('click', auth);
