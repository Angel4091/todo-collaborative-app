#!/bin/bash
# ============================================================
# Script para copiar el codigo a otro repo paquete por paquete
# con commits y ramas separadas.
#
# COMO USARLO:
#   1. Cambia SRC y DST a las rutas correctas (abajo).
#   2. Abri Git Bash en CUALQUIER carpeta y ejecuta:
#        bash copiar-a-repo.sh
#   3. El script se pausa entre pasos para que revises cada commit.
# ============================================================

# ----- CONFIGURACION (editar antes de correr) -----
SRC="/c/Users/Angel/Desktop/todo-collaborative-app"
DST="/c/Users/Angel/Desktop/proyectofinalpoo2026-grupoproyectofinalh"
# --------------------------------------------------

set -e  # frena si algo falla

paso() {
  echo ""
  echo "=========================================================="
  echo "  PASO $1: $2"
  echo "=========================================================="
}

pausa() {
  echo ""
  echo "  [Enter para continuar al siguiente paso, Ctrl+C para abortar]"
  read -r
}

mkdir_safe() {
  mkdir -p "$1"
}

# ============================================================
# Verificaciones iniciales
# ============================================================
if [ ! -d "$SRC/src/main/java/org/example" ]; then
  echo "ERROR: SRC no apunta al repo origen. Editar la variable SRC al inicio del script."
  exit 1
fi
if [ ! -d "$DST" ]; then
  echo "ERROR: DST no existe. Crear primero el repo destino y editar la variable DST."
  exit 1
fi

cd "$DST"

# ============================================================
# PASO 0: Setup del proyecto Gradle
# ============================================================
paso 0 "Setup inicial del proyecto Gradle"
git checkout -B main
git checkout -b setup/proyecto

cp "$SRC/build.gradle" .
cp "$SRC/settings.gradle" .
cp "$SRC/.gitignore" .
mkdir_safe src/main/java/org/example

git add build.gradle settings.gradle .gitignore
git commit -m "chore: setup inicial del proyecto Gradle (Java 17)"
git push -u origin setup/proyecto || true
git checkout main
git merge --no-ff setup/proyecto -m "merge: setup/proyecto"
git push origin main || true
pausa

# ============================================================
# PASO 1: catalog (enums)
# ============================================================
paso 1 "catalog (Priority, Status)"
git checkout -b feature/catalog

mkdir_safe src/main/java/org/example/catalog
cp "$SRC/src/main/java/org/example/catalog/"*.java src/main/java/org/example/catalog/

git add src/main/java/org/example/catalog/
git commit -m "feat(catalog): agregar enums Priority y Status"
git push -u origin feature/catalog || true
git checkout main
git merge --no-ff feature/catalog -m "merge: feature/catalog"
git push origin main || true
pausa

# ============================================================
# PASO 2: patterns (Strategy)
# ============================================================
paso 2 "patterns (Strategy)"
git checkout -b feature/patterns

mkdir_safe src/main/java/org/example/patterns
cp "$SRC/src/main/java/org/example/patterns/"*.java src/main/java/org/example/patterns/

git add src/main/java/org/example/patterns/
git commit -m "feat(patterns): implementar patron Strategy de notificaciones (Email y MensajeTexto)"
git push -u origin feature/patterns || true
git checkout main
git merge --no-ff feature/patterns -m "merge: feature/patterns"
git push origin main || true
pausa

# ============================================================
# PASO 3: model (entidades de dominio)
# ============================================================
paso 3 "model (User, Item, Task, Reminder y subclases)"
git checkout -b feature/model

mkdir_safe src/main/java/org/example/model
cp "$SRC/src/main/java/org/example/model/"*.java src/main/java/org/example/model/

git add src/main/java/org/example/model/
git commit -m "feat(model): jerarquias User e Item con synchronized y CopyOnWriteArrayList"
git push -u origin feature/model || true
git checkout main
git merge --no-ff feature/model -m "merge: feature/model"
git push origin main || true
pausa

