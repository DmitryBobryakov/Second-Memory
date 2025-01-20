<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Выберите опции:</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/gh/yegor256/tacit@gh-pages/tacit-css-1.6.0.min.css"/>
</head>
<const urlParams = new URLSearchParams(window.location.search);
const selectedFile = urlParams.get('file');></const>
<body>
</hr>
<h3><button type="button" style="color: red" id="переименовать" onclick="redirectToPage1('file')">переименовать</button></h3>
<h3><button type="button" style="color: red" id="переместить" onclick="redirectToPage2('file')">переместить</button></h3>
<h3><form action="/files/delite/${bucketName}/${filename}" method="post">
    <input style="color: red; background: #f2f2f2" type="submit" value="удалить">
</form></h3>
<h3><button style="color: red" id="назад" onclick="location.href='/files/direct/${bucketName}'">назад</button></h3>
<script>
    function redirectToPage1(fileInfo) {
      window.location.href = '/files/rename/${bucketName}/${filename}';
    }
</script>
<script>
    function redirectToPage2(fileInfo) {
      window.location.href = '/files/replace/${bucketName}/${filename}';
    }
</script>
</body>
</html>