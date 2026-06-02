# 🎨 Paleta de Colores — Sistema "Aurora"

Paleta moderna y profesional inspirada en Linear / Notion / Todoist.
**Indigo como color principal** + acentos semánticos para prioridades y estados.

---

## 🟣 Paleta Primaria (Brand)

| Token | Hex | Vista previa | Uso |
|---|---|---|---|
| `primary-50` | `#EEF2FF` | ![](https://placehold.co/60x30/EEF2FF/EEF2FF.png) | Background suave de cards primarias |
| `primary-100` | `#E0E7FF` | ![](https://placehold.co/60x30/E0E7FF/E0E7FF.png) | Hover muy suave |
| `primary-400` | `#818CF8` | ![](https://placehold.co/60x30/818CF8/818CF8.png) | Hover de botones primarios |
| **`primary-500`** | **`#6366F1`** | ![](https://placehold.co/60x30/6366F1/6366F1.png) | **Color principal** — botones, links, focus |
| `primary-600` | `#4F46E5` | ![](https://placehold.co/60x30/4F46E5/4F46E5.png) | Pressed / active |
| `primary-700` | `#4338CA` | ![](https://placehold.co/60x30/4338CA/4338CA.png) | Texto sobre fondo claro de marca |

---

## ⚪ Paleta Neutral (Backgrounds y Texto)

| Token | Hex | Vista previa | Uso |
|---|---|---|---|
| `bg-app` | `#F9FAFB` | ![](https://placehold.co/60x30/F9FAFB/F9FAFB.png) | Fondo general de la app |
| `bg-surface` | `#FFFFFF` | ![](https://placehold.co/60x30/FFFFFF/FFFFFF.png) | Fondo de cards y modales |
| `bg-muted` | `#F3F4F6` | ![](https://placehold.co/60x30/F3F4F6/F3F4F6.png) | Inputs deshabilitados, fondos suaves |
| `border-light` | `#E5E7EB` | ![](https://placehold.co/60x30/E5E7EB/E5E7EB.png) | Bordes finos, divisores |
| `border-default` | `#D1D5DB` | ![](https://placehold.co/60x30/D1D5DB/D1D5DB.png) | Bordes de inputs |
| `text-muted` | `#9CA3AF` | ![](https://placehold.co/60x30/9CA3AF/9CA3AF.png) | Placeholders, hints |
| `text-secondary` | `#6B7280` | ![](https://placehold.co/60x30/6B7280/6B7280.png) | Texto secundario, metadata |
| `text-body` | `#374151` | ![](https://placehold.co/60x30/374151/374151.png) | Texto principal del body |
| `text-heading` | `#111827` | ![](https://placehold.co/60x30/111827/111827.png) | Títulos y headings |

---

## 🚦 Colores Semánticos por **Prioridad**

Cada Task/Reminder se muestra con una **barra lateral del color de su prioridad** (4px de ancho).

| Prioridad | Hex | Vista previa | Uso |
|---|---|---|---|
| **HIGH** | `#EF4444` | ![](https://placehold.co/60x30/EF4444/EF4444.png) | Banda izquierda + badge rojo |
| **MEDIUM** | `#F59E0B` | ![](https://placehold.co/60x30/F59E0B/F59E0B.png) | Banda izquierda + badge ámbar |
| **LOW** | `#10B981` | ![](https://placehold.co/60x30/10B981/10B981.png) | Banda izquierda + badge verde |

### Variantes suaves para fondos de badges

| Token | Hex | Para qué |
|---|---|---|
| `high-soft` | `#FEE2E2` | Fondo del badge "Prioridad: ALTA" |
| `medium-soft` | `#FEF3C7` | Fondo del badge "Prioridad: MEDIA" |
| `low-soft` | `#D1FAE5` | Fondo del badge "Prioridad: BAJA" |

---

## 📊 Colores Semánticos por **Estado**

| Estado | Hex | Vista previa | Vista |
|---|---|---|---|
| **PENDING** | `#6B7280` | ![](https://placehold.co/60x30/6B7280/6B7280.png) | Chip gris con punto • |
| **IN_PROGRESS** | `#3B82F6` | ![](https://placehold.co/60x30/3B82F6/3B82F6.png) | Chip azul con punto • |
| **COMPLETED** | `#10B981` | ![](https://placehold.co/60x30/10B981/10B981.png) | Chip verde con ✓ |
| **CANCELED** | `#EF4444` | ![](https://placehold.co/60x30/EF4444/EF4444.png) | Chip rojo con ✗ |

---

## 🎭 Colores de Acento

| Token | Hex | Uso |
|---|---|---|
| `accent-reminder` | `#EC4899` | Color exclusivo del ícono 🔔 de Reminder |
| `accent-premium` | `#8B5CF6` | Badge "PREMIUM" en el avatar del usuario |
| `accent-classic` | `#06B6D4` | Badge "CLASSIC" en el avatar del usuario |

---

## 🖼️ Iconografía

**Task** → `✓` (checkbox) en color `primary-500` (#6366F1).
**Reminder** → `🔔` (campana) en color `accent-reminder` (#EC4899).

Los íconos van en un **círculo de 40×40px con fondo soft** del color correspondiente.

| Tipo | Ícono | Fondo del círculo |
|---|---|---|
| Task | ✓ blanco sobre indigo | `#6366F1` |
| Reminder | 🔔 blanco sobre pink | `#EC4899` |

---

## 🌗 Sombras

| Token | Valor CSS | Uso |
|---|---|---|
| `shadow-sm` | `0 1px 2px rgba(17,24,39,0.05)` | Cards normales |
| `shadow-md` | `0 4px 6px rgba(17,24,39,0.07), 0 2px 4px rgba(17,24,39,0.06)` | Cards hover |
| `shadow-lg` | `0 10px 15px rgba(17,24,39,0.1), 0 4px 6px rgba(17,24,39,0.05)` | Modales y popovers |

---

## 📐 Radios

| Token | Valor | Uso |
|---|---|---|
| `radius-sm` | `4px` | Badges, chips |
| `radius-md` | `8px` | Inputs, botones |
| `radius-lg` | `12px` | Cards de items |
| `radius-xl` | `16px` | Modales |

---

## 🅰️ Tipografía

**Familia**: [Inter](https://fonts.google.com/specimen/Inter) (gratis en Google Fonts).
**Fallback**: `-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif`.

| Token | Tamaño | Peso | Line height | Uso |
|---|---|---|---|---|
| `display` | 32px | 700 (Bold) | 1.2 | Titulo de pantalla principal |
| `h1` | 24px | 700 | 1.25 | Titulo de sección |
| `h2` | 20px | 600 (SemiBold) | 1.3 | Titulo de card |
| `h3` | 18px | 600 | 1.35 | Sub-titulo |
| `body-lg` | 16px | 400 (Regular) | 1.5 | Texto principal |
| `body` | 14px | 400 | 1.5 | Texto general |
| `small` | 12px | 500 (Medium) | 1.4 | Labels, metadata |
| `tiny` | 11px | 500 | 1.3 | Tags muy chicos |

---

## 📋 Tokens CSS listos para copiar

```css
:root {
  /* Brand */
  --primary-50:  #EEF2FF;
  --primary-100: #E0E7FF;
  --primary-400: #818CF8;
  --primary-500: #6366F1;
  --primary-600: #4F46E5;
  --primary-700: #4338CA;

  /* Neutral */
  --bg-app:        #F9FAFB;
  --bg-surface:    #FFFFFF;
  --bg-muted:      #F3F4F6;
  --border-light:  #E5E7EB;
  --border-default:#D1D5DB;
  --text-muted:    #9CA3AF;
  --text-secondary:#6B7280;
  --text-body:     #374151;
  --text-heading:  #111827;

  /* Semantic - priority */
  --priority-high:    #EF4444;
  --priority-medium:  #F59E0B;
  --priority-low:     #10B981;
  --priority-high-soft:   #FEE2E2;
  --priority-medium-soft: #FEF3C7;
  --priority-low-soft:    #D1FAE5;

  /* Semantic - status */
  --status-pending:     #6B7280;
  --status-in-progress: #3B82F6;
  --status-completed:   #10B981;
  --status-canceled:    #EF4444;

  /* Accent */
  --accent-reminder: #EC4899;
  --accent-premium:  #8B5CF6;
  --accent-classic:  #06B6D4;
}
```

---

## 🎨 Para crear los Color Styles en Figma

1. Abrí el panel **Local styles** (parte derecha)
2. Click en `+` junto a **Color**
3. Por cada token de las tablas, agregá un Color Style con:
   - **Nombre**: el token (ej: `primary/500`)
   - **Color**: el hex correspondiente
4. Agrupalos por carpetas: `primary/`, `neutral/`, `priority/`, `status/`, `accent/`

Una vez creados, cada componente que diseñes va a poder referenciar estos colores de forma centralizada.
