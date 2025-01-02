import {URL_BACKEND, validInput} from "./main.js";

export const reg = () => {
    const form = document.querySelector('.sign-up-container');
    const inputPassword = form.querySelector('.password')
    const inputEmail = form.querySelector('.email')

    const json = {"username": inputEmail.value, "password": inputPassword.value}

    for (const [key, val] of Object.entries(json)) {
        if (!validInput(val)) {
            alert(`Поле ${key} не может быть пустым`);
            return;
        }
    }

    fetch(URL_BACKEND + '/registration', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(json),
    }).then(response => {
        if (response.status === 201) {
            alert("The user has been successfully created");
        }else if (response.status === 409) {
            alert("User with this name is already exist");
        } else if (response.status === 400) {
            response.json().then(data => alert(data.details));
        } else {
            alert("Oops.. is something wrong");
        }
    });
}
