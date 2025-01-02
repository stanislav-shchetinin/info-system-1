const URL_BACKEND = 'http://localhost:9090/lab1p-1.0-SNAPSHOT/api';
const URL_FRONTEND = 'http://localhost:19723';
const HOME = '/movies'
const LOGIN = '/login'
const validInput = (str) => {
    return str != null && str !== "";
}

const authHeader = () => {
    return 'Bearer ' + localStorage.getItem("jwt");
}

const redirectToLogin = () => {
    window.location.replace(URL_FRONTEND + LOGIN)
}

const getUserRole = () => {
    return localStorage.getItem("role");
}

const displayUserDetails = () => {
    const username = localStorage.getItem("username");
    const role = localStorage.getItem("role");

    document.getElementById("user-principals").innerText = `Username: ${username}; Role: ${role}`;
}

export {URL_BACKEND, URL_FRONTEND, HOME, LOGIN, validInput, displayUserDetails, authHeader, redirectToLogin, getUserRole};
