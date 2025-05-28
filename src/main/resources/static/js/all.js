/*all.js*/
document.addEventListener('DOMContentLoaded', () => {
  const $btnDel = document.getElementById('btnDel');
  const $btnEdit = document.getElementById('btnEdit');
  const $btnAdd = document.getElementById('btnAdd');
  const $form = document.getElementById('frm');
  const $checkAll = document.getElementById('checkAll');

  // 🔁 전체 선택 체크박스 이벤트
  $checkAll?.addEventListener('change', (e) => {
    const isChecked = e.target.checked;
    const checkboxes = document.querySelectorAll('input[name="ids"]');
    checkboxes.forEach(cb => cb.checked = isChecked);
  });

  // ✅ 개별 체크박스 변경 시 전체 선택 체크 여부 갱신
  const updateCheckAllStatus = () => {
    const checkboxes = document.querySelectorAll('input[name="ids"]');
    const checked = document.querySelectorAll('input[name="ids"]:checked');
    if ($checkAll) {
      $checkAll.checked = checkboxes.length === checked.length;
    }
  };

  // 개별 체크박스에 이벤트 추가
  document.querySelectorAll('input[name="ids"]').forEach(cb => {
    cb.addEventListener('change', updateCheckAllStatus);
  });

  // 삭제
  $btnDel?.addEventListener('click', () => {
    const $checkedItems = document.querySelectorAll('input[name="ids"]:checked');
    if ($checkedItems.length === 0) {
      alert('삭제할 게시글을 선택하세요.');
      return;
    }
    if (confirm('선택한 게시글을 삭제하시겠습니까?')) {
      $form.submit();
    }
  });

  // 수정
  $btnEdit?.addEventListener('click', () => {
    const $checkedItems = document.querySelectorAll('input[name="ids"]:checked');
    if ($checkedItems.length === 0) {
      alert('수정할 게시글을 선택하세요.');
      return;
    }
    if ($checkedItems.length > 1) {
      alert('게시글은 하나만 선택할 수 있습니다.');
      return;
    }
    const id = $checkedItems[0].value;
    window.location.href = `/board/edit/${id}`;
  });

  // 등록
  $btnAdd?.addEventListener('click', () => {
    window.location.href = '/board/create';
  });
});


