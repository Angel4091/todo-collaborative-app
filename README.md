# 📘 TODO Collaborative App — Sistema de Gestión de Tareas Colaborativas

## 📌 Descripción General del Proyecto

Este proyecto consiste en el desarrollo de una aplicación de consola en **Java 17** que permite la gestión de **tareas (`Task`)** y **recordatorios (`Reminder`)** dentro de un entorno colaborativo.

El sistema permite a los usuarios:

- ✅ Crear tareas y recordatorios con prioridad y estado
- 🔁 Compartir items con otros colaboradores
- 🔄 Cambiar el estado de una tarea de forma concurrente (varios hilos a la vez)
- 🔔 Disparar alertas de recordatorios mediante distintas estrategias de notificación
- 👥 Diferenciar entre usuarios **Classic** (con límites) y **Premium** (sin límites)

El diseño se basa en **Programación Orientada a Objetos (POO)**, usando **clases abstractas, herencia, interfaces y polimorfismo**, junto con un **patrón de diseño Strategy** y **concurrencia real con `Thread` + `synchronized`**.

---

## 🎯 Objetivos de cada Entregable

| Entregable | Objetivo | Estado |
|---|---|---|
| **Entrega 1** | Diseño inicial del sistema con diagrama de clases UML aplicando POO (clases abstractas, interfaces, herencia, encapsulamiento) | ✅ Completado |
| **Entrega 2 (actual)** | Incorporar **patrón de diseño** (Strategy) y **concurrencia** (Threads + `synchronized`). Diagramas de actividad y secuencia con el proceso concurrente | ✅ Completado |
| **Entrega 3 (próximo)** | Interfaz gráfica con **JavaFX** (ListView con colores por prioridad e ícono para recordatorios) + persistencia en **base de datos** | ⏳ Pendiente |

---

## 📂 Estructura del Proyecto

```
src/main/java/org/example/
├── Main.java                          ← punto de entrada
├── catalog/
│   ├── Priority.java                  ← enum: HIGH, MEDIUM, LOW
│   └── Status.java                    ← enum: PENDING, IN_PROGRESS, COMPLETED, CANCELED
├── model/
│   ├── Authenticable.java             ← interface (login)
│   ├── Shareable.java                 ← interface (compartir)
│   ├── User.java                      ← clase base de usuario
│   ├── PremiumUser.java               ← sin límites
│   ├── ClassicUser.java               ← con límites (5 items, 3 colaboradores)
│   ├── Item.java                      ← clase abstracta base de items
│   ├── Task.java                      ← tarea con status (synchronized)
│   └── Reminder.java                  ← recordatorio con estrategia
├── patterns/
│   ├── NotificationStrategy.java      ← interface Strategy
│   ├── EmailNotification.java         ← estrategia: Email
│   └── MensajeTextoNotification.java  ← estrategia: Mensaje de texto
├── thread/
│   └── TaskWorker.java                ← Runnable para concurrencia
└── ui/
    └── MainMenu.java                  ← menú de consola
```

---

## 🎨 Patrón de Diseño Aplicado: Strategy

El proyecto usa **un solo patrón GoF**: **Strategy**, para los canales de notificación de un `Reminder`.

- `NotificationStrategy` (interface) define el contrato `notify(message)`.
- `EmailNotification` y `MensajeTextoNotification` son implementaciones concretas.
- `Reminder` mantiene una referencia a una `NotificationStrategy` y delega en ella sin conocer la implementación concreta.

Esto permite **cambiar el canal de notificación en tiempo de ejecución** sin tocar la clase `Reminder`:

```java
Reminder r = new Reminder(...);
r.setNotificationStrategy(new EmailNotification());
r.notifyAlert();   // [EMAIL] ...

r.setNotificationStrategy(new MensajeTextoNotification());
r.notifyAlert();   // [MENSAJE DE TEXTO] ...
```

---

## 🧵 Concurrencia Real

Cuando una **Task está compartida**, la opción de cambiar estado lanza automáticamente **un hilo por colaborador** + uno por el usuario actual. Todos compiten por el monitor del objeto `Task`, y `synchronized` los serializa.

### Mecanismos usados

