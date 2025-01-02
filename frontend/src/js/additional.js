import { authHeader, displayUserDetails, HOME, redirectToLogin, URL_BACKEND, URL_FRONTEND } from "./main.js";

document.getElementById('home-button').addEventListener("click", () => {
    window.location.replace(URL_FRONTEND + HOME);
});

document.addEventListener("DOMContentLoaded", () => {

    fetchAverageGoldenPalmCount();
    fetchDirectorsWithoutOscars();
    fetchGroupByNameCount();

    function fetchDirectorsWithoutOscars() {
        fetch(URL_BACKEND + '/movies/directors-no-oscars', {
            method: 'GET',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    const directorsTable = document.querySelector('#directors-without-oscars tbody');
                    directorsTable.innerHTML = '';
                    response.json().then(directors => {
                        directors.forEach(director => {
                            const row = directorsTable.insertRow();
                            row.innerHTML = `<td>${director}</td>`;
                        });
                    });
                } else if (response.status === 401) {
                    redirectToLogin();
                } else {
                    response.json().then(j => alert(j.message));
                }
            }).catch(console.error);
    }

    function fetchAverageGoldenPalmCount() {
        fetch(URL_BACKEND + '/movies/average-golden-palm', {
            method: 'GET',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    response.json().then(data => {
                        const node = document.getElementById("average-value");
                        console.log(data);
                        node.innerHTML = `<p>Average goldenPalmCount: ${data}</p>`;
                    });
                } else if (response.status === 401) {
                    redirectToLogin();
                } else {
                    response.json().then(j => alert(j.message));
                }
            }).catch(console.error);
    }

    function fetchGroupByNameCount() {
        fetch(URL_BACKEND + '/movies/group-by-name', {
            method: 'GET',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    response.json().then(groups => {
                        const table = document.getElementById("group-by-name");
                        table.innerHTML = `
                            <p>Count elements by name:</p>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Count</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    ${groups.map(([name, count]) => `<tr><td>${name}</td><td>${count}</td></tr>`).join("")}
                                </tbody>
                            </table>
                        `;
                    });
                } else if (response.status === 401) {
                    redirectToLogin();
                } else {
                    response.json().then(j => alert(j.message));
                }
            }).catch(console.error);
    }

    async function redistributeOscars(event) {
        event.preventDefault();
        const fromGenre = document.getElementById("from-genre").value;
        const toGenre = document.getElementById("to-genre").value;

        console.log(fromGenre);

        fetch(`${URL_BACKEND}/movies/redistribute-oscars?fromGenre=${encodeURIComponent(fromGenre)}&toGenre=${encodeURIComponent(toGenre)}`, {
            method: 'POST',
            headers: {
                'Authorization': authHeader(),
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert("Oscars redistributed successfully!");
                } else if (response.status === 401) {
                    redirectToLogin();
                } else {
                    response.json().then(j => alert(j.message));
                }
            }).catch(console.error);
    }

    // Функция для отправки запроса и отображения результата
    async function handleGenreFormSubmit(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение формы

        const genreInput = document.getElementById('genre-input').value;
        const outputElement = document.getElementById('genre-count-value');

        try {
            const response = await fetch(`${URL_BACKEND}/movies/count-by-genre/?genre=${encodeURIComponent(genreInput)}`);

            if (!response.ok) {
                throw new Error(`Ошибка: ${response.statusText}`);
            }

            const count = await response.json(); // Предполагается, что API возвращает число
            outputElement.textContent = count; // Обновляем значение в DOM
        } catch (error) {
            console.error('Не удалось получить данные:', error);
            outputElement.textContent = 'Ошибка загрузки';
        }
    }

    // Привязка обработчика к форме
    const form = document.getElementById('count-by-genre-form');
    form.addEventListener('submit', handleGenreFormSubmit);

    const formRestr = document.getElementById('redistribute-oscars-form');
    formRestr.addEventListener('submit', redistributeOscars);
});
