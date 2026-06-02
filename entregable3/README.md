# Entregable 3 — Interfaz Gráfica y Persistencia

Este folder contiene el **sistema de diseño** para la interfaz gráfica del proyecto.

## 📁 Contenido

| Archivo | Para qué sirve |
|---|---|
| [`paleta-colores.md`](paleta-colores.md) | Paleta de colores completa con códigos hex y guía de uso |
| [`pantallas.md`](pantallas.md) | Mockups de cada pantalla descritos con medidas y componentes |
| [`preview.html`](preview.html) | **Preview visual** — abrir en navegador para ver el diseño en vivo |

## 🎨 Resumen del diseño

**Estilo**: Moderno, limpio y profesional. Inspirado en Notion / Linear / Todoist.

**Paleta principal**: Indigo (#6366F1) con acentos vibrantes para prioridades.

**Tipografía**: Inter (gratis en Google Fonts, alternativa SF Pro / Segoe UI).

**Componente clave**: `ListView` con cards de tareas/recordatorios diferenciados por color (prioridad) e ícono (Task vs Reminder).

## 🎯 Pantallas a diseñar en Figma

1. **Login** — email + contraseña + tipo de usuario (Classic/Premium)
2. **Home** — ListView de todas las tareas y recordatorios del usuario
3. **Detalle de Task** — información completa + colaboradores + cambio de estado
4. **Detalle de Reminder** — información + disparar notificación
5. **Crear Task / Crear Reminder** — modal con formulario
6. **Compartir** — modal para agregar colaborador por email

## 🚀 Cómo usar este sistema en Figma

1. Abrí Figma y creá un proyecto nuevo
2. En `Local styles` (panel derecho), agregá los colores de `paleta-colores.md` como `Color styles`
3. Importá la fuente **Inter** (Plugin → Google Fonts)
4. Usá `pantallas.md` como guía para armar cada frame
5. Abrí `preview.html` en el navegador como referencia visual de qué hacer

## ✅ Criterios del entregable 3 cubiertos por el diseño

| Criterio de la rúbrica | Cómo lo cubre el diseño |
|---|---|
| Interfaz gráfica con JavaFX | Mockups listos para implementar 1:1 en JavaFX (FXML + CSS) |
| ListView de tareas | Pantalla Home con `ListView` como componente principal |
| Colores según prioridad | HIGH=rojo, MEDIUM=ámbar, LOW=verde — visibles en cada card |
| Icono para diferenciar recordatorios | Task = ✓ (checkbox), Reminder = 🔔 (campana) |
| Almacenamiento de datos | Pendiente: SQLite + JDBC (próxima rama `entregable3/persistencia-sqlite`) |
