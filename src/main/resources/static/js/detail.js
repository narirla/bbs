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

  // 댓글 로딩 (기본 1페이지)
  const boardId = document.getElementById('boardId').value;
  loadComments(boardId, 1);
});

// 댓글 목록 가져오기
function loadComments(boardId, page = 1) {
  fetch(`/api/comments/${boardId}?page=${page}`)
    .then(res => res.json())
    .then(data => {
      renderComments(data.comments);
      renderPagination(data.totalPages, data.currentPage, boardId);
    })
    .catch(err => {
      console.error('댓글 조회 오류:', err);
    });
}

// ✅ 댓글 목록 렌더링 (스타일 적용)
function renderComments(comments) {
  const container = document.getElementById('commentList');
  container.innerHTML = '';
  comments.forEach(comment => {
    const div = document.createElement('div');
    div.classList.add('comment-item');
    div.innerHTML = `
      <p><strong>${comment.nickname}</strong> | <small>${comment.createdAt}</small></p>
      <p>${comment.content}</p>
    `;
    container.appendChild(div);
  });
}

// ✅ 댓글 페이징 렌더링 (5페이지 단위)
function renderPagination(totalPages, currentPage, boardId) {
  const pagination = document.getElementById('commentPagination');
  pagination.innerHTML = '';

  const pageGroupSize = 5;
  const currentGroup = Math.ceil(currentPage / pageGroupSize);
  const startPage = (currentGroup - 1) * pageGroupSize + 1;
  const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

  // ◀ 이전 그룹
  if (startPage > 1) {
    const prevBtn = document.createElement('button');
    prevBtn.textContent = '◀';
    prevBtn.addEventListener('click', () => {
      loadComments(boardId, startPage - 1);
    });
    pagination.appendChild(prevBtn);
  }

  // 페이지 번호들
  for (let i = startPage; i <= endPage; i++) {
    const btn = document.createElement('button');
    btn.textContent = i;
    btn.disabled = i === currentPage;
    btn.addEventListener('click', () => loadComments(boardId, i));
    pagination.appendChild(btn);
  }

  // ▶ 다음 그룹
  if (endPage < totalPages) {
    const nextBtn = document.createElement('button');
    nextBtn.textContent = '▶';
    nextBtn.addEventListener('click', () => {
      loadComments(boardId, endPage + 1);
    });
    pagination.appendChild(nextBtn);
  }
}
