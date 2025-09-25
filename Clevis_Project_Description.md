# COMP2021: Object-Oriented Programming — Group Project (Fall 2025)

**Department of Computing, The Hong Kong Polytechnic University (PolyU)**  
**Instructors:** Dr. Max Yu Pei & Dr. Alexander Lam  
**Project Title:** A Command Line Vector Graphics Software (Clevis)  
**Date:** 18 September 2025

---

## Deadlines

1. **Group formation:** Form groups of **3–4** before **14:00, 2 October 2025**. After this time, ungrouped students will be randomly assigned. Group changes require **written agreement from all members** of the involved groups **before 14:00, 9 October 2025**.
2. **Main submission:** Submit a single **ZIP** (source code incl. tests, tests, user manual, slides & recording, project report) on **Blackboard** **before 14:00, 24 November 2025**.
3. **Design review reports:** Submit review reports (**PDF/JPG**) on **Blackboard** **before 14:00, 27 November 2025**.
4. **Late policy:** **25% deduction per day**.

---

## 1. Overview

Build **Clevis** — a **Command LinE Vector graphIcs Software** in **Java**. Users create and manipulate vector graphics composed of **line segments**, **circles**, **rectangles**, and **squares** via a **command-line interface (CLI)**.

---

## 2. Requirements

Unless noted otherwise:

- All numeric values are **`double`**.
- **Each shape name is unique.**
- When printed, **double** values must be **rounded to 2 decimal places**.
- Shapes created **later** are **on top** of those created earlier (**higher Z-index**).

> Example startup (with logging; see REQ1):
>
> ```bash
> java hk.edu.polyu.comp.comp2021.clevis.Application -html d:\log.html -txt d:\log.txt
> ```

### [REQ1] Logging (4 pts)

- **Log all operations** of a Clevis session into **two files**:
  - **HTML**: a table with two columns — **operation index** and **command** (one row per operation).
  - **TXT**: plain text; **one command per line**.
- **File names** are provided as **startup parameters** (see example above).

### [REQ2] Rectangle (1.5 pts)

- **Command:** `rectangle n x y w h`  
- **Effect:** Create rectangle named `n`, **top-left** at `(x, y)`, **width** `w`, **height** `h`.

### [REQ3] Line segment (1.5 pts)

- **Command:** `line n x1 y1 x2 y2`  
- **Effect:** Create line segment named `n` with endpoints `(x1, y1)` and `(x2, y2)`.

### [REQ4] Circle (1.5 pts)

- **Command:** `circle n x y r`  
- **Effect:** Create circle named `n`, **center** `(x, y)`, **radius** `r`.

### [REQ5] Square (1.5 pts)

- **Command:** `square n x y l`  
- **Effect:** Create square named `n`, **top-left** `(x, y)`, **side length** `l`.

### [REQ6] Group (2 pts)

- **Command:** `group n n1 n2 ...`  
- **Effect:** Create **group shape** `n` from existing shapes `n1, n2, ...`.  
  After grouping, `n1, n2, ...` **cannot** be directly used by [REQ8]–[REQ13] **until** `n` is **ungrouped**.

### [REQ7] Ungroup (2 pts)

- **Command:** `ungroup n`  
- **Effect:** **Ungroup** `n` into its component shapes. Shape `n` **ceases to exist**; its components become directly usable by [REQ8]–[REQ13].

### [REQ8] Delete (2 pts)

- **Command:** `delete n`  
- **Effect:** Delete shape `n`. If `n` is a **group**, **all its members** are also deleted.

### [REQ9] Minimum Bounding Box (4 pts)

- **Command:** `boundingbox n`  
- **Effect:** Compute and **print** the **minimum bounding box** of shape `n` in the format:  
  `x y w h` (top-left `x y`, width `w`, height `h`).  
  For a **group**, the bounding box covers **all** member shapes.  
  _Reference:_ <https://en.wikipedia.org/wiki/Minimum_bounding_box>

### [REQ10] Move (4 pts)

- **Command:** `move n dx dy`  
- **Effect:** Move shape `n` by **`dx` horizontally** and **`dy` vertically**. For groups, **move all components**.

### [REQ11] `shapeAt` (4 pts)

