<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Выберите опции:</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/gh/yegor256/tacit@gh-pages/tacit-css-1.6.0.min.css"/>
</head>
<body>
<const urlParams = new URLSearchParams(window.location.search);
       const selectedFile = urlParams.get('file');></const>
</hr>
<h3><form action="/files/replace/${id}/${file}" method="post" enctype="multipart/form-data">
    <h3>Имя: <input type="text" name="name"></h3>
    <h3>Директория: <input type="text" name="direct"></h3>
    <input style="color: red; background: #f2f2f2" type="submit" value="переместить">
    <input style="color: red; background: #f2f2f2" type="reset" value="очистка">
</form></h3>

<h3><button type="button" style="color: red" id="назад" onclick="location.href='/files/delite/${id}/${bucketName}/${filename}'">назад</button></h3>


</body>
</html>