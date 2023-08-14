# test-gts
## How to set up the project ▶

1) Склонируйте репозиторий и перейдите в него
```
git clone https://github.com/Antroverden/test-gts.git
```
2) Запустите проект в Intellij IDEA или введите в консоли
```
mvn clean package
```
3) Убедитесь, что у вас запущен Docker и введите в консоли
```
docker compose build
```
```
docker compose up
```
Примеры HTTP-запросов к контроллерам при запущенном приложении можно увидеть по ссылке:
```
http://localhost:8080/swagger-ui/index.html
```

