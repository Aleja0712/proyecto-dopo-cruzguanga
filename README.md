# Proyecto Inicial: Slot Machine (Ciclo 3)

## Autores
* **Mary Alejandra Guanga García**
* **Samuel Esteban Cruz Rodríguez**[cite: 1, 6]

---

## Descripción del Proyecto
Simulador y solucionador de una máquina tragamonedas orientado a objetos, inspirado en el **Problem I (Slot Machine)** de la maratón mundial de programación **ICPC World Finals Baku 2025**.

El sistema modela una máquina tragamonedas de $n$ ruedas y $n$ símbolos cíclicos. A través de la clase `SlotMachineContest`, se resuelve el problema algorítmico encontrando la secuencia de giros necesaria para alcanzar el **jackpot** ($k = 1$, todos los símbolos visibles iguales) bajo la cota estricta de **10.000 acciones**, interactuando con la máquina como un oráculo de pruebas a ciegas.

---

## Arquitectura y Principios de Diseño
El diseño sigue una arquitectura desacoplada por capas y delegación estricta de responsabilidades:

* **`SlotMachine` (Testing Tool / Fachada):** Expone las operaciones públicas del simulador (`spin`, `distinctSymbols`, `makeVisible`, `makeInvisible`). Actúa como el oráculo del concurso.
* **`Eje`:** Administrador estructural del conjunto de ruedas. Coordina la distribución espacial en coordenadas y la delegación de giros sincronizados[cite: 6].
* **`Wheel`:** Representa cada rueda física de la máquina[cite: 6]. Maneja su propia lista circular de símbolos, la visibilidad gráfica mediante primitivas de dibujo y la rotación cíclica (`rotate`)[cite: 6].
* **`Symbol`:** Entidad que encapsula las características de cada figura (nombre, color y forma geométrica)[cite: 6].
* **`SlotMachineContest` (Solver / Oráculo):** Implementa el algoritmo de resolución interactivo (`solve`) manteniendo la máquina invisible, y el mecanismo de reproducción gráfica (`simulate`) sobre la máquina visible.

---

## Mini-Ciclos de Desarrollo
Siguiendo las metodologías **MDD** (Model-Driven Development) y entregas incrementales de **Extreme Programming (XP)**[cite: 6]:

1. **Mini-ciclo 1 (Estructura y Dominio):** Modelado de clases y delegación entre `SlotMachine`, `Eje`, `Wheel` y `Symbol`[cite: 6].
2. **Mini-ciclo 2 (Visualización e Invariantes):** Integración con la biblioteca gráfica de formas (`shapes`), control de visibilidad (`makeVisible` / `makeInvisible`) y encapsulamiento[cite: 6].
3. **Mini-ciclo 3 (Extensión y Algoritmo de Maratón):** Implementación del constructor dinámico `SlotMachine(n)`, diseño del algoritmo de descubrimiento de permutación por colisiones en `SlotMachineContest`, y control de la cota de acciones.
4. **Mini-ciclo 4 (Verificación BDD):** Batería de pruebas unitarias automatizadas con JUnit (`SlotMachineContestTest` y `SlotMachineContestCTest`).

---

## Estructura de Archivos del Repositorio

```text
├── slotmachine/                     # Código fuente del simulador
│   ├── SlotMachine.java             # Clase principal y fachada del juego
│   ├── SlotMachineContest.java      # Algoritmo de resolución y simulación
│   ├── Eje.java                     # Coordinador de ruedas
│   ├── Wheel.java                   # Rueda y lógica de rotación
│   ├── Symbol.java                  # Representación de símbolos
│   ├── SlotMachineContestTest.java  # Pruebas de unidad automatizadas (BDD)
│   ├── SlotMachineContestCTest.java # Pruebas de unidad compartidas
│   └── (clases de shapes)           # Canvas, Rectangle, Circle, etc.
├── tragamonedas.asta                # Modelo UML completo (Astah)
├── Retrospectiva.txt                # Documento consolidado de retrospectiva XP
└── README.md                        # Ficha técnica y documentación general

