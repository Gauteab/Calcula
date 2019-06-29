  global main
  extern printf
  section .text
main:
  mov  rax, 1
  push rax
  mov  rax, 3
  neg  rax
  mov  rcx, rax
  pop  rax
  sub  rax, rcx

  mov  rdi, format
  mov  rsi, rax
  xor  rax, rax
  call printf
  ret

  section .data
format: db "%d", 10, 0

