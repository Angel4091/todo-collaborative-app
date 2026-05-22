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
├── thread/         TaskWorker                       <-- concurrencia
└── ui/             MainMenu                         <-- storage in-memory
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

### Concurrencia real en el flujo normal

La concurrencia se dispara **dentro del flujo natural** del menú, no en un demo aparte. Cuando el usuario elige la opción **4 (Cambiar estado de Task)** y la task está **compartida** con uno o más colaboradores, el sistema:

1. Genera un `TaskWorker` por **cada colaborador** y otro por el **usuario actual**.
2. Cada `TaskWorker` propone un estado distinto y se ejecuta en su propio `Thread`.
3. Todos los hilos arrancan **en paralelo** (`Thread.start()`) y compiten por el monitor de la `Task`.
4. `synchronized` en `Task.modifyStatus()` los **serializa**: solo uno entra a la sección crítica a la vez.
5. El sistema espera a todos con `Thread.join()` y muestra el estado final.

Si la task **no está compartida**, simplemente se cambia el estado sin hilos extras (no hay race condition que prevenir).

---

## Diagramas UML

En `diagramasUML/` están los archivos `.puml` (PlantUML):

| Archivo | Contenido |
|---------|-----------|
| `1_diagrama_clases_v2.puml` / `.drawio` | Diagrama de clases del entregable 2 (Strategy + concurrencia). |
| `2_diagrama_actividad_v2.puml` / `.drawio` | Actividad: cambiar estado de una Task (`modifyStatus`) con 4 swimlanes (Sistema, Colaboradores, Hilo-Maria, Hilo-Pedro). |
| `3_secuencia_cambiar_estado.puml` / `.drawio` | Secuencia: cambiar el estado de una task con `par + synchronized` y los hilos compitiendo por el monitor. |
| `4_diagrama_objetos.puml` / `.drawio` | Diagrama de objetos: snapshot del sistema con instancias concretas (Angel, Maria, Pedro, una Task compartida y un Reminder con Strategy). |

Para visualizarlos: extensión **PlantUML** en VS Code (para `.puml`), draw.io en https://app.diagrams.net (para `.drawio`), o pegar el contenido `.puml` en https://plantuml.com/plantuml.

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

### Cómo probar la concurrencia paso a paso

1. Login (cualquier email/contraseña, elegí Premium).
2. Opción **1**: crear una Task (ej: "Sprint Review").
3. Opción **3**: compartir la Task con `maria@mail.com`.
4. Opción **3** otra vez: compartir con `pedro@mail.com`.
5. Opción **4**: cambiar el estado. Como la Task tiene 2 colaboradores, automáticamente se lanzan 3 hilos en paralelo.

### Output esperado

```
[Concurrencia] La task esta compartida con 2 colaborador(es).
[Concurrencia] Cada colaborador intentara cambiar el
               estado en paralelo. synchronized los serializa.

[Hilo-Maria] Maria intenta cambiar 'Sprint Review' a IN_PROGRESS
[Hilo-Pedro] Pedro intenta cambiar 'Sprint Review' a PENDING
[Hilo-Angel] Angel intenta cambiar 'Sprint Review' a COMPLETED
[Hilo-Maria] Task 'Sprint Review': PENDING -> IN_PROGRESS
[Hilo-Pedro] Task 'Sprint Review': IN_PROGRESS -> PENDING
[Hilo-Angel] Task 'Sprint Review': PENDING -> COMPLETED

[Concurrencia] Estado final tras los 3 hilos: COMPLETED
```

El orden de los hilos puede variar entre ejecuciones (eso es lo esperado en concurrencia). Lo importante es que **siempre** hay una transición ordenada entre estados y nunca queda en un estado inconsistente — eso lo garantiza `synchronized` en `Task.modifyStatus()`.

---

## Cumplimiento de los criterios de evaluación (entregable 2)

### 1. Implementación de observaciones del entregable 1 — **40 pts**

