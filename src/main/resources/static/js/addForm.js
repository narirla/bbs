
document.addEventListener('DOMContentLoaded', () => {

  // 🔹 등록 버튼
  const $btnAdd = document.getElementById('btnAdd');
  const $title = document.getElementById('title');
  const $writer = document.getElementById('writer');
  const $content = document.getElementById('content');

  $btnAdd.addEventListener('click', (e) => {
    // 클라이언트 측 유효성 체크
    let isValid = true;

    // 제목 검사
    if (!$title.value.trim()) {
      document.getElementById('errTitle').textContent = '제목을 입력하세요.';
      isValid = false;
    } else {
      document.getElementById('errTitle').textContent = '';
    }

    // 작성자 검사
    if (!$writer.value.trim()) {
      document.getElementById('errWriter').textContent = '작성자를 입력하세요.';
      isValid = false;
    } else {
      document.getElementById('errWriter').textContent = '';
    }

    // 내용 검사
    if (!$content.value.trim()) {
      document.getElementById('errContent').textContent = '내용을 입력하세요.';
      isValid = false;
    } else {
      document.getElementById('errContent').textContent = '';
    }

    // 유효하지 않으면 submit 막기
    if (!isValid) {
      e.preventDefault();
    }
  });

  // 🔹 목록 버튼 (폼 내부에 있는 경우 폼 제출 막기 위해 JS로 처리)
  const $btnList = document.getElementById('btnList');
  $btnList.addEventListener('click', () => {
    window.location.href = '/board';
  });

});
