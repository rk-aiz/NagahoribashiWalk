(function () {
    /* カテゴリメガメニュー */
    var nav = document.getElementById('categoryNav');
    if (nav) {
        var items = nav.querySelectorAll('.cat-item');
        items.forEach(function (item) {
            item.addEventListener('mouseenter', function () {
                items.forEach(function (i) { i.classList.remove('cat-open'); });
                item.classList.add('cat-open');
            });
        });
        nav.addEventListener('mouseleave', function () {
            items.forEach(function (i) { i.classList.remove('cat-open'); });
        });
    }

    /* トップへ戻るボタン */
    var btn = document.getElementById('backToTop');
    window.addEventListener('scroll', function () {
        btn.style.display = window.scrollY > 300 ? 'block' : 'none';
    });
    btn.addEventListener('click', function () {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
    btn.addEventListener('mouseover', function () { btn.style.backgroundColor = '#e2e8f0'; });
    btn.addEventListener('mouseout', function () { btn.style.backgroundColor = '#f1f5f9'; });

    /* ログアウトトースト自動表示 */
    var logoutToastEl = document.getElementById('logoutToast');
    if (logoutToastEl) {
        new bootstrap.Toast(logoutToastEl, { delay: 3000 }).show();
    }
})();
