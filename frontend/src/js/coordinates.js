import {authHeader, displayUserDetails, HOME, redirectToLogin, URL_BACKEND, URL_FRONTEND} from "./main.js";

document.addEventListener("DOMContentLoaded", () => {
    const coordinatesTable = document.getElementById("coordinates-table").getElementsByTagName("tbody")[0];
    const addCoordinateForm = document.getElementById("add-coordinate-form");
    let isEditing = false;
    let currentEditId = null;

    displayUserDetails();

    fetchCoordinates();
    setInterval(fetchCoordinates, 5000);

    function fetchCoordinates() {
        fetch(URL_BACKEND + '/coordinates/all', {
            method: 'GET',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    coordinatesTable.innerHTML = '';
                    response.json().then(coordinates => {
                        coordinates.forEach(coordinate => {
                            const row = coordinatesTable.insertRow();
                            row.innerHTML = `
                                <td>${coordinate.id}</td>
                                <td>${coordinate.x}</td>
                                <td>${coordinate.y}</td>
                                <td>
                                    <button onclick="deleteCoordinates(${coordinate.id})">Delete</button>
                                </td>
                            `;
                            row.addEventListener("click", () => editCoordinate(coordinate));
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

    window.deleteCoordinates = function (id) {
        fetch(`${URL_BACKEND}/coordinates/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.status === 204) {
                    fetchCoordinates();
                } else {
                    response.json().then(response => {
                        alert(response.message)
                    });
                }
            })
            .catch(console.error);
    };

    addCoordinateForm.addEventListener("submit", (e) => {
        e.preventDefault();
        const data = {
            x: parseFloat(document.getElementById("x").value),
            y: parseInt(document.getElementById("y").value)
        };

        if (isEditing && currentEditId !== null) {
            fetch(`${URL_BACKEND}/coordinates/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': authHeader(),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.ok) {
                        fetchCoordinates();
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
            fetch(URL_BACKEND + '/coordinates', {
                method: 'POST',
                headers: {
                    'Authorization': authHeader(),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.ok) {
                        fetchCoordinates();
                        resetForm();
                    } else if (response.status === 400) {
                        response.json().then(j => alert(j.details));
                    } else {
                        response.json().then(j => alert(j.message));
                    }
                })
                .catch(console.error);
        }
    });

    document.getElementById('home-button').addEventListener("click", (e) => {
        window.location.replace(URL_FRONTEND + HOME)
    })

    function editCoordinate(coordinate) {
        document.getElementById("x").value = coordinate.x;
        document.getElementById("y").value = coordinate.y;

        isEditing = true;
        currentEditId = coordinate.id;
    }

    function resetForm() {
        addCoordinateForm.reset();
        isEditing = false;
        currentEditId = null;
    }
});
