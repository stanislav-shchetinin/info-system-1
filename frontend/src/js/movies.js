import {authHeader, displayUserDetails, getUserRole, HOME, redirectToLogin, URL_BACKEND, URL_FRONTEND} from "./main.js";

let currentPage = 1;
const pageSize = 10;
let filterValue = "";
let sortColumn = "id";
let editingMovieId = null;
let filterColumn = null;

const genres = ["HORROR", "FANTASY", "SCIENCE_FICTION"];
const mpaa = ["NC_17", "R", "PG", "PG_13"];
const pages = ["coordinates", "person", "location", "login", "additional"];
const coordinatesSelect = document.getElementById("coordinates");
const directorSelect = document.getElementById("director");
const operatorSelect = document.getElementById("operator");
const screenwriterSelect = document.getElementById("screenwriter");

document.addEventListener("DOMContentLoaded", () => {
    displayUserDetails();

    if (getUserRole() === 'ADMIN') {
        pages.push("application");
        document.getElementById("submitApplication").hidden = true;
    }

    initEnum(pages, document.getElementById("page"));
    initEnum(mpaa, document.getElementById("mpaaRating"));
    initEnum(genres, document.getElementById("genre"));

    document.getElementById("movie-form").addEventListener("submit", handleMovieSubmit);
    document.getElementById("prev-page").addEventListener("click", () => changePage(-1));
    document.getElementById("next-page").addEventListener("click", () => changePage(1));
    document.getElementById("submitApplication").addEventListener("click", (e) => {
        e.preventDefault();
        submitApplication();
    })
    document.getElementById("navigation").addEventListener("submit", (e) => {
        e.preventDefault();
        window.location.replace(URL_FRONTEND + "/" + document.getElementById("page").value);
    })
    document.getElementById("filter-form").addEventListener("submit", (e) => {
        e.preventDefault();
        applyFilter();
    })

    document.querySelectorAll('.click-tag').forEach(header => {
        header.addEventListener('click', () => {
            const sortField = header.getAttribute('data-sort');
            sortTable(sortField);
        });
    });

    fetchMovies();
    fetchCoordinates();
    fetchPersons();

    // setInterval(fetchMovies, 5000);
    // setInterval(fetchCoordinates, 5000);
    // setInterval(fetchPersons, 5000);
});

function fetchMovies() {
    const url = `${URL_BACKEND}/movies/all?page=${currentPage}&size=${pageSize}&sorted=${sortColumn}&filter-value=${filterValue}&filter-column=${filterColumn}`;
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': authHeader(),
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            
            renderMovies(data);
            updatePagination(data.totalPages);
        })
        .catch(console.error);
}

function submitApplication() {
    fetch(URL_BACKEND + '/admin', {
        method: 'POST',
        headers: {
            'Authorization': authHeader(),
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.status === 201) {
                alert("Submit successfully, please wait an answer from administration");
                document.getElementById("submitApplication").hidden = true;
                redirectToLogin();
            } else if (response.status === 401) {
                redirectToLogin();
            } else {
                response.json().then(j => alert(j.message));
            }
        }).catch(console.error);
}

function renderMovies(movies) {
    const moviesTable = document.getElementById("movies-table").getElementsByTagName("tbody")[0];
    moviesTable.innerHTML = "";
    if (movies == null) return;
    movies.forEach(movie => {
        const row = moviesTable.insertRow();
        row.innerHTML = `
            <td>${movie.id}</td>
            <td>${movie.name}</td>
            <td>${movie.coordinates.id}</td>
            <td>${movie.oscarsCount}</td>
            <td>${movie.budget}</td>
            <td>${movie.totalBoxOffice}</td>
            <td>${movie.mpaaRating}</td>
            <td>${movie.director?.id}</td>
            <td>${movie.screenwriter?.id}</td>
            <td>${movie.operator?.id}</td>
            <td>${movie.length}</td>
            <td>${movie.goldenPalmCount}</td>
            <td>${movie.tagline}</td>
            <td>${movie.genre}</td>
            <td>
                <button onclick="deleteMovie(${movie.id})">Delete</button>
            </td>
        `;
        row.addEventListener('click', () => editMovie(movie))
    });
}