# ============================================================
# PASO 4: dao (persistencia)
# ============================================================
paso 4 "dao (UserDAO, ItemDAO)"
git checkout -b feature/dao

mkdir_safe src/main/java/org/example/dao
cp "$SRC/src/main/java/org/example/dao/"*.java src/main/java/org/example/dao/

git add src/main/java/org/example/dao/
git commit -m "feat(dao): capa de persistencia en memoria con ConcurrentHashMap"
git push -u origin feature/dao || true
git checkout main
git merge --no-ff feature/dao -m "merge: feature/dao"
git push origin main || true
pausa

# ============================================================
# PASO 5: service
# ============================================================
paso 5 "service (UserService, AuthService)"
git checkout -b feature/service

mkdir_safe src/main/java/org/example/service
cp "$SRC/src/main/java/org/example/service/"*.java src/main/java/org/example/service/

git add src/main/java/org/example/service/
git commit -m "feat(service): capa de servicio para usuarios y autenticacion"
git push -u origin feature/service || true
git checkout main
git merge --no-ff feature/service -m "merge: feature/service"
git push origin main || true
pausa

# ============================================================
# PASO 6: thread (TaskWorker)
# ============================================================
paso 6 "thread (TaskWorker para concurrencia)"
git checkout -b feature/thread

mkdir_safe src/main/java/org/example/thread
cp "$SRC/src/main/java/org/example/thread/"*.java src/main/java/org/example/thread/

git add src/main/java/org/example/thread/
git commit -m "feat(thread): TaskWorker (Runnable) para concurrencia en cambios de estado"
git push -u origin feature/thread || true
git checkout main
git merge --no-ff feature/thread -m "merge: feature/thread"
git push origin main || true
pausa

# ============================================================
# PASO 7: ui (MainMenu)
# ============================================================
paso 7 "ui (MainMenu con login flexible y concurrencia real)"
git checkout -b feature/ui

mkdir_safe src/main/java/org/example/ui
cp "$SRC/src/main/java/org/example/ui/"*.java src/main/java/org/example/ui/

git add src/main/java/org/example/ui/
git commit -m "feat(ui): menu de consola con login flexible, eleccion de tipo y concurrencia real"
git push -u origin feature/ui || true
git checkout main
git merge --no-ff feature/ui -m "merge: feature/ui"
git push origin main || true
pausa

# ============================================================
# PASO 8: Main (entry point)
# ============================================================
paso 8 "Main (entry point)"
git checkout -b feature/main

cp "$SRC/src/main/java/org/example/Main.java" src/main/java/org/example/

git add src/main/java/org/example/Main.java
git commit -m "feat: agregar entry point Main"
git push -u origin feature/main || true
git checkout main
git merge --no-ff feature/main -m "merge: feature/main"
git push origin main || true
pausa

# ============================================================
# PASO 9: diagramas UML + README
# ============================================================
paso 9 "diagramas UML + README"
git checkout -b feature/docs

mkdir_safe diagramasUML
cp "$SRC/diagramasUML/"*.puml diagramasUML/  2>/dev/null || true
cp "$SRC/diagramasUML/"*.drawio diagramasUML/ 2>/dev/null || true
cp "$SRC/README.md" .

git add diagramasUML/ README.md
git commit -m "docs: diagramas UML (clases, actividad, secuencia) + README final"
git push -u origin feature/docs || true
git checkout main
git merge --no-ff feature/docs -m "merge: feature/docs"
git push origin main || true

# ============================================================
# FIN
# ============================================================
echo ""
echo "=========================================================="
echo "  LISTO! Todos los paquetes copiados."
echo "=========================================================="
echo ""
echo "  Ahora podes verificar:"
echo "    git log --graph --oneline --all"
echo ""
echo "  Y compilar (si tenes Gradle wrapper):"
echo "    ./gradlew run"
echo ""
