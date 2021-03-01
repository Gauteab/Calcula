# Calcula
Compiler for a simple calculator language

This was a practice project to learn about compilers.

The goal of this project was to create a simple compiler that recognizes C-like programming expressions.

It only supports integer literals (`1`, `123`), boolean literals (`T`, `F`), binary operations (arithmetic and bolean)
and the unary operator `-`.

# Running
Compile code: `java -jar jar/Calc.jar calc/mini.calc > mini.asm`
Assemble:     `nasm -felt64 mini.asm`
Link:         `gcc -no-pie mini.asm -o mini`
Run:          `./mini`

# Frontend
The frontend is a manually written recursive decent parser.

# Backend
The backend generates NASM assembly code. I have not implemented any proper register selection. Instead,
it uses a simple two register push and pop technique that essentially emulates the behaviour of a stack machine.

There is no optimization step, as the language only has literals, and would therefore make the code generation step trivial.
