  global main
  extern printf
  section .text
main:
mov  rax, 2
push rax
mov  rax, 2
mov  rcx, rax
pop  rax
imul rax, rcx
push rax
mov  rax, 3
mov  rcx, rax
pop  rax
add  rax, rcx
mov  rdi, fmt
mov  rsi, rax
xor  rax, rax
call printf
ret
fmt: db `%d\n`, 0
