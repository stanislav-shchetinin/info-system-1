import {authHeader, displayUserDetails, HOME, redirectToLogin, URL_BACKEND, URL_FRONTEND} from "./main.js";

document.addEventListener("DOMContentLoaded", () => {
    displayUserDetails();

    const applicationTable = document.getElementById("applications-table")
        .getElementsByTagName("tbody")[0];

    fetchApplications();

    document.getElementById('home-button').addEventListener("click", (e) => {
        e.preventDefault();
        window.location.replace(URL_FRONTEND + HOME)
    })

    function fetchApplications() {
        fetch(URL_BACKEND + '/admin', {
            method: 'GET',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    applicationTable.innerHTML = '';
                    response.json().then(applications => {
                        applications.forEach(application => {
                            const row = applicationTable.insertRow();
                            row.innerHTML = `
                                <td>${application.id}</td>
                                <td>${application.createdBy}</td>
                                <td>${application.username}</td>
                                <td><button>Accept</button></td>
                            `;
                            const button = row.querySelector('button');
                            button.addEventListener('click', () => acceptSubmit(application));
                        });
                    });

                } else if (response.status === 401) {
                    redirectToLogin();
                } else if (response.status === 400) {
                    response.json().then(j => alert(j.details));
                } else {
                    response.json().then(j => alert(j.message));
                }
            })
            .catch(console.error);
    }

    window.acceptSubmit = function (application) {
        fetch(`${URL_BACKEND}/admin`, {
            method: 'PUT',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(application)
        })
            .then(response => {
                if (response.ok) {
                    fetchApplications()
                } else {
                    response.json().then(response => {
                        alert(response.message)
                    });
                }
            })
            .catch(console.error);
    };
});
