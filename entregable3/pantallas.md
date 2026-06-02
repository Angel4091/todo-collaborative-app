# 📱 Mockups de Pantallas

Cada sección describe una pantalla con sus medidas, componentes y comportamiento.
**Tamaño base de frame en Figma**: `1280 × 800px` (ventana de escritorio típica).

---

## 1️⃣ Login

```
┌────────────────────────────────────────────────────┐
│                                                    │
│                  ✓ TodoFlow                        │  ← logo + nombre
│             Tareas colaborativas                   │
│                                                    │
│         ┌──────────────────────────┐               │
│         │  Email                   │               │
│         │  ┌────────────────────┐  │               │
│         │  │ angel@mail.com     │  │               │
│         │  └────────────────────┘  │               │
│         │                          │               │
│         │  Contraseña              │               │
│         │  ┌────────────────────┐  │               │
│         │  │ ••••••             │  │               │
│         │  └────────────────────┘  │               │
│         │                          │               │
│         │  Tipo de usuario         │               │
│         │  ◉ Classic   ○ Premium   │               │
│         │                          │               │
│         │   ╔══════════════════╗   │               │
│         │   ║   Iniciar sesión  ║   │               │
│         │   ╚══════════════════╝   │               │
│         └──────────────────────────┘               │
│                                                    │
└────────────────────────────────────────────────────┘
```

| Componente | Specs |
|---|---|
| Card del formulario | 400 × auto, fondo `#FFFFFF`, radius `16px`, shadow-lg, padding `32px` |
| Inputs | 100% × 44px, border `1px solid #D1D5DB`, radius `8px`, padding `12px 16px`, font `body` |
| Radio buttons | 18 × 18px, color activo `#6366F1` |
| Botón "Iniciar sesión" | 100% × 48px, fondo `#6366F1`, texto blanco, radius `8px`, font `body-lg semibold` |
| Logo | 32 × 32px, ícono `✓` en círculo `#6366F1` |
| Título "TodoFlow" | `display`, color `#111827` |
| Subtítulo | `body`, color `#6B7280` |

**Estados del botón**:
- Default: `#6366F1`
- Hover: `#818CF8`
- Pressed: `#4F46E5`
- Disabled (campos vacíos): `#E5E7EB` con texto `#9CA3AF`

---

## 2️⃣ Home (Pantalla principal)

```
┌──────────────────────────────────────────────────────────────────────┐
│  ✓ TodoFlow    [Buscar...........]    🔔(3)   [👤 Angel▼]            │ ← header
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Sidebar           │  Mis Tareas                       [+ Nueva ▼]  │
│  ──────            │  ──────────                                      │
│  📥 Bandeja        │                                                  │
│  ⭐ Importantes    │  ┌─ HIGH ────────────────────────────────────┐  │
│  📅 Hoy            │  │ │ ✓  Sprint Review                        │  │
│  ✅ Completadas    │  │ │    Demo del proyecto                    │  │
│                    │  │ │    [IN_PROGRESS]  📅 hoy   👥 +2        │  │
│  ───────           │  └─────────────────────────────────────────┘  │
│  Compartidas       │                                                  │
│  con Maria         │  ┌─ MEDIUM ──────────────────────────────────┐  │
│  con Pedro         │  │ │ 🔔  Reunión con cliente                 │  │
│                    │  │ │     Mostrar el demo                     │  │
│                    │  │ │     📅 2026-05-20 14:00  [Email]        │  │
│                    │  └─────────────────────────────────────────┘  │
│                    │                                                  │
│                    │  ┌─ LOW ──────────────────────────────────────┐ │
│                    │  │ │ ✓  Estudiar POO                          │ │
│                    │  │ │    Repasar concurrencia                  │ │
│                    │  │ │    [PENDING]                             │ │
│                    │  └─────────────────────────────────────────┘  │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```

