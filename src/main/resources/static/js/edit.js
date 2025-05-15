// public/js/edit.js
const frm = document.getElementById('frm');
const title = document.getElementById('title');
const content = document.getElementById('content');
const writer = document.getElementById('writer');

const errTitle = document.getElementById('errTitle');
const errContent = document.getElementById('errContent');
const errWriter = document.getElementById('errWriter');

document.getElementById('btnUpdate').addEventListener('click', e => {
  e.preventDefault();

  if (title.value.trim() === '') {
    errTitle.textContent = '제목을 입력하세요.';
    title.focus();
    return;
  } else errTitle.textContent = '';

  if (content.value.trim() === '') {
    errContent.textContent = '내용을 입력하세요.';
    content.focus();
    return;
  } else errContent.textContent = '';

  if (writer.value.trim() === '') {
    errWriter.textContent = '작성자를 입력하세요.';
    writer.focus();
    return;
  } else errWriter.textContent = '';

  frm.submit();
});

document.getElementById('btnList').addEventListener('click', () => {
  location.href = '/board/list';
});
