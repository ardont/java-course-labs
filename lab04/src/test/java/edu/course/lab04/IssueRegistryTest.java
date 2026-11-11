package edu.course.lab04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IssueRegistryTest {

    @Test
    @DisplayName("Эксперимент 1: Два разных экземпляра логически равного ключа находят одно значение")
    void distinctEqualKeysFindSameValue() {
        IssueRegistry registry = new IssueRegistry();
        IssueId key1 = new IssueId("PROJ", 42);
        IssueId key2 = new IssueId("PROJ", 42);

        ProjectTask task = new ProjectTask("PROJ-42", "Fix critical bug", 5);
        registry.put(key1, task);

        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
        assertTrue(registry.containsKey(key2));
        assertEquals(task, registry.get(key2));
    }

    @Test
    @DisplayName("Эксперимент 2: Повторное добавление с равным ключом заменяет значение и не увеличивает размер")
    void putWithEqualKeyReplacesValueAndDoesNotIncreaseSize() {
        IssueRegistry registry = new IssueRegistry();
        IssueId key1 = new IssueId("AUTH", 1);
        IssueId key2 = new IssueId("AUTH", 1);

        ProjectTask oldTask = new ProjectTask("AUTH-1", "Initial design", 4);
        ProjectTask newTask = new ProjectTask("AUTH-1", "Updated implementation", 8);

        registry.put(key1, oldTask);
        assertEquals(1, registry.size());
        assertEquals("Initial design", registry.get(key1).getTitle());

        registry.put(key2, newTask);
        assertEquals(1, registry.size());
        assertEquals("Updated implementation", registry.get(key1).getTitle());
    }

    @Test
    @DisplayName("Эксперимент 3: Два неравных ключа с намеренно одинаковым hashCode сохраняют разные значения (коллизия)")
    void unequalKeysWithSameHashCodeStoreDifferentValues() {
        // Учебный класс с фиксированным hashCode = 42 для моделирования коллизии
        final class CollidingKey {
            private final String id;

            CollidingKey(String id) {
                this.id = id;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                CollidingKey that = (CollidingKey) o;
                return Objects.equals(id, that.id);
            }

            @Override
            public int hashCode() {
                return 42; // намеренно одинаковый хэш-код для всех экземпляров
            }
        }

        Map<CollidingKey, String> map = new HashMap<>();
        CollidingKey keyA = new CollidingKey("A");
        CollidingKey keyB = new CollidingKey("B");

        assertNotEquals(keyA, keyB);
        assertEquals(keyA.hashCode(), keyB.hashCode()); // коллизия!

        map.put(keyA, "Value A");
        map.put(keyB, "Value B");

        assertEquals(2, map.size());
        assertEquals("Value A", map.get(keyA));
        assertEquals("Value B", map.get(keyB));
    }

    @Test
    @DisplayName("Эксперимент 4: Сравнение с null и объектом другого типа возвращает false, проверка рефлексивности и симметрии")
    void issueIdEqualityContracts() {
        IssueId id = new IssueId("TEST", 10);

        assertFalse(id.equals(null));
        assertFalse(id.equals("TEST-10")); // другой тип
        assertEquals(id, id); // рефлексивность

        IssueId same = new IssueId("TEST", 10);
        assertEquals(id, same);
        assertEquals(same, id); // симметричность
        assertEquals(id.hashCode(), same.hashCode());

        IssueId differentCode = new IssueId("OTHER", 10);
        IssueId differentNumber = new IssueId("TEST", 11);
        assertNotEquals(id, differentCode);
        assertNotEquals(id, differentNumber);
    }

    @Test
    @DisplayName("Эксперимент 5: Проблема изменяемого ключа — после изменения поля поиск в HashMap ломается")
    void mutableKeyBreaksHashMapLookup() {
        // Учебный изменяемый ключ
        final class MutableKey {
            private String code;

            MutableKey(String code) {
                this.code = code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                MutableKey that = (MutableKey) o;
                return Objects.equals(code, that.code);
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(code);
            }
        }

        Map<MutableKey, String> map = new HashMap<>();
        MutableKey key = new MutableKey("ORIGINAL");
        map.put(key, "Important Payload");

        // До изменения ключ корректно находится
        assertEquals("Important Payload", map.get(key));

        // Мутируем ключ ПОСЛЕ помещения в Map
        key.setCode("MUTATED");

        // Из-за изменения хэш-кода объект попадает в другую корзину бакетов (или не совпадает при поиске)
        // В результате поиск по тому же самому экземпляру возвращает null!
        assertNull(map.get(key), "Поиск по мутированному ключу должен возвращать null");
        assertFalse(map.containsKey(key));
    }

    @Test
    @DisplayName("Базовые операции IssueRegistry: remove, clear, containsKey")
    void registryCrudOperations() {
        IssueRegistry registry = new IssueRegistry();
        assertTrue(registry.isEmpty());
        assertEquals(0, registry.size());

        IssueId id = new IssueId("DEV", 1);
        ProjectTask task = new ProjectTask("DEV-1", "Setup CI", 3);

        registry.put(id, task);
        assertFalse(registry.isEmpty());
        assertEquals(1, registry.size());

        ProjectTask removed = registry.remove(id);
        assertEquals(task, removed);
        assertEquals(0, registry.size());
        assertFalse(registry.containsKey(id));
        assertNull(registry.get(id));
    }

    @Test
    @DisplayName("Валидация полей IssueId")
    void validatesIssueIdFields() {
        assertThrows(IllegalArgumentException.class, () -> new IssueId(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new IssueId("  ", 1));
        assertThrows(IllegalArgumentException.class, () -> new IssueId("DEV", 0));
        assertThrows(IllegalArgumentException.class, () -> new IssueId("DEV", -5));
    }
}
