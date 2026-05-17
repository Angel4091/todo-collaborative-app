# todo-collaborative-app

Aplicación de consola en Java para gestionar tareas colaborativas (Tasks y Reminders), con un patrón de diseño y soporte de concurrencia.

Proyecto académico de **POO 2026 - Entregable 2**.

---

## Estructura del proyecto

```
src/main/java/org/example/
├── Main.java
├── catalog/        Priority, Status (enums)
├── model/          Authenticable, Shareable, User, PremiumUser,
│                   ClassicUser, Item, Task, Reminder
├── patterns/       NotificationStrategy, EmailNotification,
│                   MensajeTextoNotification         <-- Strategy
├── service/        AuthService, UserService
├── dao/            UserDAO, ItemDAO
├── thread/         TaskWorker                       <-- concurrencia
└── ui/             MainMenu, DemoRunner
```

---

## Patrón de diseño aplicado: Strategy

El proyecto usa **un solo patrón de diseño**: **Strategy** para los canales de notificación de un `Reminder`.

- `NotificationStrategy` (interface) — define el contrato `notify(message)`.
- `EmailNotification` — implementación concreta para correo.
- `MensajeTextoNotification` — implementación concreta para SMS.
- `Reminder` mantiene una referencia a `NotificationStrategy` y delega en ella cuando se dispara `notifyAlert()`.

Esto permite cambiar en tiempo de ejecución cómo notifica un recordatorio sin tocar la clase `Reminder`.

```java
Reminder r = new Reminder(...);
r.setNotificationStrategy(new EmailNotification());
r.notifyAlert();   // [EMAIL] ...

r.setNotificationStrategy(new MensajeTextoNotification());
r.notifyAlert();   // [MENSAJE DE TEXTO] ...
```

---

## Concurrencia

La rúbrica del entregable 2 pide implementar hilos para simular acceso concurrente a tareas compartidas. Esto **no es un patrón GoF**, sino características de Java concurrency.

### Mecanismos usados

| Mecanismo | Dónde | Para qué |
|-----------|-------|----------|
| `synchronized` (método) | `Item.addCollaborator`, `Item.removeCollaborator`, `Task.modifyStatus` | Sección crítica: solo un hilo a la vez modifica el estado del item compartido. |
| `CopyOnWriteArrayList` | `Item.collaborators` | Lista thread-safe que permite iterar sin bloquear escrituras. |
| `Collections.synchronizedList` | `User.createdItems`, `User.sharedItems` | Sincroniza el acceso a las listas de items por usuario. |
| `ConcurrentHashMap` | `MainMenu.items` | Mapa central thread-safe para todos los items del sistema. |
| `AtomicInteger` | `MainMenu.idCounter` | Generación atómica de ids únicos. |
| `Runnable` + `Thread` | `TaskWorker` | Encapsula la operación de un usuario que modifica una task en su propio hilo. |
| `Thread.join()` | `DemoRunner` | Espera a que terminen todos los hilos antes de imprimir el estado final. |

### `TaskWorker`

Representa a un usuario que intenta cambiar el estado de una `Task` compartida.

```java
public class TaskWorker implements Runnable {
    private final Task task;
    private final Status newStatus;
    private final User user;

    public void run() {
        task.modifyStatus(newStatus);   // synchronized
    }
}
```

### Demo automático

Desde el menú principal se puede correr la opción **D** (Demo) que:

1. Crea una `Task` compartida entre Angel, Maria y Pedro.
2. Lanza dos `Thread` que ejecutan un `TaskWorker` cada uno (uno cambia el estado a `IN_PROGRESS`, el otro a `COMPLETED`) sobre la **misma** instancia.
3. Espera a ambos con `join()`.
4. Imprime el estado final.

Como `modifyStatus` es `synchronized`, los hilos se serializan en la sección crítica y nunca dejan el objeto en un estado inconsistente.

---

## Diagramas UML

En `diagramasUML/` están los archivos `.puml` (PlantUML):

| Archivo | Contenido |
|---------|-----------|
| `1_diagrama_clases_v2.puml` | Diagrama de clases del entregable 2 (Strategy + concurrencia). |
| `2_diagrama_actividad_v2.puml` | Flujo de gestión de tareas con concurrencia. |
| `3_secuencia_crear_task.puml` | Secuencia de creación de una Task. |
| `4_secuencia_compartir_task.puml` | Secuencia de compartir un item (synchronized). |
| `5_secuencia_cambiar_estado.puml` | Secuencia de cambio de estado con dos hilos paralelos. |

Para visualizarlos: extensión **PlantUML** en VS Code, o pegar el contenido en https://plantuml.com/plantuml.

---

## Cómo ejecutar

Requiere **JDK 17** y **Gradle** (o el wrapper `gradlew`).

```bash
./gradlew run
```

Usuarios de prueba precargados:

| Email | Password | Tipo |
|-------|----------|------|
| angel@mail.com | 1234 | PremiumUser |
| maria@mail.com | abcd | ClassicUser (limit 5 tasks, 3 colab) |
| pedro@mail.com | pass | ClassicUser (limit 5 tasks, 3 colab) |

Una vez dentro, opción **D** para correr el demo automático de concurrencia.
