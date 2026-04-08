var selectedFiles = []; // 選択中ファイルを独自に管理

function updateFileInput() {
    var dt = new DataTransfer();
    selectedFiles.forEach(function (f) { dt.items.add(f); });
    document.getElementById('fileInput').files = dt.files;
}

function renderPreview() {
    var label = document.getElementById('fileNames');
    var preview = document.getElementById('preview');

    // ファイル名表示
    if (selectedFiles.length > 0) {
        label.textContent = '選択中：' + selectedFiles.map(function (f) { return f.name; }).join('、');
        label.style.display = 'block';
    } else {
        label.style.display = 'none';
    }

    // プレビュー表示
    preview.innerHTML = '';
    selectedFiles.forEach(function (file, index) {
        if (!file.type.startsWith('image/')) return;

        // カード（画像＋取り消しボタン）
        var card = document.createElement('div');
        card.style.cssText = 'position:relative; display:inline-block;';

        var img = document.createElement('img');
        img.src = URL.createObjectURL(file);
        img.style.cssText = 'width:120px; height:90px; object-fit:cover; border-radius:4px; border:1px solid #e2e8f0; display:block;';
        img.onload = function () { URL.revokeObjectURL(img.src); };

        var btn = document.createElement('button');
        btn.type = 'button';
        btn.textContent = '×';
        btn.style.cssText = 'position:absolute; top:2px; right:2px; width:20px; height:20px; padding:0; line-height:1; font-size:12px; border:none; border-radius:50%; background:rgba(0,0,0,0.55); color:#fff; cursor:pointer;';
        btn.addEventListener('click', function () {
            selectedFiles.splice(index, 1);
            updateFileInput();
            renderPreview();
        });

        card.appendChild(img);
        card.appendChild(btn);
        preview.appendChild(card);
    });
    preview.style.display = selectedFiles.length > 0 ? 'flex' : 'none';
}

// ファイル選択
document.getElementById('fileInput').addEventListener('change', function () {
    Array.from(this.files).forEach(function (f) { selectedFiles.push(f); });
    updateFileInput();
    renderPreview();
});

// ドラッグ＆ドロップ
var zone = document.getElementById('dropZone');
zone.addEventListener('dragover', function (e) {
    e.preventDefault();
    zone.classList.add('dragover');
});
zone.addEventListener('dragleave', function () {
    zone.classList.remove('dragover');
});
zone.addEventListener('drop', function (e) {
    e.preventDefault();
    zone.classList.remove('dragover');
    Array.from(e.dataTransfer.files).forEach(function (f) { selectedFiles.push(f); });
    updateFileInput();
    renderPreview();
});