function handleMovieSubmit(event) {
    event.preventDefault();

    const data = {
        name: document.getElementById("name").value,
        coordinates: parseInt(coordinatesSelect.value),
        oscarsCount: parseInt(document.getElementById("oscarsCount").value),
        budget: parseFloat(document.getElementById("budget").value),
        totalBoxOffice: parseFloat(document.getElementById("totalBoxOffice").value),
        mpaaRating: document.getElementById("mpaaRating").value,
        director: parseInt(directorSelect.value),
        screenwriter: parseInt(screenwriterSelect.value),
        operator: parseInt(operatorSelect.value),
        length: parseInt(document.getElementById("length").value),
        goldenPalmCount: parseInt(document.getElementById("goldenPalmCount").value),
        tagline: document.getElementById("tagline").value,
        genre: document.getElementById("genre").value
    };

    if (editingMovieId) {
        updateMovie(editingMovieId, data);
    } else {
        createMovie(data);
    }
}


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
                coordinatesSelect.innerHTML = '<option value="">Select Coordinates</option>';
                response.json().then(coordinates => {
                    coordinates.forEach(coordinate => {
                        const option = document.createElement("option");
                        option.value = coordinate.id;
                        option.textContent = `(X: ${coordinate.x}, Y: ${coordinate.y})`;
                        coordinatesSelect.appendChild(option);
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

function createOption(person) {
    const option = document.createElement("option");

    option.value = person.id;
    option.textContent = `(Name: ${person.name})`;
    return option;
}

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
                const phrase = '<option value="">Select Person</option>';
                directorSelect.innerHTML = phrase;
                operatorSelect.innerHTML = phrase;
                screenwriterSelect.innerHTML = phrase;

                response.json().then(persons => {
                    persons.forEach(person => {
                        directorSelect.appendChild(createOption(person));
                        screenwriterSelect.appendChild(createOption(person));
                        operatorSelect.appendChild(createOption(person));
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

function createMovie(data) {
    fetch(`${URL_BACKEND}/movies`, {
        method: 'POST',
        headers: {
            'Authorization': authHeader(),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (response.status === 201) {
                response.json().then(() => {
                    fetchMovies();
                })
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

function editMovie(movie) {
    document.getElementById("name").value = movie.name;
    coordinatesSelect.value = movie.coordinates.id;
    document.getElementById("oscarsCount").value = movie.oscarsCount;
    document.getElementById("budget").value = movie.budget;
    document.getElementById("totalBoxOffice").value = movie.totalBoxOffice;
    document.getElementById("mpaaRating").value = movie.mpaaRating;
    directorSelect.value = movie.director?.id;
    screenwriterSelect.value = movie.screenwriter?.id;
    operatorSelect.value = movie.operator?.id;
    document.getElementById("length").value = movie.length;
    document.getElementById("goldenPalmCount").value = movie.goldenPalmCount;
    document.getElementById("tagline").value = movie.tagline;
    document.getElementById("genre").value = movie.genre;

    editingMovieId = movie.id;
}

function updateMovie(movieId, data) {
    fetch(`${URL_BACKEND}/movies/${movieId}`, {
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
                response.json().then(() => {
                    fetchMovies();
                    resetForm();
                })
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

window.deleteMovie = function deleteMovie(movieId) {
    fetch(`${URL_BACKEND}/movies/${movieId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': authHeader(),
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.status === 204) {
                fetchMovies();
            } else if (response.status === 401) {
                redirectToLogin();
            } else if (response.status === 400) {
                response.json().then(j => alert(j.details));
            } else {
                response.json().then(j => alert(j.message));
            }
        })
}

function applyFilter() {
    filterValue = document.getElementById("filter-value").value;
    filterColumn = document.getElementById("filter-column").value;

    fetchMovies();
}

function sortTable(column) {
    sortColumn = column;
    fetchMovies();
}

function changePage(offset) {
    currentPage += offset;
    fetchMovies();
}

function updatePagination(totalPages) {
    document.getElementById("current-page").textContent = currentPage;
    document.getElementById("prev-page").disabled = currentPage <= 1;
    document.getElementById("next-page").disabled = currentPage >= totalPages;
}

function resetForm() {
    document.getElementById("movie-form").reset();
    editingMovieId = null;
    document.getElementById("form-title").textContent = "Create New Movie";
}

function initEnum(values, element) {
    values.forEach(value => {
        const option = document.createElement("option");
        option.value = value;
        option.textContent = value;
        element.appendChild(option);
    });
}


