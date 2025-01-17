const API_URL = 'http://localhost:8080/api/ownes';

document.addEventListener('DOMContentLoaded', () => {
  fetchAllDisplay();
});

function fetchAllDisplay() {
  fetch(API_URL)
    .then((response) => response.json())
    .then((params) => {
      displayRow(params);
    })
    .catch((error) => console.error('Error fetching :', error));
}

function fetchSearch() {
  const keyword = document.getElementById('searchkeyword').value.trim();
  if (!keyword) {
    fetchAllDisplay();
    return;
  }
  fetch(`${API_URL}/search?keyword=${keyword}`)
    .then((response) => response.json())
    .then((params) => {
      displayRow(params);
    })
    .catch((error) => console.error('Error fetching :', error));
}

function displayRow(params) {
  const tableBody = document.querySelector('#tableId tbody');

  tableBody.innerHTML = '';

  params.forEach((element) => {
    const row = document.createElement('tr');
    row.innerHTML = `<td style="border: 1px solid black">${element.id}</td>
    <td style="border: 1px solid black">${element.title}</td>
    <td style="border: 1px solid black">${element.author}</td>
    <td style="border: 1px solid black">${element.publishedDate}</td>
    <td style="border: 1px solid black">${element.price}</td>
    <td style="border: 1px solid black">${element.quantity}</td>
    <td style="border: 1px solid black">
        <a  href="edit.html?id=${element.id}">Cap nhat</a>
        <button onclick="deleteRow(${element.id})">Xoa</button>
    </td>
    `;

    tableBody.appendChild(row);
  });
}

function deleteRow(id) {
  if (confirm('Ban co muon xoa khong?')) {
    fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
    })
      .then(() => {
        window.location.href = 'index.html?success=delete';
      })
      .catch((error) => console.error('Error delete :', error));
  }
}