| Mecanismo | Dónde | Para qué |
|---|---|---|
| `synchronized` | `Task.modifyStatus`, `Item.addCollaborator/removeCollaborator` | Sección crítica protegida |
| `CopyOnWriteArrayList` | `Item.collaborators` | Lista thread-safe que itera sin bloquear |
| `Collections.synchronizedList` | `User.createdItems`, `User.sharedItems` | Listas sincronizadas |
| `ConcurrentHashMap` | `MainMenu.usersByEmail`, `MainMenu.items` | Storage thread-safe |
| `AtomicInteger` | `MainMenu.idCounter` | IDs únicos sin race condition |
| `Runnable` + `Thread.start()` + `Thread.join()` | `TaskWorker` + `MainMenu.changeTaskStatus` | Hilos paralelos coordinados |

---

## 🧪 Guía de Uso del Proyecto en Java

### ▶️ Cómo ejecutar la aplicación

**Requisitos:** JDK 17 o superior.

**Opción A — con Gradle wrapper:**

```bash
./gradlew run
```

**Opción B — desde un IDE (IntelliJ, VS Code, Eclipse):**

1. Abrir la carpeta del proyecto como proyecto Gradle.
2. Esperar a que IntelliJ/IDE indexe el proyecto.
3. Ubicar la clase principal: `org.example.Main`.
4. Ejecutar la clase principal (botón ▶️ o `Shift+F10`).

---

### 🔐 Login flexible + elección de tipo

Al iniciar, el sistema **acepta cualquier email y contraseña** (no valida contra una BD). Después pregunta el **tipo de usuario** para la sesión:

```
==========================================
    TODO COLLABORATIVE APP - Consola
==========================================
Podes ingresar cualquier email y contrasena,
o usar uno de los usuarios pre-cargados:
  - angel@mail.com  (Angel)
  - maria@mail.com  (Maria)
  - pedro@mail.com  (Pedro)

Email: tu@email.com
Contrasena: lo-que-sea

Que tipo de usuario quieres usar para esta sesion?
  1. ClassicUser  (limite de 5 items y 3 colaboradores)
  2. PremiumUser  (sin limites)
Opcion: 1
```

### 👤 Usuarios pre-cargados (para probar rápido)

| Email | Contraseña | Tipo recomendado |
|---|---|---|
| `angel@mail.com` | cualquiera | PremiumUser |
| `maria@mail.com` | cualquiera | ClassicUser |
| `pedro@mail.com` | cualquiera | ClassicUser |

---

## 🧾 Indicaciones Específicas para Probar el Sistema

Una vez logueado, ves el menú principal:

```
========== MENU PRINCIPAL ==========
1. Crear Task
2. Crear Reminder          (Strategy)
3. Compartir item          (synchronized)
4. Cambiar estado de Task  (synchronized + hilos si la task esta compartida)
5. Disparar alerta Reminder
6. Ver mis items creados
7. Ver items compartidos conmigo
8. Ver todos los items del sistema
9. Cambiar de usuario
0. Salir
```

### 📋 Crear Task (opción 1)

**Pasos:**

1. Tipear el título.
2. Tipear la descripción.
3. Elegir prioridad: `1=HIGH 2=MEDIUM 3=LOW`.
4. Elegir estado inicial: `1=PENDING 2=IN_PROGRESS 3=COMPLETED 4=CANCELED` (Enter = PENDING).

**Formato esperado:**

| Campo | Tipo | Ejemplo |
|---|---|---|
| Título | texto libre | `Sprint Review` |
| Descripción | texto libre | `Demo del proyecto` |
| Prioridad | 1, 2 o 3 | `1` |
| Estado inicial | 1-4 (o Enter) | `2` |

---

### ⏰ Crear Reminder (opción 2)

**Pasos:**

1. Título.
2. Descripción.
3. Prioridad.
4. Fecha y hora (formato libre).
5. Elegir estrategia de notificación: `1=Email 2=Mensaje de texto`.

**Formato esperado:**

| Campo | Tipo | Ejemplo |
|---|---|---|
| Título | texto libre | `Reunión con cliente` |
| Fecha/Hora | string | `2026-05-20 14:00` |
| Estrategia | 1 o 2 | `1` (Email) |

---

### 🤝 Compartir item (opción 3)

**Pasos:**

1. Ingresar el ID del item a compartir.
2. Ingresar el email del colaborador (debe existir en el sistema).
3. El item queda compartido (Maria/Pedro lo verán en su lista de "compartidos conmigo").

