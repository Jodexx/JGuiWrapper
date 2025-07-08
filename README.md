# JGuiWrapper

> Библиотека для создания настраиваемых GUI на серверах **PaperMC1.20–1.21.6**

---

## Общая инициализация

```java
@Override
public void onEnable() {
    JGuiInitializer.init(this); // регистрация всех слушателей
}
```

После вызова `init` вы можете создавать и открывать GUI любому игроку.

---

## 1|`AbstractGui`

Базовый абстрактный класс, реализующий интерфейс `Gui` и обеспечивающий:

| Возможность                         | Описание                                                                                      |
| ----------------------------------- | --------------------------------------------------------------------------------------------- |
| Полный контроль над `InventoryView` | Через внутренний `NMSWrapper` можно динамически менять тип, размер и заголовок без закрытия окна. |
| Обработка событий                   | Переопределяемые методы`onOpen`, `onClose`, `onClick`, `onDrag`.                              |
| Быстрое обновление меню             | Методы `updateMenu(...)` для массового или точечного обновления.                              |
| Авто‑адаптер размера                | Статический метод `adaptSize(int)` округляет любое значение к ближайшему кратному9 между1и54. |

### Конструкторы (кратко)

```java
AbstractGui(String legacyTitle);
AbstractGui(int size, String legacyTitle);
AbstractGui(Component title);
AbstractGui(InventoryType type, Component title);
AbstractGui(int size, Component title);
```

### Ключевые методы

```java
int size();                   // текущий размер
void size(int newSize);       // задать размер
Component title();            // текущий заголовок
void title(Component t);      // задать заголовок
InventoryType type();         // тип GUI (CHEST, HOPPER…)
void type(InventoryType t);   // изменить тип (до updateMenu)

void open(HumanEntity player);                // открыть GUI игроку
void close(HumanEntity player);               // закрыть GUI
void updateHolder();                          // пересоздать внутренний Holder
```

---

## 2|`SimpleGui`

**Расположение:** `com.jodexindustries.jguiwrapper.gui`

Наследуется от `AbstractGui` и добавляет простую систему **слотовых обработчиков**.

### Ключевые возможности

- **Map\<Integer,InventoryHandler> slotClickHandlers**— быстрый доступ к логике клика по каждому слоту.
- Лямбда‑хелперы:
  ```java
  gui.onOpen(evt -> {...});
  gui.onClose(evt -> {...});
  gui.onDrag(evt -> {...});
  ```
- Параметр `cancelEmptySlots` (по умолчанию **true**) блокирует перенос предметов в пустые слоты, если для них не задан обработчик.

### Чаще всего используемые методы

```java
void setClickHandlers(InventoryHandler handler, int... slots);   // назначить
void removeClickHandlers(int... slots);                          // снять
void setCancelEmptySlots(boolean flag);                          // включить/выключить автокэнсел
```

---

## 3|`AdvancedGui`

Расширяет `SimpleGui`, внедряя **контроллеры предметов** (`GuiItemController`)— гибкий инструмент для динамического содержимого.

| Метод                                                           | Назначение                                 |
| --------------------------------------------------------------- | ------------------------------------------ |
| `registerItem(String key, Consumer<GuiItemController.Builder>)` | Строит и регистрирует контроллер по ключу. |
| `getController(String key / int slot)`                          | Получает контроллер по ключу или слоту.    |
| `Collection<GuiItemController> getControllers()`                | Набор всех контроллеров.                   |

### Мини‑пример

```java
AdvancedGui gui = new AdvancedGui("&6Меню")
    .registerItem("clock", b -> b
        .withSlots(11)
        .withDefaultItem(new ItemWrapper(() -> new ItemStack(Material.CLOCK)))
        .withDefaultClickHandler((e, c) -> e.getWhoClicked().sendMessage("Тик‑так"))
    );
```

---

## 4|`GuiItemController`

Контролирует **набор слотов** и управляет:

- **ItemWrapper** для каждого слота или дефолтный для всех;
- **AdvancedGuiClickHandler** для каждого слота или общий.

### Основные public методы

```java
void addSlot(int slot) / removeSlot(int slot);
void setSlots(int... slots);

void defaultItemWrapper(ItemWrapper w);
void setItemWrapperForSlot(int slot, ItemWrapper w);
void removeItemWrapperForSlot(int slot);

void defaultClickHandler(AdvancedGuiClickHandler h);
void setClickHandlerBySlot(int slot, AdvancedGuiClickHandler h);
void removeClickHandlerForSlot(int slot);

void redraw();               // полная перерисовка
void updateAllItemWrappers(Consumer<ItemWrapper> updater);
```

### Builder‑паттерн

`GuiItemController.Builder` облегчает декларативную настройку:

```java
controller = new GuiItemController.Builder(gui)
        .withSlots(10, 11, 12)
        .withDefaultItem(clockWrapper)
        .withDefaultClickHandler(myHandler)
        .withSlotItem(11, specialWrapper)
        .build();
```

---

## 5|`AdvancedGuiClickHandler`

Функциональный интерфейс = `InventoryHandler<InventoryClickEvent>` с удобным аксессом к `GuiItemController`:

```java
void handle(InventoryClickEvent event, GuiItemController controller);
```

Используется как для «общего» клика (по умолчанию), так и для кастомных на конкретных слотах.
