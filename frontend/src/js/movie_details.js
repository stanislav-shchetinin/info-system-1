import { URL_BACKEND, authHeader } from "./main.js";

document.addEventListener("DOMContentLoaded", function () {
    const movieId = new URLSearchParams(window.location.search).get("id");
    loadMovieDetails(movieId);
});

const loadMovieDetails = (id) => {
    fetch(`${URL_BACKEND}/movies/${id}`, {
        headers: {
            "Authorization": authHeader()
        }
    })
        .then(response => response.json())
        .then(movie => displayMovieDetails(movie))
        .catch(error => {
            console.error("Error loading movie details:", error);
            alert("Failed to load movie details.");
        });
};

const displayMovieDetails = (movie) => {
    document.querySelector("#movie-id").textContent = movie.id;
    document.querySelector("#movie-name").value = movie.name;
    document.querySelector("#movie-genre").value = movie.genre;
    document.querySelector("#movie-box-office").value = movie.totalBoxOffice;
    document.querySelector("#movie-oscars-count").value = movie.oscarsCount;

    document.querySelector("#update-movie").addEventListener("click", () => updateMovie(movie.id));
};

const updateMovie = (id) => {
    const updatedMovie = {
        name: document.querySelector("#movie-name").value,
        genre: document.querySelector("#movie-genre").value,
        totalBoxOffice: parseFloat(document.querySelector("#movie-box-office").value),
        oscarsCount: parseInt(document.querySelector("#movie-oscars-count").value),
    };

    fetch(`${URL_BACKEND}/movies?id=${id}`, {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json",
            "Authorization": authHeader()
        },
        body: JSON.stringify(updatedMovie)
    })
        .then(response => {
            if (response.ok) {
                alert("Movie updated successfully.");
                window.location.href = "movies.html";
            } else {
                response.json().then(data => alert(data.message || "Update failed"));
            }
        })
        .catch(error => {
            console.error("Error updating movie:", error);
            alert("Failed to update movie.");
        });
};
