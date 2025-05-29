/*comment.js*/
document.addEventListener('DOMContentLoaded', () => {
  const boardId = document.getElementById('cmtBoardId').value;
  const $form = document.getElementById('commentForm');
  const $content = document.getElementById('commentContent');
  const $list = document.getElementById('commentList');
  const $pagination = document.getElementById('commentPagination');

  let currentPage = 1;

  // 댓글 목록 가져오기
  async function loadComments(page = 1) {
    try {
      const res = await fetch(`/api/comments/board/${boardId}/pages?page=${page}`);
      if (!res.ok) throw new Error('댓글 조회 실패');

      const { comments, totalPages } = await res.json();
      currentPage = page;
      $list.innerHTML = '';
      $pagination.innerHTML = '';

      if (comments.length === 0) {
        $list.innerHTML = '<p>댓글이 없습니다.</p>';
        return;
      }

      comments.forEach(comment => {
        const div = document.createElement('div');
        div.classList.add('comment-item');

        const p = document.createElement('p');
        p.innerHTML = `<strong>${comment.nickname}</strong>: ${comment.content}`;

        const small = document.createElement('small');
        small.innerText = comment.createdAt?.replace('T', ' ').substring(0, 16) || '';

        div.appendChild(p);
        div.appendChild(small);

        if (comment.mine) {
          const editBtn = document.createElement('button');
          editBtn.innerText = '수정';
          editBtn.classList.add('btn-edit');
          editBtn.addEventListener('click', () => editComment(comment.id, comment.content));
          div.appendChild(editBtn);

          const delBtn = document.createElement('button');
          delBtn.innerText = '삭제';
          delBtn.classList.add('btn-delete');
          delBtn.addEventListener('click', () => deleteComment(comment.id));
          div.appendChild(delBtn);
        }

        $list.appendChild(div);
      });

      // ✅ 5페이지 단위 페이징 + 처음/끝 버튼 포함
      const pageGroupSize = 5;
      const currentGroup = Math.ceil(page / pageGroupSize);
      const startPage = (currentGroup - 1) * pageGroupSize + 1;
      const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

      // ≪ 처음
      if (page > 1) {
        const firstBtn = document.createElement('button');
        firstBtn.innerText = '≪';
        firstBtn.addEventListener('click', () => loadComments(1));
        $pagination.appendChild(firstBtn);
      }

      // ◀ 이전 그룹
      if (startPage > 1) {
        const prevBtn = document.createElement('button');
        prevBtn.innerText = '◀';
        prevBtn.addEventListener('click', () => loadComments(startPage - 1));
        $pagination.appendChild(prevBtn);
      }

      // 페이지 번호들
      for (let i = startPage; i <= endPage; i++) {
        const btn = document.createElement('button');
        btn.innerText = i;
        btn.disabled = i === page;
        btn.addEventListener('click', () => loadComments(i));
        $pagination.appendChild(btn);
      }

      // ▶ 다음 그룹
      if (endPage < totalPages) {
        const nextBtn = document.createElement('button');
        nextBtn.innerText = '▶';
        nextBtn.addEventListener('click', () => loadComments(endPage + 1));
        $pagination.appendChild(nextBtn);
      }

      // ≫ 끝
      if (page < totalPages) {
        const lastBtn = document.createElement('button');
        lastBtn.innerText = '≫';
        lastBtn.addEventListener('click', () => loadComments(totalPages));
        $pagination.appendChild(lastBtn);
      }

    } catch (err) {
      console.error('댓글 불러오기 오류:', err.message);
    }
  }

  // 댓글 등록
  $form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const data = {
      boardId: boardId,
      content: $content.value
    };

    try {
      const res = await fetch('/api/comments', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });

      if (res.ok) {
        $content.value = '';
        loadComments(1);  // 등록 후 첫 페이지 로드
      } else if (res.status === 401) {
        alert('로그인이 필요합니다!');
        location.href = '/login';
      } else {
        alert('댓글 등록 실패');
      }
    } catch (err) {
      console.error('댓글 등록 오류:', err.message);
    }
  });

  // 댓글 삭제
  async function deleteComment(commentId) {
    if (!confirm('댓글을 삭제하시겠습니까?')) return;

    try {
      const res = await fetch(`/api/comments/${commentId}`, {
        method: 'DELETE'
      });

      if (res.ok) {
        loadComments(currentPage);
      } else if (res.status === 403) {
        alert('삭제 권한이 없습니다.');
      } else {
        alert('댓글 삭제 실패');
      }
    } catch (err) {
      console.error('댓글 삭제 오류:', err.message);
    }
  }

  // 댓글 수정
  async function editComment(commentId, oldContent) {
    const newContent = prompt('댓글을 수정하세요:', oldContent);
    if (newContent === null || newContent.trim() === '') return;

    try {
      const res = await fetch(`/api/comments/${commentId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ content: newContent })
      });

      if (res.ok) {
        loadComments(currentPage);
      } else if (res.status === 403) {
        alert('수정 권한이 없습니다.');
      } else {
        alert('댓글 수정 실패');
      }
    } catch (err) {
      console.error('댓글 수정 오류:', err.message);
    }
  }

  // 초기 댓글 목록 로드
  loadComments();
});
