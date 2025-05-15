document.addEventListener('DOMContentLoaded', () => {
  const $btnDel = document.getElementById('btnDel');
  const $btnEdit = document.getElementById('btnEdit');
  const $btnAdd = document.getElementById('btnAdd');

  // 삭제
  $btnDel?.addEventListener('click', () => {
    const checked = document.querySelectorAll('input[name="ids"]:checked');
    if (checked.length === 0) {
      alert('삭제할 게시글을 선택하세요.');
      return;
    }
    if (confirm('선택한 게시글을 삭제하시겠습니까?')) {
      document.getElementById('frm').submit();
    }
  });

  // 수정
  $btnEdit?.addEventListener('click', () => {
    const checked = document.querySelectorAll('input[name="ids"]:checked');
    if (checked.length === 0) {
      alert('수정할 게시글을 선택하세요.');
      return;
    }
    if (checked.length > 1) {
      alert('게시글은 하나만 선택할 수 있습니다.');
      return;
    }
    const id = checked[0].value;
    window.location.href = `/board/edit/${id}`;
  });

  // 등록
  $btnAdd?.addEventListener('click', () => {
    window.location.href = '/board/create';
  });
});
