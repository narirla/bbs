document.addEventListener('DOMContentLoaded', () => {
  const boardId = document.getElementById('cmtBoardId').value;
  const $form = document.getElementById('commentForm');
  const $content = document.getElementById('commentContent');
  const $list = document.getElementById('commentList');

  // 댓글 목록 가져오기
  async function loadComments() {
    try {
      const res = await fetch(`/api/comments/board/${boardId}`);
      if (!res.ok) throw new Error('댓글 조회 실패');

      const comments = await res.json();
      $list.innerHTML = ''; // 초기화

      if (comments.length === 0) {
        $list.innerHTML = '<p>댓글이 없습니다.</p>';
        return;
      }

      comments.forEach(comment => {
        const div = document.createElement('div');
        div.classList.add('comment-item');

        const p = document.createElement('p');
        p.innerText = comment.content;

        const small = document.createElement('small');
        small.innerText = comment.createdAt?.replace('T', ' ').substring(0, 16) || '';

        div.appendChild(p);
        div.appendChild(small);

        // 🔐 작성자 본인에게만 수정/삭제 버튼 표시
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
        loadComments();
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
        loadComments();
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
        loadComments();
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
