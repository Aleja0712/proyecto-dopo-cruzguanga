# Proyecto Inicial: Slot Machine

## Autores
* **Mary Alejandra Guanga García**
* **Samuel Esteban Cruz Rodríguez**

---

## Descripción del Proyecto
Simulador y solucionador interactivo de una máquina tragamonedas orientado a objetos, inspirado en el **Problem I (Slot Machine)** de la maratón de programación **ICPC World Finals Baku 2025**.

El proyecto modela una máquina tragamonedas con ruedas independientes, secuencias circulares de símbolos de colores y soporte para visualización gráfica en BlueJ. A través de la clase `SlotMachineContest`, el sistema resuelve de forma algorítmica la configuración necesaria para alcanzar el **jackpot** ($k = 1$, todos los símbolos visibles idénticos) en menos de 10.000 acciones, interactuando con la máquina a ciegas como oráculo de pruebas.

---

## Evolución del Proyecto por Ciclos

### Ciclo 1: Modelo Básico y Encapsulamiento
* **Construcción del dominio inicial:** Modelado de las clases estructurales `SlotMachine`, `Eje`, `Wheel` y `Symbol`.
* **Manejo básico de componentes:** Adición y eliminación de ruedas y símbolos en posiciones específicas.
* **Control de visibilidad inicial:** Integración con primitivas gráficas (`Canvas`, `Rectangle`, `Circle`) para mostrar u ocultar la máquina (`makeVisible` / `makeInvisible`).
* **Verificación:** Pruebas de unidad tempranas sobre creación y consistencia estructural de los objetos.

### Ciclo 2: Operaciones Dinámicas, Estado y Refactoring
* **Manipulación de ruedas:** Operaciones de intercambio (`swap`), bloqueo individual (`lock`) y desbloqueo (`unlock`) para impedir giros accidentales.
* **Estrategias de rotación:** Giros aleatorios (`spin()`), giros específicos por pasos (`spin(wheel, steps)`), posicionamiento forzado por color (`placeSymbol`) y configuración coordinada (`spin(setSymbols)`).
* **Consultas de estado:** Inspección del número de símbolos distintos (`distinctSymbols`), consulta de colores visibles (`symbols`), verificación de jackpot (`isjackpot`) y reporte de configuración (`configuration`).
* **Refactoring:** Modularización del código para garantizar bajo acoplamiento y alta cohesión mediante la delegación estricta (`SlotMachine` $\rightarrow$ `Eje` $\rightarrow$ `Wheel` $\rightarrow$ `Symbol`).

### Ciclo 3: Extensión y Solución de la Maratón (ICPC)
* **Constructor dinámico:** Implementación de `SlotMachine(int n)` para inicializar una máquina con $n$ ruedas y $n$ símbolos desordenados aleatoriamente.
* **Clase `SlotMachineContest`:** 
  * `solve(int n)`: Resuelve el juego con la máquina invisible mediante un algoritmo en tres fases (separación de símbolos, detección de permutación por colisiones y alineación final), retornando la matriz `int[][]` de acciones `{rueda, pasos}`.
  * `simulate(int n)`: Reproduce visualmente la solución calculada en pantalla sobre una máquina visible paso a paso.
* **Testing Tool:** Utilización de `SlotMachine` estrictamente a través de la interfaz pública permitida (`spin(wheel, steps)` y `distinctSymbols()`), sin inspeccionar directamente los colores.
* **Batería de pruebas unitarias BDD:** Implementación de las suites de prueba `SlotMachineContestTest` y `SlotMachineContestCTest` en JUnit 4.

---

## Arquitectura del Software

El sistema aplica el patrón de diseño de **delegación y fachada**:

* **`SlotMachine`:** Fachada principal del simulador. Ofrece la API de alto nivel para el usuario y sirve como testing tool para el concurso.
* **`Eje`:** Administrador intermedio que orquesta el conjunto de ruedas, sus coordenadas geométricas sobre el canvas y la distribución de eventos de rotación.
* **`Wheel`:** Representa cada rueda física. Contiene una lista circular de `Symbol`, controla su estado de bloqueo (`locked`) y ejecuta la animación de desplazamiento mediante `rotate(int steps)`.
* **`Symbol`:** Objeto de dominio que encapsula las propiedades del símbolo (nombre, color y forma gráfica).
* **`SlotMachineContest`:** Agente solucionador desacoplado que implementa la estrategia interactiva frente al oráculo.

---

## Estructura de Archivos del Repositorio

```text
├── slotmachine/                     # Código fuente Java
│   ├── SlotMachine.java             # Fachada principal del juego
│   ├── SlotMachineContest.java      # Algoritmo de concurso (solve y simulate)
│   ├── Eje.java                     # Coordinador de ruedas
│   ├── Wheel.java                   # Manejo de rueda y símbolos
│   ├── Symbol.java                  # Representación de símbolos
│   ├── Canvas.java                  # Motor gráfico de BlueJ
│   ├── Shapes.java                  # Jerarquía de formas geométricas
│   ├── SlotMachineContestTest.java  # Pruebas unitarias de la solución
│   └── SlotMachineContestCTest.java # Pruebas unitarias compartidas
├── tragamonedas.asta                # Modelo UML en Astah
├── Retrospectiva.txt                # Documento consolidado de retrospectivas
└── README.md                        # Documentación general del proyecto
