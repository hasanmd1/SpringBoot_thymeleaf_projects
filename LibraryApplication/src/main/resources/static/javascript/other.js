// Function to add selected item to the list
function addItem(selectId, containerId, inputName) {
    const select = document.getElementById(selectId);
    const container = document.getElementById(containerId);
    const selectValue = select.value;
    const selectedText = select.options[select.selectedIndex].text;

    if (!selectValue || container.querySelector(`input[value="${selectValue}"]`)) {
        return;
    }
    const item = document.createElement('div');
    item.classList.add('item');

    item.innerHTML = `
        <span>${selectedText}</span>
        <input type="hidden" name="${inputName}" value="${selectValue}">
        <button type="button" class="remove-btn" onclick="this.parentElement.remove()">X</button>
    `;

    container.appendChild(item);
}

// Function to pre-populate the list with existing genre or author IDs
function prePopulate(existingIds, containerId, inputName, getTextCallback) {
    const container = document.getElementById(containerId);
    existingIds.forEach(id => {
        // Fetch the item text based on the ID (you could store this in your page if needed)
        const selectedText = getTextCallback(id);  // Callback to get the text for the existing ID
        if (selectedText) {
            const item = document.createElement('div');
            item.classList.add('item');

            item.innerHTML = `
                <span>${selectedText}</span>
                <input type="hidden" name="${inputName}" value="${id}">
                <button type="button" class="remove-btn" onclick="this.parentElement.remove()">X</button>
            `;

            container.appendChild(item);
        }
    });
}

// Dummy functions to simulate getting the text for genres/authors
function getGenreText(genreId) {
    const genres = window.genres || []; // Assuming genres are stored in a global object
    const genre = genres.find(g => g.id === genreId);
    return genre ? genre.name : '';
}

function getAuthorText(authorId) {
    const authors = window.authors || []; // Assuming authors are stored in a global object
    const author = authors.find(a => a.id === authorId);
    return author ? author.firstName + ' ' + author.lastName : '';
}

window.onload = function() {
    const existingGenreIds = window.existingGenreIds || [];  // Pass your existingGenreIds to the window object
    prePopulate(existingGenreIds, 'genreList', 'genreIds', getGenreText);

    // Example usage for authors
    const existingAuthorIds = window.existingAuthorIds || [];  // Pass your existingAuthorIds to the window object
    prePopulate(existingAuthorIds, 'authorList', 'bookAuthorIds', getAuthorText);
}
