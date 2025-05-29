document.addEventListener('DOMContentLoaded', () => {
  const $form = document.getElementById('frm');
  const $btnAdd = document.getElementById('btnAdd');
  const $btnEdit = document.getElementById('btnEdit');
  const $btnDel = document.getElementById('btnDel');
  const $checkAll = document.getElementById('checkAll');

  const $checkboxes = () => document.querySelectorAll('input[name="ids"]');
  const $checkedItems = () => document.querySelectorAll('input[name="ids"]:checked');

  // 전체 선택 체크박스
  $checkAll?.addEventListener('change', (e) => {
    $checkboxes().forEach(cb => cb.checked = e.target.checked);
  });

  // 개별 체크 시 전체 선택 상태 갱신
  document.querySelectorAll('input[name="ids"]').forEach(cb => {
    cb.addEventListener('change', () => {
      $checkAll.checked = $checkboxes().length === $checkedItems().length;
    });
  });

  // 등록 버튼
  $btnAdd?.addEventListener('click', () => {
    window.location.href = '/board/create';
  });

  // 수정 버튼
  $btnEdit?.addEventListener('click', () => {
    const checked = $checkedItems();
    if (checked.length === 0) {
      alert('수정할 게시글을 선택해주세요.');
      return;
    }
    if (checked.length > 1) {
      alert('수정은 하나의 게시글만 가능합니다.');
      return;
    }
    const id = checked[0].value;
    window.location.href = `/board/edit/${id}`;
  });

  // 삭제 버튼
  $btnDel?.addEventListener('click', () => {
    const checked = $checkedItems();
    if (checked.length === 0) {
      alert('삭제할 게시글을 선택해주세요.');
      return;
    }
    if (confirm('선택한 게시글을 삭제하시겠습니까?')) {
      $form.submit();
    }
  });
});