- **Command:** `shapeAt x y`  
- **Effect:** Return the **name** of the **topmost** shape (highest **Z-index**) that **covers point** `(x, y)`.  
- **Cover definition:** a shape **covers** a point iff the **minimum distance** from the point to the shape’s **outline (border only)** is **\< 0.05**.  
  - For example, a rectangle covers a point iff the **minimum distance** to **any side** is **\< 0.05**.  
  - For **groups**, the outline is the union of **all members’ outlines**.  
- **Z-order:** Later-created shapes have **higher Z-index**.  
  _Reference:_ <https://en.wikipedia.org/wiki/Z-order>

### [REQ12] `intersect` (4 pts)

- **Command:** `intersect n1 n2`  
- **Effect:** Report whether shapes `n1` and `n2` **intersect**, defined as their **minimum bounding boxes sharing any internal point**.

### [REQ13] `list` (4 pts)

- **Command:** `list n`  
- **Effect:** List basic information of shape `n`.  
  - **Simple shape:** list **construction parameters** (e.g., for circle: name, **center**, **radius**).  
  - **Group shape:** list **group name** and **direct members**.

### [REQ14] `listAll` (2 pts)

- **Command:** `listAll`  
- **Effect:** List **all shapes** in **decreasing Z-order**. Use **indentation** to indicate **containment** (groups and their members).

### [REQ15] `quit` (2 pts)

- **Command:** `quit`  
- **Effect:** Terminate Clevis. (All graphics are **discarded** on exit.)

> **Error handling:** Gracefully handle ill-formed commands and impossible actions (e.g., **duplicate name**, **undefined name**). **Poor error handling** causes point deductions.

#### Bonus Features

- **[BON1] GUI (8 pts):** Provide a GUI that displays the graphic after each operation. You may add a **text field** for commands and support **zooming** for a full view. The **CLI remains mandatory**.
- **[BON2] Undo/Redo (4 pts):** Support **undo/redo** for all commands **except** `boundingbox`, `intersect`, `list`, `listAll`, and `quit`.  
  _Reference:_ <https://en.wikipedia.org/wiki/Undo>

---

## 3. Group Forming

- Groups of **3–4**; members must be from the **same class session**.  
- Form groups before **14:00, 2 October 2025**. Late: random grouping.  
- Group change requires **written consent of all members** **before 14:00, 9 October 2025**.

---

## 4. Code Inspection

- Use IntelliJ IDEA’s **Inspection** tool with the provided rules (**`COMP2021 PROJECT.xml`**).  
- **One point is deducted for each error** reported by the tool (unit tests excluded).  
  _Reference:_ <https://www.jetbrains.com/help/idea/code-inspection.html>

---

## 5. Line Coverage of Unit Tests

- Follow **MVC**. Put all **model** classes under the package:  
  `hk.edu.polyu.comp.comp2021.clevis.model`  
- Measure **line coverage** on this package using IntelliJ’s coverage tool.  
  - **Scoring (base points):**  
    - 90–100% → **10 pts**, 80–89% → **9 pts**, …, \<30% → **0 pts**.  
  - **Final points for coverage** = **base points × requirements coverage (%)**.  
    - Example: If you implement **60%** of requirements and achieve **95% coverage**, points = **10 × 60% = 6**.  
  _References:_ <http://en.wikipedia.org/wiki/Code_coverage>, <https://www.jetbrains.com/help/idea/code-coverage.html>, <https://en.wikipedia.org/wiki/Model-view-controller>

---

## 6. Design Review

- Week 13 lecture: **peer review** in clusters of **X** groups (TBD).  
- Each group reviews **every other group** in its cluster; submit **X–1 review reports**.  
- Reports: identify **good choices**, **questionable choices**, **anti-patterns**, with **reasons**.  
- Further details (organization & format) will be announced before the review.

---

## 7. Project Report

- Summarize the **design** and **implementation**, following the **template** in Appendix A.
- **Individual contributions:** State each member’s **percentage** (e.g., for 3 members, 33.3% each if equal). This **affects individual scores** (see §11).  
- **Learning-to-Learn:** Reflect on how you planned, conducted, evaluated, and adjusted **self-learning** to cover topics **not fully in lectures**, and outline **improvements**.

---

## 8. Use of GenAI

- GenAI is **allowed**, but you must **acknowledge** all GenAI-assisted contents.  
- Include the **“Honour Declaration for Group Project”** in the final submission, **even if** you did not use GenAI.  
- Guidance: <https://www.polyu.edu.hk/ar/docdrive/polyu-students/Student-guide-on-the-use-of-GenAI_revised_250214.pdf>

---

## 9. Presentation