| Componente | Specs |
|---|---|
| Header | 100% × 64px, fondo `#FFFFFF`, border-bottom `1px #E5E7EB` |
| Sidebar | 240 × 100%, fondo `#F9FAFB`, padding `24px 16px` |
| Cada item del sidebar | 100% × 36px, radius `8px`, hover `#F3F4F6`, activo `#EEF2FF` con texto `#6366F1` |
| Botón "+ Nueva" | 120 × 40px, fondo `#6366F1`, texto blanco, radius `8px`, dropdown con "Task" y "Reminder" |
| **Card de item (ListView item)** | 100% × auto, padding `16px`, radius `12px`, fondo `#FFFFFF`, shadow-sm |
| **Banda lateral de prioridad** | 4px de ancho a la izquierda de la card, color según prioridad |
| Ícono Task | Círculo 40 × 40px, fondo `#6366F1`, ícono `✓` blanco |
| Ícono Reminder | Círculo 40 × 40px, fondo `#EC4899`, ícono `🔔` blanco |
| Título del item | `h3 semibold`, `#111827` |
| Descripción | `body`, `#6B7280`, máximo 1 línea con ellipsis |
| Chip de estado | padding `4px 10px`, radius `4px`, font `tiny semibold`, color según estado |
| Avatar grupal (colaboradores) | 24 × 24px, overlap de 8px, mostrar máximo 3 + "+N" |

### Cards detalladas

#### Task card

```
┌──┬──────────────────────────────────────────────┐
│  │  ✓   Sprint Review                            │
│██│      Demo del proyecto                        │  ← banda HIGH = #EF4444
│  │      ● IN_PROGRESS    📅 hoy    👥👥+1        │
└──┴──────────────────────────────────────────────┘
```

#### Reminder card

```
┌──┬──────────────────────────────────────────────┐
│  │  🔔  Reunión con cliente                      │
│██│      Mostrar el demo                          │  ← banda MEDIUM = #F59E0B
│  │      📅 2026-05-20 14:00   ✉️ Email           │
└──┴──────────────────────────────────────────────┘
```

#### Estados de chip

| Estado | Color de fondo | Color de texto | Ícono |
|---|---|---|---|
| PENDING | `#F3F4F6` | `#6B7280` | • |
| IN_PROGRESS | `#DBEAFE` | `#3B82F6` | • |
| COMPLETED | `#D1FAE5` | `#10B981` | ✓ |
| CANCELED | `#FEE2E2` | `#EF4444` | ✗ |

---

## 3️⃣ Detalle de Task (panel lateral o modal)

```
┌──────────────────────────────────────────────┐
│  ← Volver                              ✏️  🗑️ │
├──────────────────────────────────────────────┤
│                                              │
│  [HIGH]  Sprint Review                       │
│                                              │
│  Descripción                                 │
│  Demo del proyecto para el catedrático       │
│                                              │
│  Estado actual                               │
│  ┌──────────────┐                            │
│  │  ● IN_PROGRESS  ▼                         │
│  └──────────────┘                            │
│  (al cambiar acá se dispara el concurrencia) │
│                                              │
│  Owner                                       │
│  👤 Angel (PremiumUser)                      │
│                                              │
│  Colaboradores                               │
│  👤 María                              [×]   │
│  👤 Pedro                              [×]   │
│  [+ Agregar colaborador]                     │
│                                              │
│  Metadata                                    │
│  📅 Creada hoy                               │
│  🔄 Última modificación: ahora               │
│                                              │
└──────────────────────────────────────────────┘
```

| Componente | Specs |
|---|---|
| Header | 100% × 56px, botón "Volver" + acciones a la derecha |
| Badge de prioridad | inline, padding `4px 12px`, color según prioridad |
| Título | `h1`, `#111827` |
| Labels de sección | `small uppercase`, `#9CA3AF`, letter-spacing `0.05em` |
| Dropdown de estado | 200px, mismo estilo que un input, con ícono del estado actual |
| Lista de colaboradores | Cada uno es una row de 48px con avatar, nombre y botón × |
| Botón "+ Agregar colaborador" | ghost, color `#6366F1`, ícono + |

