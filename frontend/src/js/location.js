import {authHeader, displayUserDetails, HOME, redirectToLogin, URL_BACKEND, URL_FRONTEND} from "./main.js";

document.addEventListener("DOMContentLoaded", () => {
    const locationTable = document.getElementById("location-table").getElementsByTagName("tbody")[0];
    const addLocationForm = document.getElementById("add-location-form");
    let isEditing = false;
    let currentEditId = null;

    displayUserDetails();

    fetchLocations();
    // setInterval(fetchLocations, 5000);

    function fetchLocations() {
        fetch(URL_BACKEND + '/locations/all', {
            method: 'GET',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    locationTable.innerHTML = '';
                    response.json().then(locations => {
                        locations.forEach(location => {
                            const row = locationTable.insertRow();
                            row.innerHTML = `
                                <td>${location.id}</td>
                                <td>${location.x}</td>
                                <td>${location.y}</td>
                                <td>${location.z}</td>
                                <td>
                                    <button onclick="deleteLocation(${location.id})">Delete</button>
                                </td>
                            `;
                            row.addEventListener("click", () => editLocation(location));
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

    document.getElementById('home-button').addEventListener("click", (e) => {
        window.location.replace(URL_FRONTEND + HOME)
    })

    addLocationForm.addEventListener("submit", (e) => {
        e.preventDefault();
        const data = {
            x: parseFloat(document.getElementById("x").value),
            y: parseInt(document.getElementById("y").value),
            z: parseInt(document.getElementById("z").value),
        };

        if (isEditing && currentEditId !== null) {
            fetch(URL_BACKEND + `/locations/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': authHeader(),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.ok) {
                        fetchLocations();
                    } else if (response.status === 401) {
                        redirectToLogin();
                    } else if (response.status === 400) {
                        response.json().then(j => alert(j.details));
                    } else {
                        response.json().then(j => alert(j.message));
                    }
                })
                .catch(console.error);

            resetForm();
        } else {
            fetch(URL_BACKEND + '/locations', {
                method: 'POST',
                headers: {
                    'Authorization': authHeader(),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.ok) {
                        fetchLocations();
                        resetForm();
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
    });

    function editLocation(location) {
        document.getElementById("x").value = location.x;
        document.getElementById("y").value = location.y;
        document.getElementById("z").value = location.z;

        isEditing = true;
        currentEditId = location.id;
    }

    function resetForm() {
        addLocationForm.reset();
        isEditing = false;
        currentEditId = null;
    }

    window.deleteLocation = function (id) {
        fetch(`${URL_BACKEND}/locations/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.status === 204) {
                    fetchLocations();
                } else {
                    response.json().then(response => {
                        alert(response.message)
                    });
                }
            })
            .catch(console.error);
    };
});
