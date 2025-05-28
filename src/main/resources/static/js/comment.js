/*comment.js*/
document.addEventListener('DOMContentLoaded', () => {
  const boardId = document.getElementById('cmtBoardId').value;
  const $form = document.getElementById('commentForm');
  const $content = document.getElementById('commentContent');
  const $list = document.getElementById('commentList');
  const $pagination = document.getElementById('commentPagination');

  let currentPage = 1;
  const commentsPerPage = 10;

  // 댓글 목록 가져오기
  async function loadComments(page = 1) {
    try {
      const res = await fetch(`/api/comments/board/${boardId}/pages?page=${page}`);
      if (!res.ok) throw new Error('댓글 조회 실패');

      const { comments, totalPages } = await res.json();  // 서버는 comments, totalPages를 포함해야 함
      currentPage = page;
      $list.innerHTML = '';
      $pagination.innerHTML = '';

      if (comments.length === 0) {
        $list.innerHTML = '<p>댓글이 없습니다.</p>';
        return;
      }

      comments.forEach((comment, index) => {
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

      // 페이징 버튼 생성
      for (let i = 1; i <= totalPages; i++) {
        const btn = document.createElement('button');
        btn.innerText = i;
        if (i === page) btn.disabled = true;
        btn.addEventListener('click', () => loadComments(i));
        $pagination.appendChild(btn);
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
        loadComments(1);  // 등록 후 1페이지로 이동
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

  // 페이지 로드시 댓글 목록 조회
  loadComments();
});
