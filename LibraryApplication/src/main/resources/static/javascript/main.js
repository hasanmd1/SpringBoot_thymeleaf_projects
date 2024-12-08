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
