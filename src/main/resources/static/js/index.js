// index.js

document.addEventListener("DOMContentLoaded", () => {
  const nicknameEl = document.querySelector(".main-container strong");
  const loginLink = document.querySelector("a[href*='/login']");
  const logoutLink = document.querySelector("a[href*='/logout']");

  // 로그인 상태일 경우
  if (nicknameEl) {
    console.log(`환영합니다, ${nicknameEl.textContent}님`);
  }

  // 로그아웃 확인
  if (logoutLink) {
    logoutLink.addEventListener("click", (e) => {
      if (!confirm("정말 로그아웃 하시겠습니까?")) {
        e.preventDefault();
      }
    });
  }

  // 로그인 알림 (비로그인 상태에서 클릭 시)
  if (loginLink) {
    loginLink.addEventListener("click", () => {
      console.log("로그인 페이지로 이동합니다.");
    });
  }
});