- **≤ 6 minutes** total; **each member presents ≥ 1 minute**.  
- Must include:  
  1) **Overall architecture**;  
  2) A **design choice** involving **inheritance & polymorphism**;  
  3) How **OO design/programming** improved **reusability/scalability**.  
- You may add snapshots or short clips of **CVFS** in action; keep them brief.  
- **Video format:** MP4. At the beginning, **show student ID card and face** of each presenter; the **current presenter’s face must remain visible**.  
  - **Missing face/ID** → **50% deduction** for that member.  
- **Privacy:** videos are **automatically destroyed after one year**.

---

## 10. Submission

Submit **one ZIP** containing **all deliverables**:

1. **Main directory** named with **GroupID** (optionally include project name).  
2. Inside it, create:  
   - `ProjectCode/` — IntelliJ IDEA project folder  
   - `Presentation/` — slides & video  
3. Place files (PDF unless specified):  
   - **Project report** — in **main directory**  
   - **Honour Declaration for Group Project** — in **main directory**  
   - **User manual** — in **main directory**  
   - **IntelliJ project** — in `ProjectCode/`  
   - **Slides (PDF)** and **video (MP4)** — in `Presentation/`  
4. Zip **everything inside** the main directory.

**Example structure**

```plaintext
GroupID/
├─ project report.pdf
├─ Honour Declaration for Group Project.pdf
├─ user manual.pdf
├─ ProjectCode/
│  └─ <IntelliJ IDEA project folder>
└─ Presentation/
   ├─ slides.pdf
   └─ video.mp4
```

---

## 11. Grading

- **Requirements coverage:** 40 pts (see §2)  
- **Code quality:** 20 pts total — **OO design** (10) + **code style via inspection** (10)  
- **Unit test line coverage:** 10 pts (see §5)  
- **Presentation:** 7 pts  
- **Design review report:** 8 pts  
- **Individual report & user manual:** 15 pts  
- **Bonus:** up to **12 pts** (cannot exceed **100 total**)

**Individual score formula:**  
If the group’s overall score is **x**, group size is **y (3 ≤ y ≤ 4)**, and a member’s contribution is **z%**, then the member’s score is:  
`min(x * y * z%, x)`. **Equal contribution** is encouraged.

**Penalty:** Failure to submit the **Honour Declaration** or submitting a **false declaration** may incur **up to 40% penalty**.

---

## 12. General Notes

- **Tools for grading:** **JDK 21** and **IntelliJ IDEA Community Edition 2024.2**.  
  - Configure IntelliJ (**File → Project Structure → Project**):  
    - **SDK:** 21  
    - **Language level:** **8** (Lambdas, type annotations, etc.)
- **Standard library only:** Do **not** rely on libraries beyond **Java SE 21**.
- The project material package includes:  
  - **Honour Declaration for Group Project.docx**  
  - **SampleProject.zip** (sample project structure)  
  - **IntelliJ IDEA Tutorial.pdf** (how-to’s)  
  - **COMP2021 PROJECT.xml** (inspection rules)
- If you used other tools/IDEs, ensure your classes/tests are in an **IntelliJ project** ready to **compile & run**.  
  **Penalty:** **−50 pts** for non-compliant project or **compilation errors**.

**Useful links:**  

- JDK 21: <https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html>  
- IntelliJ Community 2024.2: <https://www.jetbrains.com/idea/download/other.html>

---

## Appendices

### A. Project Report Template

**Project Report**  
COMP2021 Object-Oriented Programming (Fall 2025)  

**Group XYZ**  

**Members and contribution percentages:**  

- Member1: a%  
- Member2: b%  
- Member3: c%  
- Member4: d%

#### 1. Introduction

Briefly outline the design and implementation of **Clevis**.

#### 2. The Command Line Vector Graphics Software (Clevis)

##### 2.1 Design

Provide **class diagram(s)** and explain how components fit together. Elaborate on **design patterns** and justify **critical design decisions**.

##### 2.2 Implementation of Requirements

For each requirement (including bonus), state **(1) implemented or not**, **(2) how** you implemented it, and **(3) error handling**.

Example:

- **[REQ1]**  
  1) Implemented.  
  2) Implementation details …  
  3) Error handling …  

- **[REQ2]**  
  1) Not implemented.  

…

#### 3. Learning-to-Learn

Reflect on **learning-to-learn** experiences and outline a **plan to improve** your **self-learning**.
