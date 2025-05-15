document.addEventListener('DOMContentLoaded', () => {
  // 수정 버튼
document.getElementById('btnEdit').addEventListener('click', () => {
  const id = document.getElementById('boardId').value;
  location.href = `/board/edit/${id}`;
});

  // 삭제 버튼
  document.getElementById('btnDelete')?.addEventListener('click', () => {
    const id = document.getElementById('boardId').value;
    const $modalDel = document.getElementById("modalDel");
    const $btnYes = document.querySelector(".btnYes");
    const $btnNo = document.querySelector(".btnNo");

    $modalDel.showModal();

    $modalDel.addEventListener('close', () => {
      if ($modalDel.returnValue === 'yes') {
        location.href = `/board/delete/${id}`;
      }
    }, { once: true });

    $btnYes.addEventListener("click", () => {
      $modalDel.close("yes");
    }, { once: true });

    $btnNo.addEventListener("click", () => {
      $modalDel.close("no");
    }, { once: true });
  });

  // 목록 버튼
  document.getElementById('btnList')?.addEventListener('click', () => {
    location.href = '/board';
  });
});
