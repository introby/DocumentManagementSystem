Для запуска приложения:
1) выполнить mvn clean install или Edit Configurations - Before launch - добавить первым пунктом clean install
2) Установить environment variables. File | Settings | Build, Execution, Deployment | Build Tools | Maven | Runner
   Добавить в environment variables строку -
   POSTGRES_HOST=localhost;POSTGRES_PORT=5432;POSTGRES_DB=dms;POSTGRES_DRIVER=org.postgresql.Driver;POSTGRES_PASSWORD=postgres;POSTGRES_USERNAME=postgres