import {authHeader, displayUserDetails, HOME, redirectToLogin, URL_BACKEND, URL_FRONTEND} from "./main.js";

document.addEventListener("DOMContentLoaded", () => {
    const personTable = document.getElementById("person-table").getElementsByTagName("tbody")[0];
    const addPersonForm = document.getElementById("add-person-form");
    const locationSelect = document.getElementById("location");
    const eyeColorSelect = document.getElementById("eyeColor");
    const hairColorSelect = document.getElementById("hairColor");
    const nationalitySelect = document.getElementById("nationality");

    let isEditing = false;
    let currentEditId = null;

    displayUserDetails();

    fetchPersons();
    fetchLocations();
    setInterval(fetchPersons, 5000);
    setInterval(fetchLocations, 5000);

    const eyeColors = ["GREEN", "YELLOW", "ORANGE", "BLUE"];
    const hairColors = ["GREEN", "YELLOW", "ORANGE", "BLUE"];
    const nationalities = ["UNITED_KINGDOM", "NORTH_KOREA", "JAPAN"];

    eyeColors.forEach(color => {
        const option = document.createElement("option");
        option.value = color;
        option.textContent = color;
        eyeColorSelect.appendChild(option);
    });

    hairColors.forEach(color => {
        const option = document.createElement("option");
        option.value = color;
        option.textContent = color;
        hairColorSelect.appendChild(option);
    });

    nationalities.forEach(country => {
        const option = document.createElement("option");
        option.value = country;
        option.textContent = country;
        nationalitySelect.appendChild(option);
    });

    function fetchPersons() {
        fetch(URL_BACKEND + '/persons/all', {
            method: 'GET',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    personTable.innerHTML = '';
                    response.json().then(persons => {
                        persons.forEach(person => {
                            const row = personTable.insertRow();
                            row.innerHTML = `
                                <td>${person.id}</td>
                                <td>${person.name}</td>
                                <td>${person.eyeColor}</td>
                                <td>${person.hairColor}</td>
                                <td>${person.location?.id}</td>
                                <td>${processDateTime(person.birthday)}</td>
                                <td>${person.nationality}</td>
                                <td>
                                    <button onclick="deletePerson(${person.id})">Delete</button>
                                </td>
                            `;
                            row.addEventListener('click', () => editPerson(person))
                        });
                    });
                } else if (response.status === 401) {
                    redirectToLogin();
                } else {
                    response.json().then(j => alert(j.message));
                }
            }).catch(console.error);
    }

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
                    if (locationSelect.innerHTML !== '') return;
                    locationSelect.innerHTML = '<option value="">Select Location</option>';
                    response.json().then(locations => {
                        locations.forEach(location => {
                            const option = document.createElement("option");
                            option.value = location.id;
                            option.textContent = `(X: ${location.x}, Y: ${location.y}, Z: ${location.z})`;
                            locationSelect.appendChild(option);
                        });
                    });
                } else if (response.status === 401) {
                    redirectToLogin();
                } else {
                    response.json().then(j => alert(j.message));
                }
            })
            .catch(console.error);
    }

    document.getElementById('home-button').addEventListener("click", (e) => {
        window.location.replace(URL_FRONTEND + HOME)
    })

    addPersonForm.addEventListener("submit", (e) => {
        e.preventDefault();
        const data = {
            name: document.getElementById("name").value,
            eyeColor: document.getElementById("eyeColor").value,
            hairColor: document.getElementById("hairColor").value,
            location: parseInt(locationSelect.value),
            birthday: new Date(document.getElementById("birthday").value).toISOString(),
            nationality: document.getElementById("nationality").value
        };

        if (isEditing && currentEditId !== null) {
            console.log(data)
            fetch(`${URL_BACKEND}/persons/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': authHeader(),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    console.log(data)
                    if (response.ok) {
                        fetchPersons();
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
            fetch(URL_BACKEND + '/persons', {
                method: 'POST',
                headers: {
                    'Authorization': authHeader(),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (response.status === 201) {
                        fetchPersons();
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

    window.deletePerson = function (personId) {
        fetch(`${URL_BACKEND}/persons/${personId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                console.log(response);
                if (response.status === 204) {
                    fetchPersons();
                } else {
                    response.json().then(response => {
                        alert(response.message)
                    });
                }
            })
            .catch(console.error);
    };

    function editPerson(person) {
        document.getElementById("name").value = person.name;
        document.getElementById("eyeColor").value = person.eyeColor;
        document.getElementById("hairColor").value = person.hairColor;
        document.getElementById("birthday").value = processDateTime(person.birthday);
        document.getElementById("nationality").value = person.nationality;
        locationSelect.value = person.locationId;

        isEditing = true;
        currentEditId = person.id;
    }

    function resetForm() {
        addPersonForm.reset();

        isEditing = false;
        currentEditId = null;
    }

    function processDateTime(dateTime) {
        const [datePart] = dateTime.split(" ");

        const [day, month, year] = datePart.split("-");

        return `${year}-${month}-${day}`;
    }
});
