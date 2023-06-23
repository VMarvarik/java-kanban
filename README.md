# java-kanban

Приложение для организации совместной работы над задачами. Программа позволяет выполнять CRUD-операции над задачами.
Сами задачи делятся на разные типы: общие, подзадачи и эпики. Эпики включают в себя подзадачи. Функционал программы
реализован в трех формах:

1) сохранение данных в оперативной памяти на локальной машине,
2) сохранением данных в файл на локальной машине,
3) сохранение данных в файл на сервере с использованием клиента.

-----

### Стек-технологий и опыт разработки (Java Core)

В ходе реализации проекта был применен следующий стек технологий:

- Проектирование в стиле ООП:
    - методы и классы
    - инкапсуляция (пакеты, модификаторы доступа, геттеры-сеттеры)
    - наследование, правило DRY, сокрытие полей, переопределение методов, super, this
    - IDEA: автогенерация кода, горячие клавиши, плагины, дебаггер
    - класс Object, метод equals, hashCode, toString
    - code style
- Области видимости переменных
- Оператор switch
- Git: add, commit (хэш, лог, HEAD), status, .gitkeep, .gitignore, conventional commits, log, reset, diff
- MarkDown for ReadMe


- Абстракция и полиморфизм:
    - абстрактные класс и метод
    - интерфейсы
    - виды полиморфизма: классический, ad-hoc (динамический, статический: перегрузка метода), параметрический
- Модификаторы: static, final
- Константы
- Перечисляемый тип Enum
- Приведение типов: явное, скрытое, instanceof
- Дженерики, типизированные классы и интерфейсы, границы дженериков
- Git: branch, checkout, merge, revert
- Утилитарный класс: фабрика


- Алгоритмы:
    - асимптотическая сложность
    - поиск минимума/максимума в массиве (линейный поиск)
    - бинарный поиск
    - сортировка (вставками, поразрядная)
- Структуры данных:
    - Java Collections Framework:
        - Iterable, Collection, List, Queue, Set
        - Map
        - Comparator, Comparable, сортировка коллекций, String.CASE_INSENSITIVE_ORDER
        - Collections


- String:
    - неизменяемость, пул строк, методы, подстроки, сборка-разборка, форматирование
- StringBuilder
- Регулярные выражения
- Исключения:
    - иерархия, стек-трейс, throw, throws
    - обработка исключений (try-catch-finally)
- Работа с файлами:
    - File, Files, Path, Paths
    - Streams: байтовые - InputStream, OutputStream; символьные - Reader, Writer; буферизация
    - try-with-resources
    - кодировки, Юникод, Charset, StandardCharsets
- Ключевые и зарезервированные слова


- Функциональный стиль программирования: анонимные классы
- Функциональные интерфейсы: Consumer, Supplier, Predicate, Function, UnaryOperator
- Лямбда-выражение, замыкание, ссылка на метод, класс Optional
- Дженерик-интерфейс Stream, промежуточные и терминальные операции
- Время и дата: unix-время, класс Instant
- LocalDateTime, LocalDate, LocalTime, DateTimeFormatter
- Period, Duration, TemporalAmount
- ZoneId (фиксированное смещение, привязка к региону), ZonedDateTime
- Функциональное тестирование: классы эквивалентности, граничные значения, покрытие кода и требований
- JUnit Test Framework


- Интернет. Модель клиент-сервер. Сетевые протоколы (IP, TCP, HTTP)
- URL-адрес, URL-кодирование, HTML, HTTP-сообщение (стартовая строка, методы, код ответа, заголовки, тело)
- URI, параметры пути, параметры строки запроса
- Инструменты разработчика в браузере (на примере Google Chrome)
- Веб-сервис и WEB API
- Формат данных JSON, класс POJO
- Эндпоинты, сетевой пакет, номер порта, сокет
- HttpServer, InetSockerAddress, HttpHandler, HttpExchange, Headers
- GSON (JsonArray, JsonElement, JsonObject,JsonParser), сериализация, десериализация
- GsonBuilder, TypeAdapter, JsonWriter, JsonReader
- Приложение Insomnia для тестирования API
- HttpClient, HttpRequest, HttpResponse, BodyHandler
- Обработка ошибок HTTP-запросов (IOException, InterruptedException, IllegalArgumentException)