---

## 4️⃣ Modal: Crear Task

```
┌─────────────────────────────────────────┐
│  Nueva Task                          ✕  │
├─────────────────────────────────────────┤
│                                         │
│  Título *                               │
│  ┌─────────────────────────────────┐    │
│  │ Sprint Review                    │   │
│  └─────────────────────────────────┘    │
│                                         │
│  Descripción                            │
│  ┌─────────────────────────────────┐    │
│  │                                  │   │
│  │                                  │   │
│  └─────────────────────────────────┘    │
│                                         │
│  Prioridad                              │
│  [ HIGH ] [ MEDIUM ] [ LOW ]            │
│                                         │
│  Estado inicial                         │
│  [ PENDING ] [ IN_PROGRESS ] ...        │
│                                         │
│              [ Cancelar ] [  Crear  ]   │
└─────────────────────────────────────────┘
```

| Componente | Specs |
|---|---|
| Modal | 480 × auto, fondo `#FFFFFF`, radius `16px`, shadow-lg, centrado |
| Overlay | 100% × 100%, fondo `rgba(17,24,39,0.5)`, backdrop-filter blur 4px |
| Pills de prioridad/estado | toggle group, 80 × 36px, radius `8px`, activo con fondo del color soft |
| Botón "Crear" | primary, 100 × 40px |
| Botón "Cancelar" | ghost, mismo tamaño |

---

## 5️⃣ Modal: Crear Reminder

Igual al de Task pero agregando:

```
  Fecha y hora *
  ┌──────────────┐  ┌──────┐
  │ 2026-05-20   │  │ 14:00 │
  └──────────────┘  └──────┘

  Estrategia de notificación
  ◉ ✉️ Email
  ○ 💬 Mensaje de texto
```

---

## 6️⃣ Modal: Compartir Item

```
┌─────────────────────────────────┐
│  Compartir "Sprint Review"  ✕   │
├─────────────────────────────────┤
│                                 │
│  Email del colaborador          │
│  ┌─────────────────────────┐    │
│  │ maria@mail.com           │   │
│  └─────────────────────────┘    │
│                                 │
│  [ Cancelar ] [  Compartir  ]   │
└─────────────────────────────────┘
```

---

## 🎨 Estados interactivos generales

| Componente | Default | Hover | Pressed | Focus |
|---|---|---|---|---|
| Botón primario | bg `#6366F1` | bg `#818CF8` | bg `#4F46E5` | ring `#A5B4FC` |
| Botón ghost | text `#6366F1` | bg `#EEF2FF` | bg `#E0E7FF` | ring `#A5B4FC` |
| Input | border `#D1D5DB` | border `#9CA3AF` | — | border `#6366F1` + ring |
| Card del ListView | shadow-sm | shadow-md + transform `translateY(-1px)` | — | — |

---

## ⌨️ Atajos de teclado sugeridos

| Atajo | Acción |
|---|---|
| `Ctrl/Cmd + N` | Nueva Task |
| `Ctrl/Cmd + R` | Nuevo Reminder |
| `Ctrl/Cmd + K` | Búsqueda global |
| `Esc` | Cerrar modal / volver |

---

## 📐 Grid y espaciado

**Sistema de espaciado** (escala 4px):

| Token | Valor | Uso |
|---|---|---|
| `space-1` | 4px | Separación mínima |
| `space-2` | 8px | Entre ícono y texto |
| `space-3` | 12px | Padding interno de chips |
| `space-4` | 16px | Padding de cards |
| `space-6` | 24px | Entre secciones |
| `space-8` | 32px | Padding de modales |
| `space-12` | 48px | Margen entre bloques grandes |
