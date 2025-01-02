#!/bin/zsh

export DB_URL=jdbc:postgresql://localhost:5433/is
export DB_USER=postgres
export DB_PASSWORD=123

# Путь к Maven
MVN_PATH="/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn"

# Путь к WildFly
WILDFLY_HOME="$HOME/Dev/wildfly-27.0.1.Final"
JBOSS_CLI="$WILDFLY_HOME/bin/jboss-cli.sh"

# Порт WildFly, который ищем для завершения
WILDFLY_HTTP_PORT=9090

# Название вашего проекта и имя итогового WAR файла
APP_NAME="lab1p"
WAR_FILE="target/$APP_NAME-1.0-SNAPSHOT.war"

# Проверка наличия mvn
if ! command -v $MVN_PATH &> /dev/null
then
    echo "Maven не установлен. Пожалуйста, установите Maven и попробуйте снова."
    exit
fi

# Проверка наличия jboss-cli
if [ ! -f "$JBOSS_CLI" ]; then
    echo "jboss-cli.sh не найден. Проверьте переменную WILDFLY_HOME."
    exit
fi

# Завершение предыдущей версии WildFly, если она запущена
echo "Завершение предыдущей версии WildFly, если она запущена..."
PIDS=$(lsof -ti:$WILDFLY_HTTP_PORT)

if [ -n "$PIDS" ]; then
    echo "Найден запущенный процесс WildFly на порту $WILDFLY_HTTP_PORT. Завершаем процесс ${PIDS}..."
    kill -9 $PIDS
    if [ $? -eq 0 ]; then
        echo "Старый процесс WildFly завершен."
    else
        echo "Не удалось завершить старый процесс. Проверьте права доступа."
        exit 1
    fi
else
    echo "Предыдущая версия WildFly на порту $WILDFLY_HTTP_PORT не найдена."
fi

# Сборка приложения с помощью Maven
echo "Сборка приложения с помощью Maven..."
$MVN_PATH clean package
$MVN_PATH war:war

# Проверка успешности сборки
if [ ! -f "$WAR_FILE" ]; then
    echo "Сборка не удалась. Проверьте логи сборки."
    exit
fi

# Деплой приложения на WildFly
echo "Деплой приложения на WildFly..."
$JBOSS_CLI -c "deploy $WAR_FILE --force"

# Запуск сервера WildFly
echo "Запуск сервера WildFly..."
$WILDFLY_HOME/bin/standalone.sh &

echo "Приложение успешно запущено на WildFly."