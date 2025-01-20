<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Загрузка файла</title>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/gh/yegor256/tacit@gh-pages/tacit-css-1.6.0.min.css"/>
</head>
<body>
<form action="/files/upload/${bucketName}" method="post" enctype="multipart/form-data">
    <input style="color: red; background: #f2f2f2" type="file" name="file">
    <input style="color: red; background: #f2f2f2" type="submit" value="Загрузить">
</form>
<h3><button type="button" style="color: red" id="назад" onclick="location.href='/files/info/${id}/${bucketName}'">назад</button></h3>
</body>
</html>