**Reglas:**
- Solo el **owner** del item puede compartir.
- No te podés compartir contigo mismo.
- Si sos `ClassicUser`, hay un máximo de 3 colaboradores por item.

---

### 🔄 Cambiar estado de Task (opción 4) — con concurrencia

**Pasos:**

1. Ingresar el ID de la Task.
2. Elegir el nuevo estado: `1=PENDING 2=IN_PROGRESS 3=COMPLETED 4=CANCELED`.

**Comportamiento:**
- Si la task **NO está compartida** → cambio simple en el hilo actual.
- Si la task **SÍ está compartida** → el sistema lanza **un hilo por cada colaborador** (más uno por el usuario actual), todos proponiendo estados distintos en paralelo. El `synchronized` en `Task.modifyStatus` los serializa: solo uno modifica a la vez, los demás quedan en estado **BLOCKED** hasta que se libere el monitor.

---

### 🔔 Disparar alerta de Reminder (opción 5)

**Pasos:**

1. Ingresar el ID del Reminder.
2. El sistema imprime la alerta usando la estrategia configurada:
   - `[EMAIL] Recordatorio '...' programado para ...`
   - `[MENSAJE DE TEXTO] Recordatorio '...' programado para ...`

---

## 📥📤 Ejemplos Prácticos de Entrada y Salida

### 🔹 Ejemplo 1: Login + crear Task

**Entrada:**
```
Email: angel@mail.com
Contrasena: 1234
Tipo (1=Classic 2=Premium): 2
```

**Salida:**
```
Bienvenido Angel (PremiumUser)
```

Luego en el menú:
```
Opcion: 1
Titulo: Sprint Review
Descripcion: Demo del proyecto
Prioridad: 1=HIGH 2=MEDIUM 3=LOW
Opcion: 1
Estado inicial: 1=PENDING 2=IN_PROGRESS 3=COMPLETED 4=CANCELED
Opcion (Enter = PENDING): [Enter]
```

**Salida esperada:**
```
Task creada con ID 1 (estado inicial: PENDING)
```

---

### 🔹 Ejemplo 2: Crear Reminder con Strategy

**Entrada:**
```
Opcion: 2
Titulo: Reunion con cliente
Descripcion: Mostrar el demo
Prioridad: 1=HIGH 2=MEDIUM 3=LOW
Opcion: 2
Fecha/Hora (ej 2026-05-15 10:00): 2026-05-20 14:00
Estrategia de notificacion: 1=Email 2=Mensaje de texto
Opcion: 1
```

**Salida esperada:**
```
Reminder creado con ID 2
```

Luego, opción 5 con ID 2:
```
[EMAIL] Recordatorio 'Reunion con cliente' programado para 2026-05-20 14:00
```

---

### 🔹 Ejemplo 3: Compartir Task

**Entrada:**
```
Opcion: 3
ID del item: 1
Email del colaborador: maria@mail.com
```

**Salida esperada:**
```
[Item Sprint Review] Maria agregado como colaborador.
```

---

### 🔹 Ejemplo 4: Cambiar estado con concurrencia real

**Pre-condición:** Task con ID 1 compartida con Maria y Pedro.

**Entrada:**
```
Opcion: 4
ID de la Task: 1
Estado actual: PENDING
Estado: 1=PENDING 2=IN_PROGRESS 3=COMPLETED 4=CANCELED
Opcion: 3
```

**Salida esperada (el orden de los hilos puede variar):**
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

> 💡 **Observación:** el orden en que entran los hilos al `synchronized` puede variar entre ejecuciones (es lo esperado en concurrencia). Lo importante es que **las transiciones de estado siempre son ordenadas y nunca queda un estado intermedio corrupto**.

---

### 🔹 Ejemplo 5: Límite de items en ClassicUser

**Entrada:** logueado como Maria (Classic), crear el 6to item:

```
Opcion: 1
```

**Salida esperada (sin pedir título ni descripción):**
```
[Limite alcanzado] No puedes crear mas items. Tu cuenta (ClassicUser) ya llego al tope.
```

---

## 📊 Diagramas UML

En la carpeta `diagramasUML/` están todos los archivos (`.puml` para PlantUML y `.drawio` para draw.io):

