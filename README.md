# Лабораторные работы по программированию на Java

**Студент:** Шибаев Сергей Максимович  
**Группа:** БКН2501  
**Направление:** 09.03.04 «Программная инженерия»  
**Репозиторий:** [https://github.com/ardont/java-course-labs](https://github.com/ardont/java-course-labs)

---

## Статус выполнения лабораторных работ

| ЛР | Название темы | Ветка | Pull Request | Статус |
|:---:|:---|:---:|:---:|:---:|
| **ЛР 01** | Методы, управление выполнением и тесты | `lab/01-basics` | [#1](https://github.com/ardont/java-course-labs/pull/1) | ✅ Выполнено |
| **ЛР 02** | Классы и инкапсуляция | `lab/02-classes` | [#2](https://github.com/ardont/java-course-labs/pull/2) | ✅ Выполнено |
| **ЛР 03** | Интерфейсы и полиморфизм | `lab/03-polymorphism` | [#3](https://github.com/ardont/java-course-labs/pull/3) | ✅ Выполнено |
| **ЛР 04** | `equals`, `hashCode` и `HashMap` | `lab/04-object-contracts` | [#4](https://github.com/ardont/java-course-labs/pull/4) | ✅ Выполнено |
| **ЛР 05** | Файлы, исключения и валидация | `lab/05-files` | [#5](https://github.com/ardont/java-course-labs/pull/5) | ✅ Выполнено |
| **ЛР 06** | Коллекции и введение в дженерики | `lab/06-generics` | [#6](https://github.com/ardont/java-course-labs/pull/6) | ✅ Выполнено |
| **ЛР 07** | Лямбды и Stream API | `lab/07-streams` | [#7](https://github.com/ardont/java-course-labs/pull/7) | ✅ Выполнено |
| **ЛР 08** | `ExecutorService` и конкурентная обработка | `lab/08-concurrency` | [#8](https://github.com/ardont/java-course-labs/pull/8) | ✅ Выполнено |

---

## Сборка и запуск тестов

Проект собран на основе Java 25 LTS и управляется через Maven Wrapper:

```powershell
# Запуск тестов всех модулей:
.\mvnw.cmd test

# Запуск тестов отдельного модуля (например, lab01):
.\mvnw.cmd -pl lab01 test
```