| Sub-criterio | Evidencia |
|---|---|
| Aplica correctamente todas las observaciones del entregable 1 (remover Factory) | Commit `1110d58` — borra `ItemFactory`, `TaskFactory`, `ReminderFactory` y adapta `MainMenu`/`DemoRunner` para instanciar `Task`/`Reminder` directamente. |
| Commits claros y frecuentes | Más de 45 commits con mensajes convencionales (`feat:`, `fix:`, `refactor:`, `docs(uml):`, `docs(java):`, `revert:`, `merge:`). |
| Uso adecuado de ramas y fusión ordenada | Ramas `feature/readme-demo-output` (merge `7408ae5`) y `feature/javadoc-clases-clave` (merge `91687d5`), ambas mergeadas a `main` con `--no-ff` para que el commit de merge quede visible en el historial. |
| Diagrama de clases con mejoras en relaciones, encapsulamiento e interfaces | [`diagramasUML/1_diagrama_clases_v2.puml`](diagramasUML/1_diagrama_clases_v2.puml): interfaces (`Authenticable`, `Shareable`, `NotificationStrategy`), clases abstractas (`User`, `Item`), composición/agregación con multiplicidad, visibilidad explícita (`-`, `#`, `+`), y todas las capas UI/service/dao representadas. |
| Implementación Java coherente con los diagramas | Cada clase del código tiene su contraparte exacta en el diagrama de clases. Compila limpio con JDK 17 (verificado). |

### 2. Diseño de los diagramas de actividad y secuencia con concurrencia — **40 pts**

| Sub-criterio | Evidencia |
|---|---|
| Representan clara y completamente un proceso concurrente | Ambos diagramas cubren `Task.modifyStatus()`: la actividad con `fork`/`join` y 4 swimlanes (Sistema, Colaboradores, Hilo-Maria, Hilo-Pedro); la secuencia con `par + loop + alt`. |
| Identifican hilos, flujo de control y comunicación entre objetos | Hilos explícitos (`Hilo-Maria`, `Hilo-Pedro`), decisiones (`Monitor disponible?`), mensajes entre `TaskWorker` y `Task` con `synchronized(this)`, `status = ...`, `log "X -> Y"`. |
| Notación UML correcta y consistente | PlantUML estándar (validado generando PNG); rombos para decisiones, barras horizontales para fork/join, lifelines con activaciones para la secuencia. |

### 3. Implementación en Java — **20 pts**

| Sub-criterio | Evidencia |
|---|---|
| Funcional, organizada, aplica POO | Compila con JDK 17 y corre end-to-end (probado con `angel@mail.com` + tipo Premium + opción D). Herencia (`PremiumUser`/`ClassicUser` extienden `User`), polimorfismo (override de `addItem`/`shareItem` en `ClassicUser`), encapsulamiento (atributos `private`/`protected` con getters). |
| Usa clases, interfaces, encapsulamiento y concurrencia | 3 interfaces, 2 clases abstractas, `synchronized`, `CopyOnWriteArrayList`, `ConcurrentHashMap`, `AtomicInteger`, `Runnable` + `Thread.start()` + `Thread.join()`. |
| Código limpio y modular | 7 paquetes: `catalog`, `model`, `patterns`, `service`, `dao`, `thread`, `ui`. Clases clave documentadas con JavaDoc (`Task`, `TaskWorker`, `Item`, `Reminder`, `NotificationStrategy` + implementaciones). |

### Tabla de la rúbrica original (comportamiento del sistema y concurrencia)

| Requisito | Estado |
|---|---|
| Actualización del diagrama de clases | ✅ `1_diagrama_clases_v2.puml` |
| Incorporación de patrón de diseño | ✅ Strategy (`NotificationStrategy` + 2 implementaciones) |
| Diagrama de secuencia | ✅ `3_secuencia_cambiar_estado.puml` (cambiar estado) |
| Diagrama de actividad (flujo de gestión de tareas) | ✅ `2_diagrama_actividad_v2.puml` (cambiar estado con concurrencia) |
| Uso de patrón de diseño en Java | ✅ `Reminder.setNotificationStrategy()` |
| Implementación de hilos para acceso concurrente a tareas compartidas | ✅ `TaskWorker` + `DemoRunner` lanza 2 hilos sobre la misma task |
| La aplicación funciona a través de consola | ✅ `Main.java` → `MainMenu.start()` con `Scanner` |