| Archivo | Contenido |
|---|---|
| `1_diagrama_clases_v2.puml` / `.drawio` | Diagrama de clases (Strategy + concurrencia). |
| `2_diagrama_actividad_v2.puml` / `.drawio` | Actividad: cambiar estado de una Task (`modifyStatus`) con 4 swimlanes (Sistema, Colaboradores, Hilo-Maria, Hilo-Pedro). |
| `3_secuencia_cambiar_estado.puml` / `.drawio` | Secuencia: cambiar el estado de una task con `par` + `synchronized` y los hilos compitiendo por el monitor. |
| `4_diagrama_objetos.puml` / `.drawio` | Diagrama de objetos: snapshot del sistema con instancias concretas (Angel, Maria, Pedro, una Task compartida, un Reminder con Strategy y dos TaskWorker). |

**Cómo visualizarlos:**
- **`.puml`** → extensión **PlantUML** en VS Code, o pegar en https://plantuml.com/plantuml
- **`.drawio`** → abrir directamente en https://app.diagrams.net

---

## 🧠 Explicación General del Funcionamiento

El sistema se estructura en torno a tres elementos principales:

- **👤 Usuarios** — crean y gestionan items. Hay dos tipos:
  - `ClassicUser`: con límites (5 items, 3 colaboradores por item).
  - `PremiumUser`: sin límites.
- **📋 Tasks** — actividades con estado (PENDING → IN_PROGRESS → COMPLETED / CANCELED).
- **⏰ Reminders** — recordatorios con fecha/hora y estrategia de notificación.

Las Tasks pueden:
- Tener prioridad (HIGH / MEDIUM / LOW).
- Ser compartidas con otros usuarios.
- Cambiar de estado de forma **concurrente** (con varios hilos competing por el monitor `synchronized`).

Los Reminders usan el **patrón Strategy**: la forma en que entregan su notificación (Email o Mensaje de Texto) puede cambiar en tiempo de ejecución sin modificar la clase.

---

## ✅ Cumplimiento de los Criterios de Evaluación (Entregable 2)

### 1. Implementación de observaciones del entregable 1 — **40 pts**

| Sub-criterio | Evidencia |
|---|---|
| Observaciones del entregable 1 aplicadas (Factory removido) | Commit `1110d58` borra Factory y adapta MainMenu. |
| Commits claros y frecuentes | 50+ commits con mensajes `feat:`, `fix:`, `docs(uml):`, `refactor:`, `revert:`, `merge:`. |
| Uso adecuado de **ramas y fusión ordenada** | 14+ ramas mergeadas con `--no-ff` (visible en `git log --graph --all`). |
| Diagrama de clases con mejoras en relaciones, encapsulamiento e interfaces | `diagramasUML/1_diagrama_clases_v2.puml`. |
| Implementación Java coherente con los diagramas | Cada clase del código aparece en el diagrama y viceversa. Compila limpio. |

### 2. Diagramas de actividad y secuencia con concurrencia — **40 pts**

| Sub-criterio | Evidencia |
|---|---|
| Representan clara y completamente un proceso concurrente | Actividad con `fork`/`join` y 4 swimlanes; Secuencia con `par` + `synchronized`. |
| Identifican hilos, flujo de control y comunicación entre objetos | Hilos `Hilo-Maria`, `Hilo-Pedro`, decisión `Monitor disponible?`, estereotipo `«synchronized»`. |
| Notación UML correcta y consistente | PlantUML validado generando PNG. |

### 3. Implementación en Java — **20 pts**

| Sub-criterio | Evidencia |
|---|---|
| Funcional, organizada, aplica POO | Compila con JDK 17 y corre end-to-end. Herencia, polimorfismo, encapsulamiento. |
| Usa clases, interfaces, encapsulamiento y concurrencia | 3 interfaces, 2 clases abstractas, `synchronized`, `CopyOnWriteArrayList`, `ConcurrentHashMap`, `AtomicInteger`, `Runnable` + `Thread`. |
| Código limpio y modular | Paquetes por responsabilidad (`catalog`, `model`, `patterns`, `thread`, `ui`). Todas las clases documentadas con **JavaDoc**. |

---

## 📎 Conclusión

Este proyecto demuestra la aplicación de **principios de POO**, un **patrón de diseño** (Strategy) y **concurrencia con hilos** en Java, integrados en un sistema funcional, modular y documentado. Está preparado para la siguiente etapa (GUI con JavaFX y persistencia con base de datos) sin necesidad de reescrituras mayores: solo agregar las capas correspondientes.
