<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Информация о файле</title>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/gh/yegor256/tacit@gh-pages/tacit-css-1.6.0.min.css"/>
</head>
<body>

<h1>Сведения</h1>
</hr>
<h3>Владелец: ${model["fileInfo"].get(0)}</h3>
<h3>Дата добавления: ${model["fileInfo"].get(1)}</h3>
<h3>Последнее обновление: ${model["fileInfo"].get(2)}</h3>
<h3>Теги: ${model["fileInfo"].get(3)}</h3>

</body>
</html>